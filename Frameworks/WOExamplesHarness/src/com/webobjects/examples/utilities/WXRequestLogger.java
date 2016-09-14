/*
 WXRequestLogger.java
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
import com.webobjects.appserver.WORequest;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;


/**
WXRequestLogger is a useful class for tracking exactly what form values, headers, and cookies arrive at your app from the client browser as well as any headers (among other settings) that come in from the web server and WOAdaptor combination. To actually perform logging, your WOApplication should call <code>logRequest</code>. The best place to do so is probably in a custom override of WOApplication's <code>dispatchRequest</code> before you call super.
 <p>
 Two sets of methods are provided, those that take the WORequest as an argument and those that take the WOContext as argument. The latter methods log data associated with the context's current WORequest.

 @see WXResponseLogger
 */
public class WXRequestLogger  {

    public static void logRequest(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequest for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequest(aContext.request());
            System.out.println("**** Finished logRequest for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logRequestHeaders(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequestHeaders for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequestHeaders(aContext.request());
            System.out.println("**** Finished logRequestHeaders for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logRequestContent(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequestContent for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequestContent(aContext.request());
            System.out.println("**** Finished logRequestContent for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method


    public static void logRequestProperties(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequestProperties for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequestProperties(aContext.request());
            System.out.println("**** Finished logRequestProperties for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logRequestFormValues(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequestFormValues for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequestFormValues(aContext.request());
            System.out.println("**** Finished logRequestFormValues for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logRequestCookies(WOContext aContext){
        if(aContext!=null){
            System.out.println("**** logRequestCookies for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
            WXRequestLogger.logRequestCookies(aContext.request());
            System.out.println("**** Finished logRequestCookies for SessionID:"+aContext.session().sessionID()+" ContextID:"+aContext.contextID()+" ****");
        }
    }//end method

    public static void logRequest(WORequest aRequest){
        if(aRequest!=null){
            System.out.println("******** Begin logRequest ********");
            WXRequestLogger.logRequestProperties(aRequest);
            WXRequestLogger.logRequestHeaders(aRequest);
            WXRequestLogger.logRequestFormValues(aRequest);
            WXRequestLogger.logRequestCookies(aRequest);
            WXRequestLogger.logRequestContent(aRequest);
            System.out.println("******** End logRequest ********");
        }
    }//end method

    public static void logRequestCookies(WORequest aRequest){
        if(aRequest!=null){
            System.out.println("------------ Begin Request Cookies ------------");
            Enumeration en=aRequest.cookieValues().keyEnumerator();
            while(en.hasMoreElements()){
                String ck=(String)en.nextElement();
                Enumeration en1=((NSArray)aRequest.cookieValues().objectForKey(ck)).objectEnumerator();
                while(en1.hasMoreElements()){
                    System.out.println(ck+"="+en1.nextElement());
                }
            }
            System.out.println("------------ End Request Cookies ------------");
        }
    }//end method


    public static void logRequestFormValues(WORequest aRequest){
        if(aRequest!=null){
            System.out.println("------------ Begin Request Form Values ------------");
            Enumeration en=aRequest.formValueKeys().objectEnumerator();
            while(en.hasMoreElements()){
                String fv=(String)en.nextElement();
                Enumeration en1=aRequest.formValuesForKey(fv).objectEnumerator();
                while(en1.hasMoreElements()){
                    System.out.println(fv+"="+en1.nextElement());
                }
            }
            System.out.println("formValueEncoding="+aRequest.formValueEncoding());
            System.out.println("defaultFormValueEncoding="+aRequest.defaultFormValueEncoding());
            System.out.println("isFormValueEncodingDetectionEnabled="+aRequest.isFormValueEncodingDetectionEnabled());
            System.out.println("------------ End Request Form Values ------------");
        }
    }//end method

    public static void logRequestContent(WORequest aRequest){
        if(aRequest!=null){
            NSData data=aRequest.content();
            System.out.println("------------ Begin Request Content ------------");
            System.out.println(new String(data.bytes(0,data.length())));
            System.out.println("------------ End Request Content ------------");
        }
    }//end method

    public static void logRequestProperties(WORequest aRequest){
        if(aRequest!=null){
            System.out.println("------------ Begin Request Properties ------------");
            System.out.println("httpVersion="+aRequest.httpVersion());
            System.out.println("uri="+aRequest.uri());
            System.out.println("method="+aRequest.method());
            System.out.println("sessionID="+aRequest.sessionID());
            System.out.println("adaptorPrefix="+aRequest.adaptorPrefix());
            System.out.println("applicationName="+aRequest.applicationName());
            System.out.println("applicationNumber="+aRequest.applicationNumber());
            System.out.println("browserLanguages="+aRequest. browserLanguages());
            System.out.println("requestHandlerKey="+aRequest. requestHandlerKey());
            System.out.println("requestHandlerPath="+aRequest.requestHandlerPath());
            //System.out.println("isFromClientComponent="+aRequest.isFromClientComponent());
            System.out.println("userInfo="+aRequest.userInfo());
            System.out.println("------------ End Request Properties ------------");
        }
    }//end method

    public static void logRequestHeaders(WORequest aRequest){
        if(aRequest!=null){
            System.out.println("------------ Begin Request Headers ------------");
            Enumeration en=aRequest.headerKeys().objectEnumerator();
            while(en.hasMoreElements()){
                String hdr=(String)en.nextElement();
                Enumeration en1=aRequest.headersForKey(hdr).objectEnumerator();
                while(en1.hasMoreElements()){
                    System.out.println(hdr+":"+en1.nextElement());
                }
            }
            System.out.println("------------ End Request Headers ------------");
        }
    }//end method


}//end class
