/*
 Application.java
 [VirtualStoreLiteX Project]

© Copyright 2005-2007 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.virtualstorelite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPropertyListSerialization;

public class Application extends com.webobjects.appserver.WOApplication {

    public NSDictionary products;
    public NSArray hotspot;

    public static void main(String argv[]) {
        WOApplication.main(argv, Application.class);
    }

    public static Object objectFromPListFile(File f) throws IOException {
        return NSPropertyListSerialization.propertyListFromString(stringFromFile(f));
    }

    static public String stringFromFile(File f) throws IOException {
        if (f==null)
            throw new IOException("null file");
        int size=(int) f.length();
        FileInputStream fis=new FileInputStream(f);
        byte [] data = new byte[size];
        int bytesRead=0;
        while (bytesRead<size)
            bytesRead+=fis.read(data,bytesRead,size-bytesRead);
        fis.close();
        return new String(data);
    }

    public Application() throws Exception {
        super();
        NSDictionary dict;
        java.net.URL pathURL = resourceManager().pathURLForResourceNamed("VirtualStore.plist",null,null);
        dict = (NSDictionary) NSPropertyListSerialization.propertyListWithPathURL(pathURL);
        hotspot = (NSArray) dict.objectForKey( "Hotspot");
        products = (NSDictionary) dict.objectForKey( "Products");
        NSLog.debug.appendln("Welcome to " + name() + " !");
    }

    public WOResponse handleSessionRestorationErrorInContext(WOContext aContext) {
        WOComponent nextPage;
        nextPage = pageWithName("RestartPage", aContext);
        return nextPage.generateResponse();
    }

    public NSArray projectSearchPath() {
        return new NSArray();
    }
}
