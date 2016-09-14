
/*
 IShacksCookie.java
 [iShacks Project]

© Copyright 2005-2007  Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/*
 * WOCookie creates a cookie based on Netscape's cookies definition. The spec allows
 * the creation of a cookie with at least a key value pair of information
 * (NAME=VALUE). In the case of iShacks we would want to store and distinguish more
 * information in the value part than just a single value. In order to allow a
 * user to log-in automatically the client needs to store the user's password as well
 * as the name and userName.
 * A way to accomplish this task would be to create three cookies: one for each piece of
 * information. However, for the purpose of this example we decided to store just
 * one cookie with the proper information.
 *
 * We have subclassed WOCookie and added some logic to set the cookie's
 * values as a string containing the customer's information separated by a non typical
 * char. IShacksCookie will take care of storing this information in the cookie with the
 * proper format as well as parsing the information back.
 *
 * One disadvantage of this is that the separator could interfere with the
 * information provided.
 *
 * The cookie is managed by the session. The session instance
 * is the one at the end who decides when to append a cookie to the reponse or not.
 * A new cookie is created in the submitAction of the EditUserPage.
 * Look in the awake methods of Session and Main for more on how to get a
 * handle of the cookies.
 */
package webobjectsexamples.ishacks;

import java.util.StringTokenizer;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOCookie;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

public class IShacksCookie extends WOCookie {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6143212007592082780L;
	protected NSTimestamp cookieStandarExpirationDate = null;
    protected String userFirstName;
    protected String userName;
    protected String password;
    private static final String VALUEDELIMITER = "~";
    private static final String cookiePath = "/";


    /*
     * Constructs a WOCookie with the form: iShacks = aFirstName~aUserName~aPassword.
     * At least the first name should be provided otherwise it will throw. It will
     * set the expiration time with the cookieStandarExpirationDate value.
     */
    public IShacksCookie(String aFirstName, String aUserName, String aPassword) {
        super(WOApplication.application().name(),"");

        if(aFirstName == null) {
            throw new IllegalArgumentException("<"+getClass().getName()+">: At least first name should be provided.");
        }

        userFirstName = aFirstName;
        userName = aUserName;
        password = aPassword;

        setValue(cookieValue());
        setExpires(cookieStandarExpirationDate());
        setPath(cookiePath);
    }

    /*
     * Contsructs a cookie with the customer's name.
     */
    public IShacksCookie(String value) {
        super(WOApplication.application().name(),value);

        setExpires(cookieStandarExpirationDate());
        setCookieValue(value);
        setPath(cookiePath);
    }

    // ==== accessor methods

    public String userFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String name) {
        userFirstName = name;
    }

    public String userName() {
        return userName;
    }

    public void setUserName(String name) {
        userName = name;
    }

    public String password() {
        return password;
    }

    public void setPassword(String passwd) {
        password = passwd;
    }

    /*
     * Gets a string of the form: firstName~userName~password.
     */
    public String cookieValue() {
        StringBuffer stringValue = new StringBuffer();

        stringValue.append(userFirstName);

        if(userName != null) {
            stringValue.append(VALUEDELIMITER);
            stringValue.append(userName);
        }

        if(password != null) {
            stringValue.append(VALUEDELIMITER);
            stringValue.append(password);
        }

        return stringValue.toString();
    }

    /*
     * Sets a string of the form: firstName~userName~password into
     * the ivars username,name and password as well as setting the
     * values in the WOCookie.
     */
    public void setCookieValue(String aValue) {
        StringTokenizer  tokenizer;
        NSMutableArray values;

        if(aValue == null) {
            return;
        }

        //Sets the superclass value.
        setValue(aValue);

        //Parse the string a set the iShacks ivars.
        tokenizer  = new StringTokenizer(aValue,VALUEDELIMITER);
        values = new NSMutableArray();
        while (tokenizer.hasMoreTokens()) {
            values.addObject(tokenizer.nextToken());
        }

        userFirstName = (String)values.objectAtIndex(0);

        if (values.count() > 1) {
            userName = (String)values.objectAtIndex(1);
        }

        if(values.count() > 2){
            password = (String)values.objectAtIndex(2);
        }
    }

    /*
     *	Gets a times stamp of a week in the future.
     */
    public NSTimestamp cookieStandarExpirationDate() {

        if(cookieStandarExpirationDate == null) {
            //Expire the cookie in 7 days.
            NSTimestamp now = new NSTimestamp();
            cookieStandarExpirationDate = now.timestampByAddingGregorianUnits(0, 0, 7, 0, 0, 0);
        }

        return cookieStandarExpirationDate;
    }
}
