/*
 WXWebUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

import org.xml.sax.InputSource;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOCookie;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver.WOSession;
import com.webobjects.appserver.xml.WOXMLCoder;
import com.webobjects.appserver.xml.WOXMLDecoder;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;

/**
WXUtilities includes miscellaneous methods related to or dependent on the WebObjects Framework. The class establishes a handy convention for setting WOF related constants such as the names of certain WOF headers.
*/
public class WXWebUtilities {

    // Use defaults
    public static final String WXServerPortHeader = "SERVER_PORT";
    public static final String WXServerPortHeaderX = "x-webobjects-server-port";
    public static final String WXServerNameHeader = "SERVER_NAME";
    public static final String WXServerNameHeaderX = "x-webobjects-server-name";

    // Set defaults
    public static final String WXServerPortSecureValue = "443";

    // Key used for secure response
    //private static final String WXResponseSecureKey = "WXResponseSecureKey";

   /**
     * Algorithm indicating if the passed in WORequest came in over a SSL link. If the HTTPS header
     * is present, then it was an SSL request and the method returns
     * <code>true</code>. If this header is missing, it still might
     * be SSL since not all adaptors/web servers send this header. So
     * next the method looks at the SERVER_PORT header if present. If that is
     * 443 then the method guesses it is SSL. Note that this is not absolute
     * so for your site you might have to implement the delegate
     * method <code>requestIsSecure</code>. And other web servers may send
     * different headers with different values to represent SSL. Therefore
     * you should never rely solely on this method, but incorporate it into
     * your own systems in such a way that your method can be subclassed.
     * <p>
     * Simply calls WXSecurityUtilities version of the method in release 1.1 of the
     * WXUtilities.framework
     *
     * @param r  	  a WORequest to be tested for SSL access
     * @return		  <code>true</code> to indicate that the request was secure
     */
    public static boolean isRequestSecure(WORequest request) {
        boolean issecure = false;
        try {
            //String serverPort = r.headerForKey("SERVER_PORT");
            String serverPort = WXWebUtilities.serverPort(request);
            String https = request.headerForKey("HTTPS");
            if(https != null)
                if(https.equals("ON"))
                    issecure = true;
            if(serverPort != null)
                if(serverPort.equals(WXServerPortSecureValue))
                    issecure = true;
        }
        catch (Throwable e) {
        	e.printStackTrace();
        }
        return issecure;
    }

    /**
     * Extracts a WOXMLDecoder from file with the passed in filename located in
     * the framework with the passed in name.
     * The method borrows some code from a WO example to
     * test for the OS type because the file URL passed to WOXMLDecoder is different
     * for Windows vs. other OSes.
     *
     * @param filename     file with XML model file
     * @param framework    framework resources where file resides
     * @return             the WOXMLDecoder found in the file & framework
     */
    public static WOXMLDecoder xmlDecoderWithFilename(String filename, String framework){
        WOApplication app = WOApplication.application();
        WOResourceManager rm = app.resourceManager();
        URL pathURL = rm.pathURLForResourceNamed(filename, framework, null);
        String mappingURL = pathURL.toExternalForm();
        WOXMLDecoder decoder = WOXMLDecoder.decoderWithMapping(mappingURL);
        return decoder;
    }


    /**
     * returns an XML string of an object graph rooted at the object toArchive
     *
     * @param toArchive     Object to archive into XML
     * @return				String of XML
     */
    static public String xmlForObject( Object toArchive ) {
        String name = toArchive.getClass().getName();
        return WOXMLCoder.coder().encodeRootObjectForKey(toArchive, name);
    }


    /**
     * Stores an object graph rooted at the object toArchive to a file
     *
     * @param toArchive     Object to archive into XML
     * @param archiveFile   name of file (path) to store XML
     */
    static public void archiveToXMLAtPath( Object toArchive, String archiveFile ) {
        String xmlString = WXWebUtilities.xmlForObject( toArchive );
        try {
            FileOutputStream out = new FileOutputStream(archiveFile);
            out.write(xmlString.getBytes());
            out.close();
        } catch (IOException e) {
            System.out.println("WXUtilities: exception archiving to file" + archiveFile + ": " + e.toString());
            e.printStackTrace();
        }
    }


    /**
     * Returns an object graph rooted in the object returned from the
     * XML parsed from the passed in XML String
     *
     * @param String		XML to parse
     * @return              object archived from XML
     */
    static public Object objectFromXML( String xmlString ) {
        return (WOXMLDecoder.decoder().decodeRootObject( xmlString ));
    }


    /**
     * Returns an object graph rooted in the object returned from the
     * XML parsed from the passed in filename. Returns null if the
     * cannot be accessed. Uses the default XML model.
     *
     * @param archiveFile   name of file (path) with XML to parse
     * @return              object archived from XML
     */

    static public Object restoreFromXMLAtPath( String archiveFile ) {
        InputSource in = null;
        try {
            in = new InputSource( new FileInputStream( archiveFile ) );
        } catch (IOException e) {
            System.out.println( "WXUtilities: exception restoring: " + e.toString() );
            e.printStackTrace();
            return null;
        }
        return ( WOXMLDecoder.decoder().decodeRootObject( in ) );
    }


