/*
 WXCase.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/**
 This class is an example of a dynamic element (extending WODynamicGroup) which
 creates a "case" element.
 */
package webobjectsexamples.wocomponentexamples;

import com.webobjects.appserver.WOAssociation;
import com.webobjects.appserver.WOElement;
import com.webobjects.appserver._private.WODynamicGroup;
import com.webobjects.foundation.NSDictionary;

public class WXCase extends WODynamicGroup {

    // Static instance variable for the case attribute
    public static String WOHTMLCaseAttribute = "case";

    // Instance variable for the case value
    private Object _caseValue;


    /**
     * Accessor method to return the value of the private instance variable
     * for the case value.
     */

    public Object caseValue() {
        return _caseValue;
    }


    /**
     * Constructor.  This metho takes three arguments - the name of the element, the
     * dictionary of associations for the element, and the template for the element.
     * This is the standard constructor for a dynamic element.
     */

    public WXCase( String aName, NSDictionary associations, WOElement template )  {

        super( aName, null, template );
        WOAssociation aCaseAssociation = (WOAssociation)associations.objectForKey( WOHTMLCaseAttribute );
        if ( aCaseAssociation.isValueConstant() ) {
            _caseValue = aCaseAssociation.valueInComponent( null );
        } else {
            throw new IllegalArgumentException( "<" + getClass().getName() +
						": case attribute must be constant -- found '" +
						aCaseAssociation+"'.");
        }
    }
}