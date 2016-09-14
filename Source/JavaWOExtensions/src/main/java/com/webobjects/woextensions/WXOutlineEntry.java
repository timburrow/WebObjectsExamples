/*
 * WXOutlineEntry.java [JavaWOExtensions Project] �Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or
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

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSRange;

public class WXOutlineEntry extends WOComponent {
	private static final long	serialVersionUID	= 39155973473419070L;

	int							_nestingLevel;

	public WXOutlineEntry(WOContext aContext) {
		super(aContext);
	}

	// ///////////
	// No-Sync
	// //////////
	@Override
	public boolean synchronizesVariablesWithBindings() {
		return false;
	}

	@Override
	public void awake() {
		super.awake();
		Object nestLevelBinding = _WOJExtensionsUtil.valueForBindingOrNull("nestingLevel", this);
		if (nestLevelBinding instanceof Number) {
			_nestingLevel = ((Number) nestLevelBinding).intValue();
			return;
		}

		if ((nestLevelBinding == null) || nestLevelBinding.equals("")) {
			_nestingLevel = 0;
			return;
		}
		try {
			_nestingLevel = Integer.parseInt(nestLevelBinding.toString());
		} catch (NumberFormatException e) {
			throw new IllegalStateException("WXOutLineEntry - problem parsing int from nestingLevel binding " + e);
		}
	}

	public int nestingLevel() {
		return _nestingLevel;
	}

	public boolean isExpanded() {
		Object currentItem = valueForBinding("item");
		NSArray selectionPath = (NSArray) _WOJExtensionsUtil.valueForBindingOrNull("selectionPath", this);
		return (_nestingLevel < selectionPath.count()) && selectionPath.objectAtIndex(_nestingLevel).equals(currentItem);
	}

	public int nestingLevelForChildren() {
		return _nestingLevel + 1;
	}

	@SuppressWarnings("unchecked")
	public WOComponent toggleExpansion() {
		NSArray<Object> selectionPath = (NSArray) _WOJExtensionsUtil.valueForBindingOrNull("selectionPath", this);

		selectionPath = selectionPath.subarrayWithRange(new NSRange(0, _nestingLevel));

		if (!isExpanded()) {
			Object currentItem = valueForBinding("item");
			// NSLog(@"*** currentItem=%@", currentItem);
			selectionPath = selectionPath.arrayByAddingObject(currentItem);
		}

		setValueForBinding(selectionPath, "selectionPath");
		return null;
	}

	public boolean hasChildren() {
		return ((Boolean) valueForBinding("hasChildren")).booleanValue();
	}

	@Override
	public void takeValuesFromRequest(WORequest aRequest, WOContext aContext) {
		session().setObjectForKey(this, "_outlineEntry");
		super.takeValuesFromRequest(aRequest, aContext);
	}

	@Override
	public WOActionResults invokeAction(WORequest aRequest, WOContext aContext) {
		WOActionResults returnElement;
		session().setObjectForKey(this, "_outlineEntry");
		returnElement = super.invokeAction(aRequest, aContext);
		return returnElement;
	}

	@Override
	public void appendToResponse(WOResponse aResponse, WOContext aContext) {
		session().setObjectForKey(this, "_outlineEntry");
		super.appendToResponse(aResponse, aContext);
	}
}
