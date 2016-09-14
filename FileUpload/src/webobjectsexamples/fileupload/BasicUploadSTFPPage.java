/*
 * BasicUploadPage.java
 * [FileUpload Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
 /**
 The class and the associated component demonstrate the basics of using a WOFileUpload
 component to process uploads using component actions.  Please read the information in
 the submitClicked() method on how (and why) the data from the upload is copied by the
 application.
 */
package webobjectsexamples.fileupload;

import java.io.File;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPathUtilities;


public class BasicUploadSTFPPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7911623476410205466L;
	// Instance variables for the name, mimetype of the file upload
    public String aFileName;
    public String aMimeType;

    // Instance variable for the path at which the file actually ended up
    public String actualFilePath;


    /**
     * Constructor
     */
    public BasicUploadSTFPPage(WOContext context) {
        super(context);
    }


    /**
     * Method performed when the submit button is clicked on the form.  This method takes the
     * file upload data and writes it out to disk in the defined location, then logs some
     * information to the console to show what it has done.
     */
    public WOComponent submitClicked()  {
	NSLog.out.appendln();
        NSLog.out.appendln( "==============================" );
        NSLog.out.appendln( "Component File to File Upload:" );

        // Note that this basic test writes the data at once on the disk,
        // but does NOT display the data. Thus, this example can be used to
	// upload arbitrarily large files. Essentially, the file is being
	// streamed directly from the client to disk.

	// Also note that the instance variable actualFilePath will be the
	// path where the file was actually written. This can be different
	// from the path that was requested in the component bindings.

	if ( (actualFilePath != null) && (actualFilePath.length() > 0) ) {
	    File uploadedFile = new File(actualFilePath);

	    NSLog.out.appendln( "Wrote file to: "+actualFilePath );
	    NSLog.out.appendln( "Length: "+uploadedFile.length() );
	} else {
	    NSLog.out.appendln( "No File Uploaded" );
	}

        return this;
    }

    /**
     * This is the filepath where the file should be written to disk if possible.
     * Note that the actual location of the file will be store in the instance
     * variable 'actualFilePath'
     */
    public String dynamicName() {
        return Application.fileUploadPath() + File.separator + NSPathUtilities.lastPathComponent(aFileName);
    }

    /**
     * Method to conditionally return true or false based on the filename
     */
    public boolean hasFile() {
        if ( (actualFilePath != null) && (actualFilePath.length() > 0) ) {
            return true;
        }
        return false;
    }

}
