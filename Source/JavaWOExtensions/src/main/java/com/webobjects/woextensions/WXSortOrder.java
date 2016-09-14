/*
 * WXSortOrder.java [JavaWOExtensions Project] �Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or
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
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSSelector;

public class WXSortOrder extends WOComponent {
	private static final long	serialVersionUID	= 2162236325831711154L;

	WODisplayGroup				_displayGroup;

	String						_key;

	String						_displayKey;

	public WXSortOrder(WOContext aContext) {
		super(aContext);
	}

	// ///////////
	// No-Sync
	// //////////
	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	// ///////////
	// Bindings
	// //////////
	public WODisplayGroup displayGroup() {
		if (null == _displayGroup) {
			_displayGroup = (WODisplayGroup) _WOJExtensionsUtil.valueForBindingOrNull("displayGroup", this);
		}
		return _displayGroup;
	}

	public String key() {
		if (null == _key) {
			_key = (String) _WOJExtensionsUtil.valueForBindingOrNull("key", this);
		}
		return _key;
	}

	public String displayKey() {
		if (null == _displayKey) {
			_displayKey = (String) _WOJExtensionsUtil.valueForBindingOrNull("displayKey", this);
		}
		return _displayKey;
	}

	// /////////
	// Utility
	// /////////
	public String imageName() {
		if (_isCurrentKeyPrimary()) {
			NSSelector aCurrentState = _primaryKeyOrderingSelector();
			if (aCurrentState == EOSortOrdering.CompareCaseInsensitiveAscending) {
				return "Ascending.gif";
			}
			if (aCurrentState == EOSortOrdering.CompareCaseInsensitiveDescending) {
				return "Descending.gif";
			}
		}
		return "Unsorted.gif";
	}

	public boolean _isCurrentKeyPrimary() {
		EOSortOrdering anOrdering = _primaryOrdering();
		if (anOrdering.key().equals(key())) {
			return true;
		}
		return false;
	}

	public NSSelector _primaryKeyOrderingSelector() {
		EOSortOrdering anOrdering = _primaryOrdering();
		return anOrdering.selector();
	}

	public EOSortOrdering _primaryOrdering() {
		NSArray<EOSortOrdering> anArray = _sortOrderingArray();
		if (anArray.count() > 0) {
			EOSortOrdering anOrdering = anArray.objectAtIndex(0);
			return anOrdering;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private NSMutableArray<EOSortOrdering> _sortOrderingArray() {
		return (NSMutableArray) _WOJExtensionsUtil.valueForBindingOrNull("sortOrderings", this);
	}

	private void _removeOrderingWithKey(String aKey) {
		int anIndex = 0;
		EOSortOrdering ordering;
		NSArray orderingArray = _sortOrderingArray();
		Enumeration anEnumerator = orderingArray.objectEnumerator();
		while (anEnumerator.hasMoreElements()) {
			ordering = (EOSortOrdering) anEnumerator.nextElement();
			if (aKey.equals(ordering.key())) {
				((NSMutableArray) orderingArray).removeObjectAtIndex(anIndex);
				return;
			}
			anIndex++;
		}
	}

	private void _makePrimaryOrderingWithSelector(NSSelector aSelector) {
		NSMutableArray<EOSortOrdering> orderingArray = _sortOrderingArray();
		EOSortOrdering aNewOrdering = EOSortOrdering.sortOrderingWithKey(key(), aSelector);
		orderingArray.insertObjectAtIndex(aNewOrdering, 0);
		if (orderingArray.count() > 3) {
			// ** limits ing to 3 levels
			orderingArray.removeLastObject();
		}
	}

	public String helpString() {
		return "Push to toggle sorting order according to " + displayKey();
	}

	// ///////////
	// Actions
	// ///////////

	public WOComponent toggleClicked() {
		if (_isCurrentKeyPrimary()) {
			NSSelector aCurrentState = _primaryKeyOrderingSelector();
			if (aCurrentState == EOSortOrdering.CompareCaseInsensitiveAscending) {
				_removeOrderingWithKey(key());
				_makePrimaryOrderingWithSelector(EOSortOrdering.CompareCaseInsensitiveDescending);
			} else if (aCurrentState == EOSortOrdering.CompareCaseInsensitiveDescending) {
				_removeOrderingWithKey(key());
			}
		} else {
			_removeOrderingWithKey(key());
			_makePrimaryOrderingWithSelector(EOSortOrdering.CompareCaseInsensitiveAscending);
		}
		displayGroup().updateDisplayedObjects();
		return null;
	}

}
