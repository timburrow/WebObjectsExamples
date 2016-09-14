/*
 * DirectAction.java
 * [FileUpload Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/**
 * This is the DirectAction class and is used to demonstrate file uploading
 * using a DirectAction (versus a component action).
 */
package webobjectsexamples.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WOMultipartIterator;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPathUtilities;


public class DirectAction extends WODirectAction {

    /**
     * Constructor
     */
    public DirectAction( WORequest aRequest ) {
        super( aRequest );
    }

    /**
     * Default action
     */
    @Override
	public WOActionResults defaultAction() {
        return pageWithName("Main");
    }


    /**
     * Method to return the Direct Action Data Upload example page.  This method is used to
     * begin the example;  the processing of the example takes place in the next
     * method ("ProcessFileUploadAsDataAction")
     */
    public WOActionResults directDataAction()  {
        return pageWithName( "DAUploadDataPage" );
    }

    /**
     * Method to process the file upload information.  This method takes the uploaded information
     * and writes the content out to disk in the defined location, and then logs what it did
     * to the console.
     * This will load the entire uploaded file into memory twice.
     */
    public WOActionResults ProcessFileUploadAsDataAction()  {
	NSLog.out.appendln();
	NSLog.out.appendln( "==========================" );
	NSLog.out.appendln( "Direct Action Data Upload:" );

        // the NSData 'aFileContents' points to bytes that were never copied and reside in the
        // WOAdaptor for this request's scope; the request content object points to them as well.
        // When you ask for request.content(), you get a fresh copy of the bytes.

        // When you ask for an NSData in the form values, you get a transitory NSData,
        // whose bytes are valid ONLY for this request, as they will be reused for next request.

        // Here we are accessing the NSData out of the request and saving it at once
        // on the disk. This is safe and happens entirely within the scope of this request,
        // so there is no risk of having bytes changed underneath you.

	// However, note that when we want to return the NSData to the page for display, we must
	// make a copy of the underlying bytes.

        // When using WOFileUpload in a component action, the 'copyData' binding
        // can be used to obtain the same result when set to NO. The default value for copyData is YES,
        // which safely copies the file uploaded, but may not be the right thing to do for a
        // 10MB file...

        // Get the form information from the request
        String aFileName = request().stringFormValueForKey( "thefile.filename" );
        NSData aFileContents = (NSData)request().formValueForKey( "thefile" );

        // Get just the name for the uploaded file from aFileName.
        String fileName = NSPathUtilities.lastPathComponent( aFileName );

        // Create the output path for the file on the application server
        String outputFilePath = new String( Application.fileUploadPath() + File.separator + fileName );

        // Write the file out to the location
        if ((fileName!=null) && (fileName.length()>0)) {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);
                aFileContents.writeToStream(fileOutputStream);
                fileOutputStream.close();
            } catch (IOException e) {
                NSLog.err.appendln("Error writing file: " + e);
            }

