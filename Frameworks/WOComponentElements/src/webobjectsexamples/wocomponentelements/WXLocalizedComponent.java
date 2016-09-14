/*
 WXLocalizedComponent.java
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
 This class is part of the demonstration of localization shown in the
 WXLocalizedString class and associated component.  This component provides
 most of the implementation of the string localization methods to be used
 in the other component (done by extending this class, rather than WOComponent.
 */

public class WXLocalizedComponent extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1310028227909284073L;

	// Private instance variable for the table name
    private String  _tableName;

    // Private instance variable for the framework name
    private String  _frameworkName;


    /**
     * Constructor
     */

    public WXLocalizedComponent(WOContext aContext)  {
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
     * Method to return the framework name.  This method first checks the local
     * instance variable and, if null, takes the value from the "framework" binding.
     * If the value from the binding is null as well, we assume the table is in the
     * parent component's framework and use that.  If the parent component is null
     * (if this is a top-level component) then we take the framework name from the
     * superclass.
     */

    public String frameworkName()  {

        if (_frameworkName==null) {
            _frameworkName = (String)valueForBinding( "framework" );
            if ( _frameworkName == null ) {
                WOComponent aParent = parent();
                if ( aParent != null) {
                    _frameworkName = aParent.frameworkName();
                } else {
                    _frameworkName = super.frameworkName();
                }
            }
        }
        return _frameworkName;
    }


    /**
     * Method to return the table name for the localization information.  This method
     * first checks the local instance variable, and then the "table" binding for
     * the component.  If both are null, the table name defaults to "Localizable".
     */

    public String tableName()  {
        if (_tableName==null) {
            _tableName = (String)valueForBinding("table");
            if (_tableName==null) {
                _tableName = "Localizable";
            }
        }
        return _tableName;
    }


    /**
     * Method to return a localized version of a string.  This method takes a single
     * string as an argument and returns a localized version using the WOResourceManager
     * (and the tableName, frameworkName, and the array of languages from the session.
     *
     * @param aKeyString	the string to localize
     * @return			a localized version of the string argument
     */

    public String localizeString( String aKeyString )  {

        String  aLocalizedString = null;
        WOResourceManager rm = application().resourceManager();
        aLocalizedString = rm.stringForKey( aKeyString,
					    tableName(),
					    aKeyString,
					    frameworkName(),
					    session().languages() );
        return aLocalizedString;
    }
}
