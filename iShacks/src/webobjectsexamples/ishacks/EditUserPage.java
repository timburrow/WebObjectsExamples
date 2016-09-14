/*
 EditUserPage.java
 [iShacks Project]

 © Copyright 2005-2007  Apple Inc. All rights reserved.

 IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

 In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 */

package webobjectsexamples.ishacks;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;
import com.webobjects.eocontrol.*;
import com.webobjects.eoaccess.*;
import webobjectsexamples.realestate.server.*;

public class EditUserPage extends ValidationAwareComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5437541701141150491L;
	public static final String ListingsKey = "listings";
    public static final String PhotoKey = "photo";
    public static final String RatingsKey = "ratings";
    public static final String UsersKey = "customers";

    /** @TypeInfo ContactInfo */
    public EOEnterpriseObject contactInfo;
    /** @TypeInfo ContactType */
    public EOEnterpriseObject contactType;
    /** @TypeInfo ContactType */
    public EOEnterpriseObject selectedContactType;

    public String confirmPassword;
    public String contactString;
    public NSMutableArray contactTypes;
    public boolean createCookie = false;
    public boolean dataMissing = false;
    public boolean duplicateRecord = false;
    public EOEditingContext defaultEditingContext;
    public boolean incorrectPassword = false;
    public boolean newUserMode = false;
    public String originalUserName;
    public User user;

    protected boolean firstNameValidationError;
    protected boolean lastNameValidationError;
    protected boolean loginValidationError;
    protected boolean passwordValidationError;


    public EditUserPage(WOContext context) {
        super(context);

        //Cache the editing context.
        Session session = (Session)session();
        defaultEditingContext = session.defaultEditingContext();

        //Preload the contact types.
        contactTypes = (NSMutableArray)EOUtilities.objectsForEntityNamed(defaultEditingContext, "ContactType");
    }

    @Override
	public void takeValuesFromRequest(WORequest aRequest,WOContext aContext) {
        //Clear out validationErrorFlags.
        _clearValidationErrorFlags();

        super.takeValuesFromRequest(aRequest,aContext);
    }

    // == accessor methods

    //Sets the agent's photo.
    public void setAgentPhoto(NSData newPhotoData) {
        Agent agent = (Agent)user;

        //If don't have an agent yet just return.
        if(agent == null) {
            return;
        }

        if (newPhotoData == null) {
            return;
        }

        EOEnterpriseObject agentPhoto = agent.photo();
        if (agentPhoto == null) {
            agentPhoto = new EOGenericRecord(EOClassDescription.classDescriptionForEntityName("AgentPhoto"));
            defaultEditingContext.insertObject(agentPhoto);
            agent.addObjectToBothSidesOfRelationshipWithKey(agentPhoto,"photo");
        }
        agentPhoto.takeValueForKey(newPhotoData,"photo");
    }

    //Decides if we should add or not a cookie when a new user gets created.
    public void setCreateCookie() {
        createCookie = true;
    }

    public User user() {
        return user;
    }

    public void setNewUser(User newUser) {
        newUser.setLogin("");
        setUser(newUser);
        newUserMode = true;
    }

    public void setUser(User newUser) {
        user = newUser;
        originalUserName = newUser.login();
        //We need to populate those values not present
        //in the User EO such as the password confirmation value.
        syncConfirmAndUserPassword();
    }

    public String userTypeName()
    {
        return user.entityName();
    }

    // ==== action methods

    //Add contact information to the user.
    public WOComponent addContactInfoAction()
    {
        //Don't add an empty contact.
        if(contactString == null || selectedContactType == null) {
            return null;
        }

        EOEnterpriseObject newContactInfo = EOUtilities.createAndInsertInstance(defaultEditingContext, "ContactInfo");

        newContactInfo.takeValueForKey(contactString, "contact");
        newContactInfo.takeValueForKey(user, "user");
        newContactInfo.addObjectToBothSidesOfRelationshipWithKey(selectedContactType, "contactType");

        user.addToContactInfo(newContactInfo);

        // save changes to user in the database
        try {
            defaultEditingContext.saveChanges();
        } catch (Exception e) {
            defaultEditingContext.revert();
            NSLog.err.appendln("An exception occurred while trying to save the contact information: " + e);
        }

        return null;//Display the new contact.
    }

    //The method will bring a page specialized on image upload. It will
    //set wich page to get back as well as the method to call to persist
    //the image(setAgentPhoto).
    public AddPhotoPage changeAgentPhotoAction()
    {
        AddPhotoPage nextPage = (AddPhotoPage)pageWithName("AddPhotoPage");
        nextPage.setCallbackPageAndKey(this,"agentPhoto");
        return nextPage;
    }

    //Remove contact from user's contct list.
    public WOComponent removeContactInfoAction()
    {
        user.removeFromContactInfo(contactInfo);

        // save changes to user in the database
        try {
            defaultEditingContext.saveChanges();
        } catch (Exception e) {
            defaultEditingContext.revert();
            NSLog.err.appendln("An exception occurred while trying to remove the contact information: " + e);
        }

        return null;
    }

    //Save changes for an existant user or otherwise create
    //a new one.
    public HomePage submitAction()
    {
        //If there are no input errors try to save information.
        //Otherwise return the same page with the apropiate flags
        //to show up error status.
        if (_validateUserInput()) {

            // All new customers must have an Agent.
            // Add a default one for the new cutomers.
            if (newUserMode && user instanceof Customer) {
                Customer customer = (Customer)user;
                NSArray agents = EOUtilities.objectsForEntityNamed(defaultEditingContext, Constants.AgentEntityName);

                //Assign the first agent in the list.
                if(agents.count() > 0) {
                    Agent agent = (Agent) agents.objectAtIndex(0);
                    customer.setAgent(agent);
                }
            }

            //Save changes to user in the database
            try {

                //If it is a new user insert it first.
                if(newUserMode) {
                    defaultEditingContext.insertObject(user);
                }

                defaultEditingContext.saveChanges();

            } catch (Exception e) {
                defaultEditingContext.revert();
                NSLog.err.appendln("An exception occurred while trying to save the user information: " + e);
            }

            //Log user into the app only if we he is a new custmer.
            if (newUserMode && user instanceof Customer) {
                ((Session)session()).setUser(user);
            }

            //We will try to create a cookie and get to the home page if an user
            //logged in succesfully.
            if(((Session)session()).user() != null) {

                //This action sets a new cookie for the new user.
                if(createCookie) {

                    IShacksCookie cookie = new IShacksCookie(user.firstName(),null,null);

                    ((Session)session()).setCookie(cookie);
                    ((Session)session()).setAddCookieToResponse();
                }

                //Go to homepage.
                HomePage nextPage = (HomePage)pageWithName("HomePage");
                return nextPage;
            }
        }

        return null;//Show the edit page with proper error mesaages.
    }


    // ==== validation methods

    // Since we subclassing ValidationAwareComponent then we must provide the
    // flags that will tell us what input fields had validation errors.
    // We are using these flags in the page to turn on red the labels of those
    // fields failing the validation.
    public boolean firstNameValidationError()
    {
        return firstNameValidationError;
    }
    public void setFirstNameValidationError(boolean newFirstNameValidationError)
    {
        firstNameValidationError = newFirstNameValidationError;
    }

    public boolean lastNameValidationError()
    {
        return lastNameValidationError;
    }
    public void setLastNameValidationError(boolean newLastNameValidationError)
    {
        lastNameValidationError = newLastNameValidationError;
    }

    public boolean loginValidationError()
    {
        return loginValidationError;
    }
    public void setLoginValidationError(boolean newLoginValidationError)
    {
        loginValidationError = newLoginValidationError;
    }

    //The firts time we present the user his personal information
    //we neeedt to sync the value of the confirm password ivar with
    //the user's password.
    public void syncConfirmAndUserPassword() {

        if(confirmPassword == null && user.password() != null) {
            confirmPassword = user.password();
        }
    }

    //Will validate that the password and user information is valid.
    //This method has the side effect of turning on the proper flags to
    //display errors on the page.
    //Returns true if everything looks ok false otherwise.
    private boolean _validateUserInput() {

        boolean validInput = false;

        //Check that the password is ok and set the gobal
        //flag incorrectPassword to the proper value.
        if (confirmPassword != null && user.password() != null
            && confirmPassword.equals(user.password())) {
            incorrectPassword = false;
        } else {
            incorrectPassword = true;
        }

        //Check that all the input fields have information and that
        //this is correct.
        dataMissing = invalidInputFieldDetected();

        //verify we are not trying to add a new user
        //with a already existent login name.
        if(!incorrectPassword &&
           !dataMissing &&
           !(originalUserName.equals(user.login()))
           ) {

            //If there is an user with that login name we should display an error message.
            if(EOUtilities.objectsMatchingKeyAndValue(defaultEditingContext, Constants.UserEntityName, "login", user.login()).count() > 0) {

                duplicateRecord = true;
                //Turn on the error flag to show the login label in red.
                setLoginValidationError(duplicateRecord);
            }
        }

        validInput = (dataMissing || incorrectPassword || duplicateRecord) ? false :true;

        return validInput;

    }

    //Clear the flags that allow the page to display red labels
    //in the page. This method is executed at the beginning of the
    //request response loop to clear out any validation error flag
    //set on previous request.
    private void _clearValidationErrorFlags() {

        //Set the general state flag to false
        setInvalidInputFieldDetected(false);


        firstNameValidationError = false;
        lastNameValidationError = false;
        loginValidationError = false;
        passwordValidationError = false;
        duplicateRecord = false;
    }

    public boolean passwordValidationError()
    {
        return passwordValidationError;
    }
    public void setPasswordValidationError(boolean newPasswordValidationError)
    {
        passwordValidationError = newPasswordValidationError;
    }

}
