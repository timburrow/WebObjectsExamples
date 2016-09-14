/*******************************************************************************
 *
 * [SophisticatedDatabaseExample Project]
 *
 * © 2005, 2007 Apple Computer, Inc.. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.
 *
 * The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 *
 *******************************************************************************/

package webobjectsexamples.sophisticateddatabaseexample;

import java.util.GregorianCalendar;

import com.webobjects.appserver.WOSession;
import com.webobjects.eoaccess.EOObjectNotAvailableException;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSKeyValueCoding;
import com.webobjects.foundation.NSTimestamp;

public class Session extends WOSession {
	/**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= -6228521688077084835L;

	protected String			password;

	protected String			login;

	protected Admin				admin;

	protected Student			guest;

	public Session() {
		super();

		/* ** Put your per-session initialization code here ** */
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String newPassword) {
		password = newPassword;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String newLogin) {
		login = newLogin;
	}

	/**
	 * Test if user logged in as admin user
	 */
	public boolean isAdmin() {
		return (isLoggedIn() && isValidUser() && login.equals("admin"));
	}

	/**
	 * Test if user is valid
	 */
	public boolean isValidUser() {
		boolean result = false;
		EOEditingContext ec = defaultEditingContext();

		// Create "guest" user, if not yet defined
		try {
			guest = (Student) EOUtilities.objectMatchingKeyAndValue(ec, "Student", "login", "guest");
		} catch (EOObjectNotAvailableException exception1) {
			guest = new Student();
			HomeAddress homeAddress = new HomeAddress();
			guest.setAddress(homeAddress);
			guest.setFirstName("Guest");
			guest.setLogin("guest");
			guest.setPassword("guest");
			ec.insertObject(guest);
			ec.insertObject(homeAddress);
			// save changes permanently in database
			ec.saveChanges();
		}

		// Create "admin" user, if not yet defined
		try {
			/* Admin admin = (Admin) */EOUtilities.objectMatchingKeyAndValue(ec, "Admin", "login", "admin");
		} catch (EOObjectNotAvailableException exception2) {
			admin = new Admin();
			WorkAddress workAddress = new WorkAddress();
			admin.setAddress(workAddress);
			admin.setFirstName("Administrator");
			admin.setLogin("admin");
			admin.setPassword("admin");
			ec.insertObject(admin);
			ec.insertObject(workAddress);
			// save changes permanently in database
			ec.saveChanges();
		}

		// Search database for valid student
		Object searchLogin = (login != null) ? (Object) login : (Object) NSKeyValueCoding.NullValue;

		try {
			Student found = (Student) EOUtilities.objectMatchingKeyAndValue(ec, "Student", "login", searchLogin);
			if (found.password().equals(password)) {
				return true;
			}
		} catch (EOUtilities.MoreThanOneException ex) {
			return false;
		} catch (EOObjectNotAvailableException exception3) {
			// Search database for valid employee
			try {
				Employee found = (Employee) EOUtilities.objectMatchingKeyAndValue(ec, "Employee", "login", searchLogin);
				if (found.password().equals(password)) {
					return true;
				}
			} catch (EOObjectNotAvailableException exception4) {
				return false;
			} catch (EOUtilities.MoreThanOneException ex) {
				return false;
			}
		}
		return (result);
	}

	/**
	 * Test if user has logged in yet
	 */
	public boolean isLoggedIn() {
		return (login != null);
	}

	/**
	 * Test if name is valid
	 */
	public String isValidName(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetter(c)) {
					msg = "The value \"" + value + "\" is not a valid name. Avoid numbers and punctuation in names. Try something like \"Mary\"";
					break;
				}
			}
		} else {
			msg = "A valid name is required. Avoid numbers and punctuation in names. Try something like \"Mary\"";
		}
		return (msg);
	}

	/**
	 * Test if street address is valid
	 */
	public String isValidStreet(String value) {
		String msg = null;
		boolean hasLetters = false;
		boolean hasDigits = false;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetter(c)) {
					hasLetters = true;
				} else if (!Character.isDigit(c)) {
					hasDigits = true;
				}
			}
			if (!hasLetters || !hasDigits) {
				// FIXME [PJYF Mar 1 2007]]
			}
		} else {
			msg = "A valid street address is required. You need both the street name and number. Try something like \"42 Hyde Street\"";
		}
		return (msg);
	}

	/**
	 * Test if city is valid
	 */
	public String isValidCity(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
					msg = "The value \"" + value + "\" is not a valid city. Avoid numbers and punctuation in cities. Try something like \"San Francisco\"";
					break;
				}
			}
		} else {
			msg = "A valid city is required. Avoid numbers and punctuation in cities. Try something like \"San Francisco\"";
		}
		return (msg);
	}

	/**
	 * Test if state is valid
	 */
	public String isValidState(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetter(c) || value.length() > 2) {
					msg = "The value \"" + value + "\" is not a valid state. Please use the 2-letter abbreviation for the state. Try something like \"CA\"";
					break;
				}
			}
		} else {
			msg = "A valid state is required. Please use the 2-letter abbreviation for the state. Try something like \"CA\"";
		}
		return (msg);
	}

	/**
	 * Test if date is valid
	 */
	public String isValidDate(NSTimestamp value) {
		String msg = null;
		String fullDate = null;
		int month = -1;
		int day = -1;
		int year = -1;
		GregorianCalendar calendar = new GregorianCalendar();

		if (value != null) {
			calendar.setTime(value);
			month = calendar.get(GregorianCalendar.MONTH) + 1;
			day = calendar.get(GregorianCalendar.DAY_OF_MONTH);
			year = calendar.get(GregorianCalendar.YEAR);
			fullDate = month + "/" + day + "/" + year;
			if (month < 1 || month > 12 || day < 1 || day > 31) {
				msg = "The value \"" + fullDate + "\" is not a valid date. Try something like \"3/24/01\"";
			}
		} else {
			msg = "A valid date is required. Try something like \"3/24/01\"";
		}
		return (msg);
	}

	/**
	 * Test if ZIP code is valid
	 */
	public String isValidZip(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isDigit(c) || value.length() > 5) {
					msg = "The value \"" + value + "\" is not a valid ZIP code. Use only numbers with 5 digits. Try something like \"95014\"";
					break;
				}
			}
		} else {
			msg = "A valid ZIP code is required. Use only numbers with 5 digits. Try something like \"95014\"";
		}
		return (msg);
	}

	/**
	 * Test if e-mail address is valid
	 */
	public String isValidEmail(String value) {
		String msg = null;
		if (value != null) {
			if (value.indexOf("@") == -1 || value.indexOf(".") == -1) {
				msg = "The value \"" + value + "\" is not a valid email address. Try something like \"sjones@apple.com\"";
			}
		} else {
			msg = "A valid e-mail address is required. Try something like \"sjones@apple.com\"";
		}
		return (msg);
	}

	/**
	 * Test if login is valid
	 */
	public String isValidLogin(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetterOrDigit(c) || value.length() > 8 || value.length() < 4) {
					msg = "The value \"" + value + "\" is not a valid login. Use at least 4 and no more than 8 letters and numbers. Try something like \"sjones\"";
					break;
				}
			}
		} else {
			msg = "A valid login is required. Use at least 4 and no more than 8 letters and numbers. Try something like \"sjones\"";
		}
		return (msg);
	}

	/**
	 * Test if password is valid
	 */
	public String isValidPassword(String value) {
		String msg = null;
		if (value != null) {
			for (int index = 0; index < value.length(); index++) {
				char c = value.charAt(index);
				if (!Character.isLetterOrDigit(c) || value.length() > 8 || value.length() < 4) {
					msg = "The value \"" + value + "\" is not a valid password. Use a combination of least 4 and no more than 8 letters and numbers. Try something like \"l0ve\"";
					break;
				}
			}
		} else {
			msg = "A valid password is required. Use a combination of least 4 and no more than 8 letters and numbers. Try something like \"l0ve\"";
		}
		return (msg);
	}
}
