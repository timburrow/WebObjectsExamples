/*
 EHHeaderFooter.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.examplesharness;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;


public class EHHeaderFooter extends WOComponent {


    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2703987736008646272L;

	/**
     * Constructor
     */

    public EHHeaderFooter(WOContext aContext)  {
        super(aContext);
    }


	/**
	 * Override of method to set the synchronization policy for the component.
	 * Here we set it to false, to not synchronize the variables on this component
	 * with its bindings (from the parent component)
	 */

    @Override
	public boolean synchronizesVariablesWithBindings() {
        return false;
    }


    /**
     * Method to return the source view for a specific component.  The component name
     * is provided by the binding  "sourcePageName" and passed to the source viewing
     * component.
     */

    public WOComponent viewSourceClicked()  {

        WOComponent aComponent = pageWithName("EHSourceViewPage");
        String aComponentName = (String)valueForBinding("sourcePageName");
        if (aComponentName==null) {
            aComponentName = context().page().name();
        }
        aComponent.takeValueForKey(getNonQualifiedClassName(aComponentName), "componentName");
        return aComponent;
    }

    private String getNonQualifiedClassName( String qualifiedClassName ) {

        int dotIndex = qualifiedClassName.lastIndexOf(".");
        String name = (dotIndex != -1) ? qualifiedClassName.substring(dotIndex + 1) :
                                         qualifiedClassName;
        return name;
    }

}
