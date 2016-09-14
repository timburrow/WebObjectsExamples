/*
 WXPopUp.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/**
 This class and the associated component demonstrates the creation of an
 HTML form "SELECT" element (also known as a WOPopUpButton).  This element uses
 a couple of WOGenericContainers and a WORepetition to create the element,
 and normal form-value processing to handle the selection.

 This component requires the following bindings:

 - (NSArray) list		the list of elements to display
 - (Object) currentItem		the current (iterated) item from the list
 - (Number) currentIndex	the index of the current item
 - (Object) selection		the selected item from the list

 */
package webobjectsexamples.wocomponentelements;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

public class  WXPopUp extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3174002828991976401L;

	// Instance variable for the current item
    public Object currentItem;

    // Instance variable for the current item index
    public Number currentIndex;

    // Private instance variable for the current selection
    private Object _selection;


    /**
     * Constructor
     */

    public  WXPopUp( WOContext aContext )  {
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
     * Method to set the current item from the list.  This method sets the
     * current item in the local instance variable AND pushes the current
     * item into the parent through the bindings.
     */

    public void setCurrentItem( Object aValue )  {
        currentItem = aValue;
        setValueForBinding( currentItem , "item" );
    }


    /**
     * Method to set the current item from the list.  This method sets the
     * current item in the local instance variable AND pushes the current
     * item into the parent through the bindings.
     */

    public void setCurrentIndex( Number aValue )  {
        currentIndex = aValue;
        setValueForBinding( currentIndex , "index" );
    }


    /**
     * Method to return the selected object.ieons.  This method first checks
     * the local instance variable and, if null, obtains the value from the
     * 'selection' binding.  Note that the local instance variable is set
     * to null EACH TIME the 'setSelection' method is called, so the local
     * instance variable ONLY (ever) contains the selection set from the parent.
     */

    public Object selection()  {
        if ( _selection == null ) {
            _selection = valueForBinding( "selection" );
        }
        return _selection;
    }



    /**
     * Method to set the selection from the form value.  This method takes the
     * passed form value ('anIndex') which is the index in the list of displayed items
     * for the selected object.  This method takes the index, gets the object from the list
     * at that index, and then sets it into the parent via the 'selection' binding.  The
     * local instance variable is then set to null (ready to be set after the next pass).
     */

    public void setSelection( Object anIndex )  {
        NSArray  anObjectList = (NSArray)valueForBinding( "list" );
        Object aSelectedObject = anObjectList.objectAtIndex( Integer.parseInt( (String)anIndex ) );
	setValueForBinding( aSelectedObject , "selection" );
        _selection = null;
    }


    /**
     * Method to return the optional attribute "selected" for an OPTION item in the list
     * (set via the 'otherTagString').  This attribute is appended into the HTML tag,
     * thus making it render as a selection in the list.  The "selected" attribute is
     * only returned if the current object is the same as the selected object.  If it
     * is not, null is returned.
     */

    public String isCurrentItemSelected()  {
        if ( currentItem == selection() ) {
            return "selected";
        }
        return null;
    }
}

