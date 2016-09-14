/*
 HomePage.java
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
import webobjectsexamples.realestate.server.Agent;
import webobjectsexamples.realestate.server.Customer;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSLog;

public class HomePage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -325483980443844648L;

	/** @TypeInfo Customer */
    public EOEnterpriseObject aCustomer;

    public Listing listing;


    public HomePage(WOContext context) {
        super(context);
    }

    // ==== accessor methods

    public Listing listing() {
        return listing;
    }

    public void setListing(Listing newListing) {
        listing = newListing;
    }

    // ==== action methods

    //Removes the selected listing from the suggested listing list.
    public WOComponent deleteSelectedListingAction() {

        //Get the customer
        Session session = (Session)session();
        Customer customer = session.customer();

        //Remove the listing from the customer.
        customer.removeFromSuggestedListings(listing);

        //Save to the database.
        EOEditingContext ec = session.defaultEditingContext();
        try {
            ec.saveChanges();
        } catch (Exception e) {
            NSLog.err.appendln("An exception occurred while trying to remove a listing: " + e);
            ec.revert();
        }

        return null;
    }

    //Takes the user to the detail of the selected listing.
    public ListingDetailPage selectListingAction(){

        ListingDetailPage nextPage = (ListingDetailPage)pageWithName("ListingDetailPage");

        nextPage.setListing(listing);

        return nextPage;
    }

    //Allows user to navigate to a page where he can selct a different agent.
    public ChooseAgentPage selectAgentAction() {

        ChooseAgentPage nextPage = (ChooseAgentPage)pageWithName("ChooseAgentPage");

        return nextPage;
    }

    //Allows the user to navigate to see his agent info.
    public AgentDetailPage viewAgentAction() {

        //Get agent
        Session session = (Session)session();
        Customer customer = session.customer();
        Agent agent = customer.agent();

        //Navigate to the AgentDetailPage page.
        AgentDetailPage nextPage = (AgentDetailPage)pageWithName("AgentDetailPage");
        nextPage.setAgent(agent);

        return nextPage;
    }

}
