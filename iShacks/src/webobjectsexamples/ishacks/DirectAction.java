/*
 DirectAction.java
 [iShacks Project]

© Copyright 2005-2007  Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/*
 * Direct Actions recognized by the app
 */

package webobjectsexamples.ishacks;

import java.math.BigDecimal;

import com.webobjects.appserver.WOActionResults;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WODirectAction;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WORequest;
import com.webobjects.eoaccess.EODatabaseDataSource;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableDictionary;

public class DirectAction extends WODirectAction {

    public DirectAction(WORequest aRequest) {
        super(aRequest);
    }

    @Override
	public WOActionResults defaultAction() {
        return pageWithName("Main");
    }

    // ==== direct action methods

    // direct action named FindListings
    // allows querying of property listings
    // see ReadMe file for an example URL that uses it
    public WOComponent FindListingsAction() {
        WOComponent nextPage = null;
        NSMutableDictionary queryDict = _queryDictionaryFromRequest(request());

        EOEditingContext listingEC = new EOEditingContext();
        EODatabaseDataSource listingDS = new EODatabaseDataSource(listingEC, Constants.ListingEntityName);

        EOModelGroup listingMG = EOUtilities.modelGroup(listingEC);
        EOFetchSpecification listingFS = listingMG.fetchSpecificationNamed(Constants.SearchListingFetchSpecName, Constants.ListingEntityName);
        listingFS = listingFS.fetchSpecificationWithQualifierBindings(queryDict);
        listingDS.setFetchSpecification(listingFS);

        try {
            WODisplayGroup listingDG = new WODisplayGroup();

            listingDG.setDataSource(listingDS);

            listingEC.lock();

            listingDG.qualifyDataSource();
            NSArray listings = listingDG.displayedObjects();

            if (listings.count() > 0) {
                nextPage = pageWithName("SearchResultPage");
                nextPage.takeValueForKey(listingDG, "listingDisplayGroup");
            } else {
                nextPage = pageWithName("SearchPage");
            }
        } finally {
            listingEC.unlock();
        }

        nextPage.takeValueForKey(queryDict, "queryDict");

        return nextPage;
    }

    // support methods

    private NSMutableDictionary _queryDictionaryFromRequest(WORequest request) {

        String bathrooms = request.stringFormValueForKey(Constants.QueryDictBathroomsKey);
        String bedrooms = request.stringFormValueForKey(Constants.QueryDictBedroomsKey);
        String city = request.stringFormValueForKey(Constants.QueryDictCityKey);
        String lowerLimitPrice = request.stringFormValueForKey(Constants.QueryDictLowerLimitPriceKey);
        String state = request.stringFormValueForKey(Constants.QueryDictStateKey);
        String street = request.stringFormValueForKey(Constants.QueryDictStreetKey);
        String upperLimitPrice = request.stringFormValueForKey(Constants.QueryDictUpperLimitPriceKey);
        String zip = request.stringFormValueForKey(Constants.QueryDictZipKey);

        NSMutableDictionary queryDict = new NSMutableDictionary();

        queryDict.takeValueForKey(state, Constants.QueryDictStateKey);
        if (city != null) {
            queryDict.takeValueForKey(city, Constants.QueryDictCityKey);
        }
        if (street != null) {
            queryDict.takeValueForKey(street, Constants.QueryDictStreetKey);
        }
        if (zip != null) {
            queryDict.takeValueForKey(zip, Constants.QueryDictZipKey);
        }
        if (lowerLimitPrice != null) {
            queryDict.takeValueForKey(new BigDecimal(lowerLimitPrice), Constants.QueryDictLowerLimitPriceKey);
        }
        if (upperLimitPrice != null) {
            queryDict.takeValueForKey(new BigDecimal(upperLimitPrice), Constants.QueryDictUpperLimitPriceKey);
        }
        if (bedrooms != null) {
            queryDict.takeValueForKey(new Float(bedrooms), Constants.QueryDictBedroomsKey);
        }
        if (bathrooms != null) {
            queryDict.takeValueForKey(new Float(bathrooms), Constants.QueryDictBathroomsKey);
        }

        return queryDict;
    }

}
