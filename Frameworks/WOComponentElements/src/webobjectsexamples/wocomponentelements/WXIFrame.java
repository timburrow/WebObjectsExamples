/*
 WXIFrame.java
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
 HTML "iFrame" element (created in the IE spec intially).  This is a "floating"
 frame which can be embedded into another window, making it useful for some
 viewing elements.

 This component is similar to the WXFrame component in design, but offers
 additional binding options for the content of the frame.  This component
 requires one of the following bindings:

 - (String) src 	the source URL for the content of the frame
 - (String) pageName	the pageName for the content of the frame
 - (String) value	the WOActionResult for the content of the frame.

 If a 'src' binding is present it is used as the source for the content;
 otherwise, if a 'pageName' or 'value' binding is present, the context's
 componentActionURL is used for the source (the true value of which is
 determined later by the invokeAction method.

 */
package webobjectsexamples.wocomponentelements;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOElement;

public class WXIFrame extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6135290930522546788L;


	/**
     * Constructor
     */

    public WXIFrame(WOContext aContext)  {
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
     * Method to return the source URL for the frame content.  If a
     * 'src' binding exists, it is used for the URL; otherwise, if a
     * 'pageName' or 'value' binding exists, the context's componentActionURL
     * is used.  If none exist, an error string is returned.
     */

    public String srcUrl()  {

        if ( hasBinding( "src" ) ) {
            return (String)valueForBinding( "src" );
        }
        if ( hasBinding( "pageName" ) || hasBinding( "value" ) ) {
            return context().componentActionURL();
        }
        return "ERROR_URL_NOT_FOUND";
    }


    /**
     * Method to return the content for the frame from the "pageName" or
     * "value" binding.  Much like with the WOHyperlink, this method is performed
     * from the invokeAction binding for the element, and returns the appropriate
     * content based on the available binding information.
     */

    public WOElement getFrameContent()  {

        WOElement aContentElement = null;
        if ( hasBinding( "pageName" ) ) {
            String  aPageName = (String)valueForBinding( "pageName" );
            aContentElement = pageWithName( aPageName );
        } else {
            aContentElement = (WOElement)valueForBinding( "value" );
        }
        return aContentElement;
    }
}
