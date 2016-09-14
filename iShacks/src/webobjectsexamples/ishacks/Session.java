/*
 Session.java
 [iShacks Project]

 © Copyright 2005-2007  Apple Inc. All rights reserved.

 IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

 In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.
 */

/*
 * Session class
 *
 * 	The primary functionality in this subclass, in addition to simple accesor methods, is
 * the managing of the cookie to store user information and allow automatic login
 */
package webobjectsexamples.ishacks;

import webobjectsexamples.realestate.server.Administrator;
import webobjectsexamples.realestate.server.Agent;
import webobjectsexamples.realestate.server.Customer;
import webobjectsexamples.realestate.server.User;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.appserver.WOSession;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSTimestamp;

public class Session extends WOSession {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1314669203965489939L;
	public boolean addCookieToResponse;
    public IShacksCookie cookie;
    public NSTimestamp cookieStandarExpirationDate = null;
    protected boolean remeberPassword;
    public User user;
    protected boolean userLoggedInAutomatically;

    public Session() {
        super();
    }

    //Find out if the requests holds a cookie. If so create an iShacks
    //cookie with the cookie's value and notify we don't want to append
    //any cookie back.
    @Override
	public void awake() {
        super.awake();

        String cookieKey = WOApplication.application().name();
        String requestCookieValue = context().request().cookieValueForKey(cookieKey);

        if(requestCookieValue != null) {
            cookie = new IShacksCookie(requestCookieValue);
            addCookieToResponse = false;
        }
    }

    @Override
	public void appendToResponse(WOResponse response,WOContext aContext) {
        /**
        * Add a cookie containg the name of the user as a value.
         * We should only add a cookie to the response if is a new
         * customer adds himself. We don't add a cookie when an Admin
         * creates an user.
         */
        if(addCookieToResponse && (cookie != null)) {
            response.addCookie(cookie);
        }

        super.appendToResponse(response, aContext);
    }

    // ==== accessor methods

    public Agent agent() {
        Agent agent = null;

        if (user instanceof Agent) {
            agent = (Agent)user;
        }

        return agent;
    }

    public Customer customer() {
        Customer customer = null;

        if (user instanceof Customer) {
            customer = (Customer)user;
        }

        return customer;
    }

    public boolean userLoggedIn() {
        return (user != null);
    }

    public boolean userLoggedInAutomatically() {
        return userLoggedInAutomatically;
    }

    public void setUserLoggedInAutomatically(boolean flag) {
        userLoggedInAutomatically = flag;
    }

    public boolean userHasAdministrationPrivileges() {
        return ((user instanceof Agent) || (user instanceof Administrator));
    }

    public boolean remeberPassword() {
        return remeberPassword;
    }

    public void setRemeberPassword(boolean newRemeberPassword) {
        remeberPassword = newRemeberPassword;
    }

    public void setUser(User aUser) {
        user = aUser;
    }

    public User user() {
        return user;
    }

    public String starsImageNameForAgent(Agent anAgent)
    {
        String fileName;

        int rating = (anAgent.averageRating() == null) ? 0 : (anAgent.averageRating()).intValue();

        switch(rating) {
            case 1:
                fileName = "fourstars.jpg";
                break;
            case 2:
                fileName = "threestar.jpg";
                break;
            case 3:
                fileName = "twostars.jpg";
                break;
            case 4:
                fileName = "onestar.jpg";
                break;
            default:
                fileName = "onestar.jpg";
        }

        return fileName;
    }

    // cookie manipulation methods

    public IShacksCookie cookie() {
        return cookie;
    }

    public void setCookie(IShacksCookie newCookie) {
        cookie = newCookie;
    }

    //Only if told so the seeion will append the cookie to the response.
    public void setAddCookieToResponse() {
        addCookieToResponse = true;
    }

    public void clearCookie() {

        NSTimestamp now = new NSTimestamp();

        cookie.setExpires(now.timestampByAddingGregorianUnits(0, 0, -1, 0, 0, 0));
        addCookieToResponse = true;
    }

    public void logOutUserClearingCookie(boolean clearCookie) {
        user = null;
        userLoggedInAutomatically = false;
        if(clearCookie) {
            clearCookie();
        }
        this.terminate();
    }

    public boolean loginAutomatically() {

        NSArray users;
        User aUser = null;
        String login;
        String password;

        if(cookie() == null) {
            return false;
        }

        //We can't log in the user atumatically if we don have
        //the login and password.
        login = cookie().userName();
        password = cookie().password();
        if ((login == null) || (password == null)) {
            return false;
        }

        //If we are already logged in don't try it to do it again.
        if(userLoggedIn()) {
            return false;
        }

        users= EOUtilities.objectsMatchingKeyAndValue(defaultEditingContext(), Constants.UserEntityName, "login", login);

        //If we didn't find a user don't log in.
        if (users.count() < 0) {
            return false;
        }

        //If the user's name doesn't match the passord don't log in.
        aUser = (User) users.objectAtIndex(0);
        if (!aUser.password().equals(password)) {
            return false;
        }

        //Other wise log the user in and inform the session about it.
        setUser(aUser);
        userLoggedInAutomatically = true;

        return true;
    }

}
