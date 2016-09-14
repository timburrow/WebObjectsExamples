/*
 * WOAnyField.java [JavaWOExtensions Project] ©Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non- exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple
 * Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived
 * from the Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or
 * by other works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.webobjects.woextensions;

import java.text.ParseException;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eoaccess.EOAttribute;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EORelationship;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSNumberFormatter;
import com.webobjects.foundation.NSTimestampFormatter;

public class WOAnyField extends WOComponent {
	private static final long	serialVersionUID		= 8039522683298340956L;

	protected static String		DEFAULT_DATE_FORMAT		= "YYYY/MM/DD";

	protected static String		DEFAULT_NUMBER_FORMAT	= "0";

	private String				_relationshipKey;

	private String				_selectedKey;

	private String				_selectedOperator;

	private Object				_value;

	private String				_textFieldValue;

	private WODisplayGroup		_displayGroup;

	// ivars for PopUp
	public String				selectedKeyItem;

	public String				selectedOperatorItem;

	public WOAnyField(WOContext aContext) {
		super(aContext);
	}

	@Override
	public boolean isStateless() {
		return true;
	}

	public String relationshipKey() {
		if (_relationshipKey == null) {
			_relationshipKey = (String) _WOJExtensionsUtil.valueForBindingOrNull("relationshipKey", this);
		}
		return _relationshipKey;
	}

	public String selectedKey() {
		if (_selectedKey == null) {
			_selectedKey = (String) _WOJExtensionsUtil.valueForBindingOrNull("selectedKey", this);
		}
		return _selectedKey;
	}

	public void setSelectedKey(String key) {
		_selectedKey = key;
	}

	public String valueClassNameForKey(String key) {
		String entityName = (String) _WOJExtensionsUtil.valueForBindingOrNull("sourceEntityName", this);
		EOModelGroup modelGroup = EOModelGroup.defaultGroup();
		EOEntity entity = modelGroup.entityNamed(entityName);
		EOAttribute selectedAttribute = null;
		if (relationshipKey() != null) {
			EORelationship relationship = entity.relationshipNamed(relationshipKey());
			EOEntity destinationEntity = relationship.destinationEntity();
			selectedAttribute = destinationEntity.attributeNamed(key);
		} else {
			selectedAttribute = entity.attributeNamed(key);
		}
		return selectedAttribute.className();
	}

	public String formatterForKey(String key) {
		String formatter = null;
		if (hasBinding("formatter")) {
			setValueForBinding(key, "key");
			formatter = (String) _WOJExtensionsUtil.valueForBindingOrNull("formatter", this);
		}
		if (formatter == null) {
			String className = valueClassNameForKey(key);
			if (className.equals("com.webobjects.foundation.NSTimestamp")) {
				formatter = DEFAULT_DATE_FORMAT;
			} else if (className.equals("java.lang.Number") || className.equals("java.math.BigDecimal")) {
				formatter = DEFAULT_NUMBER_FORMAT;
			}
		}
		return formatter;
	}

	public WODisplayGroup displayGroup() {
		if (_displayGroup == null) {
			_displayGroup = (WODisplayGroup) _WOJExtensionsUtil.valueForBindingOrNull("displayGroup", this);
		}
		return _displayGroup;
	}

	public String selectedOperator() {
		return _selectedOperator;
	}

	public void setSelectedOperator(String anOperator) {
		_selectedOperator = (anOperator.equals("=")) ? "" : anOperator;
	}

	public Object value() {
		if (_value == null) {
			_value = _WOJExtensionsUtil.valueForBindingOrNull("value", this);
		}
		return _value;
	}

	public void setValue(Object newValue) {
		_value = newValue;
		WODisplayGroup displayGroup = displayGroup();
		if (displayGroup != null) {
			displayGroup.queryMatch().removeAllObjects();
			if (relationshipKey() != null) {
				displayGroup.queryMatch().takeValueForKey(newValue, relationshipKey() + "." + selectedKey());
				if (newValue != null) {
					displayGroup.queryOperator().takeValueForKey(selectedOperator(), relationshipKey() + "." + selectedKey());
				}
			} else {
				displayGroup.queryMatch().takeValueForKey(newValue, selectedKey());
				if (newValue != null) {
					displayGroup.queryOperator().takeValueForKey(selectedOperator(), selectedKey());
				}
			}
		}
	}

	public String textFieldValue() {
		if (_textFieldValue != null) {
			return _textFieldValue;
		}
		Object value = value();
		setValue(value);
		if (value == null) {
			return null;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			java.text.Format formatter = null;
			String className = valueClassNameForKey(selectedKey());
			if (className.equals("com.webobjects.foundation.NSTimestamp")) {
				String dateFormatterString = formatterForKey(selectedKey());
				formatter = new NSTimestampFormatter(dateFormatterString);
			} else if (className.equals("java.lang.Number") || className.equals("java.math.BigDecimal")) {
				String numberFormatterString = formatterForKey(selectedKey());
				formatter = new NSNumberFormatter(numberFormatterString);
			}
			return (formatter != null) ? formatter.format(value) : value.toString();
		}
	}

	public void setTextFieldValue(String value) {
		String className = valueClassNameForKey(selectedKey());
		if (className.equals("com.webobjects.foundation.NSTimestamp")) {
			String dateFormatterString = formatterForKey(selectedKey());
			NSTimestampFormatter dateFormatter = new NSTimestampFormatter(dateFormatterString);
			Object objectValue = null;
			try {
				objectValue = dateFormatter.parseObject((value != null) ? value.toString() : "");
			} catch (ParseException e) {
				if (NSLog.debugLoggingAllowedForLevelAndGroups(NSLog.DebugLevelDetailed, NSLog.DebugGroupWebObjects)) {
					NSLog.debug.appendln(e);
				}
			}
			setValue(objectValue);
		} else if (className.equals("java.lang.Number") || className.equals("java.math.BigDecimal")) {
			String numberFormatterString = formatterForKey(selectedKey());
			NSNumberFormatter numberFormatter = new NSNumberFormatter(numberFormatterString);
			Object objectValue = null;
			try {
				objectValue = numberFormatter.parseObject((value != null) ? value.toString() : "");
			} catch (ParseException e) {
				if (NSLog.debugLoggingAllowedForLevelAndGroups(NSLog.DebugLevelDetailed, NSLog.DebugGroupWebObjects)) {
					NSLog.debug.appendln(e);
				}
			}
			setValue(objectValue);
		} else {
			// Assume String
			setValue(value);
		}
	}

	public void invalidateCaches() {
		// In order for this to behave like an element, all instance
		// variables need to be flushed before this components is used again
		// so that it will pull via association.
		_relationshipKey = null;
		_selectedKey = null;
		_selectedOperator = null;
		_value = null;
		_textFieldValue = null;
		_displayGroup = null;
	}

	@Override
	public void finalize() throws Throwable {
		super.finalize();
		invalidateCaches();
	}

	@Override
	public void reset() {
		invalidateCaches();
	}
}
