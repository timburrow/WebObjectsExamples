/*
 * MultipleUploadPage.java
 * [FileUpload Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
 /**
 The class and the associated component demonstrate the basics of using multiple WOFileUpload
 components on the same page to process uploads using component actions.  Note that this is
 really no different than using a single upload, except we have to perform the saving of data
 three times (one for each upload).  Please read the information in the submitClicked() method
 on how (and why) the data from the upload is copied by the application.
 */
package webobjectsexamples.fileupload;

import java.io.ByteArrayOutputStream;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;

 public class MultipleUploadOSPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 704419718151842469L;
	// Instance variables for the names and contents of uploaded files
    public String aFileName1 = null;
    public String aFileName2 = null;
    public String aFileName3 = null;

    public NSData aFileContents1 = null;
    public NSData aFileContents2 = null;
    public NSData aFileContents3 = null;

    public ByteArrayOutputStream baos1 = null;
    public ByteArrayOutputStream baos2 = null;
    public ByteArrayOutputStream baos3 = null;


    /**
     * Constructor
     */
    public MultipleUploadOSPage(WOContext aContext )  {
        super( aContext );
        baos1 = new ByteArrayOutputStream(1024*1024);
        baos2 = new ByteArrayOutputStream(1024*1024);
        baos3 = new ByteArrayOutputStream(1024*1024);
    }


    /**
     * Method performed when the submit button is clicked on the form.  This method takes the
     * file upload data and writes it out to disk in the defined location, then logs some
     * information to the console to show what it has done.
     */
    public WOComponent submitClicked()  {
	NSLog.out.appendln();
        NSLog.out.appendln( "=======================================" );
        NSLog.out.appendln( "Multiple Component OutputStream Upload:" );

        // Note that this basic test writes the data at once on the disk,
        // and displays it on screen. Because the data in aFileContents is
        // passed to the next page in a WOImage, it is very important
        // that the bytes in the NSData be copied.  This is achieved automatically by
        // the copyData binding in WOFileUpload.  The default value is copyData=YES which
        // is what we want here.

        // Should we set copyData=NO, then the data cached in the resource manager
        // for display in the refreshed page would get corrupted when the image request
        // calls back.  The optimized request content buffer in WOAdaptor would change
        // behind us, and the data point to garbage !

        aFileContents1 = Application.save(baos1, aFileName1);
        aFileContents2 = Application.save(baos2, aFileName2);
        aFileContents3 = Application.save(baos3, aFileName3);

	return this;
    }

    /**
     * Methods to conditionally return true or false based on the existence of NSData
     * content for the file upload.  If a file was uploaded from the WOFileUpload component,
     * the binding for the data will not be null (and will have some size to it).  If the
     * file has content, we return true; otherwise we return false;
     */
    public boolean haveData1() {
        if (aFileContents1 != null && aFileContents1.length() > 0) {
            return true;
        }
        return false;
    }
    public boolean haveData2() {
        if (aFileContents2 != null && aFileContents2.length() > 0) {
            return true;
        }
        return false;
    }
    public boolean haveData3() {
        if (aFileContents3 != null && aFileContents3.length() > 0) {
            return true;
        }
        return false;
    }
}
