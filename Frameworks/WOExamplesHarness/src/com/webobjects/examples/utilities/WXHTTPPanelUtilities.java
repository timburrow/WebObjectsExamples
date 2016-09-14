/*
 WXHTTPPanelUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.StringTokenizer;

import sun.misc.BASE64Decoder;

import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;

public class WXHTTPPanelUtilities  {

    /**
     * Modifies the passed in response to prompt for an HTTP challenge panel
     * (the 401 status authorization request panel). You can customize the
     * the error page shown if the user cancels or repeatedly fails authorization
     * using a passed in WOResponse or error HTML text.
     *
     * @param aResponse   WOResponse to turn into an HTTP challenge panel
     * @param aRealm      The URL realm that this response applies to; if null
     *                    a GUID is calculated and used
     * @param errorText   Error page HTML to show if auth fails and error response is null.
     * @param errorResponse   Error response to show if auth fails.
     */
    public static void responseForAuthorizationPrimitive(WOResponse aResponse, String realm, String errorHTML, WOResponse errorResponse){
        NSData errorBytes;
		if (errorResponse == null) {
			String anErrorHTML;
			if (errorHTML != null) {
				anErrorHTML = errorHTML;
			} else {
				anErrorHTML = "HTTP/1.0 401 Unauthorized Access  <br><br>   <font color=ff0000><b>Unauthorized</b> Access Attempted</font>";
			}
			errorBytes = new NSData(anErrorHTML.getBytes());
		} else {
			errorBytes = errorResponse.content();
		}
		// this gives me a different space every time
        String aRealm = realm;
        if(aRealm == null) aRealm = WXUtilities.generateGUID();

        aResponse.setStatus(401); // authorization request
        aResponse.setHeader("Basic realm=\"" + aRealm + "\"" , "WWW-Authenticate");
        aResponse.setContent(errorBytes);
        aResponse.setHeader(new Integer(errorBytes.length()).toString(), "content-length");
        aResponse.setHeader("text/html" , "content-type");
    }


    /**
     * Extracts the decoded authorization header as a string from the raw Base64
     * encoded data in the appropriate WORequest header. This header is used
     * in HTTP_PANEL authorization and holds the username/password entered
     * by a user in an HTTP challenge panel. If your web server
     * is not passing you the "authorization" header, this method will raise a
     * runtime exception.
     * <p>
     * Note that DirectConnect and the CGI interface do
     * <b>not</b> pass this header. NSAPI does. Other interfaces like ISAPI
     * and Apache mod should but this method was only tested against NSAPI.
     * Once the contents of the header are obtained, those contents still look
     * like jibberish because the string is actually Base64 encoded data. So we
     * take the string, get the bytes, and pass these to a Base64 decoder
     * available in the Sun JDK, and create a decoded string of plain text
     * from this.
     *
     * @param aReq	WORequest to attempt to extract a authorization string
     *	                as clear text.
     * @return          Decoded plain text of the authorization header
     */
    public static String authStringFromRequest(WORequest aReq){
        String encodedAuth=null, decodedAuth=null;

        BASE64Decoder decoder=new BASE64Decoder();
        // encoded string starts after "Basic "
        encodedAuth=aReq.headerForKey("authorization");
        if(encodedAuth==null)
            throw new RuntimeException("No 'authorization' header in request. Is your http server, web server WOAdaptor, or web server interface setup to pass on this header?");
        encodedAuth=encodedAuth.substring(encodedAuth.indexOf(" ")+1);
        try{
            decodedAuth=new String(decoder.decodeBuffer((new ByteArrayInputStream(encodedAuth.getBytes()) )));
        } catch(IOException ex) {System.out.println("IOException while decoding auth header for HTTP_PANEL login method:"+ex);}
        return decodedAuth;
    }

    /**
     * Extracts the username string from a decoded authorization string.
     * Use <code>authStringFromRequest</code> to get the decoded auth string used
     * as an argument to the method. If auth is null, returns an empty string.
     *
     * @param auth	decoded authorization header string
     * @return 	        username string found within the auth string
     * @see #authStringFromRequest
     */
    public static String userNameFromDecodedAuthString(String auth) {
        if(auth!=null){
            StringTokenizer st=new StringTokenizer(auth,":",true);
            String username;
            if(( username=st.nextToken())!=null){
                if(!username.equals(":")){
                    return username;
                }
            }
        }
        return "";
    }

    /**
     * Extracts the password string from a decoded authorization string.
     * Use <code>authStringFromRequest</code> to get the decoded auth string used
     * as an argument to the method. If auth is null, returns an empty string.
     *
     * @param auth	decoded authorization header string
     * @return 	        password string found within the the auth string
     * @see #authStringFromRequest
     */
    public static String passwordFromDecodedAuthString(String auth ) {
        if(auth!=null){
            StringTokenizer st=new StringTokenizer(auth,":",true);
            if(st.countTokens()==3){
                st.nextToken(); st.nextToken();
                return st.nextToken();
            }
            else if(st.countTokens()==2){
                st.nextToken();
                String passwd=st.nextToken();
                if(!passwd.equals(":"))
                    return passwd;
            }
        }
        return "";
    }


}
