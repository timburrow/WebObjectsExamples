/*
 * Listing.java
 * [JavaRealEstate Project]
 *
 * © Copyright 2001-2007 Apple Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (ÒAppleÓ) in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms,
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under AppleÕs copyrights in this original Apple
 * software (the ÒApple SoftwareÓ), to use, reproduce, modify and
 * redistribute the Apple Software, with or without modifications, in
 * source and/or binary forms; provided that if you redistribute the
 * Apple Software in its entirety and without modifications, you must
 * retain this notice and the following text and disclaimers in all such
 * redistributions of the Apple Software.  Neither the name, trademarks,
 * service marks or logos of Apple Computer, Inc. may be used to endorse
 * or promote products derived from the Apple Software without specific
 * prior written permission from Apple.  Except as expressly stated in
 * this notice, no other rights or licenses, express or implied, are
 * granted by Apple herein, including but not limited to any patent
 * rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated.
 *
 * The Apple Software is provided by Apple on an "AS IS" basis.  APPLE
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS
 * USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT,
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE,
 * REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE,
 * HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING
 * NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.realestate.common;

import java.math.BigDecimal;

import com.webobjects.eocontrol.EOCustomObject;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;

public class Listing extends EOCustomObject {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2770991505878635003L;
	public static final String AskingPriceKey = "askingPrice";
    public static final String BathroomsKey = "bathrooms";
    public static final String BedroomsKey = "bedrooms";
    public static final String IsSoldKey = "isSold";
    public static final String ListingNumberKey = "listingNumber";
    public static final String LotSqFtKey = "lotSqFt";
    public static final String SellingPriceKey = "sellingPrice";
    public static final String SizeSqFtKey = "sizeSqFt";
    public static final String VirtualTourURLKey = "virtualTourURL";
    public static final String YearBuiltKey = "yearBuilt";
    public static final String AddressKey = "address";
    public static final String AgentKey = "agent";
    public static final String ListingTypeKey = "listingType";
    public static final String FeaturesKey = "features";
    public static final String ListingPhotosKey = "listingPhotos";
    public static final String SuggestedCustomersKey = "suggestedCustomers";

    private static NSData _defaultListingPhotoData = null;

    private transient BigDecimal _askingPrice = null;
    private transient Double _bathrooms = null;
    private transient Double _bedrooms = null;
    private transient Boolean _isSold = null;
    private transient Integer _lotSqFt = null;
    private transient String _listingNumber = null;
    private transient BigDecimal _sellingPrice = null;
    private transient Integer _sizeSqFt = null;
    private transient String _virtualTourURL = null;
    private transient Integer _yearBuilt = null;
    private transient EOEnterpriseObject _address = null;
    private transient EOEnterpriseObject _agent = null;
    private transient EOEnterpriseObject _listingType = null;
    private transient NSArray _features = null;
    private transient NSArray _listingPhotos = null;
    private transient NSArray _suggestedCustomers = null;

    public static boolean usesDeferredFaultCreation() {
        return true;
    }

    public static void setDefaultListingPhotoData(NSData data) {
        _defaultListingPhotoData = data;
    }

    public static NSData defaultListingPhotoData() {
        return _defaultListingPhotoData;
    }

    public Listing() {
        super();
    }

    @Override
	public void awakeFromInsertion(EOEditingContext editingContext) {
        super.awakeFromInsertion(editingContext);
        // create something like a random listing identifier for example purposes
        String number = new Long(System.currentTimeMillis()).toString();
        int length = number.length();
        if (length > 6) {
            number = number.substring(length - 6, length);
        }
        setListingNumber("MLS #" + number);
    }

    public BigDecimal askingPrice() {
        willRead();
        return _askingPrice;
    }

    public void setAskingPrice(BigDecimal value) {
        willChange();
        _askingPrice = value;
    }

    public Double bathrooms() {
        willRead();
        return _bathrooms;
    }

    public void setBathrooms(Double value) {
        willChange();
        _bathrooms = value;
    }

    public Double bedrooms() {
        willRead();
        return _bedrooms;
    }

    public void setBedrooms(Double value) {
        willChange();
        _bedrooms = value;
    }

    public Boolean isSold() {
        willRead();
        return _isSold;
    }

    public void setIsSold(Boolean value) {
        willChange();
        _isSold = value;
    }

    public Integer lotSqFt() {
        willRead();
        return _lotSqFt;
    }

    public void setLotSqFt(Integer value) {
        willChange();
        _lotSqFt = value;
    }

    public String listingNumber() {
        willRead();
        return _listingNumber;
    }

    public void setListingNumber(String value) {
        willChange();
        _listingNumber = value;
    }

    public BigDecimal sellingPrice() {
        willRead();
        return _sellingPrice;
    }

    public void setSellingPrice(BigDecimal value) {
        willChange();
        _sellingPrice = value;
    }

    public Integer sizeSqFt() {
        willRead();
        return _sizeSqFt;
    }

    public void setSizeSqFt(Integer value) {
        willChange();
        _sizeSqFt = value;
    }

    public String virtualTourURL() {
        willRead();
        return _virtualTourURL;
    }

    public void setVirtualTourURL(String value) {
        willChange();
        _virtualTourURL = value;
    }

    public Integer yearBuilt() {
        willRead();
        return _yearBuilt;
    }

    public void setYearBuilt(Integer value) {
        willChange();
        _yearBuilt = value;
    }

    public EOEnterpriseObject address() {
        willRead();
        willReadRelationship(_address);
        return _address;
    }

    public void setAddress(EOEnterpriseObject value) {
        willChange();
        _address = value;
    }

    public EOEnterpriseObject agent() {
        willRead();
        willReadRelationship(_agent);
        return _agent;
    }

    public void setAgent(EOEnterpriseObject value) {
        willChange();
        _agent = value;
    }

    public EOEnterpriseObject listingType() {
        willRead();
        willReadRelationship(_listingType);
        return _listingType;
    }

    public void setListingType(EOEnterpriseObject value) {
        willChange();
        _listingType = value;
    }

    public NSArray features() {
        willRead();
        willReadRelationship(_features);
        return _features;
    }

    public void setFeatures(NSArray value) {
        willChange();
        _features = value;
    }

    public void addToFeatures(EOEnterpriseObject object) {
        includeObjectIntoPropertyWithKey(object, FeaturesKey);
    }

    public void removeFromFeatures(EOEnterpriseObject object) {
        excludeObjectFromPropertyWithKey(object, FeaturesKey);
    }

    public NSArray listingPhotos() {
        willRead();
        willReadRelationship(_listingPhotos);
        return _listingPhotos;
    }

    public void setListingPhotos(NSArray value) {
        willChange();
        _listingPhotos = value;
    }

    public void addToListingPhotos(ListingPhoto object) {
        ListingPhoto primaryPhoto = primaryPhoto();
        includeObjectIntoPropertyWithKey(object, ListingPhotosKey);
        if (primaryPhoto != null) {
            ensureListingPhotoIsPrimaryPhoto(primaryPhoto);
        }
    }

    public void removeFromListingPhotos(ListingPhoto object) {
        excludeObjectFromPropertyWithKey(object, ListingPhotosKey);
    }

    public NSArray suggestedCustomers() {
        willRead();
        willReadRelationship(_suggestedCustomers);
        return _suggestedCustomers;
    }

    public void setSuggestedCustomers(NSArray value) {
        willChange();
        _suggestedCustomers = value;
    }

    public void addToSuggestedCustomers(EOEnterpriseObject object) {
        includeObjectIntoPropertyWithKey(object, SuggestedCustomersKey);
    }

    public void removeFromSuggestedCustomers(EOEnterpriseObject object) {
        excludeObjectFromPropertyWithKey(object, SuggestedCustomersKey);
    }

    public ListingPhoto primaryPhoto() {
        NSArray listingPhotos = listingPhotos();
        int count = listingPhotos.count();
        for (int i = 0; i < count; i++) {
            ListingPhoto photo = (ListingPhoto)(listingPhotos.objectAtIndex(i));
            Boolean isPrimaryPhoto = photo.isPrimaryPhoto();
            if ((isPrimaryPhoto != null) && (isPrimaryPhoto.booleanValue() == true)) {
                return photo;
            }
        }
        return (count > 0) ? (ListingPhoto)(listingPhotos.objectAtIndex(0)) : null;
    }

    public NSData primaryPhotoData() {
        ListingPhoto primaryPhoto = primaryPhoto();
        return (primaryPhoto != null) ? primaryPhoto.photo() : defaultListingPhotoData();
    }

    public void ensureListingPhotoIsPrimaryPhoto(EOEnterpriseObject primaryPhoto) {
        NSArray listingPhotos = listingPhotos();
        int count = listingPhotos.count();
        for (int i = 0; i < count; i++) {
            ListingPhoto photo = (ListingPhoto)(listingPhotos.objectAtIndex(i));
            if (photo != primaryPhoto) {
                Boolean isPrimaryPhoto = photo.isPrimaryPhoto();
                if ((isPrimaryPhoto != null) && (isPrimaryPhoto.booleanValue() == true)) {
                    photo.setIsPrimaryPhoto(Boolean.FALSE);
                }
            }
        }
    }
}
