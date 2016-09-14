/*
 WXBody.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wocomponentelements;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;


/**
 This class and the associated component demonstrates the creation of an
 HTML body tag, optionally taking a color and resource image for the background.
 This component demonstrates the use of the WOResourceManager to return the URL
 for a resource, as well as the use of a WOGenericContainer.

 This component has the following optional bindings:

 - (String) filename		the name of the resource for the background
 - (String) framework		the framework the resource image is in
 - (String) bgcolor		the background color for the page

 */

public class WXBody extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5303432509321809433L;


	/**
     * Constructor
     */

    public WXBody(WOContext aContext)  {
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
     * Method to return the background color for the page.  This method first
     * attempts to get the value from the bindings:  if no 'bgcolor' binding
     * value is found, then a default color (white) is returned.
     */

    public String bgColor()  {
        String aColorString = (String)valueForBinding( "bgcolor" );
        if ( aColorString == null ) {
            aColorString = "#ffffff";
        }
        return aColorString;
    }


    /**
     * Method to return the URL for the background image.  This method uses the
     * WOResourceManager to return the URL for the image named in the 'filename'
     * binding.  If a 'framework' binding exists, it is also used by the resource
     * manager.
     */

    public String bgImageUrl()  {

	String imageURL = null;

	if ( hasBinding( "filename" ) ) {

	    WOResourceManager rm = application().resourceManager();
	    String aFilename = (String) valueForBinding( "filename" );
	    String  aFramework = (String) valueForBinding("framework");
	    imageURL = rm.urlForResourceNamed( aFilename,
					       aFramework,
					       context().session().languages(),
					       context().request() );
	}

	return imageURL;
    }
}