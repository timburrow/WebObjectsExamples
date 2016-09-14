/*
 WXCodecUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/**
This class collects useful encoders and decoders utilities in particular decoders/encoders that take Strings as well as bytes for arguments. Some additional decoding/encoding routines are also present to faciliate archiving graphs of objects to data in the absence of NSArchiver and NSUnarchiver.
 <p>
<code>urlDecode</code>  can be used to take a URL encoded string and convert it to a normal string. URL encoding is a way to express strings on URL so that URL content strings are not confused with URL logical strings (strings like the "/", "=", or "?" among many other characters).
<p>
 */
package com.webobjects.examples.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.webobjects.foundation.NSData;

public class WXCodecUtilities {
	public static final String UTF8_ENCODING = "UTF-8";

    /**
     * Encodes the given string using the base64-encoding
     * specified in RFC-2045 (Section 6.8). It's used for example in the
     * "Basic" authorization scheme.
     *
     * @param  str the string
     * @return the base64-encoded <var>str</var>
     */
    public final static String base64Encode(String str)
    {
	if (str == null)  return  null;

	//byte data[] = new byte[str.length()];
	//str.getBytes(0, str.length(), data, 0);
        sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
	return enc.encode(str.getBytes());
    }


    /**
     * Decodes the given string using the base64-encoding
     * specified in RFC-2045 (Section 6.8).
     *
     * @param  str the base64-encoded string.
     * @return the decoded <var>str</var>.
     */
    public final static String base64Decode(String str)
    {
	if (str == null)  return  null;

	//byte data[] = new byte[str.length()];
	//str.getBytes(0, str.length(), data, 0);
        sun.misc.BASE64Decoder dec = new sun.misc.BASE64Decoder();
        String decodedString = null;
        try{
            decodedString = new String(dec.decodeBuffer( new ByteArrayInputStream( str.getBytes() ) ));
        }catch(java.io.IOException e){
       		e.printStackTrace();
        }
	return decodedString;
    }

    public static String urlEncode(String s) {
    	try {
    		return URLEncoder.encode(s, UTF8_ENCODING);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }

    public static String urlDecode(String s) {
    	try {
            return URLDecoder.decode(s, UTF8_ENCODING);
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }



    public static NSData archivedDataFromObject(Serializable object){
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        byte[] bytes;
        NSData data = null;

        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);
            objectOutputStream.flush();
            bytes = byteArrayOutputStream.toByteArray();
            data = new NSData(bytes);

        } catch (IOException ioException) {
            WXDebug.println(1, "ioException:"+ioException);
        }
        return data;
    }

    public static Object objectFromArchivedData(NSData data){
        Object object = null;
        try {
            byte[] bytes;
            ByteArrayInputStream byteArrayInputStream;
            ObjectInputStream objectInputStream;

            bytes = data.bytes();
            byteArrayInputStream = new ByteArrayInputStream(bytes);
            objectInputStream = new ObjectInputStream(byteArrayInputStream);

            object = objectInputStream.readObject();
            if (object != null) {
                //WXDebug.println(1, "+++ Restored object " + object + " from server +++");
            }

        } catch(Throwable localException) {
            WXDebug.println(1, "localException:"+localException);
        }
        return object;
    }


}
