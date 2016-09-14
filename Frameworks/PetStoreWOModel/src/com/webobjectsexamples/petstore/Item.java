/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
// Item.java
// Created on Sun Sep 30 20:23:58 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;


import java.math.BigDecimal;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;


public class Item extends EOGenericRecord {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7980492264915295784L;

	public Item() {
        super();
    }
//------------------ Custom Business Logic -----------------------------------------

    public String inStock(){
        if(isInStock())
            return "Yes";
        else
            return "No";
    }

    public boolean isInStock(){
        if(inventory()!=null && inventory().qty().intValue()>0)
            return true;
        else
            return false;
    }
//------------------ End Custom Business Logic -----------------------------------------

    public BigDecimal listPrice() {
        return (BigDecimal)storedValueForKey("listPrice");
    }

    public void setListPrice(BigDecimal value) {
        takeStoredValueForKey(value, "listPrice");
    }

    public BigDecimal unitCost() {
        return (BigDecimal)storedValueForKey("unitCost");
    }

    public void setUnitCost(BigDecimal value) {
        takeStoredValueForKey(value, "unitCost");
    }

    public String status() {
        return (String)storedValueForKey("status");
    }

    public void setStatus(String value) {
        takeStoredValueForKey(value, "status");
    }

    public String attr1() {
        return (String)storedValueForKey("attr1");
    }

    public void setAttr1(String value) {
        takeStoredValueForKey(value, "attr1");
    }

    public String attr2() {
        return (String)storedValueForKey("attr2");
    }

    public void setAttr2(String value) {
        takeStoredValueForKey(value, "attr2");
    }

    public String attr3() {
        return (String)storedValueForKey("attr3");
    }

    public void setAttr3(String value) {
        takeStoredValueForKey(value, "attr3");
    }

    public String attr4() {
        return (String)storedValueForKey("attr4");
    }

    public void setAttr4(String value) {
        takeStoredValueForKey(value, "attr4");
    }

    public String attr5() {
        return (String)storedValueForKey("attr5");
    }

    public void setAttr5(String value) {
        takeStoredValueForKey(value, "attr5");
    }

    public String itemid() {
        return (String)storedValueForKey("itemid");
    }

    public void setItemid(String value) {
        takeStoredValueForKey(value, "itemid");
    }

    public String productid() {
        return (String)storedValueForKey("productid");
    }

    public void setProductid(String value) {
        takeStoredValueForKey(value, "productid");
    }

    public Product product() {
        return (Product)storedValueForKey("product");
    }

    public void setProduct(Product value) {
        takeStoredValueForKey(value, "product");
    }

    public Inventory inventory() {
        return (Inventory)storedValueForKey("inventory");
    }

    public void setInventory(Inventory value) {
        takeStoredValueForKey(value, "inventory");
    }

    public EOEnterpriseObject supplier() {
        return (EOEnterpriseObject)storedValueForKey("supplier");
    }

    public void setSupplier(EOEnterpriseObject value) {
        takeStoredValueForKey(value, "supplier");
    }
}
