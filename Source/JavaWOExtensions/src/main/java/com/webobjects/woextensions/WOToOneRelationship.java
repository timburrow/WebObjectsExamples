/*
 * WOToOneRelationship.java [JavaWOExtensions Project] �Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non- exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple
 * Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived
 * from the Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or
 * by other works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.webobjects.woextensions;

import java.util.Enumeration;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EODatabaseDataSource;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EODataSource;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class WOToOneRelationship extends WOComponent {
	private static final long	serialVersionUID	= 5065019896974184314L;

	// ** passed-in (required)
	String						_sourceEntityName;

	String						_relationshipKey;

	Object						_sourceObject;

	// ** passed-in (optional)
	String						_destinationDisplayKey;

	EODatabaseDataSource		_dataSource;

	String						_uiStyle;

	boolean						_isMandatory;

	// ** internal
	Object						theCurrentItem;

	NSArray						_privateList;

	Object						_privateSelection;

	public static String		_noneString			= "- none -";

	public WOToOneRelationship(WOContext aContext) {
		super(aContext);
	}

	@Override
	public boolean isStateless() {
		return true;
	}

	public String sourceEntityName() {
		return _sourceEntityName;
	}

	public void setSourceEntityName(String aValue) {
		_sourceEntityName = aValue;
	}

	public String relationshipKey() {
		return _relationshipKey;
	}

	public void setRelationshipKey(String aValue) {
		_relationshipKey = aValue;
	}

	public Object sourceObject() {
		return _sourceObject;
	}

	public void setSourceObject(Object aValue) {
		_sourceObject = aValue;
	}

	public String destinationDisplayKey() {
		return _destinationDisplayKey;
	}

	public void setDestinationDisplayKey(String aValue) {
		_destinationDisplayKey = aValue;
	}

	public EODatabaseDataSource dataSource() {
		return _dataSource;
	}

	public void setDataSource(EODatabaseDataSource aValue) {
		_dataSource = aValue;
	}

	public String uiStyle() {
		return _uiStyle;
	}

	public void setUiStyle(String aValue) {
		_uiStyle = aValue;
	}

	boolean isMandatory() {
		return _isMandatory;
	}

	public void setIsMandatory(Object aValue) {
		try {
			_isMandatory = _WOJExtensionsUtil.booleanValue(aValue);
		} catch (Throwable e) {
			String error = "WOToOneRelationship (setIsMandatory) - unable to set isMandatory value " + e.getMessage();
			NSLog.err.appendln(error);
		}
	}

	// ////////////////////////////////////////////////
	// The following are used internally/privately
	// ////////////////////////////////////////////////
	public Object theCurrentItem() {
		return theCurrentItem;
	}

	public void setTheCurrentItem(Object aValue) {
		theCurrentItem = aValue;
	}

	private NSArray _privateList() {
		return _privateList;
	}

	public void set_privateList(NSArray aValue) {
		_privateList = aValue;
	}

	public Object _privateSelection() {
		return _privateSelection;
	}

	public void set_privateSelection(Object aValue) {
		_privateSelection = aValue;
	}

	private void _invalidateCaches() {
		// In order for this to behave like an element, all instance
		// variables need to be flushed when this component sleeps
		// so that it will pull via association.
		setSourceEntityName(null);
		setRelationshipKey(null);
		setSourceObject(null);
		setDataSource(null);
		setDestinationDisplayKey(null);
		setUiStyle(null);
		setIsMandatory(null);
		setTheCurrentItem(null);
		set_privateList(null);
		set_privateSelection(null);
	}

	@Override
	public void reset() {
		_invalidateCaches();
	}

	// /////////////////////
	// Internal Accessors
	// /////////////////////
	private Object _localSourceObject() {
		if (null == sourceObject()) {
			setSourceObject(valueForBinding("sourceObject"));
			if (null == sourceObject()) {
				throw new IllegalStateException("<" + getClass().getName() + " sourceObject binding required. sourceObject value is nil or missing>");
			}
		}
		return sourceObject();
	}

	private boolean _localIsMandatory() {
		if (!isMandatory()) {
			Object aValue = valueForBinding("isMandatory");
			setIsMandatory(aValue);
		}
		return isMandatory();
	}

	private String _localSourceEntityName() {
		if (null == sourceEntityName()) {
			setSourceEntityName((String) _WOJExtensionsUtil.valueForBindingOrNull("sourceEntityName", this));
			if (null == sourceEntityName()) {
				throw new IllegalStateException("<" + getClass().getName() + " sourceEntityName binding required. sourceEntityName value is nil or missing>");
			}
		}
		return sourceEntityName();
	}

	private String _localRelationshipKey() {
		if (null == relationshipKey()) {
			setRelationshipKey((String) _WOJExtensionsUtil.valueForBindingOrNull("relationshipKey", this));
			if (null == relationshipKey()) {
				throw new IllegalStateException("<" + getClass().getName() + " relationshipKey binding required. relationshipKey value is nil or missing>");
			}
		}
		return relationshipKey();
	}

	private String _localDestinationDisplayKey() {
		String destinationDisplayKey = destinationDisplayKey();
		if (null == destinationDisplayKey) {
			setDestinationDisplayKey((String) _WOJExtensionsUtil.valueForBindingOrNull("destinationDisplayKey", this));
			if (null == destinationDisplayKey()) {
				setDestinationDisplayKey("userPresentableDescription");
			}
			return destinationDisplayKey();
		} else
			return destinationDisplayKey;
	}

	public EOEntity entityWithEntityAndKeyPath(EOEntity entity, String keyPath) {
		NSArray keys = NSArray.componentsSeparatedByString(keyPath, ".");
		Enumeration keysEnumerator = keys.objectEnumerator();
		String key = null;
		EOEntity result = entity;
		while (keysEnumerator.hasMoreElements()) {
			key = (String) keysEnumerator.nextElement();
			result = result.relationshipNamed(key).destinationEntity();
		}
		return result;
	}

	private EODataSource _localDataSource() {

		if (null == dataSource()) {
			setDataSource((EODatabaseDataSource) _WOJExtensionsUtil.valueForBindingOrNull("dataSource", this));
			if (null == dataSource()) {
				String anEntityName = _localSourceEntityName();
				EOModelGroup aModelGroup = EOModelGroup.defaultGroup();
				EOEntity anEntity = aModelGroup.entityNamed(anEntityName);

				if (anEntity == null) {
					throw new IllegalStateException("<" + getClass().getName() + " could not find entity named " + anEntityName + ">");
				}

				EOEntity destinationEntity = entityWithEntityAndKeyPath(anEntity, _localRelationshipKey());
				Object _source = _localSourceObject();
				EOEditingContext anEditingContext = null;
				if (_source instanceof EOEnterpriseObject) {
					anEditingContext = ((EOEnterpriseObject) _source).editingContext();
				}
				if (anEditingContext == null) {
					anEditingContext = session().defaultEditingContext();
				}
				EODatabaseDataSource aDatabaseDataSource = new EODatabaseDataSource(anEditingContext, destinationEntity.name());
				setDataSource(aDatabaseDataSource);
			}
		}

		return dataSource();
	}

	private Object _localUiStyle() {
		if (null == uiStyle()) {
			setUiStyle((String) _WOJExtensionsUtil.valueForBindingOrNull("uiStyle", this));
			// if still no value let's determine one
			if (null == uiStyle()) {
				int aSize = theList().count();
				if (aSize < 5) {
					setUiStyle("radio");
				}
				if ((aSize >= 5) && (aSize < 20)) {
					setUiStyle("popup");
				}
				if (aSize >= 20) {
					setUiStyle("browser");
				}
			}
		}
		return uiStyle();
	}

	/*
	 * -updateSourceObject does the real work here updating the relationship (or setting the keys for a query).
	 */

	public void updateSourceObject(Object anEO) {
		String masterKey = _localRelationshipKey();
		Object aSourceObject = _localSourceObject();
		boolean isDictionary = (aSourceObject instanceof NSMutableDictionary);
		NSMutableDictionary _dictionary = (isDictionary) ? (NSMutableDictionary) aSourceObject : null;
		EOEnterpriseObject _eo = !(isDictionary) ? (EOEnterpriseObject) aSourceObject : null;

		if (anEO != null) {

			if (isDictionary) {
				_dictionary.setObjectForKey(anEO, masterKey);
			} else if (_eo.valueForKey(masterKey) != anEO) {
				_eo.addObjectToBothSidesOfRelationshipWithKey((EOEnterpriseObject) anEO, masterKey);
			}

		} else { // remove

			if (isDictionary) {
				_dictionary.removeObjectForKey(masterKey);
			} else if (_eo.valueForKey(masterKey) != null) {
				_eo.removeObjectFromBothSidesOfRelationshipWithKey((EOEnterpriseObject) _eo.valueForKey(masterKey), masterKey);
			}

		}

	}

	// //////////////////////////////////
	// Accessed through HTML and WOD
	// //////////////////////////////////

	/*
	 * -selection and -setSelection: are called by WOF when syncing up the contents of this component. These are accessed only through the declarations.
	 */

	public void setSelection(Object anEO) {
		Object aValue = null;
		Object anEnterpriseObject = anEO;

		// deal with array when ui is browser
		if ((anEnterpriseObject != null) && (anEnterpriseObject instanceof NSArray)) {
			NSArray anEOArray = (NSArray) anEnterpriseObject;
			if (anEOArray.count() != 0) {
				anEnterpriseObject = anEOArray.objectAtIndex(0);
			}
		}

		if (anEnterpriseObject != _noneString) {
			aValue = anEnterpriseObject;
		}

		set_privateSelection(aValue);
		// this set method needs to trigger the setSourceObject:
		// it's the only way our value will get back into the parent
		updateSourceObject(aValue);
	}

	public Object selection() {
		if (_privateSelection() == null) {
			set_privateSelection(NSKeyValueCoding.Utility.valueForKey(_localSourceObject(), _localRelationshipKey()));
		}
		// deal with isMandatory
		if ((_privateSelection() == null) && !_localIsMandatory()) {
			setSelection(_noneString);
		}
		return _privateSelection();
	}

	@SuppressWarnings("unchecked")
	public NSArray theList() {
		NSMutableArray aSortedArray;
		NSArray anUnsortedArray;
		if (_privateList() == null) {
			EODataSource aDataSource = _localDataSource();
			anUnsortedArray = aDataSource.fetchObjects();
			// 81398 sort contents
			aSortedArray = new NSMutableArray(anUnsortedArray);
			try {
				_WOJExtensionsUtil._sortEOsUsingSingleKey(aSortedArray, _localDestinationDisplayKey());
			} catch (NSComparator.ComparisonException e) {
				throw NSForwardException._runtimeExceptionForThrowable(e);
			}

			if (!_localIsMandatory()) {
				aSortedArray.insertObjectAtIndex(_noneString, 0);
			}
			set_privateList(aSortedArray);
		}
		return _privateList();
	}

	public void setTheList(NSArray aValue) {/**/}

	public Object theCurrentValue() {
		// handle the case where it's the - none - string
		if (theCurrentItem == _noneString) {
			return theCurrentItem;
		}
		return NSKeyValueCoding.Utility.valueForKey(theCurrentItem, _localDestinationDisplayKey());

	}

	public boolean isRadio() {
		if (_localUiStyle().equals("radio")) {
			return true;
		}
		return false;
	}

	public boolean isPopup() {
		if (_localUiStyle().equals("popup")) {
			return true;
		}
		return false;
	}

	public boolean isBrowser() {
		if (_localUiStyle().equals("browser")) {
			return true;
		}
		return false;
	}
}
