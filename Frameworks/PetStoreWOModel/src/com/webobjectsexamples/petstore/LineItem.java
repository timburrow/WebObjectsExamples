/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//LineItem.java
//Created on Sun Sep 30 20:19:44 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import java.math.*;

public class LineItem extends EOGenericRecord {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9047005020697346653L;

	public LineItem() {
		super();
	}

//	------------------ Custom Business Logic -----------------------------------------

	public double totalPrice() {
		return unitprice().doubleValue() * quantity().intValue();
	}

	static public LineItem createWithOrderAndCartItem(Order ord, NSDictionary cartItem){
		EOEditingContext ec = ord.editingContext();

		LineItem li = new LineItem();
		ec.insertObject(li);

		li.setLineitemid(new Integer(ord.lineItems().count()+1));
		Item item = (Item)cartItem.objectForKey("item");

		//Item & Inventory needs to be registered in LineItem's editing context, since there both probably in the shared ec right now.
		ec.setSharedEditingContext(null);
		Inventory inventory = (Inventory)EOUtilities.localInstanceOfObject(ec, item.inventory());
		item = (Item)EOUtilities.localInstanceOfObject(ec, item);

		li.addObjectToBothSidesOfRelationshipWithKey(item, "item");

		Number qty = (Number)cartItem.objectForKey("qty");
		li.setQuantity(qty);

		inventory.subtractQty(qty);

		li.setUnitprice(item.listPrice());

		return li;
	}
//	------------------ End Custom Business Logic -----------------------------------------

	public String itemid() {
		return (String)storedValueForKey("itemid");
	}

	public void setItemid(String value) {
		takeStoredValueForKey(value, "itemid");
	}

	public Number quantity() {
		return (Number)storedValueForKey("quantity");
	}

	public void setQuantity(Number value) {
		takeStoredValueForKey(value, "quantity");
	}

	public BigDecimal unitprice() {
		return (BigDecimal)storedValueForKey("unitprice");
	}

	public void setUnitprice(BigDecimal value) {
		takeStoredValueForKey(value, "unitprice");
	}

	public Number orderid() {
		return (Number)storedValueForKey("orderid");
	}

	public void setOrderid(Number value) {
		takeStoredValueForKey(value, "orderid");
	}

	public Number lineitemid() {

		return (Number)storedValueForKey("lineitemid");
	}

	public void setLineitemid(Number value) {
		takeStoredValueForKey(value, "lineitemid");
	}

	public Item item() {
		return (Item)storedValueForKey("item");
	}

	public void setItem(Item value) {
		takeStoredValueForKey(value, "item");
	}
}
