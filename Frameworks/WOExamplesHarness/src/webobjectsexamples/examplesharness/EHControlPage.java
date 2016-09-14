/*
 EHControlPage.java
 [WOExamplesHarness Project]

© Copyright 2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */


package webobjectsexamples.examplesharness;

import java.util.Enumeration;
import java.util.Iterator;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;


public class EHControlPage extends WOComponent {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -825806723886941475L;
	public String  currentComponentTitle;
    public String  selectedComponentTitle;

    private static NSDictionary _examplesDictionary = null;
    private static NSMutableArray<String> _sortedComponentTitlesList = null;

    public EHControlPage(WOContext aContext)  {
        super(aContext);
        session().savePageInPermanentCache(this);
    }

    private static NSDictionary _allExamplesDictionary() {

		// Create a dictionary for the example information, and add the intro page
		NSMutableDictionary aMutableDictionary = new NSMutableDictionary();
		aMutableDictionary.setObjectForKey("EHIntroductionPage", "Introduction");

		// Get the information on the examples from the defined list
		NSMutableArray<NSBundle> aBundleArray = new NSMutableArray<NSBundle>();
		aBundleArray.addObject(NSBundle.mainBundle());
		aBundleArray.addObjectsFromArray(NSBundle.frameworkBundles());

		for (Iterator<NSBundle> iterator = aBundleArray.iterator(); iterator.hasNext();) {

			NSBundle aBundle = iterator.next();
			if (aBundle != null) {

				String propPath = aBundle.resourcePathForLocalizedResourceNamed("ExamplesProperties.plist", null);
				if (propPath != null) {

					NSDictionary aDictionary = (NSDictionary) JavaPropertyListUtilities.propertyListFromContentsOfFile(propPath);
					if (aDictionary != null) {

						NSDictionary examplesDict = (NSDictionary) aDictionary.objectForKey("ExamplePages");
						if (examplesDict != null) {
							aMutableDictionary.addEntriesFromDictionary(examplesDict);
						}
					}
				}
			}
		}
		return aMutableDictionary;
	}

    public static NSDictionary examplesDictionary()  {
        if (_examplesDictionary==null) {
            _examplesDictionary = EHControlPage._allExamplesDictionary();
        }
        return _examplesDictionary;
    }

    public static String pageNameForTitle(String aComponentTitle)  {
        NSDictionary aComponentNameDictionary = EHControlPage.examplesDictionary();
        String  aComponentName = (String)aComponentNameDictionary.objectForKey(aComponentTitle);
        return aComponentName;
    }

    public static NSArray<String> sortedComponentTitlesList()  {
        if (_sortedComponentTitlesList==null) {
            String  anIntroductionPageTitle = "Introduction";
            NSDictionary anExamplesDictionary = EHControlPage.examplesDictionary();

            Enumeration  aComponentTitlesList = anExamplesDictionary.keyEnumerator();
            _sortedComponentTitlesList = new NSMutableArray<String>();
            _sortedComponentTitlesList.addObject(anIntroductionPageTitle);
            while (aComponentTitlesList.hasMoreElements()) {
                String aTitle = (String)aComponentTitlesList.nextElement();
                if (!anIntroductionPageTitle.equals(aTitle)) {
                    char c = aTitle.toLowerCase().charAt(0);
                    int count = _sortedComponentTitlesList.count();
                    int i;
                    for (i = 1; i < count; i++) {
                        if (_sortedComponentTitlesList.objectAtIndex(i).toLowerCase().charAt(0) > c) {
                            break;
                        }
                    }
                    _sortedComponentTitlesList.insertObjectAtIndex(aTitle, i);
                }
            }
        }
        return _sortedComponentTitlesList;
    }

    public NSArray sortedComponentTitles() {
        return EHControlPage.sortedComponentTitlesList();
    }

    public static String introductionPageName()  {
        NSArray   aPageNameList = EHControlPage.sortedComponentTitlesList();
        String  aPageTitle = (String)aPageNameList.objectAtIndex(0);
        String  aPageName = EHControlPage.pageNameForTitle(aPageTitle);
        return aPageName;
    }

    /////////////
    // Actions
    /////////////
    public WOComponent componentTitleClicked()  {
        String aSelectedComponentName = pageNameForTitle( currentComponentTitle );
        WOComponent anExamplePage = pageWithName( aSelectedComponentName );
        return anExamplePage;
    }
}
