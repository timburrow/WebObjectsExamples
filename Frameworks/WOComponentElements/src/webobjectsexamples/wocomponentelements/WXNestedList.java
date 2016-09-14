/*
 WXNestedList.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/**
 This class and the associated component demonstrates the creation of a
 nested list (a list of lists) using a recursion (basically this component
 embedding itself).  This component uses a generic element and a repetition
 to create the list, showing content from the parent for each link.

 This component also demonstrates the use of a WOMethodInvokation element,
 used here to push and pop the index for the list (so we know how "deep" we
 are in the list).

 This component requires the following bindings:

 - (NSArray) list		the list of items to show
 - (Object) item		an object to iterate through the list with
 - (Number) index		a number to represent the index of the current item
 - (Object) isOrdered		an object to turn ordering on or off
 - (WOActionResults) action	an action to perform when an item is clicked

 */
package webobjectsexamples.wocomponentelements;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class WXNestedList extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2382366667551066575L;


	/**
     * Constructor
     */

    public WXNestedList(WOContext aContext)  {
        super(aContext);
    }


    /**
     * Override of method for synchronization of local instance variables with
     * bindings (pushing and pulling values from the bindings).  Here we turn
     * OFF synchronization.
     */

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }


    /**
     * Method to push the current level (always 1) into the parent;  this always
     * happens at the top/beginning of a list (to let the parent know we are in
     * the list
     */

    public void pushLevel()  {
        setValueForBinding( new Integer(1) , "level" );
    }


    /**
     * Method to push the current level (always 0) into the parent;  this always
     * happens at the bottom/end of a list (to let the parent know we are done with
     * the list
     */

    public void  popLevel()  {
        setValueForBinding( new Integer(0) , "level" );
    }


    /**
     * Method to return the current level.  This method always returns null and is
     * basically a no-op, but it is required by Key-Value coding (since we have a
     * setCurrentLevel method).
     */

    public Number currentLevel() {
        return null;
    }


    /**
     * Method to set the current level (based on the child level).  Whatever the
     * child passes in, we add one (to represent another level deep in the order).
     * By the time the value get to the root, it reflects the total number of levels
     * between the top and bottom.
     */

    public void setCurrentLevel(Number aChildLevel)  {
        setValueForBinding(new Integer(aChildLevel.intValue() + 1) , "level");
    }


    /**
     * Method to return the tag name for the list.  If the 'isOrdered' binding is
     * present, the list is an ORDERED-LIST (<OL>), otherwise the list is an
     * UNORDERED LIST (<UL>).  This information populates the elementName of the
     * generic element for the list.
     */

    public String listTagName()  {
        if ( valueForBinding( "isOrdered" ) != null ) {
            return "ol";
        }
        return "ul";
    }
}
