/*
 * MovieComponent.java [ThinkMovies Project] Copyright 2005 - 2007  Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software
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
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSKeyValueCoding;

/*
 * This class is used as an enhanced class for the components to extend in this application. This class contains a number of instance variables for the names of the form fields, and methods to return the ID for a movie, the index of the movie, and the index of the movie ID. These are used by a
 * number of components for batching and other processes.
 */

public class MovieComponent extends WOComponent {
	private static final long	serialVersionUID	= 964584880638359152L;

	// Static instance variable for the form attribute names
	public static final String	MovieKey			= "movie";

	public static final String	RawMovieKey			= "rawMovie";

	public static final String	MovieIDKey			= "movieID";

	public static final String	MovieTitleKey		= "title";

	// Instance variable for the movie object
	/** @TypeInfo Movie */
	public EOEnterpriseObject	movie;

	// Instance variable for the raw movie information
	public NSKeyValueCoding		rawMovie;

	/**
	 * Constructor for the component
	 */

	public MovieComponent(WOContext context) {
		super(context);
	}

	/**
	 * Method returning the movie ID for the local instance variable for the movie. This method uses the information on the movie to get the raw (dictionary) data for the movie from the Application class, and then returns the value for the ID attribute.
	 */

	public Number movieID() {

		if (rawMovie == null) {

			if (movie != null) {
				Object result = Application.myApplication().primaryKeyForObject(movie);
				String title = (String) movie.valueForKey(MovieTitleKey);
				rawMovie = new NSDictionary<String, Object>(new Object[] { result, title }, new String[] { MovieIDKey, MovieTitleKey });
			} else {
				return null;
			}
		}
		return (Number) rawMovie.valueForKey(MovieIDKey);
	}

	/**
	 * Method to return the index of the current movie in the list of movies. This method uses the primary key for the movie and then, based on the index of at key in the list of all primary keys, returns the appropriate index.
	 *
	 * @param list
	 *            list of movies
	 * @param aMovie
	 *            more to find the index of
	 * @return the index of the movie in the list
	 */

	public int indexOfMovie(NSArray list, EOEnterpriseObject aMovie) {

		Object result = Application.myApplication().primaryKeyForObject(aMovie);
		if (result == null) {
			return -1;
		}

		return indexOfMovieID(list, result);
	}

	/**
	 * Method to return the index of the current movieID in the list of all indexes.
	 *
	 * @param list
	 *            list of movie IDs
	 * @param movieID
	 *            ID for the current movie
	 * @return the index of the movie ID in the list
	 */

	public int indexOfMovieID(NSArray list, Object movieID) {
		if (movieID == null) {
			return -1;
		}

		int c = list.count();
		for (int i = 0; i < c; i++) {
			NSKeyValueCoding object = (NSKeyValueCoding) list.objectAtIndex(i);
			Object id = object.valueForKey(MovieIDKey);
			if ((id != null) && (id.equals(movieID))) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Override of the awake method. This method uses a local instance variable to lock and get the Application's editing context while displaying the information on the page.
	 */

	@Override
	public void awake() {
		super.awake();
		/* EOEditingContext ignore = */((Application) application()).lockEC();
	}

	/**
	 * Override of the sleep method -- this method uses the Application's method to unlock the editing context for the Application (which was locked during the awake() method.)
	 */

	@Override
	public void sleep() {
		((Application) application()).unlockEC();
		super.sleep();
	}

}