            NSLog.out.appendln( "Wrote file to '" + outputFilePath + "'" );
        } else {
            NSLog.out.appendln( "No File Uploaded" );
        }

        DAUploadDataPage result = (DAUploadDataPage) pageWithName( "DAUploadDataPage" );
        result.setFileContents(new NSData(aFileContents));
        result.setFileName(aFileName);
        return result;
    }


    /**
    * Method to return the Direct Action Iterator Upload example page.  This method is used to
     * begin the example;  the processing of the example takes place in the next
     * method ("ProcessFileUploadWithIteratorAction")
     */
    public WOActionResults directIteratorAction()  {
        return pageWithName( "DAUploadIteratorPage" );
    }

    /**
     * Method to process the file upload information.  This method takes the uploaded information
     * and writes the content out to disk in the defined location, and then logs what it did
     * to the console.
     * By using the WOMultipartIterator object, this avoids loading the uploaded file into memory
     * all at once. Thus, this DirectAction can be used to upload arbitrarily large files.
     */
    public WOActionResults ProcessFileUploadWithIteratorAction()  {
	NSLog.out.appendln();
	NSLog.out.appendln( "==============================" );
	NSLog.out.appendln( "Direct Action Iterator Upload:" );

	String fileName = null;

	// Grab the iterator for the multiple form submission
	WOMultipartIterator mpI = request().multipartIterator();

	// This implies that this wasn't a multi-part request
	if (mpI == null) {
	    throw new IllegalStateException("Could not get the WOMultipartIterator. Verify that the WOForm's 'enctype' binding is set to 'multipart/form-data'");
	}

	// Iterator through the WOMultipartIterator, looking for the file upload we're interested in.
	// "thefile" was set in the WOFileUpload on the invoking page.
	WOMultipartIterator.WOFormData wfd = null;
	while (true) {
	    wfd = mpI.nextFormData();
	    if (wfd == null) break;
	    if ("thefile".equals(wfd.contentDispositionHeaders().valueForKey("name"))) break;
	}

	if ( (wfd != null) && (wfd.isFileUpload()) ) {
	    // Get the filename information from the request
	    NSDictionary contentDispHeaders = wfd.contentDispositionHeaders();
	    String aFileName = (String) contentDispHeaders.valueForKey("filename");

	    // Get just the name for the uploaded file from aFileName.
	    fileName = NSPathUtilities.lastPathComponent( aFileName );

	    // Create the output path for the file on the application server
	    String outputFilePath = new String( Application.fileUploadPath() + File.separator + fileName );

	    // Write the file out to the location
	    if ((fileName!=null) && (fileName.length()>0)) {
		try {
		    InputStream wfdIs = wfd.formDataInputStream();
		    FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath);

		    // This will use a 1MB buffer to transfer chunks of data from the Client to the Disk
		    int read = 0;
		    byte[] buffer = new byte[1024 * 1024]; // 1MB buffer
		    do {
			read = wfdIs.read(buffer);
			if (read != -1) {
			    fileOutputStream.write(buffer, 0, read);
			} else {
			    break;
			}
		    } while (read != -1);

		    fileOutputStream.flush();
		    fileOutputStream.close();

		} catch (IOException e) {
		    NSLog.err.appendln("Error writing file: " + e);
		}

		NSLog.out.appendln( "Wrote file to '" + outputFilePath + "'" );
	    } else {
		// fileName == null or length 0
		NSLog.out.appendln( "No File Uploaded" );
	    }
	} else {
	    // wfd == null means that there was no file uploaded.
	    NSLog.out.appendln( "No File Uploaded" );
	}

        DAUploadIteratorPage result = (DAUploadIteratorPage) pageWithName( "DAUploadIteratorPage" );
        result.setFileName(fileName);
        return result;
    }


    /**
    * Method to return the Direct Action Streaming Download example page.  This method is used to
     * begin the example;  the processing of the example takes place in the next
     * method ("DownloadFileAction")
     */
    public WOActionResults directDownloadAction()  {
        return pageWithName( "DADownloadPage" );
    }

    /**
    * Method to return the contents of a file to the client. This method takes the filepath
     * from the query string of the request and returns the file at that location directly
     * to the client.
     * By using an InputStream to represent the file, loading the file into memory
     * all at once is avoided. Thus, this DirectAction can be used to download arbitrarily large files.
     */
    public WOActionResults DownloadFileAction() {
	WORequest aRequest = request();
	WOResponse aResponse = WOApplication.application().createResponseInContext(null);

	// the QueryString should have been parsed into the formValues
	String filepath = aRequest.stringFormValueForKey("filePath");

	// If the filepath is valid, present the contents for download
	if (filepath != null && filepath.length() > 0) {
	    try {
		// Grab the content-type of the file at that location
		String contentType = WOApplication.application().resourceManager().contentTypeForResourceNamed(filepath);
		aResponse.setHeader(contentType, "content-type");

		// Set the file contents as an InputStream representing the content of the response
		// A transfer buffer of 1MB is requested.
		File f = new File(filepath);
		int bufferSize = (f.length() < 1024*1024) ? (int) f.length() : 1024*1024;
		aResponse.setContentStream(new FileInputStream(f), bufferSize, (int) f.length());

		// Get just the name for the uploaded file from filepath.
		String fileName = NSPathUtilities.lastPathComponent( filepath );

		// Set the content-disposition headers so that browsers will use the correct filename
		// IE for Mac 5.x doesn't use this header, so the downloaded files will be named wrong
		aResponse.setHeader("filename="+fileName, "content-disposition");
	    } catch (Exception e) {
		NSLog.err.appendln("Error downloading file: " + e);
		aResponse = new WOResponse();
		aResponse.appendContentString("Error downloading file: " + e);
	    }
	}
	return aResponse;
    }


    /**
    * Method to return the Direct Action Request Content Streaming Upload example page.  This method is used to
     * begin the example;  the processing of the example takes place in the next
     * method ("ProcessRequestContentAction")
     */
    public WOActionResults directRequestContentAction()  {
        return pageWithName( "DARequestContentPage" );
    }

    /**
    * Method to process the content of a raw HTTP Request.  This method takes WORequest object
     * and streams the complete contents out to disk. This requires using a special RequestHandler,
     * the "WOStreamRequestHandler" (default key "wis") and studiously avoiding calling formValues()
     * By doing this, this method avoids loading the request contents into memory
     * all at once. Thus, this DirectAction can be used too look at the contents of arbitrarily large
     * HTTP requests.
     */
    public WOActionResults ProcessRequestContentAction()  {
	NSLog.out.appendln( "===============================================" );
	NSLog.out.appendln( "Direct Action Request Content Streaming Upload:" );

	WORequest aRequest = request();
	String fileName = null;

	// Make sure there is a content-length associated with the request
	int contentLength = Integer.parseInt(aRequest.headerForKey("content-length"));
	if (contentLength <= 0) {
	    NSLog.err.appendln("Error: No content-length header");
	} else {
	    try {
		// Create the output path for the file on the application server
		File tempFile = File.createTempFile("DARCSUReq", null);
		if (tempFile != null) {
		    fileName = tempFile.getCanonicalPath();

		    // Get the InputStream for the request contents
		    InputStream contentStream = aRequest.contentInputStream();
		    if (contentStream != null) {
			FileOutputStream fileOutputStream = new FileOutputStream(tempFile);

			// Use a 1MB buffer by default
			int bufferSize = (contentLength < 1024*1024) ? contentLength : 1024*1024;
			byte[] buffer = new byte[bufferSize];

			// Stream the content to disk
			int read = 0;
			do {
			    read = contentStream.read(buffer);
			    if (read != -1) {
				contentLength -= read;
				fileOutputStream.write(buffer, 0, read);
			    } else {
				break;
			    }
			} while ( (read != -1) && (contentLength > 0) );

			fileOutputStream.flush();
			fileOutputStream.close();

			NSLog.out.appendln( "Wrote file to '" + fileName + "'" );
		    } else {
			throw new IOException("Request InputStream was null!");
		    }
		} else {
		    throw new IOException("Could not create temporary file!");
		}
	    } catch (IOException e) {
		NSLog.err.appendln("Error writing file: " + e);
	    }
	}

        DARequestContentPage result = (DARequestContentPage) pageWithName( "DARequestContentPage" );
        result.setFileName(fileName);
        return result;
    }
}
