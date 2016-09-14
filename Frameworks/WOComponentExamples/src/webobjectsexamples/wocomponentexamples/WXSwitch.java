/*
 WXSwitch.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/**
 This class is an example of a dynamic element (extending WODynamicElement) which
 creates a "switch" element (much like a WOSwitchComponent).
 */

package webobjectsexamples.wocomponentexamples;

import com.webobjects.appserver.*;
import com.webobjects.appserver._private.*;
import com.webobjects.foundation.*;
import java.util.*;


public class WXSwitch extends WODynamicElement {

    // Instance variable for the case
    private WOAssociation _case;

    // Instance variable for the child cases
    private NSMutableDictionary _childCases;


    /**
     * Private method to insert a case value for a child:  this method takes a
     * child as an argument and (if the child is a WXCase element) sets the case
     * value of the child into all of the child cases.
     */

    private void _insertCaseValueForChild( WOElement aChild )  {

        if ( aChild instanceof WXCase ) {
            Object aCaseValue = ( (WXCase)aChild ).caseValue();
            _childCases.setObjectForKey( aChild, aCaseValue );

        } else if ( !( aChild instanceof WOHTMLBareString ) ) {
            throw new RuntimeException( "<" + getClass().getName() + ">: improper type of child.  Found " +
					aChild.getClass().getName());
        }
    }


    /**
     * Private method to initialize the child cases from a template.  This method
     * takes a WOTemplate as an argument and, if the template is an instance of
     * WODynamicGroup (WXCase), enumerates over all of the child cases and inserts
     * the template (as the case value) for each one.
     */

    private void _initChildCasesFromTemplate( WOElement template )  {

        _childCases= new NSMutableDictionary();
        if ( template instanceof WODynamicGroup ) {
            Enumeration aChildEnumerator = null;
            ((WODynamicGroup)template).childrenElements();
            WOElement aChild = null;
            while (aChildEnumerator.hasMoreElements()) {
                aChild = (WOElement)aChildEnumerator.nextElement();
                _insertCaseValueForChild(aChild);
            }
        } else {
            _insertCaseValueForChild(template);
        }
    }


    /**
     * Constructor.  This metho takes three arguments - the name of the element, the
     * dictionary of associations for the element, and the template for the element.
     * This is the standard constructor for a dynamic element.
     */

    public WXSwitch( String aName, NSDictionary associations, WOElement template )  {
        super(null, null, null);
        _case = (WOAssociation)associations.objectForKey( WXCase.WOHTMLCaseAttribute );
        _initChildCasesFromTemplate( template );
    }


    /**
     * Private method to return the child case for the current component in the
     * context.
     */

    private WOElement _childCaseInContext( WOContext aContext )  {

	WOElement aChild = null;
        WOComponent aComponent = aContext.component();
        Object aCaseValue = _case.valueInComponent(aComponent);

	if (null==aCaseValue) {
            aCaseValue = "default";
        }

	aChild = (WOElement)_childCases.objectForKey(aCaseValue);
        if (null==aChild) {
            aChild = (WOElement)_childCases.objectForKey("default");
        }
        return aChild;
    }


    /**
     * Method to override takeValuesFromRequest to get the child case from the
     * context and send the take values to it.
     */

    public void takeValuesFromRequest(WORequest aRequest, WOContext aContext)  {
        WOElement aChildCase = _childCaseInContext(aContext);
        aChildCase.takeValuesFromRequest(aRequest, aContext);
    }


    /**
     * Method to override invokeAction to get the child case from the
     * context and send the invokeAction to it.
     */

    public WOActionResults invokeAction(WORequest aRequest, WOContext aContext)  {
        WOElement aChildCase = _childCaseInContext(aContext);
        WOActionResults anElement = aChildCase.invokeAction(aRequest, aContext);
        return anElement;
    }


    /**
     * Method to override appendToResponse to get the child case from the
     * context and send the appendToResponse to it.
     */

    public void appendToResponse(WOResponse aResponse, WOContext aContext)  {
        WOElement aChildCase = _childCaseInContext(aContext);
        aChildCase.appendToResponse(aResponse, aContext);
    }


    /**
     * toString() method for this element, to return the class name and
     * the class names of all of the child cases
     */

    public String toString() {
        return "<"+getClass().getName()+" "+_childCases.toString()+" >";
    }
}