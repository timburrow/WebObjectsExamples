/*
 * AddMoviePage.java [ThinkMovies Project] Copyright 2005 - 2007  Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software
 * in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the
 * Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE,
 * REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.thinkmovies;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSKeyValueCoding;

/*
 * This class and the associated component creates a page to gather information for a new movie. This page only takes the information in and passes it along to the DirectAction bound to the form: the EO is created in the DirectAction itself.
 */

public class AddMoviePage extends WOComponent {
	private static final long	serialVersionUID	= 4440014303165535837L;

	// Instance variables for the new movie information
	public String				aTitle;

	public String				aDateReleased;

	public String				aCategory;

	public String				aRevenue;

	// Instance variable for the Studio
	/** @TypeInfo Studio */
	public NSKeyValueCoding		aStudio;

	// Instance variable for the array of all studios
	/** @TypeInfo Studio */
	public NSArray				aStudioList;

	// Instance variable for possible exceptions
	public Exception			validationException;

	/**
	 * Constructor
	 */

	public AddMoviePage(WOContext context) {
		super(context);
	}

	/**
	 * Override of the awake method - this override checks the local instance variable for null; if it is null, it is populated with the cached studio list from the Application class.
	 */

	@Override
	public void awake() {
		super.awake();
		if (aStudioList == null) {
			aStudioList = ((Application) application()).studioList();
		}
	}

	/**
	 * Method to return the status of an exception: if the local instance variable for exceptions is not null after the form processing then an error has occurred; otherwise everything is ok.
	 */

	public boolean didValidationFail() {
		return (validationException != null);
	}

	/**
	 * Method to return the (possible) validation method for the component. This method checks the to see if the validation failed, and if it did it creates an appropriate message to return to the user.
	 */

	public String validationMessage() {

		if (didValidationFail()) {
			String msg = validationException.getMessage();
			if ((msg.indexOf("title") > -1) && (msg.lastIndexOf("null") > -1)) {
				return "The movie title must not be left blank.";
			} else {
				return msg;
			}
		} else {
			return "";
		}
	}

}
