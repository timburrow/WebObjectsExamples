/*
 EHSourceView.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.examplesharness;

import java.net.URL;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPathUtilities;


public class EHSourceView extends WOComponent
{
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3127188017035534022L;
	private NSArray   _suffixes = null;
    public String  currentSuffix;
    private String  _componentName;
    public String  selectedSuffix = "html";

    public EHSourceView(WOContext aContext)  {
        super(aContext);
    }

    @Override
	public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    public NSArray suffixes()  {
        if (_suffixes==null) {
            NSMutableArray aMutableArray = new NSMutableArray();
            aMutableArray.addObject("html");
            aMutableArray.addObject("wod");
            WOApplication anApplication = WOApplication.application();
            WOResourceManager aResourceManager = anApplication.resourceManager();
            String  aComponentName = componentName();
            WOComponent aTempPage = pageWithName(aComponentName);
            String aFrameworkName = aTempPage.frameworkName();
            String aSourceFileName = aComponentName+".java";
            URL aSourcePathURL = aResourceManager.pathURLForResourceNamed(aSourceFileName , aFrameworkName, null);
            if (aSourcePathURL!=null) {
                aMutableArray.addObject("java");
            }
            _suffixes = aMutableArray;
        }
        return _suffixes;
    }

    public String componentName()  {
        if (_componentName==null) {
            _componentName = (String)valueForBinding("componentName");
        }
        return _componentName;
    }

    public String sourceString()  {
        String  aSourceFileString = null;
        String  aComponentName = componentName();
        String  aSourceFileName = aComponentName+"."+selectedSuffix;
        WOComponent aTempPage = pageWithName(aComponentName);
        String  aFrameworkName = aTempPage.frameworkName();
        String  aSourceFilePath = null;
        URL aWOPathURL = null;

        if (selectedSuffix.equals("wod") || selectedSuffix.equals("html")) {
            aWOPathURL = aTempPage.pathURL();
        	aSourceFilePath = NSPathUtilities.stringByAppendingPathComponent(aWOPathURL.getPath(), aSourceFileName);
        } else {
            WOApplication anApplication = application();
            WOResourceManager aResourceManager = anApplication.resourceManager();
            aWOPathURL = aResourceManager.pathURLForResourceNamed(aSourceFileName, aFrameworkName, null);
            aSourceFilePath = aWOPathURL.getPath();
        }
        if (aSourceFilePath!=null) {
            aSourceFileString = JavaPropertyListUtilities.stringFromFile( aSourceFilePath );
        } else {
            aSourceFileString = "Cannot locate source file named \""+aSourceFileName+"\" in framework \""+aFrameworkName+"\"";
        }
        return aSourceFileString;
    }

}