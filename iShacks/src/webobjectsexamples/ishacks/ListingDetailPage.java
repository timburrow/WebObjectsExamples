/*
 ListingDetailPage.java
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

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSLog;

public class ListingDetailPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -9156497887972467711L;

	/** @TypeInfo Feature */
    public EOEnterpriseObject feature;

    protected Listing listing;
    public String login;

    public ListingDetailPage(WOContext context) {
        super(context);
    }

    // ==== accessor methods
    public void setListing(Listing newListing) {
        listing = newListing;
    }

    public Listing listing() {
        return listing;
    }

    // ==== action methods
    public HomePage deleteListingAction() {
        EOEditingContext ec = session().defaultEditingContext();
        ec.deleteObject(listing);
        ec.saveChanges();
        return (HomePage)pageWithName("HomePage");
    }


    public EditListingPage editListingAction() {
        EditListingPage nextPage = (EditListingPage)pageWithName("EditListingPage");

        nextPage.setListing(listing);
        nextPage.setReturnPage(this);

        return nextPage;
    }

    public WOComponent recommendToCustomerAction() {
        EOEditingContext ec = session().defaultEditingContext();
        EOEnterpriseObject customer = null;

        try {
            customer = EOUtilities.objectMatchingKeyAndValue(ec, Constants.CustomerEntityName, "login", login);
        } catch(Exception e) {
            //Swallow the exception since we are checking the cutomer value later.
        }

        if (customer != null) {
            listing.addObjectToBothSidesOfRelationshipWithKey(customer, "suggestedCustomers");

            //Save recomendation to database.
            try {
                ec.saveChanges();
            } catch (Exception e) {
                ec.revert();
                NSLog.err.appendln("An exception occurred while trying to save agen'ts recomendation.: " + e);
            }
        }

        return null;
    }
}
