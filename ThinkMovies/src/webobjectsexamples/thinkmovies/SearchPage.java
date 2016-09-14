/*
 * SearchPage.java [ThinkMovies Project] Copyright 2005 - 2007  Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software
 * in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the
 * Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE,
 * REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.thinkmovies;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

/**
 * This class and the associated component creates the search page for looking up movie information. The actual search is performed by a directAction: all this page does is take the form information and pass it along. Note that this page utilizes a WOForm tag with a directActionName binding, and all
 * of the form elements all have defined "name" bindings (rather than using the generated element IDs.) This is done so that the directAction for searching knows specifically which values are from which elements.
 */

public class SearchPage extends WOComponent {
	private static final long	serialVersionUID	= -7151168238523602861L;

	// Instance variable for the query dictionary for the search
	public NSMutableDictionary	queryDict;

	// Static instance variable set for the failed query key
	public static final String	QueryFailedKey		= "QueryFailed";

	/**
	 * Constructor - this method instantiates the query dictionary to a new NSMutableDictionary to get the form values
	 */

	public SearchPage(WOContext context) {
		super(context);
		queryDict = new NSMutableDictionary();
	}

	/**
	 * Method to check to see if the query failed. If the query dictionary still has a value for the QueryFailedKey, then return true: otherwise return false.
	 */

	public boolean didQueryFail() {
		return (queryDict.valueForKey(QueryFailedKey) != null);
	}

	/**
	 * Cover method to return the date operator list from the Application class to populate the pop-up button of options.
	 */

	public NSArray dateOperatorList() {
		return Application.dateOperators;
	}

	/**
	 * Cover method to return the text operator list from the Application class to populate the pop-up button of options.
	 */

	public NSArray textOperatorList() {
		return Application.textOperators;
	}

}
