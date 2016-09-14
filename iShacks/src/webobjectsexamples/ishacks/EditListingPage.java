/*
 EditListingPage.java
 [iShacks Project]

 © Copyright 2005-2007  Apple Inc. All rights reserved.

 IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

 In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 */

package webobjectsexamples.ishacks;

import webobjectsexamples.realestate.common.Listing;
import webobjectsexamples.realestate.common.ListingPhoto;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.appserver.WORequest;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;

public class EditListingPage extends ValidationAwareComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2119991793127178549L;
	public EOEnterpriseObject currentFeature;
    public EOEditingContext defaultEditingContext;
    /** @TypeInfo Listing */
    public Listing listing;
    /** @TypeInfo ListingType */
    public EOEnterpriseObject listingType;
    public WODisplayGroup listingDisplayGroup;
    public boolean newListingMode;
    public WOComponent returnPage;

    //Validation ivars.
    protected boolean askingPriceValidationError;
    protected boolean sellingPriceValidationError;
    protected boolean bathroomsValidationError;
    protected boolean bedroomsValidationError;
    protected boolean cityValidationError;
    protected boolean lotSqFtValidationError;
    protected boolean sizeSqFtValidationError;
    protected boolean stateValidationError;
    protected boolean streetValidationError;
    protected boolean yearBuiltValidationError;
    protected boolean zipValidationError;

    public EditListingPage(WOContext context) {
        super(context);

        //Cache the editing context.
        Session session = (Session)session();
        defaultEditingContext = session.defaultEditingContext();
    }

    @Override
	public void takeValuesFromRequest(WORequest aRequest,WOContext aContext) {

        //Clear out validationErrorFlags.
        _clearValidationErrorFlags();

        super.takeValuesFromRequest(aRequest,aContext);
    }

    // ==== accessor methods

    /** @TypeInfo ListingType */
    public NSArray allListingTypes()
    {
        return EOUtilities.objectsForEntityNamed(defaultEditingContext,"ListingType");
    }

    /** @TypeInfo Feature */
    public NSArray allFeatures()
    {
        return EOUtilities.objectsForEntityNamed(defaultEditingContext,"Feature");
    }

    //Decides wheter or not this page should act as edition or creation mode.
    public void setNewListingMode() {
        newListingMode = true;
    }

    public void setListing(Listing aListing) {
        listing = aListing;
    }

    public Listing listing() {
        return listing;
    }

    public NSData photo() {

        if(listing.listingPhotos() == null) {
            return null;
        }

        if(listing.primaryPhoto() == null) {
            return null;
        }

        return listing.primaryPhoto().photo();
    }

    public void setPhoto(NSData newPhoto) {
        listing.primaryPhoto().setPhoto(newPhoto);
    }

    public void setPrimaryPhoto(NSData newPhotoData) {

        if (newPhotoData == null) {
            return;
        }

        if(listing.listingPhotos() == null) {
            listing.setListingPhotos(new NSArray());
        }

        ListingPhoto primaryPhoto = listing.primaryPhoto();
        if (primaryPhoto == null) {
            primaryPhoto = new ListingPhoto();
            defaultEditingContext.insertObject(primaryPhoto);
            listing.addObjectToBothSidesOfRelationshipWithKey(primaryPhoto,"listingPhotos");
        }
        primaryPhoto.setPhoto(newPhotoData);
        primaryPhoto.setIsPrimaryPhoto(Boolean.TRUE);
        listing.ensureListingPhotoIsPrimaryPhoto(primaryPhoto);
    }

    public void setReturnPage(WOComponent aPage) {
        returnPage = aPage;
    }

    public WOComponent returnPage() {
        if (returnPage == null) {
            return pageWithName("HomePage");
        }
		return returnPage;
    }

    // ==== action methods

    public WOComponent cancelEdits()
    {
        defaultEditingContext.revert();
        return returnPage();
    }

    public WOComponent changePhotoAction() {
        AddPhotoPage nextPage = (AddPhotoPage)pageWithName("AddPhotoPage");
        nextPage.setCallbackPageAndKey(this,"primaryPhoto");
        return nextPage;
    }

    public WOComponent saveEdits() {

        //Reload the page showing in red the fields with input issues.
        if (!isValidUserInputInformation()) {
            return null;
        }

        try {
            //Insert the listing if we are in insertion mode.
            if(newListingMode) {
                //Insert tthe address first.
                defaultEditingContext.insertObject(listing.address());
                defaultEditingContext.insertObject(listing);

                //Link both objects.
                listing.addObjectToBothSidesOfRelationshipWithKey(listing.address(),"address");

                //Link agents and the new listing..
                listing.addObjectToBothSidesOfRelationshipWithKey(((Session)session()).agent(),"agent");
            }

            defaultEditingContext.saveChanges();

        } catch (Exception e) {
            NSLog.err.appendln("An exception occurred while trying to save the listing information: " + e);
            defaultEditingContext.revert();
            return null;
        }
        return returnPage();
    }


    // ==== validation methods

    // Since we subclassing ValidationAwareComponent then we must provide the
    // flags that will tell us what input fields had validation errors.
    // We are using these flags in the page to turn on red the labels of those
    // fields failing the validation.
    public boolean askingPriceValidationError() {
        return askingPriceValidationError;
    }

    public void setAskingPriceValidationError(boolean flag) {
        askingPriceValidationError = flag;
    }

    public boolean sellingPriceValidationError()
    {
        return sellingPriceValidationError;
    }
    public void setSellingPriceValidationError(boolean newSellingPriceValidationError)
    {
        sellingPriceValidationError = newSellingPriceValidationError;
    }

    public boolean bathroomsValidationError()
    {
        return bathroomsValidationError;
    }

    public void setBathroomsValidationError(boolean newBathroomsValidationError)
    {
        bathroomsValidationError = newBathroomsValidationError;
    }

    public boolean bedroomsValidationError() {
        return bedroomsValidationError;
    }

    public void setBedroomsValidationError(boolean flag) {
        bedroomsValidationError = flag;
    }

    public boolean cityValidationError() {
        return cityValidationError;
    }

    public boolean lotSqFtValidationError()
    {
        return lotSqFtValidationError;
    }

    public void setLotSqFtValidationError(boolean newLotSqFtValidationError)
    {
        lotSqFtValidationError = newLotSqFtValidationError;
    }

    public void setCityValidationError(boolean flag) {
        cityValidationError = flag;
    }

    public boolean sizeSqFtValidationError()
    {
        return sizeSqFtValidationError;
    }

    public void setSizeSqFtValidationError(boolean newSizeSqFtValidationError)
    {
        sizeSqFtValidationError = newSizeSqFtValidationError;
    }

    public boolean stateValidationError() {
        return stateValidationError;
    }

    public void setStateValidationError(boolean flag) {
        stateValidationError = flag;
    }

    public boolean streetValidationError() {
        return streetValidationError;
    }

    public void setStreetValidationError(boolean newStreetValidationError) {
        streetValidationError = newStreetValidationError;
    }

    public boolean yearBuiltValidationError()
    {
        return yearBuiltValidationError;
    }

    public void setYearBuiltValidationError(boolean newYearBuiltValidationError)
    {
        yearBuiltValidationError = newYearBuiltValidationError;
    }

    public boolean zipValidationError() {
        return zipValidationError;
    }

    public void setZipValidationError(boolean flag) {
        zipValidationError = flag;
    }

    //This method could have more business logic to find out if there
    //is an isssue with the information provided by the user. Right now
    //just returns the general state of teh component(see ValidationAwareComponent).
    public boolean isValidUserInputInformation() {

        return (invalidInputFieldDetected()) ? false : true;
    }

    //Clear the flags that allow the page to display red labels
    //in the page.
    public void _clearValidationErrorFlags() {

        //Set the general state flag to false
        setInvalidInputFieldDetected(false);

        //Set false to all the ivars watching for invalid input.
        streetValidationError = false;
        cityValidationError = false;
        stateValidationError = false;
        zipValidationError= false;
        askingPriceValidationError = false;
        bedroomsValidationError = false;
        bathroomsValidationError = false;
        lotSqFtValidationError = false;
        sizeSqFtValidationError = false;
        yearBuiltValidationError = false;
        sellingPriceValidationError = false;

    }
}
