/*
 * ContentHelp.java
 * [Related Links Project]
 *
 * © Copyright 2005-2007 Apple, Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (“Apple”) in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms,
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under Apple’s copyrights in this original Apple
 * software (the “Apple Software”), to use, reproduce, modify and
 * redistribute the Apple Software, with or without modifications, in
 * source and/or binary forms; provided that if you redistribute the
 * Apple Software in its entirety and without modifications, you must
 * retain this notice and the following text and disclaimers in all such
 * redistributions of the Apple Software.  Neither the name, trademarks,
 * service marks or logos of Apple Computer, Inc. may be used to endorse
 * or promote products derived from the Apple Software without specific
 * prior written permission from Apple.  Except as expressly stated in
 * this notice, no other rights or licenses, express or implied, are
 * granted by Apple herein, including but not limited to any patent
 * rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated.
 *
 * The Apple Software is provided by Apple on an "AS IS" basis.  APPLE
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS
 * USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT,
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE,
 * REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE,
 * HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING
 * NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/**
 This class facilitates obtaining content from the related links site by creating
 a connection to the site (href), sending the request, receiving the response, and
 then correcting the related links data for illegal XML characters
 */
package webobjectsexamples.relatedlinks;

import com.webobjects.appserver.WOHTTPConnection;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;



public class ContentHelper extends Object {
    // User-Agent elements
    private final static String userAgent = "WebObjects";
    private final static String userAgentVersion = "5.2";
    private static String userAgentText;

    // static varaible for related link site
    private final static String rlSite = "www-rl.netscape.com";

    // static variable for the http version
    private final static String httpVersion = "HTTP/1.1";

    // static variable for the related link query
    private final static String rlQuery = "/wtgn?";

    static {
        String osname = System.getProperty("os.name");
        String osarch = System.getProperty("os.arch");
        String osversion = System.getProperty("os.version");
        userAgentText = userAgent + "/" + userAgentVersion + " [en] (" + osarch + "; " + osname + " " + osversion + ")";
    }
    /**
     * Method to, given a web site, return the related links as NSData, optionally
     * correcting it for known non-XML allowed characters.  This method takes two
     * arguments - the website URL and a boolean to optionally correct the data.
     *
     * @param webSite		the website URL to parse
     * @param correctXMLData	boolean, to optionally correct the response
     * @return			NSData, the website
     */

    public static String relatedLinksContent( String webSite, boolean correctXMLData ) {

        // make a connection to the specified site
        WOHTTPConnection connection = new WOHTTPConnection( rlSite, 80 );

        // related links site now requires the header "Accept-Language: en"
        NSMutableDictionary<String, NSArray<String>> headers = new NSMutableDictionary<String, NSArray<String>>();
        headers.setObjectForKey(new NSArray<String>("en"), "Accept-Language");
        headers.setObjectForKey(new NSArray<String>("iso-8859-1,*,utf-8"), "Accept-Charset");
        headers.setObjectForKey(new NSArray<String>("image/gif, image/x-xbitmap, image/jpeg, image/pjpeg, image/png, */*"), "Accept");
        headers.setObjectForKey(new NSArray<String>(userAgentText), "User-Agent");
        WORequest request = new WORequest("GET", rlQuery + webSite, httpVersion, headers, null, null);
        connection.sendRequest(request);
        WOResponse response = connection.readResponse();
        String contentData = (response != null) ? response.contentString() : null;
        if((contentData != null) && correctXMLData) {
            contentData=correctRelatedLinksData(contentData);
        }
        return contentData;
    }

    /**
     * Method to correct the data from the site due to the '&' and the quoting problem
     * in the XML vended from the related links site
     *
     * @param data	String for the related link site
     * @return 		corrected XML for the site
     */

    public static String correctRelatedLinksData(String data) {
        if (data == null) {
            return null;
        }
        // first pass, replace the '&' with escaped version
        StringBuffer sb=new StringBuffer(data);
        int sblength=sb.length();
        int i=0;
        while(i<sblength) {
        // only escape if not already escaped!
            if(sb.charAt(i)=='&') {
                if(sblength-i>=4 &&
                    sb.charAt(i+1)=='a' &&
                    sb.charAt(i+2)=='m' &&
                    sb.charAt(i+3)=='p' &&
                    sb.charAt(i+4)==';') {
                	// Already escaped
                    }else {
                        sb.insert(++i, "amp;");
                        sblength+=4;
                        i+=3;
                    }
            }
            ++i;
        }
        // the site doesn't quote the contents for the "type" type, so we have to do it here
        final String type="type=";
        int pos=0;
        i=0;
        while(i<sblength) {
            if(sb.charAt(i)==type.charAt(pos)) {
                ++pos;
                if(pos==type.length()) {
                    sb.insert(++i, '\"');
                    while(sb.charAt(i)!='/') {
                        ++i;
                    }
                    sb.insert(i, '\"');
                    i+=2;
                    sblength+=2;
                    pos=0;
                }
            } else {
                pos=0;
            }
            ++i;
        }
        return sb.toString();
    }
}
