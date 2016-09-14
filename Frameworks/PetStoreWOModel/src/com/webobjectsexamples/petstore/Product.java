/*
© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
// Product.java
// Created on Mon Sep 24 16:28:35 US/Pacific 2001 by Apple EOModeler Version 5.0
package com.webobjectsexamples.petstore;

import java.util.StringTokenizer;

import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class Product extends EOGenericRecord {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5004147003342200142L;
	String imageName, description;

    public Product() {
        super();
    }
//------------------ Custom Business Logic -----------------------------------------

//these two methods allow  to work better with the database that comes with the sun version of the petstore, using the resource manager.

    public String imageName(){
        if(imageName==null){
            StringTokenizer st = new StringTokenizer(descn(),">",false);
            if(st.countTokens()>0){
                String s=st.nextToken();
                imageName = s.substring(s.lastIndexOf("/")+1, s.lastIndexOf("\""));
            }
        }
        return imageName;
    }

    public String description(){
        if(description==null){
            StringTokenizer st = new StringTokenizer(descn(),">",false);
            if(st.countTokens()>1){
                st.nextToken();
                description = st.nextToken();
            }
        }
        return description;
    }
//------------------ End Custom Business Logic -----------------------------------------

    public String catid() {
        return (String)storedValueForKey("catid");
    }

    public void setCatid(String value) {
        takeStoredValueForKey(value, "catid");
    }

    public String name() {
        return (String)storedValueForKey("name");
    }

    public void setName(String value) {
        takeStoredValueForKey(value, "name");
    }

    public String descn() {
        return (String)storedValueForKey("descn");
    }

    public void setDescn(String value) {
        takeStoredValueForKey(value, "descn");
    }

    public String productid() {
        return (String)storedValueForKey("productid");
    }

    public void setProductid(String value) {
        takeStoredValueForKey(value, "productid");
    }

    public Category category() {
        return (Category)storedValueForKey("category");
    }

    public void setCategory(Category value) {
        takeStoredValueForKey(value, "category");
    }

    public NSArray items() {
        return (NSArray)storedValueForKey("items");
    }

    public void setItems(NSMutableArray value) {
        takeStoredValueForKey(value, "items");
    }

    public void addToItems(Item object) {
        NSMutableArray array = (NSMutableArray)items();

        willChange();
        array.addObject(object);
    }

    public void removeFromItems(Item object) {
        NSMutableArray array = (NSMutableArray)items();

        willChange();
        array.removeObject(object);
    }
}
