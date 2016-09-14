/*
 Main.java
 [iShacks Project]

© Copyright 2005-2007  Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.ishacks;

import webobjectsexamples.realestate.server.Customer;
import webobjectsexamples.realestate.server.User;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;

public class Main extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5511006703688193369L;
	public String cookieUserName;
    public String login;
    public boolean loginFailed;
    public String password;

    public Main(WOContext context) {
        super(context);
    }

    @Override
	public void awake() {
        super.awake();
        Session session = (Session)session();
        EOEditingContext ec = session.defaultEditingContext();
        ec.revert();

        //If we had a cookie we want to set the login text field
        //with whatever was stored in the cookie as username.
        IShacksCookie cookie = ((Session)session()).cookie();

        if(cookie != null && login == null) {
           login = cookie.userName();
        }

    }

    // == accessor methods

    //Returns the value of session cookie. This value is used to display the customers
    //name in the page.
    public String userFromCookie()
    {
        String cookieValue = null;
        Session session = (Session)session();
        IShacksCookie cookie = session.cookie();

        if(cookie != null) {
            cookieValue = cookie.userFirstName().toUpperCase();
        }

        return cookieValue;
    }

    public boolean showUserNameFromCookie() {

        return (userFromCookie() != null) ? true : false;
    }


    // ==== action methods.
    public HomePage loginAction()
    {
        if ((login == null) || (password == null)) {
            loginFailed = true;
            return null;
        }

        // allow user to log in
        Session session = (Session)session();
        EOEditingContext ec = session.defaultEditingContext();
        NSArray users = EOUtilities.objectsMatchingKeyAndValue(ec, Constants.UserEntityName, "login", login);
        User user = null;

        // could match more than one user, though it shouldn't
        if (users.count() > 0) {
            user = (User) users.objectAtIndex(0);
            loginFailed = false;
        } else {
            loginFailed = true;
            return null;
        }

        if (user.password().equals(password)) {
            session.setUser(user);
        } else {
            loginFailed = true;
        }

        if (session.userLoggedIn()) {
            IShacksCookie cookie;

            // Create a cookie. Store the password only if the user wants to
            cookie = new IShacksCookie( user.firstName(), user.login(), session.remeberPassword() ? user.password() : null);

            session.setCookie(cookie);
            session.setAddCookieToResponse();

            HomePage nextPage = (HomePage)pageWithName("HomePage");
            return nextPage;
        }

        return null;
    }

    public EditUserPage newUserAction()
    {
        EditUserPage nextPage = (EditUserPage)pageWithName("EditUserPage");
        Customer newCustomer = new Customer();
        nextPage.setNewUser(newCustomer);

        //Only if the user creates itself we should append a cookie
        //to the response. Remember, we could get into the edit page if
        //an agent decides to create another agent. For the later
        //we don't want to create a cookie for the new user.
        nextPage.setCreateCookie();

        return nextPage;
    }
}
