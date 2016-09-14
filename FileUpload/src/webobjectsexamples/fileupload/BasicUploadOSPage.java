/*
 * BasicUploadPage.java
 * [FileUpload Project]

� Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.fileupload;

import java.io.ByteArrayOutputStream;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;


/**
 The class and the associated component demonstrate the basics of using a WOFileUpload
 component to process uploads using component actions.  Please read the information in
 the submitClicked() method on how (and why) the data from the upload is copied by the
 application.
 */
public class BasicUploadOSPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -189001579310705749L;
	// Instance variables for the name, mimetype and content of the file upload
    public String aFileName;
    public String aMimeType;
    public NSData aFileContents = null;
    public ByteArrayOutputStream baos = null;


    /**
     * Constructor
     */
    public BasicUploadOSPage(WOContext context) {
        super(context);
        baos = new ByteArrayOutputStream(1024*1024);
    }


    /**
     * Method performed when the submit button is clicked on the form.  This method takes the
     * file upload data and writes it out to disk in the defined location, then logs some
     * information to the console to show what it has done.
     */
    public WOComponent submitClicked()  {
	NSLog.out.appendln();
        NSLog.out.appendln( "==============================" );
        NSLog.out.appendln( "Component OutputStream Upload:" );

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

        aFileContents = Application.save(baos, aFileName);

        return this;
    }


    /**
     * Method to conditionally return true or false based on the existence of NSData
     * content for the file upload.  If a file was uploaded from the WOFileUpload component,
     * the binding for the data will not be null (and will have some size to it).  If the
     * file has content, we return true; otherwise we return false;
     */
    public boolean hasUploadData() {
        if ( aFileContents != null && aFileContents.length() > 0 ) {
            return true;
        }
        return false;
    }

}
