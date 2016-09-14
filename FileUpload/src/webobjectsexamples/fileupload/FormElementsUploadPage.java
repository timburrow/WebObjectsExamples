/*
 * FormElementsUploadPage.java
 * [FileUpload Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
 /**
 The class and the associated component demonstrate the basics of using a WOFileUpload
 component to process uploads and other form elements using component actions.  In this
 example we do not actually write the file to disk, only display the information on the
 upload to demonstrate how you could programmatically use form information to affect an
 upload (for example, by re-naming the upload with a passed in form value).
 */
package webobjectsexamples.fileupload;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;

public class FormElementsUploadPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7429016556434743440L;
	// Instance variables for selections
    public NSArray browserSelection = null;
    public String popupSelection = null;
    public String radioButtonSelection = null;

    // Instance variable for checkbox
    public boolean isCheckbox = false;

    // Instance variables for text fields
    public String aTextArea = "<favorite food>";
    public String aTextField = "<your name>";

    // Instance variable for iterating the lists in browsers and pop-ups
    public String anItem;

    // Instance variables for the name and contents of the upload
    public String aFileName;
    public NSData aFileContents = null;

    // Instance variables for the pop-up list information
    public String[] popupStrings = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" };
    public NSArray<String> numberList = new NSArray <String>( popupStrings );

    // Instance variables for the browser list information
    public String[] browserStrings = { "white", "red", "orange", "yellow", "green", "blue", "purple", "brown", "black" };
    public NSArray<String> colorList = new NSArray<String>( browserStrings );

    // Instance variables for the output
    public String outputInfo = null;
    public boolean showOutputInfo = false;


    /**
     * Constructor
     */
    public FormElementsUploadPage( WOContext aContext )  {
        super(aContext);
    }


    /**
     * Method performed when the submit button is clicked on the form.  This method sets the
     * boolean value to show the value of the form submission and then sets the information
     * to display about the processed form values.
     */
    public WOComponent submitClicked() {
	NSLog.out.appendln();
	NSLog.out.appendln( "==============================" );
        NSLog.out.appendln( "Component Data Upload in Form:" );

        StringBuffer outputInfoBuffer = new StringBuffer();
        outputInfoBuffer.append( "The form output information was: \n" );
        outputInfoBuffer.append( "==================================\n");
        outputInfoBuffer.append( "Your name (textfield): '" + aTextField + "'\n" );
        outputInfoBuffer.append( "Your favorite food (textarea): '" + aTextArea + "'\n" );
        outputInfoBuffer.append( "Your favorite color (browser): '" + browserSelection + "'\n" );
        outputInfoBuffer.append( "Selected number (pop-up button): '" + popupSelection + "'\n" );
        outputInfoBuffer.append( "Are you over 21 (checkbox): '" + isCheckbox + "'\n" );
        outputInfoBuffer.append( "Gender (radio button): '" + radioButtonSelection + "'\n" );
	if ( (aFileName != null) && (aFileName.length() > 0) ) {
	    outputInfoBuffer.append( "Uploaded file: '" + aFileName + "' " + aFileContents.length() + " bytes\n" );
	} else {
	    outputInfoBuffer.append( "No File Uploaded\n" );
	}

	outputInfo = new String(outputInfoBuffer);

        showOutputInfo = true;
        return this;
    }


    /**
     * Method to conditionally return true or false based on the existence of NSData
     * content for the file upload.  If a file was uploaded from the WOFileUpload component,
     * the binding for the data will not be null (and will have some size to it).  If the
     * file has content, we return true; otherwise we return false;
     */
    public boolean haveData() {
        if ( aFileContents != null && aFileContents.length() > 0 ) {
            return true;
        }
        return false;
    }


    /**
     * Method to reset the form - basically this method switches the boolean which is used
     * by the WOConditionals to show the form values instead of the output.  We also clear
     * out the information on the file that was uploaded (if any)
     */
     public WOComponent backToFormValues() {
        aFileName = null;
        aFileContents = null;
        showOutputInfo = false;
        return this;
    }

}
