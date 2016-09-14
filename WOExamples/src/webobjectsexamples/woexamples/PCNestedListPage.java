/*
 PCNestedListPage.java
 [WOExamples Project]

© Copyright 2005-2007 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.woexamples;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;


/*
 This class and the associated component demonstrates the WXNestedList
 component element (found in the ComponentElements framework).
 */

public class PCNestedListPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1617058780827454472L;
	// Instance variables for the current and selected items
    public Object currentItem;
    public Object selectedItem;

    // Instance variables for the index and level for the current item
    public Number  currentIndex;
    public Number  currentLevel;


    /**
     * Constructor for the component
     */

    public PCNestedListPage(WOContext aContext)  {
        super(aContext);
    }


    /**
     * Method to return a boolean value is the list is ordered;  this method
     * returns true if the current level is 1 or 2, otherwise it returns false
     */

    public boolean isOrderedList() {
        int cLevel = currentLevel.intValue();
        if (cLevel == 1) {
            return true;
        } else if (cLevel == 2) {
            return true;
        } else if (cLevel == 3) {
            return false;
        } else if (cLevel == 4) {
            return false;
        }
        return true;
    }


    /**
     * Method to set the selected item from the current item, and log the
     * information to the console.  Then return the current component.
     */

    public WOComponent nestedListClicked() {
        //debugString( "**** WXNestedList Clicked *****");
        //debugString( "currentItem == "+ currentItem);
        selectedItem = currentItem;
        return null;
    }


    /**
     * Method to get the 'isNew' value from the current item
     */

    public Object getIsNew() {
        return ( (NSDictionary)currentItem ).objectForKey( "isNew" );
    }


    /**
     * Method to get the 'sublist' value from the current item
     */

    public Object getSublist() {
        return ( (NSDictionary)currentItem ).objectForKey( "sublist" );
    }


    /**
     * Method to get the 'label' value from the current itemConstructor for the component
     */

    public Object getLabel() {
        return ( (NSDictionary)currentItem ).objectForKey( "label" );
    }

}