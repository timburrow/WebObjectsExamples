/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//Account.java
//Created on Sun Sep 23 01:06:24 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSValidation;


public class Account extends EOGenericRecord {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4147875016349987249L;
	static public NSArray<String> States = new NSArray <String>(new String[]{"California","New York", "Texas"});
	static public NSArray<String> Countries = new NSArray <String>(new String[]{"USA", "Canada", "Japan"});

	//These are just arbitarily selected to show how validation can work
	int MIN_USERID_LEN = 4;
	int MAX_USERID_LEN = 10;
	int MIN_PSWD_LEN = 4;
	int MAX_PSWD_LEN = 10;

	public Account() {
		super();

	}

//	------------------ Custom Business Logic -----------------------------------------

	public static Account newAccount(EOEditingContext ec){
		Account accnt = new Account();
		ec.insertObject(accnt);

		return accnt;
	}

	public String displayName(){
		if(firstname()!=null && firstname().length()>0)
			return firstname();
		return "Back";
	}

	public static Account getAccount(String userID, String password, EOEditingContext ec) {
		Account accnt=null;

		if(userID==null)
			return null;

		NSMutableDictionary dict=new NSMutableDictionary();
		dict.setObjectForKey(userID,"userID");
		if(password!=null)
			dict.setObjectForKey(password,"password");

		try{
			accnt = (Account)EOUtilities.objectMatchingValues(ec, "Account", dict);
		}catch(Exception ex){
			// Why arte we not reporting the exception?
		}

		return accnt;
	}


//	------------------ Validation Logic -----------------------------------------

	@Override
	public void validateForInsert(){
		super.validateForInsert();
		if(getAccount( userID(), null, editingContext())!=null)
			throw new NSValidation.ValidationException("Account '"+userID()+"' already exists!");
	}

	public String validateUserID(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"UserID was not provided");
		Validation.minLengthCheck(s, MIN_USERID_LEN,"UserID must be longer than "+MIN_USERID_LEN+" characters.");
		Validation.maxLengthCheck(s, MAX_USERID_LEN,"UserID must be less than "+MAX_USERID_LEN+" characters.");

		return s;
	}

	public String validatePassword(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"Password was not provided");
		Validation.minLengthCheck(s, MIN_PSWD_LEN,"Password must be longer than "+MIN_PSWD_LEN+" characters.");
		Validation.maxLengthCheck(s, MAX_PSWD_LEN,"Password must be less than "+MAX_PSWD_LEN+" characters.");

		return s;
	}

	public String validateFirstname(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"Firstname was not provided");
		Validation.zeroLengthCheck(s,"Firstname was not entered");

		return s;
	}

	public String validateLastname(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"Lastname was not provided");
		Validation.zeroLengthCheck(s,"Lastname was not entered");

		return s;
	}

	public String validateEmail(String s) throws NSValidation.ValidationException {

		Validation.nullCheck(s,"Email address was not provided");
		Validation.zeroLengthCheck(s,"Email address was not entered");

		return s;
	}

//	-----------------------------------------------------------------------------

	public String password() {
		return (String)storedValueForKey("password");
	}

	public void setPassword(String value) {
		takeStoredValueForKey(value, "password");
	}

	public String email() {
		return (String)storedValueForKey("email");
	}

	public void setEmail(String value) {
		takeStoredValueForKey(value, "email");
	}

	public String firstname() {
		return (String)storedValueForKey("firstname");
	}

	public void setFirstname(String value) {
		takeStoredValueForKey(value, "firstname");
	}

	public String lastname() {
		return (String)storedValueForKey("lastname");
	}

	public void setLastname(String value) {
		takeStoredValueForKey(value, "lastname");
	}

	public String status() {
		return (String)storedValueForKey("status");
	}

	public void setStatus(String value) {
		takeStoredValueForKey(value, "status");
	}

	public String addr1() {
		return (String)storedValueForKey("addr1");
	}

	public void setAddr1(String value) {
		takeStoredValueForKey(value, "addr1");
	}

	public String addr2() {
		return (String)storedValueForKey("addr2");
	}

	public void setAddr2(String value) {
		takeStoredValueForKey(value, "addr2");
	}

	public String city() {
		return (String)storedValueForKey("city");
	}

	public void setCity(String value) {
		takeStoredValueForKey(value, "city");
	}

	public String state() {
		return (String)storedValueForKey("state");
	}

	public void setState(String value) {
		takeStoredValueForKey(value, "state");
	}

	public String zip() {
		return (String)storedValueForKey("zip");
	}

	public void setZip(String value) {
		takeStoredValueForKey(value, "zip");
	}

	public String country() {
		return (String)storedValueForKey("country");
	}

	public void setCountry(String value) {
		takeStoredValueForKey(value, "country");
	}

	public String phone() {
		return (String)storedValueForKey("phone");
	}

	public void setPhone(String value) {
		takeStoredValueForKey(value, "phone");
	}

	public String userID() {
		return (String)storedValueForKey("userID");
	}

	public void setUserID(String value) {
		takeStoredValueForKey(value, "userID");
	}


	public Profile profile() {
		return (Profile)storedValueForKey("profile");
	}

	public void setProfile(Profile value) {
		takeStoredValueForKey(value, "profile");
	}

	public NSArray orders() {
		return (NSArray)storedValueForKey("orders");
	}

	public void setOrders(NSMutableArray value) {
		takeStoredValueForKey(value, "orders");
	}

	public void addToOrders(Order object) {
		NSMutableArray array = (NSMutableArray)orders();

		willChange();
		array.addObject(object);
	}

	public void removeFromOrders(Order object) {
		NSMutableArray array = (NSMutableArray)orders();

		willChange();
		array.removeObject(object);
	}
}
