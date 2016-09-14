/*
 JavaPropertyListUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.examplesharness;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPropertyListSerialization;


/**
 This class extends the NSPropertyListUtilities found in the Foundation class
 providing a number of utility methods for utilizing and manipulating property
 lists from external files.
 */

public class JavaPropertyListUtilities extends Object {

    /**
     * Static method to return a string from a file path
     *
     * @param path	path to the file
     * @return 		string content of the file
     */

    public static String stringFromFile( String path ) {
         return stringFromFile( path, null );
     }


    /**
     * Static method to return a string from a file path with a particular encoding
     *
     * @param path	path to the file
     * @param encoding	encoding type to use when reading the file
     * @return 		string content of the file
     */

     public static String stringFromFile( String path, String encoding ) {
         File file = new File( path );
         return stringFromFile( file, encoding );
     }


    /**
     * Static method to return a string from a file (using the default encoding)
     *
     * @param file	Java File to use
     * @return 		string content of the file
     */

     public static String stringFromFile( File f ) {
          return stringFromFile( f, null );
      }


    /**
     * Static method to return a string from a file with a particular encoding
     *
     * @param file	Java File to use
     * @param encoding	encoding type to use when reading the file
     * @return 		string content of the file
     */

     public static String stringFromFile( File f, String encoding ) {

	 FileInputStream fis = null;
         byte[] data = null;

         if ( f == null) {
             throw new IllegalArgumentException("Cannot open null file object.");
         }

         if (!f.exists()) {
             return null;
         }

         try {
             int size = (int) f.length();
             fis = new FileInputStream(f);
             data = new byte[size];
             int bytesRead = 0;

             while (bytesRead < size) {
                 bytesRead += fis.read( data, bytesRead, size - bytesRead);
             }
         } catch (IOException e) {
             throw new NSForwardException(e);
         } finally {
             if ( fis != null) {
                 try {
                      fis.close();
                  } catch (IOException e) {
                      if (NSLog.debugLoggingAllowedForLevelAndGroups(NSLog.DebugLevelInformational, NSLog.DebugGroupIO)) {
                          NSLog.debug.appendln("Exception while closing file input stream: " + e.getMessage());
                          NSLog.debug.appendln(e);
                      }
                  }
                 fis = null;
             }
          }

         if (encoding == null || encoding.length() == 0) {
             return new String(data);
         } else {
             try {
                 return new String(data, encoding);
             } catch (UnsupportedEncodingException e) {
                 return null;
             }
         }
     }

    /**
     * Return a string from the contents of an URL using the default encoding
     *
     * @param url	url to load content from
     * @return 		string content of the url
     */
    public static String stringFromPathURL(URL url) {
        return stringFromPathURL(url, null);
    }

    /**
     * Return a string from the contents of an URL using a particular encoding
     *
     * @param url	url to load content from
     * @param encoding	encoding type to use when reading the content
     * @return 		string content of the url
     */
    public static String stringFromPathURL(URL url, String encoding) {
        String aString = null;
        if (url != null) {
            try {
                InputStream is = url.openStream();

                int avail = is.available();
                if (avail <=0) avail = 4096;
                ByteArrayOutputStream baos = new ByteArrayOutputStream(avail);
                try {
                    byte[] data = new byte[avail];
                    do {
                        int bytesRead = is.read(data);
                        if (bytesRead <= 0) break;
                        baos.write(data, 0, bytesRead);
                    } while (true);
                } finally {
                    is.close();
                }
                aString = new String(baos.toByteArray(), encoding);
            } catch (Exception e) {
                throw new NSForwardException(e);
            }
        }
        return aString;
    }



    /**
     * Static method to return an Object from the content of a file at
     * a particular path.
     *
     * @param path	path to the file
     * @return 		Object content of the file
     */

    public static Object propertyListFromContentsOfFile( String path ) {
		if ( path != null && path.length() > 0 ) {
			String someContentsOfFile = stringFromFile( path );
			return NSPropertyListSerialization.propertyListFromString( someContentsOfFile );
		}
		return null;
    }
}