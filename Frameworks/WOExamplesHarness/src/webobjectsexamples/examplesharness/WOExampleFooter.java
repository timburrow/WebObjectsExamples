/*
 * WOExampleFooter.java
 * [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.examplesharness;

import java.io.File;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;


public class WOExampleFooter extends WOComponent {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3552663400716313382L;
	// Instance variable for the project source path (if found)
	public String projectPath;


	/**
	 * Constructor
	 */

    public WOExampleFooter( WOContext context ) {
        super( context );
		setProjectPath( searchForProject() );
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
	 * Method to open the project source file (Project Builder) file.  This method
	 * is only performed if the project search path is found;  if not, there is a
	 * conditional that will not show the hyperlink to perform this method.  To
	 * open the project we use the 'open' command with the JavaCommandShell.
	 */

	public WOComponent openProject() {
		/* NSDictionary output = */JavaCommandShell.invokeCommand(new String("open " + projectPath));
		return null;
	}


	/**
	 * Method to find the project file (Project Builder/Xcode file) for the running appplication. If it is found, it is set as the project path.
	 */

	public String searchForProject() {

		String returnString = null;

		// Get the current path, and look for the project folder above
		// String mainPath = NSBundle.mainBundle().bundlePath();

		String currentPath = System.getProperty( "user.dir" );
		String projPath = currentPath + "/../../" + application().name() + ".pbproj";
		String xcodeprojPath = currentPath + "/../../" + application().name() + ".xcodeproj";

		// If the project source file exists, set it as the return string
		File projectFile = new File( projPath );
		if ( projectFile.exists() ) {
			returnString = projPath;
		} else {
			projectFile = new File( xcodeprojPath );
			if ( projectFile.exists() ) {
				returnString = xcodeprojPath;
			}
		}

		return returnString;
	}


	/**
	 * Accessor for project source path
	 */

	public void setProjectPath( String aPath ) {
		projectPath = aPath;
	}

}
