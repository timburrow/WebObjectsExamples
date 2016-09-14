/*
 WXRadioButtonList.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/**
 This class and the associated component demonstrates how to create a list
 of HTML input radio button items.  This component uses a technique of having the
 WORepetition compute the form values for us (which may seem a little odd).
 It may well be clearer to simply use takeValuesFromRequest... in here and
 not use this trick.  However, the ability to ask an element for its elementID
 is logical and useful (as we use it for the umbrealla name here).

 Note in the case of this element, the "selections" from the form are passed
 in as an array of the INDEXES of the elements from the list (array) of
 displayed elements.  This requires the component to parse the list of displayed
 elements and obtain the object references.  The current items and selections
 are passed back up into the parent component using setValueForBinding().

 This component requires the following bindings:

 - (NSArray) list	a list of values for the radio buttons
 - (Object) item	an object to iterate through the list with
 - (Number) index	an object to iterate through the list with
 - (NSArray) selection	an array for the selected values

 */
package webobjectsexamples.wocomponentelements;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;


public class WXRadioButtonList extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6837634132235493222L;

	// Instance variable for the current item
    public Object currentItem;

    // Private instance variable for the selection
    private Object _selection;

    // Instance variable for the wrapper element ID
    public String wrapperElementID;


    /**
     * Constructor
     */

    public WXRadioButtonList( WOContext aContext )  {
        super( aContext );
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
     * Accessor method to set the current item from the list.  This method sets
     * the local instance variable for the current item AND pushes the value into
     * the parent through the binding "item".
     */

    public void setCurrentItem( Object aValue )  {
        currentItem = aValue;
        setValueForBinding( aValue, "item" );
    }


    /**
     * Methd to return the selections.  This method first checks the local
     * instance variable and, if null, takes the value from the binding "selection".
     * Note that the local instance variable is set to NULL after setSelection() is
     * performed, so the only thing the local instance variable can (ever) contain
     * is the selection set from the parent.
     */

    public Object selection()  {
        if ( _selection == null ) {
            _selection = valueForBinding( "selection" );
        }
        return _selection;
    }


    /**
     * Method to set the selection from the form values.  This method takes the
     * passed form value ('anIndex') which is the index of the selected item in the
     * list of displayed elements.  This method gets the object from the list and then
     * sets the object into the parent via the 'select' binding.  The local instance
     * variable is then set to null (so the next time a new value is pulled in from
     * the parent.
     */

    public void setSelection( Object anIndex )  {
        NSArray anItemList = (NSArray)valueForBinding( "list" );
        Object aSelectedObject = anItemList.objectAtIndex( Integer.parseInt( (String)anIndex ) );
        setValueForBinding( aSelectedObject, "selection" );
        _selection = null;
    }


    /**
     * Method to optionally add the "checked" attribute to the HTML tag for the
     * item.  This method checks to see if the selection is equal to the current item:
     * if it is, the "checked" attribute is added to the HTML (via the "otherTagString").
     * Otherwise, null is returned.
     */

    public String isCurrentItemSelected()  {
        if ( selection() == currentItem ) {
            return "checked";
        }
        return "";
    }
}