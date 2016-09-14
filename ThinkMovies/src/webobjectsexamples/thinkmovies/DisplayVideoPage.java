/*
 * DisplayVideoPage.java [ThinkMovies Project] Copyright 2005 - 2007  Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or
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

import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

/*
 * This class and the associated component create a page to display the information for a particular Movie. This page shows the details for a Movie, the list of actors/Roles for the Movie, and the Director for the Movie.
 */

public class DisplayVideoPage extends MovieComponent {
	private static final long	serialVersionUID	= -494755808354701508L;

	// Instance variable for the full list of movies
	/** @TypeInfo Movie */
	public NSMutableArray		list;

	// Instance variable for the query dictionary
	public NSMutableDictionary	queryDict;

	// Instance variable for a Role in the movie
	/** @TypeInfo MovieRole */
	public EOEnterpriseObject	aRole;

	// Instance variable for the Director of the movie
	/** @TypeInfo Director */
	public EOEnterpriseObject	aDirector;

	/**
	 * Constructor
	 */

	public DisplayVideoPage(WOContext context) {
		super(context);
	}

	/**
	 * Method to return the index of the Movie ID in the full list
	 */

	private int movieIndex() {
		return indexOfMovieID(list, movieID());
	}

	/**
	 * Method to get the summary information for a particular Movie. This method gets the information from the "plotSummary" attribute of the Movie (which is the Summary EO) and then gets the "summary" attribute from it.
	 */

	public String summaryString() {
		EOGenericRecord plotSummary = (EOGenericRecord) movie.valueForKey("plotSummary");
		return (String) plotSummary.valueForKey("summary");
	}

	/**
	 * Method to return the ID number for the previous movie in the list. This ID number is used on a hyperlink to show the details for the previous movie.
	 */

	public Number previousMovieID() {
		int aPreviousMovieIndex = movieIndex() - 1;
		if ((aPreviousMovieIndex >= 0) && (aPreviousMovieIndex < list.count())) {
			NSKeyValueCoding aPreviousMovie = (NSKeyValueCoding) list.objectAtIndex(aPreviousMovieIndex);
			return (Number) aPreviousMovie.valueForKey("movieID");
		}
		return movieID();
	}

	/**
	 * Method to return the ID number for the next movie in the list. This ID number is used on a hyperlink to show the details for the next movie.
	 */

	public Number nextMovieID() {
		int aNextMovieIndex = movieIndex() + 1;
		if ((aNextMovieIndex >= 0) && (aNextMovieIndex < list.count())) {
			NSKeyValueCoding aNextMovie = (NSKeyValueCoding) list.objectAtIndex(aNextMovieIndex);
			return (Number) aNextMovie.valueForKey("movieID");
		}
		return movieID();
	}

	/**
	 * Method to compute the number of roles in the movie, and then add one. This number is used as the COLSPAN attribute for the generic table element to display the information.
	 */

	public int computedRolesRowspan() {
		NSArray roles = (NSArray) movie.valueForKey("roles");
		return roles.count() + 1;
	}

	/**
	 * Method to return the image for a specific Talent EO. This component displays the list of roles in the Movie, and then shows the Talent performing the role. This method fetches the image data for the Talent EO (which could be null).
	 */

	private NSData talentPhotoForTalent(EOEnterpriseObject talent) {
		EOEnterpriseObject aTalentPhoto = (EOEnterpriseObject) talent.valueForKey("photo");
		NSData aPhoto = null;
		if (aTalentPhoto != null) {
			aPhoto = (NSData) aTalentPhoto.valueForKey("photo");
			if (aPhoto != null && aPhoto.length() == 0) {
				aPhoto = null;
			}
		}
		return aPhoto;
	}

	/**
	 * Method to return the image data for a particular Talent, OR to use the default image ("PictureNotFound") if no image data is available. The image data used for unavailable pictures is cached in the Application class.
	 */

	public NSData actorImageData() {
		EOEnterpriseObject aTalent = (EOEnterpriseObject) aRole.valueForKey("talent");
		NSData aPhoto = talentPhotoForTalent(aTalent);
		if (aPhoto == null) {
			aPhoto = ((Application) application()).pictureNotFoundImage();
		}
		return aPhoto;
	}

	/**
	 * Method to generate the URL to the RentalStore application - this method will create a link to the RentalStore and allow the user to jump to the page to add the selected Movie to a shopping basket (if the application is running). This method will NOT work correctly if the application is
	 * running in DirectConnect mode: you must go through the adaptor.
	 */

	public String rentalStoreURL() {
		WORequest r = context().request();
		return r.adaptorPrefix() + "/RentalStore.woa/wa/AddMovieToShoppingBasket";
	}

	/**
	 * Method to return the caching key for the image for a particular Talent. This key is by the browser to cache the data for the image to allow for the use of the cached version the next time it is displayed. For images with image data, the global ID for the Talent EO is used; otherwise the
	 * "PictureNotFoundImage" key is used.
	 */

	public String talentPhotoCachingKey() {

		EOEnterpriseObject anEO = (EOEnterpriseObject) aRole.valueForKey("talent");
		NSData aPhoto = talentPhotoForTalent(anEO);
		if (aPhoto == null) {
			return "PictureNotFoundImage";
		}

		EOEditingContext anEditingContext = anEO.editingContext();
		EOGlobalID aGlobalID;
		anEditingContext.lock();
		aGlobalID = anEditingContext.globalIDForObject(anEO);
		anEditingContext.unlock();
		return aGlobalID.toString();
	}

}
