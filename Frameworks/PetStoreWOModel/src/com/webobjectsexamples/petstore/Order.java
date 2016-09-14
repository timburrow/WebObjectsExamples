/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//Order.java
//Created on Sun Sep 30 20:21:03 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import com.webobjects.foundation.*;
import com.webobjects.eocontrol.*;
import java.math.BigDecimal;

public class Order extends EOGenericRecord {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1324202484388601309L;
	public static NSArray<String> CreditCardTypes = new NSArray<String>(new String[]{"Visa","American Express", "Mastercard"});
	public static NSArray<String> Months = new NSArray<String>(new String[]{"01", "02", "03", "04","05","06","07","08","09","10","11","12"});
	public static NSArray<String> Years = new NSArray<String>(new String[]{"2000","2001","2002","2003","2004"});

	public Order() {
		super();
	}

	@Override
	public void awakeFromInsertion(EOEditingContext anEOEditingContext){
		super.awakeFromInsertion( anEOEditingContext);
		//set default values upon insert of new Order into editing context
		setCreditcard("9999-9999-9999-9999");
		setOrderdate(new NSTimestamp());
	}

//	------------------ Custom Business Logic -----------------------------------------

	public static Order createAndInsertOrder(Account account, NSArray cartItems, EOEditingContext ec) {
		//create new order and add to the Account
		Order order = new Order();
		ec.insertObject(order);
		account.addObjectToBothSidesOfRelationshipWithKey(order,"orders");

		//create lineItems from cart Items
		for (int i=0; i < cartItems.count(); i++) {
			NSDictionary cartItem = (NSDictionary)cartItems.objectAtIndex(i);

			//create new LineItem from the cartItem
			LineItem lineItem = LineItem.createWithOrderAndCartItem(order, cartItem);

			//add the lineItem to the Order
			order.addObjectToBothSidesOfRelationshipWithKey(lineItem, "lineItems");
			order.setTotalprice((BigDecimal)cartItems.valueForKeyPath("@sum.total"));

			//and the orderstatus
			OrderStatus.newOrderStatusForOrderLineItem(order,lineItem);
		}
		return order;
	}
//	------------------ Validation Logic -----------------------------------------

	@Override
	public void validateForSave(){
		super.validateForSave();
		if(lineItems().count()==0)
			throw new NSValidation.ValidationException("Cannot save Order with no line items.");
	}

	public String validateCreditcard(String s) throws NSValidation.ValidationException {

		//this is a good place for a method to call some real CC validation logic!
		Validation.nullCheck(s,"Credit Card Number was not provided.");
		Validation.minLengthCheck(s, 19,"Credit Card Number too short");
		Validation.maxLengthCheck(s, 19,"Credit Card Number too long");

		return s;
	}

	public String validateExperdate(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s," was not provided.");
		Validation.minLengthCheck(s,7,"Credit Card Expire date is invalid");
		Validation.maxLengthCheck(s,7,"Credit Card Expire date is invalid" );

		return s;
	}
	public String validateCardtype(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"Credit Card type was not provided.");
		Validation.zeroLengthCheck(s,"Credit Card type was not provided");

		return s;
	}