    /**
     * Returns the complete URL including cgi-key, app name, instance number,
     * action name, sesion ID (as formvalue for DA, in-URL path for CA).
     * form values found in the request are appended to the URL in GET-style
     * format. The protocol and hostname are not on this URL.
     * <p>
     * The method is useful when you want to capture the URL that invoked an
     * action so that you can later redirect to it this re-invoking the same
     * action.
     *
     * @param req  request object to construct a URL for
     * @param s  session for the request, can be null
     * @param custFormValues dict of custom form values to append to the URL
     *                        override formvalues of the same key name that
     *                        were in the request
     * @return URL for the given request.
     */
    public static String completeUrlFromRequest(WORequest req, WOSession s, NSDictionary custFormValues){

        // Here we gather the formvalues from the context's request
        NSMutableDictionary formValues = new NSMutableDictionary();
        NSDictionary reqFormValues = req.formValues();
        String formValuesForURL = null;
        if (reqFormValues != null) {
            formValues = reqFormValues.mutableClone();
        }
        if(custFormValues != null)
            formValues.addEntriesFromDictionary(custFormValues);

        // Here we decide if there is an existing session and if
        // so set a String representing the session ID and set
        // a flag indicating a session was found.
        String sid = WXUtilities.EmptyString;
        boolean hasSession = false;
        if (s != null) {
        	hasSession = true;
            sid = s.sessionID();
            formValues.setObjectForKey(sid,"wosid");
      }

        String directActionURLFragment = WXUtilities.EmptyString;
        if(req.requestHandlerKey().equals("wa")){
            directActionURLFragment = "/"+req.requestHandlerKey()+"/" + req.requestHandlerPath();
        }else{
            String hkey = "/"+req.requestHandlerKey();
            if(hasSession){
                directActionURLFragment = hkey + "/" + sid;
                formValues.removeObjectForKey("wosid");
            }
        }

        // Next we decide if this a component action or direct action
        // we need to return to after the login app succeeds. This decision
        // affects additional formvalues that will have to get included on
        // the return URL. It also affects a part of the return URL affecting
        // action handling (directActionURLFragment).

        formValuesForURL = WXUtilities.formValueURIFromDictionary(formValues);

        WOApplication app = WOApplication.application();
        String destAppReturnURL = req.adaptorPrefix() + "/" + app.name() + ".woa/" +
            req.applicationNumber() + directActionURLFragment + "?" + formValuesForURL;
        return destAppReturnURL;
    }

    /**
     * Returns the web server port that received the browser's request.
     * Usually this is "80". For secure SSL requests, it is ususally "443".
     * Looks in the headers named "SERVER_PORT" and "x-webobjects-server-port".
     *
     * @param request WORequest to extract a web server port from
     * @return  web server port.
     */
    public static String serverPort(WORequest request) {
        String value;
        value = request.headerForKey(WXServerPortHeader);
        if (value != null) return value;
        value = request.headerForKey(WXServerPortHeaderX);
        if (value != null) return value;
        return null;
    }

    /**
     * Returns the web server host name that received the browser's request.
     * Looks in the headers named "SERVER_NAME" and "x-webobjects-server-name".
     *
     * @param request WORequest to extract a web server name from
     * @return  web server host name.
     */
    public static String serverName(WORequest request) {
        String value;
        value = request.headerForKey(WXServerNameHeader);
        if (value != null) return value;
        value = request.headerForKey(WXServerNameHeaderX);
        if (value != null) return value;
        value = request.headerForKey("SERVER_NAME");
        if (value != null) return value;
        return null;
    }


    /**
     * Takes content, headers, and cookies from the passed in fromResp and
     * applies them to the passed in toResp.
     *
     * @param fromResp WOResponse to extract properties from
     * @param toResp WOResponse to apply properties to
     */
    public static void applyResponseToResponse(WOResponse fromResp, WOResponse toResp) {
		for (Iterator<String> iterator = fromResp.headerKeys().iterator(); iterator.hasNext();) {
			String key = iterator.next();
			String header = fromResp.headerForKey(key);
			toResp.setHeader(header, key);
		}

		for (Iterator<WOCookie> iterator = fromResp.cookies().iterator(); iterator.hasNext();) {
			toResp.addCookie(iterator.next());
		}

		toResp.setHTTPVersion(fromResp.httpVersion());
		toResp.setContent(fromResp.content());
	}


    /**
	 * Returns the current document root for the configured webserver, as defined in the "WebServerConfig.plist" file. This method takes one argument, the installation path of the WebObjects distribution (typically "C:/Apple" or "/Apple" ). We cannot get that from the system, because it's an
	 * environment variable and Java doesn't like those.
	 *
	 * @param installPath
	 *            String, the WebObjects distribution install path (e.g. "C:/Apple")
	 * @return String, the current document root path
	 */
    public static String documentRootPathFromInstallPath( String installPath ) {

        // Get the WebServerConfig.plist file
        File configPlist = new File( new String( installPath + "/Library/WebObjects/Configuration/" ), "WebServerConfig.plist" );
        NSDictionary configDict = (NSDictionary)WXFileUtilities.objectFromPListFile( configPlist );

        // Return the content
        return (String)configDict.objectForKey( "DocumentRoot" );
    }

}
