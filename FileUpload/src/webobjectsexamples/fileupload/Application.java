/*
 * Application.java
 * [FileUpload Project]

© Copyright 2005-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.webobjects.appserver.WOApplication;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPathUtilities;
import com.webobjects.foundation.NSRange;

public class Application extends WOApplication {
    static String _fileUploadPath = null;

    public static void main(String argv[]) {
        WOApplication.main(argv, Application.class);
    }

    public Application() {
        super();
        NSLog.out.appendln("Welcome to " + this.name() + "!");
    }

    /**
     * Returns the directory where uploaded files will be written to disk. If possible, it will use the
     * system temp directory, but will default to the user's home directory if not.
     */
    public static String fileUploadPath() {
	if (_fileUploadPath == null) {
	    String tmpdir = System.getProperty("java.io.tmpdir");
	    if (tmpdir != null) {
		File tmpPath = new File(tmpdir);
		if (tmpPath.exists()) {
		    _fileUploadPath = tmpPath.getAbsolutePath();
		}
	    }
	    if (_fileUploadPath == null) {
		NSLog.err.appendln("FileUpload: 'java.io.tmpdir' does not exist. Please launch this application again with the 'java.io.tmpdir' System Property set to a directory to which you have write permissions.");
	    }
	}
	return _fileUploadPath;
    }

    /**
     * Given a ByteArrayOutputStream and filename, will write the contents of the ByteArrayOutputStream to disk
     * in a file named aFileName. It will then return an NSData based on the contents of the ByteArrayOutputStream.
     * Note that we do not copy the data in the ByteArrayInputStream.
     */
    public static NSData save(ByteArrayOutputStream baos, String aFileName) {
	byte[] byteArray = baos.toByteArray();
	NSData aFileContents = new NSData(byteArray, new NSRange(0, byteArray.length), true);

        // Get just the name for the uploaded file from aFileName.
        String fileName = NSPathUtilities.lastPathComponent( aFileName );

        // Create the output path for the files on the application server
        String outputFilePath = new String( Application.fileUploadPath() + File.separator + fileName );

        if ((fileName!=null) && (fileName.length()>0)) {
            // Write the file out to the location
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
                aFileContents.writeToStream(fileOutputStream);
                fileOutputStream.close();
                NSLog.out.appendln( "Wrote file to: "+outputFilePath );
            } catch (IOException e) {
                NSLog.err.appendln("Error writing file: " + e);
            }
        } else {
            NSLog.out.appendln( "No FileName - No File Uploaded" );
        }
        return aFileContents;
    }
}
