/*
 AgentDetailPage.java
 [iShacks Project]

 © Copyright 2001 Apple Computer, Inc. All rights reserved.

 IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

 In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 */

package webobjectsexamples.ishacks;

import webobjectsexamples.realestate.server.Agent;
import webobjectsexamples.realestate.server.Customer;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;

public class AgentDetailPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2379016422836871431L;
	/** @TypeInfo AgentRating */
    public EOEnterpriseObject agentRating;
    /** @TypeInfo Rating */
    public NSMutableArray ratings;
    /** @TypeInfo Rating */
    public EOEnterpriseObject rating;
    /** @TypeInfo Rating */
    public EOEnterpriseObject selectedRating;

    public Agent agent;
    public String commentString;
    public EOEditingContext defaultEditingContext;
    public Session session;
    protected boolean shouldShowChooseAgentButton;

    public AgentDetailPage(WOContext context) {
        super(context);

        session = (Session)session();
        defaultEditingContext = session.defaultEditingContext();

        //Preload the raiting list.
        ratings = (NSMutableArray)EOUtilities.objectsForEntityNamed(defaultEditingContext, "Rating");
    }

    // ==== accessor methods

    public Agent agent()
    {
        return agent;
    }

    public void setAgent(Agent newAgent)
    {
        agent = newAgent;
    }

    public void setShouldShowChooseAgentButton(boolean newShouldShowChooseAgentButton)
    {
        shouldShowChooseAgentButton = newShouldShowChooseAgentButton;
    }

    public boolean shouldShowChooseAgentButton()
    {
        return shouldShowChooseAgentButton;
    }

    public boolean shouldShowRatingImage()
    {
        return (agent.averageRating() != null);
    }

    // Get the star Image file name corresponding to the Agen't average rating.
    public String starsImageName() {
        return ((Session)session()).starsImageNameForAgent(agent);
    }

    // ==== action methods

    public WOComponent addRatingAction()
    {

        EOEnterpriseObject newAgentRating = EOUtilities.createAndInsertInstance(defaultEditingContext, "AgentRating");

        //Use key-value coding  to add to the new EO the rating,comment and agent information.
        newAgentRating.takeValueForKey(selectedRating, "rating");
        newAgentRating.takeValueForKey(commentString, "comment");
        newAgentRating.takeValueForKey(agent, "agent");

        //Add rating object to rating Agent relationship.
        agent.addToRatings(newAgentRating);

        // save changes to user in the database
        try {
            defaultEditingContext.saveChanges();
        } catch (Exception e) {
            NSLog.err.appendln("An exception occurred while trying to remove a rating: " + e);
            defaultEditingContext.revert();
        }
        return null;
    }

    public WOComponent removeRatingAction()
    {
        agent.removeFromRatings(agentRating);

        // save changes to user in the database
        try {
            defaultEditingContext.saveChanges();
        } catch (Exception e) {
            NSLog.err.appendln("An exception occurred while trying to remove a rating: " + e);
            defaultEditingContext.revert();
        }

        return null;
    }

    public HomePage selectAgentAction()
    {
        HomePage nextPage = (HomePage)pageWithName("HomePage");
        Customer customer = session.customer();

        if (customer != null) {
            customer.setAgent(agent);

            try {
                defaultEditingContext.saveChanges();
            } catch (Exception e) {
                NSLog.err.appendln("An exception occurred while trying to select an agent: " + e);
                defaultEditingContext.revert();
            }
        }

        return nextPage;
    }

}

