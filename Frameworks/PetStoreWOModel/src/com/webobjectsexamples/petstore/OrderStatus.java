/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//OrderStatus.java
//Created on Sun Nov 11 14:47:59 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSTimestamp;

public class OrderStatus extends EOGenericRecord {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2704058046468258133L;
	//these codes might differ from sun's petstore
	public static final String STATUS_NEWORDER = "NO";
	public static final String STATUS_BACKORDERED = "BO";
	public static final String STATUS_CANCELLED = "CL";
	public static final String STATUS_FILLED = "FL";

	public OrderStatus() {
		super();
	}
//	------------------ Custom Business Logic -----------------------------------------
	public static void newOrderStatusForOrderLineItem(Order order, LineItem li){
		OrderStatus orderStatus = new OrderStatus();
		order.editingContext().insertObject(orderStatus);

		orderStatus.setUpdatedate(new NSTimestamp());
		orderStatus.setLinenum(li.lineitemid());
		orderStatus.setStatus(OrderStatus.STATUS_NEWORDER);

		order.addObjectToBothSidesOfRelationshipWithKey(orderStatus,"orderStatus");
	}
//	------------------ End Custom Business Logic -----------------------------------------


	public NSTimestamp updatedate() {
		return (NSTimestamp)storedValueForKey("updatedate");
	}

	public void setUpdatedate(NSTimestamp value) {
		takeStoredValueForKey(value, "updatedate");
	}

	public String status() {
		return (String)storedValueForKey("status");
	}

	public void setStatus(String value) {
		takeStoredValueForKey(value, "status");
	}

	public Number orderid() {
		return (Number)storedValueForKey("orderid");
	}

	public void setOrderid(Number value) {
		takeStoredValueForKey(value, "orderid");
	}

	public Number linenum() {
		return (Number)storedValueForKey("linenum");
	}

	public void setLinenum(Number value) {
		takeStoredValueForKey(value, "linenum");
	}
}
