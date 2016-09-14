/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//Profile.java
//Created on Mon Sep 24 16:29:02 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;

public class Profile extends EOGenericRecord {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9013682524755644621L;
	static public String LANG_ENGLISH = "English";
	static public String LANG_JAPANESE = "Japanese";
	static public NSArray<String> languages = new NSArray<String>(new String[]{LANG_ENGLISH, LANG_JAPANESE});

	public Profile() {
		super();
	}

	@Override
	public void awakeFromInsertion(EOEditingContext anEOEditingContext){
		super.awakeFromInsertion( anEOEditingContext);
		//set default values upon insert of new Profile into editingcontext
		setBanneropt(new Integer(0));
		setFavcategory(Category.AnyCategory);
		setLangpref(LANG_ENGLISH);
	}
//	------------------ Custom Business Logic -----------------------------------------

	public String favcategory() {
		String fc = (String)storedValueForKey("favcategory");
		if(fc==null)
			fc = (String)Category.randomCategory().valueForKey("name");

		return fc;
	}
//	------------------ Custom Business Logic -----------------------------------------

	public String langpref() {
		return (String)storedValueForKey("langpref");
	}

	public void setLangpref(String value) {
		takeStoredValueForKey(value, "langpref");
	}


	public void setFavcategory(String value) {
		takeStoredValueForKey(value, "favcategory");
	}

	public Number mylistopt() {
		return (Number)storedValueForKey("mylistopt");
	}

	public void setMylistopt(Number value) {
		takeStoredValueForKey(value, "mylistopt");
	}

	public Number banneropt() {
		return (Number)storedValueForKey("banneropt");
	}

	public void setBanneropt(Number value) {
		takeStoredValueForKey(value, "banneropt");
	}

	public String userID() {
		return (String)storedValueForKey("userID");
	}

	public void setUserID(String value) {
		takeStoredValueForKey(value, "userID");
	}
}
