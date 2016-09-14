/*
 WXResponseLogger.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.util.Enumeration;

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;

/**
WXResponseLogger is a useful class for tracking exactly what your app is or is not returning in terms of headers, cookies, or just about anything else that might be associated with a returned WOResponse.  To actually perform logging, your WOApplication should call <code>logResponse</code>. The best place to do so is probably in a custom override of WOApplication's <code>dispatchRequest</code> before you call super.
 <p>
 Two sets of methods are provided, those that take the WOResponse as an argument and those that take the WOContext as argument. The latter methods log data associated with the context's current WOResponse.

 @see WXRequestLogger
 */
public class WXResponseLogger  {

    public static void logResponse(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logResponse for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXResponseLogger.logResponse(aContext.response());
            System.out.println("**** Finished logResponse for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logResponseHeaders(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logResponseHeaders for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXResponseLogger.logResponseHeaders(aContext.response());
            System.out.println("**** Finished logResponseHeaders for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logResponseContent(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logResponseContent for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXResponseLogger.logResponseContent(aContext.response());
            System.out.println("**** Finished logResponseContent for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method


    public static void logResponseProperties(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logResponseProperties for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXResponseLogger.logResponseProperties(aContext.response());
            System.out.println("**** Finished logResponseProperties for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logResponseCookies(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logResponseCookies for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXResponseLogger.logResponseCookies(aContext.response());
            System.out.println("**** Finished logResponseCookies for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logResponse(WOResponse aResponse){
        if(aResponse!=null){
            System.out.println("******** Begin logResponse ********");
            WXResponseLogger.logResponseProperties(aResponse);
            WXResponseLogger.logResponseHeaders(aResponse);
            WXResponseLogger.logResponseCookies(aResponse);
            WXResponseLogger.logResponseContent(aResponse);
            System.out.println("******** End logResponse ********");
        }
    }//end method

    public static void logResponseCookies(WOResponse aResponse){
        if(aResponse!=null){
            System.out.println("------------ Begin Response Cookies ------------");
            Enumeration en=aResponse.cookies().objectEnumerator();
            while(en.hasMoreElements()){
                System.out.println(en.nextElement());
            }
            System.out.println("------------ End Response Cookies ------------");
        }
    }//end method

    public static void logResponseContent(WOResponse aResponse){
        if(aResponse!=null){
            NSData data=aResponse.content();
            System.out.println("------------ Begin Response Content ------------");
            System.out.println(new String(data.bytes(0,data.length())));
            System.out.println("------------ End Response Content ------------");
        }
    }//end method


    public static void logResponseProperties(WOResponse aResponse){
        if(aResponse!=null){
            System.out.println("------------ Begin Response Properties ------------");
            System.out.println("httpVersion="+aResponse.httpVersion());
            System.out.println("http status="+aResponse.status());
            System.out.println("contentEncoding="+aResponse.contentEncoding());
            System.out.println("userInfo="+aResponse.userInfo());
            System.out.println("------------ End Response Properties ------------");
        }
    }//end method

    public static void logResponseHeaders(WOResponse aResponse){
        if(aResponse!=null){
            System.out.println("------------ Begin Response Headers ------------");
            Enumeration en=aResponse.headerKeys().objectEnumerator();
            while(en.hasMoreElements()){
                String hdr=(String)en.nextElement();
                Enumeration en1=aResponse.headersForKey(hdr).objectEnumerator();
                while(en1.hasMoreElements()){
                    System.out.println(hdr+":"+en1.nextElement());
                }
            }
            System.out.println("------------ End Response Headers ------------");
        }
    }//end method


}//end class
