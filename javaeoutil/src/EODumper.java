/*
 * © Copyright 2006-2007 Apple Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes
 * acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non- exclusive license, under
 * Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications,
 * you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior
 * written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may
 * be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND
 * OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN
 * ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;

import com.webobjects.eoaccess.EOAdaptor;
import com.webobjects.eoaccess.EOAdaptorChannel;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOSQLExpression;
import com.webobjects.eoaccess.EOSQLExpressionFactory;
import com.webobjects.eoaccess.synchronization.EOSchemaGeneration;
import com.webobjects.eoaccess.synchronization.EOSchemaGenerationOptions;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSTimestampFormatter;

public class EODumper {
	public static final String	NullValueString				= "__NULL__";

	public static final String	TimestampFormatterPattern	= "%b %d %Y %H:%M";

	private static EOAttribute	_countAttribute;

	private EOModel				_model;

	private NSArray				_entities;

	private EOAdaptor			_adaptor;

	/*
	 * Little convenience method returning a description of the EO represented by row. The description consists of all of the receiver's primary key components separated by a period.
	 */
	private static String _descriptionOfRow(EOEntity entity, NSDictionary row) {
		StringBuffer result = new StringBuffer();
		NSArray attributeNames = entity.primaryKeyAttributeNames();
		int count = attributeNames.count();

		for (int i = 0; i < count; i++) {
			Object attribute = row.objectForKey(attributeNames.objectAtIndex(i));

			result.append("" + attribute + "" + ((count > 1 && i < count - 1) ? "." : ""));
		}

		return new String(result);
	}

	/*
	 * A relatively abstract way to retrieve entity's row count. The entity is passed to the method primarily because EOAttribute's setDefinition: doesn't work unless the receiver has an owner. The consumer is responsible for removing the returned attribute once the count has been fetched.
	 */
	private static EOAttribute _addCountAttributeToEntity(EOEntity entity) {

		if (_countAttribute == null) {
			_countAttribute = new EOAttribute();

			_countAttribute.setName("EODumperCountAttribute");
			_countAttribute.setExternalType("NUMBER");
			_countAttribute.setClassName("NSNumber");
			_countAttribute.setReadOnly(true);
			_countAttribute.setValueType("i");
		}

		entity.addAttribute(_countAttribute);
		_countAttribute.setDefinition("COUNT(*)");

		return _countAttribute;
	}

	private static int _rowCountForEntityAndFetchSpecification(EOAdaptorChannel channel, EOEntity entity, EOFetchSpecification fetchSpec) {
		EOAttribute countAttribute = _addCountAttributeToEntity(entity);

		channel.selectAttributes(new NSArray(countAttribute), fetchSpec, false, entity);

		int rowCount = ((Integer) channel.fetchRow().objectForKey(countAttribute.name())).intValue();

		channel.cancelFetch();
		entity.removeAttribute(countAttribute);

		return rowCount;
	}

	private static NSArray _rowsForEntityAttributes(EOAdaptorChannel channel, EOEntity entity, NSArray attrsToFetch) {

		if (entity.externalName() == null || attrsToFetch.count() == 0) {
			return null;
		}

		NSMutableArray result = new NSMutableArray();
		EOFetchSpecification fetchSpec = new EOFetchSpecification(entity.name(), entity.restrictingQualifier(), null);

		channel.adaptorContext().transactionDidBegin();
		channel.selectAttributes(attrsToFetch, fetchSpec, false, entity);

		while (true) {
			NSDictionary row = channel.fetchRow();

			if (row == null) {
				break;
			}

			result.addObject(row);
		}

		channel.adaptorContext().transactionDidCommit();

		if (result.count() == 0) {
			result = null;
		}

		return result;
	}

	private static void _insertRowsForEntityForce(EOAdaptorChannel channel, NSArray rows, EOEntity entity, boolean force) {
		channel.adaptorContext().beginTransaction();

		for (int i = 0; i < rows.count(); i++) {
			// Added the following variable here to allow us to
			// check the value of an auto-generated key and
			// catch the value, too.
			NSDictionary newPrimaryKey;
			NSDictionary row = (NSDictionary) rows.objectAtIndex(i);

			try {
				// Find the primary key attribute for the entity
				// represented by row. If this isn't computable,
				// this means that the primary key attribute(s)
				// are not present in row.
				//
				// In this case, see if we can auto-generate a
				// primary key for this entity. If not, forget
				// it. If so, create a new row with that primary
				// key and try to insert that instead of row.
				//
				// In the event we can't auto-generate a primary
				// key (e.g. the primary key is compound), you'll
				// catch and see the exception that eoutil used
				// to throw, anyway.

				if (entity.primaryKeyForRow(row) == null) {
					newPrimaryKey = channel.primaryKeyForNewRowWithEntity(entity);

					if (newPrimaryKey != null) {
						NSMutableDictionary newRow = new NSMutableDictionary();

						newRow.addEntriesFromDictionary(row);
						newRow.addEntriesFromDictionary(newPrimaryKey);
						channel.insertRow(newRow, entity);
					}

				} else {
					channel.insertRow(row, entity);
				}

			} catch (Throwable e) {
				channel.adaptorContext().rollbackTransaction();

				if (force) {
					throw new NSForwardException(e);
				}

				NSLog.err.appendln("Caught exception: " + e.getMessage());

				if (NSLog._debugLoggingAllowedForLevel(NSLog.DebugLevelCritical)) {
					NSLog.debug.appendln(e);
				}

			}

		}

		channel.adaptorContext().commitTransaction();
	}

	public EODumper(String path, NSArray entityNames) {
		URL url = null;
		try {
			url = new URL("file", null, path);
		} catch (java.net.MalformedURLException e) {
			e.printStackTrace();
		}

		_model = EOModelGroup.defaultGroup().modelWithPathURL(url);
		int count = (entityNames == null) ? 0 : entityNames.count();

		if (count > 0) {
			NSMutableArray selectedEntities = new NSMutableArray(count);

			for (int i = 0; i < count; i++) {
				String entityName = (String) entityNames.objectAtIndex(i);
				EOEntity entity = _model.entityNamed(entityName);

				selectedEntities.addObject(entity);
			}

			_entities = new NSArray(selectedEntities);
		}

	}

	private EOAdaptor _adaptor() {

		if (_adaptor == null) {
			_adaptor = EOAdaptor.adaptorWithModel(_model);
		}

		return _adaptor;
	}

	private EOAdaptorChannel _openAdaptorChannel() {
		EOAdaptorChannel channel = _adaptor().createAdaptorContext().createAdaptorChannel();

		channel.openChannel();
		return channel;
	}

	private NSArray _attributesToDumpForEntity(EOEntity entity) {
		// we should remove flattened attrs and custom datatypes.
		NSMutableArray result = new NSMutableArray();
		NSArray entityAttributes = entity.attributes();

		for (int i = 0; i < entityAttributes.count(); i++) {
			EOAttribute attr = (EOAttribute) entityAttributes.objectAtIndex(i);

			if (!attr.isDerived() && !attr.isFlattened() && attr.columnName().length() > 0) {
				// If custom value is specified, revert to base type.

				if (attr.valueFactoryMethod() != null || attr.adaptorValueConversionMethod() != null) {
					int type = attr.factoryMethodArgumentType();

					if ((type == EOAttribute.FactoryMethodArgumentIsData) || (type == EOAttribute.FactoryMethodArgumentIsBytes)) {
						attr.setFactoryMethodArgumentType(EOAttribute.FactoryMethodArgumentIsData);
						attr.setClassName("NSData");
					} else if (type == EOAttribute.FactoryMethodArgumentIsString) {
						attr.setClassName("NSString");
					}

					attr.setAdaptorValueConversionMethodName(null);
					attr.setValueFactoryMethodName(null);
				}

				result.addObject(attr);
			}

		}

		return result;
	}

	private static Object _formatValueForAttribute(Object val, EOAttribute attr) {

		if (val == null || (val == NSKeyValueCoding.NullValue)) {
			return NullValueString;
		}

		switch (attr.adaptorValueType()) {
			case EOAttribute.AdaptorDateType:
				// Format with default format.
				return (new NSTimestampFormatter(TimestampFormatterPattern)).format(val);
				// These will all format fine.
			case EOAttribute.AdaptorNumberType:
				if (val instanceof java.lang.Boolean) {
					return ((Boolean) val).booleanValue() ? "1" : "0";
				}
				return val.toString();
			case EOAttribute.AdaptorCharactersType:
			case EOAttribute.AdaptorBytesType:
			default:
				return val;
		}

	}

	private Object _processValueForAttribute(Object val, EOAttribute attr) {

		if (val.equals(NullValueString)) {
			return NSKeyValueCoding.NullValue;
		}

		switch (attr.adaptorValueType()) {
			case EOAttribute.AdaptorDateType:
				// Format with default format.

				try {
					return (new NSTimestampFormatter(TimestampFormatterPattern)).parseObject((String) val);
				} catch (ParseException e) {
					NSLog.err.appendln("Error processing value for date: " + e.getMessage());

					if (NSLog._debugLoggingAllowedForLevel(NSLog.DebugLevelCritical)) {
						NSLog.debug.appendln(e);
					}

					return NSKeyValueCoding.NullValue;
				}

				// These will all format fine.
			case EOAttribute.AdaptorNumberType:
				return new BigDecimal((String) val);
			case EOAttribute.AdaptorCharactersType:
			case EOAttribute.AdaptorBytesType:
			default:
				return val;
				// call newValueForBytes...
		}
	}

	/*
	 * format is: { attributeNames = (name1, name2); rows = ( (value1, value2), (value1, value2), ...); }
	 */
	private NSDictionary _plistForRowsEntity(NSArray rows, EOEntity entity) {
		NSMutableDictionary result = new NSMutableDictionary();
		NSMutableArray rowValues = new NSMutableArray();
		int rowCount = rows.count();
		NSArray attributes = _attributesToDumpForEntity(entity);
		int attrCount = attributes.count();

		for (int i = 0; i < rowCount; i++) {
			NSDictionary row = (NSDictionary) rows.objectAtIndex(i);
			NSMutableArray rowValueArray = new NSMutableArray(attrCount);

			for (int j = 0; j < attrCount; j++) {
				EOAttribute attr = (EOAttribute) attributes.objectAtIndex(j);
				Object value = row.objectForKey(attr.name());

				rowValueArray.addObject(_formatValueForAttribute(value, attr));
			}

			rowValues.addObject(rowValueArray);

			// ETK
			rowValueArray = null;
		}

		NSMutableArray selectedNames = new NSMutableArray(attrCount);

		for (int i = 0; i < attrCount; i++) {
			EOAttribute attr = (EOAttribute) attributes.objectAtIndex(i);
			String attrName = attr.name();

			selectedNames.addObject(attrName);
		}

		result.setObjectForKey(new NSArray(selectedNames), "attributeNames");
		result.setObjectForKey(rowValues, "rows");
		return result;
	}

	private void _appendToScriptRowsEntityAdaptor(StringBuffer script, NSArray rows, EOEntity entity, EOAdaptor adaptor) {
		EOSQLExpressionFactory exprFactory = adaptor.expressionFactory();
		EOSchemaGeneration syncFactory = adaptor.synchronizationFactory();

		for (int i = 0; i < rows.count(); i++) {
			NSDictionary row = (NSDictionary) rows.objectAtIndex(i);
			EOSQLExpression expr = exprFactory.insertStatementForRow(row, entity);

			syncFactory.appendExpressionToScript(expr, script);
		}

	}

	public NSDictionary rowSetFromDatabase() {
		NSMutableDictionary rowSet = new NSMutableDictionary();
		EOAdaptorChannel channel = _openAdaptorChannel();
		NSArray entities = (_entities == null) ? _model.entities() : _entities;

		for (int i = 0; i < entities.count(); i++) {
			EOEntity entity = (EOEntity) entities.objectAtIndex(i);
			NSArray attrsToFetch = _attributesToDumpForEntity(entity);
			NSArray rows = _rowsForEntityAttributes(channel, entity, attrsToFetch);

			if (rows != null) {
				rowSet.setObjectForKey(rows, entity.name());
			}

		}

		return rowSet;
	}

	private NSArray _rowsForEntityPlistEntity(NSDictionary plist, EOEntity entity) {
		NSArray rowList = (NSArray) plist.objectForKey("rows");
		int rowListCount = rowList.count();
		NSMutableArray rows = new NSMutableArray();

		NSArray attributeNames = (NSArray) plist.objectForKey("attributeNames");
		int nameCount = attributeNames.count();
		NSMutableArray attributes = new NSMutableArray();

		EOAttribute ignoreAttribute = new EOAttribute();

		for (int i = 0; i < nameCount; i++) {
			EOAttribute attr = entity.attributeNamed((String) attributeNames.objectAtIndex(i));

			if (attr == null) {
				attr = ignoreAttribute;

				NSLog.out.appendln("ignoring " + attributeNames.objectAtIndex(i) + " in " + entity.name());
			}

			attributes.addObject(attr);
		}

		for (int i = 0; i < rowListCount; i++) {
			NSArray rowValues = (NSArray) rowList.objectAtIndex(i);
			NSMutableDictionary row = new NSMutableDictionary(nameCount);

			for (int j = 0; j < nameCount; j++) {
				EOAttribute attr = (EOAttribute) attributes.objectAtIndex(j);

				if (attr == ignoreAttribute) {
					continue;
				}

				Object value = _processValueForAttribute(rowValues.objectAtIndex(j), attr);

				// Added folowing conditional around the row call.
				if (value != null) {
					row.setObjectForKey(value, attr.name());
				}

			}

			rows.addObject(row);
		}

		return rows;
	}

	public NSDictionary rowSetFromFile(String path) {
		NSArray entities = _entities;
		NSDictionary plist = null;
		NSMutableDictionary rowSet = new NSMutableDictionary();

		try {
			plist = NSPropertyListSerialization.dictionaryForString(path);
		} catch (Exception e) {
			throw new IllegalArgumentException("Unable to read row set from input stream: " + e.getMessage());
		}

		if (plist == null) {
			return null;
		}

		if (entities == null) {
			NSArray keys = plist.allKeys();
			NSMutableArray selectedEntities = new NSMutableArray();

			for (int i = 0; i < keys.count(); i++) {
				selectedEntities.addObject(_model.entityNamed((String) keys.objectAtIndex(i)));
			}

			entities = new NSArray(selectedEntities);
		}

		for (int i = 0; i < entities.count(); i++) {
			EOEntity entity = (EOEntity) entities.objectAtIndex(i);
			NSArray attrsToFetch = _attributesToDumpForEntity(entity);
			NSArray rows = _rowsForEntityPlistEntity((NSDictionary) plist.objectForKey(entity.name()), entity);

			rowSet.setObjectForKey(rows, entity.name());
		}

		return rowSet;
	}

	public void dumpRowSetToDatabaseForceORIG(NSDictionary rowSet, boolean force) {
		NSArray keys = rowSet.allKeys();
		EOAdaptorChannel channel = _openAdaptorChannel();

		for (int i = 0; i < keys.count(); i++) {
			String entityName = (String) keys.objectAtIndex(i);
			EOEntity entity = _model.entityNamed(entityName);

			if (entity == null) {
				throw new IllegalArgumentException("Model does not contain entity named: " + entityName);
			}

			NSArray rows = (NSArray) rowSet.objectForKey(entityName);

			_insertRowsForEntityForce(channel, rows, entity, force);
		}

	}

	public void dumpRowSetToDatabaseForce(NSDictionary rowSet, boolean force) {
		EOAdaptorChannel channel = _openAdaptorChannel();
		NSArray entityNames = rowSet.allKeys();

		for (int i = 0; i < entityNames.count(); i++) {
			String entityName = (String) entityNames.objectAtIndex(i);
			NSLog.out.appendln(" dumping " + entityName + " to db");
			EOEntity entity = _model.entityNamed(entityName);

			if (entity == null) {
				throw new IllegalArgumentException("Model does not contain entity named: " + entityName);
			}

			NSArray rows = (NSArray) rowSet.objectForKey(entityName);

			_insertRowsForEntityForce(channel, rows, entity, force);
		}

	}

	public void dumpRowSetAsPlist(NSDictionary rowSet, OutputStream os) {
		NSMutableDictionary dumps = new NSMutableDictionary();
		NSArray keys = rowSet.allKeys();

		for (int i = 0; i < keys.count(); i++) {
			String entityName = (String) keys.objectAtIndex(i);
			EOEntity entity = _model.entityNamed(entityName);

			if (entity == null) {
				throw new IllegalArgumentException("Model does not contain entity named: " + entityName);
			}

			NSArray rows = (NSArray) rowSet.objectForKey(entityName);

			dumps.setObjectForKey(_plistForRowsEntity(rows, entity), entityName);
		}

		PrintWriter pw = new PrintWriter(os);

		pw.println(NSPropertyListSerialization.stringFromPropertyList(dumps));
		pw.close();
	}

	public void dumpRowSetAsScript(NSDictionary rowSet) {
		StringBuffer script = new StringBuffer();
		NSArray keys = rowSet.allKeys();

		for (int i = 0; i < keys.count(); i++) {
			String entityName = (String) keys.objectAtIndex(i);
			EOEntity entity = _model.entityNamed(entityName);

			if (entity == null) {
				throw new IllegalArgumentException("Model does not contain entity named: " + entityName);
			}

			NSArray rows = (NSArray) rowSet.objectForKey(entityName);

			_appendToScriptRowsEntityAdaptor(script, rows, entity, _adaptor());
		}

		NSLog.out.appendln(script);
	}

	private void _executeStatementsForce(NSArray statements, boolean force) {

		if (statements.count() == 0) {
			return;
		}

		EOAdaptorChannel channel = _openAdaptorChannel();

		for (int i = 0; i < statements.count(); i++) {
			EOSQLExpression statement = (EOSQLExpression) statements.objectAtIndex(i);

			try {
				channel.evaluateExpression(statement);
			} catch (Throwable e) {

				if (!force) {
					throw new NSForwardException(e);
				}

				NSLog.out.appendln("Exception caught: " + e.getMessage());

				if (NSLog._debugLoggingAllowedForLevel(NSLog.DebugLevelCritical)) {
					NSLog.debug.appendln(e);
				}

			}

		}

	}

	public void dumpSchemaToDatabaseUsingOptionsForce(EOSchemaGenerationOptions options, boolean force) {
		NSArray entities = (_entities != null) ? _entities : _model.entities();
		EOSchemaGeneration syncFactory = _adaptor().synchronizationFactory();
		NSArray statements = syncFactory.schemaCreationStatementsForEntities(entities, options);

		_executeStatementsForce(statements, force);
	}

	public void dumpSchemaAsScriptUsingOptions(EOSchemaGenerationOptions options) {
		StringBuffer script = new StringBuffer();
		NSArray entities = (_entities != null) ? _entities : _model.entities();
		EOSchemaGeneration syncFactory = _adaptor().synchronizationFactory();
		NSArray statements = syncFactory.schemaCreationStatementsForEntities(entities, options);

		for (int i = 0; i < statements.count(); i++) {
			EOSQLExpression statement = (EOSQLExpression) statements.objectAtIndex(i);

			syncFactory.appendExpressionToScript(statement, script);
		}

		NSLog.out.appendln(script);
	}

	public void dumpPostInstallStatementsToDatabaseUsingForce(boolean force) {
		NSMutableArray expressions = new NSMutableArray();
		EOSQLExpressionFactory exprFactory = _adaptor().expressionFactory();
		String name = _model.adaptorName();
		String key = "" + name + "PostInstallExpressions";
		NSArray statements = (NSArray) _model.userInfo().objectForKey(key);

		for (int i = 0; i < statements.count(); i++) {
			EOSQLExpression statement = exprFactory.expressionForString((String) statements.objectAtIndex(i));

			expressions.addObject(statement);
		}

		if (expressions != null) {
			_executeStatementsForce(expressions, force);
		}

	}

	public void dumpPostInstallStatementsToScript() {
		StringBuffer script = new StringBuffer();
		EOSQLExpressionFactory exprFactory = _adaptor().expressionFactory();
		EOSchemaGeneration syncFactory = _adaptor().synchronizationFactory();
		String name = _model.adaptorName();
		String key = "" + name + "PostInstallExpressions";
		NSArray statements = (NSArray) _model.userInfo().objectForKey(key);

		for (int i = 0; i < statements.count(); i++) {
			EOSQLExpression statement = exprFactory.expressionForString((String) statements.objectAtIndex(i));

			syncFactory.appendExpressionToScript(statement, script);
		}

		NSLog.out.appendln(script);
	}

	public void finalize() {
		_model = null;
		_entities = null;
		_adaptor = null;
	}
}