//	---------------------------------------------------------------------------

	public String userID() {
		return (String)storedValueForKey("userID");
	}

	public void setUserID(String value) {
		takeStoredValueForKey(value, "userID");
	}

	public NSTimestamp orderdate() {
		return (NSTimestamp)storedValueForKey("orderdate");
	}

	public void setOrderdate(NSTimestamp value) {
		takeStoredValueForKey(value, "orderdate");
	}

	public String shipaddr1() {
		return (String)storedValueForKey("shipaddr1");
	}

	public void setShipaddr1(String value) {
		takeStoredValueForKey(value, "shipaddr1");
	}

	public String shipaddr2() {
		return (String)storedValueForKey("shipaddr2");
	}

	public void setShipaddr2(String value) {
		takeStoredValueForKey(value, "shipaddr2");
	}

	public String shipcity() {
		return (String)storedValueForKey("shipcity");
	}

	public void setShipcity(String value) {
		takeStoredValueForKey(value, "shipcity");
	}

	public String shipstate() {
		return (String)storedValueForKey("shipstate");
	}

	public void setShipstate(String value) {
		takeStoredValueForKey(value, "shipstate");
	}

	public String shipzip() {
		return (String)storedValueForKey("shipzip");
	}

	public void setShipzip(String value) {
		takeStoredValueForKey(value, "shipzip");
	}

	public String shipcountry() {
		return (String)storedValueForKey("shipcountry");
	}

	public void setShipcountry(String value) {
		takeStoredValueForKey(value, "shipcountry");
	}

	public String billaddr1() {
		return (String)storedValueForKey("billaddr1");
	}

	public void setBilladdr1(String value) {
		takeStoredValueForKey(value, "billaddr1");
	}

	public String billaddr2() {
		return (String)storedValueForKey("billaddr2");
	}

	public void setBilladdr2(String value) {
		takeStoredValueForKey(value, "billaddr2");
	}

	public String billcity() {
		return (String)storedValueForKey("billcity");
	}

	public void setBillcity(String value) {
		takeStoredValueForKey(value, "billcity");
	}

	public String billstate() {
		return (String)storedValueForKey("billstate");
	}

	public void setBillstate(String value) {
		takeStoredValueForKey(value, "billstate");
	}

	public String billzip() {
		return (String)storedValueForKey("billzip");
	}

	public void setBillzip(String value) {
		takeStoredValueForKey(value, "billzip");
	}

	public String billcountry() {
		return (String)storedValueForKey("billcountry");
	}

	public void setBillcountry(String value) {
		takeStoredValueForKey(value, "billcountry");
	}

	public String courier() {
		return (String)storedValueForKey("courier");
	}

	public void setCourier(String value) {
		takeStoredValueForKey(value, "courier");
	}

	public BigDecimal totalprice() {
		return (BigDecimal)storedValueForKey("totalprice");
	}

	public void setTotalprice(BigDecimal value) {
		takeStoredValueForKey(value, "totalprice");
	}

	public String billtofirstname() {
		return (String)storedValueForKey("billtofirstname");
	}

	public void setBilltofirstname(String value) {
		takeStoredValueForKey(value, "billtofirstname");
	}

	public String billtolastname() {
		return (String)storedValueForKey("billtolastname");
	}

	public void setBilltolastname(String value) {
		takeStoredValueForKey(value, "billtolastname");
	}

	public String shiptofirstname() {
		return (String)storedValueForKey("shiptofirstname");
	}

	public void setShiptofirstname(String value) {
		takeStoredValueForKey(value, "shiptofirstname");
	}

	public String shiptolastname() {
		return (String)storedValueForKey("shiptolastname");
	}

	public void setShiptolastname(String value) {
		takeStoredValueForKey(value, "shiptolastname");
	}

	public String creditcard() {
		return (String)storedValueForKey("creditcard");
	}

	public void setCreditcard(String value) {
		takeStoredValueForKey(value, "creditcard");
	}

	public String exprdate() {
		return (String)storedValueForKey("exprdate");
	}

	public void setExprdate(String value) {
		takeStoredValueForKey(value, "exprdate");
	}

	public String cardtype() {
		return (String)storedValueForKey("cardtype");
	}

	public void setCardtype(String value) {
		takeStoredValueForKey(value, "cardtype");
	}

	public Number orderid() {
		return (Number)storedValueForKey("orderid");
	}

	public void setOrderid(Number value) {
		takeStoredValueForKey(value, "orderid");
	}

	public NSArray lineItems() {
		return (NSArray)storedValueForKey("lineItems");
	}

	public void setLineItems(NSMutableArray value) {
		takeStoredValueForKey(value, "lineItems");
	}

	public void addToLineItems(LineItem object) {
		NSMutableArray array = (NSMutableArray)lineItems();

		willChange();
		array.addObject(object);
	}

	public void removeFromLineItems(LineItem object) {
		NSMutableArray array = (NSMutableArray)lineItems();

		willChange();
		array.removeObject(object);
	}

	public NSArray orderStatus() {
		return (NSArray)storedValueForKey("orderStatus");
	}

	public void setOrderStatus(NSMutableArray value) {
		takeStoredValueForKey(value, "orderStatus");
	}

	public void addToOrderStatus(OrderStatus object) {
		NSMutableArray array = (NSMutableArray)orderStatus();

		willChange();
		array.addObject(object);
	}

	public void removeFromOrderStatus(OrderStatus object) {
		NSMutableArray array = (NSMutableArray)orderStatus();

		willChange();
		array.removeObject(object);
	}
}
