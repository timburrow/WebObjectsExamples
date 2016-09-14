/*
 * WOSortOrderManyKey.java [JavaWOExtensions Project] �Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or
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

public class WOSortOrderManyKey extends WOComponent {
	private static final long	serialVersionUID	= -8495075098972394200L;

	private String				_currentKey;

	private String				_selectedKey;

	// ** Internal Caching
	WODisplayGroup				_displayGroup;

	public WOSortOrderManyKey(WOContext aContext) {
		super(aContext);
	}

	@Override
	public boolean isStateless() {
		return true;
	}

	@Override
	public void reset() {
		_currentKey = null;
		_selectedKey = null;
		_displayGroup = null;
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

	// /////////
	// Utility
	// /////////
	private EOSortOrdering _primarySortOrdering() {
		NSArray anArray = displayGroup().sortOrderings();
		if ((anArray != null) && (anArray.count() > 0)) {
			EOSortOrdering anOrdering = (EOSortOrdering) anArray.objectAtIndex(0);
			return anOrdering;
		}
		return null;
	}

	public void setSelectedKey(String aNewValue) {
		_selectedKey = aNewValue;
		if (_isCurrentKeyPrimary()) {
			_removeSortOrderingWithKey(selectedKey());
		}
		_makePrimarySortOrderingWithSelector(EOSortOrdering.CompareAscending);
	}

	public String selectedKey() {
		if (null == _selectedKey) {
			setSelectedKey(_primarySortOrdering().key());
		}
		return _selectedKey;
	}

	private boolean _isCurrentKeyPrimary() {
		EOSortOrdering anOrdering = _primarySortOrdering();
		if ((anOrdering != null) && anOrdering.key().equals(selectedKey())) {
			return true;
		}
		return false;
	}

	private void _removeSortOrderingWithKey(String aKey) {
		int anIndex = 0;
		EOSortOrdering aSortOrdering = null;
		WODisplayGroup aDisplayGroup = displayGroup();
		NSArray sortOrderings = aDisplayGroup.sortOrderings();
		if (sortOrderings != null) {
			NSMutableArray aSortOrderingArray = sortOrderings.mutableClone();
			Enumeration anEnumerator = aSortOrderingArray.objectEnumerator();
			while (anEnumerator.hasMoreElements()) {
				aSortOrdering = (EOSortOrdering) anEnumerator.nextElement();
				if (aKey.equals(aSortOrdering.key())) {
					aSortOrderingArray.removeObjectAtIndex(anIndex);
					break;
				}
				anIndex++;
			}
			aDisplayGroup.setSortOrderings(aSortOrderingArray);
		}
	}

	private void _makePrimarySortOrderingWithSelector(NSSelector aSelector) {
		String aKey = selectedKey();
		WODisplayGroup aDisplayGroup = displayGroup();
		NSArray<EOSortOrdering> sortOrderings = aDisplayGroup.sortOrderings();
		NSMutableArray<EOSortOrdering> aSortOrderingArray;
		if (sortOrderings != null) {
			aSortOrderingArray = sortOrderings.mutableClone();
		} else {
			aSortOrderingArray = new NSMutableArray<EOSortOrdering>();
		}
		EOSortOrdering aNewSortOrdering = EOSortOrdering.sortOrderingWithKey(aKey, aSelector);
		aSortOrderingArray.insertObjectAtIndex(aNewSortOrdering, 0);
		if (aSortOrderingArray.count() > 3) {
			// ** limits sorting to 3 levels
			aSortOrderingArray.removeLastObject();
		}
		aDisplayGroup.setSortOrderings(aSortOrderingArray);
	}

	// ///////////
	// Actions
	// ///////////
	public WOComponent sortAscendingClicked() {
		if (_isCurrentKeyPrimary()) {
			_removeSortOrderingWithKey(selectedKey());
		}
		_makePrimarySortOrderingWithSelector(EOSortOrdering.CompareAscending);
		displayGroup().updateDisplayedObjects();
		return null;
	}

	public WOComponent sortDescendingClicked() {
		if (_isCurrentKeyPrimary()) {
			_removeSortOrderingWithKey(selectedKey());
		}
		_makePrimarySortOrderingWithSelector(EOSortOrdering.CompareDescending);
		displayGroup().updateDisplayedObjects();
		return null;
	}

	/**
	 * @return the currentKey
	 */
	public String currentKey() {
		return this._currentKey;
	}

	/**
	 * @param currentKey
	 *            the currentKey to set
	 */
	public void setCurrentKey(String currentKey) {
		this._currentKey = currentKey;
	}

}