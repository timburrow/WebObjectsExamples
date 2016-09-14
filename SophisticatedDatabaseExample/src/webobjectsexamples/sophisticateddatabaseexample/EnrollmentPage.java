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

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;

public class EnrollmentPage extends WOComponent {
	private static final long	serialVersionUID	= 4359849883143896363L;

	private Exception			error				= null;

	protected Student			student;

	protected HomeAddress		homeAddress;

	public EnrollmentPage(WOContext context) {
		super(context);
		Session session = (Session) session();
		EOEditingContext ec = session.defaultEditingContext();

		// check for currently logged-in user
		if (!session.getLogin().equals("guest") && session.isValidUser()) {
			student = (Student) EOUtilities.objectMatchingKeyAndValue(ec, "Student", "login", session.getLogin());
			homeAddress = (HomeAddress) student.address();
		} else {
			// Add new Student to editing context
			student = new Student();
			homeAddress = new HomeAddress();
			ec.insertObject(student);
			ec.insertObject(homeAddress);
			student.setAddress(homeAddress);
		}
	}

	public SchedulePage schedulePage() {
		SchedulePage nextPage = (SchedulePage) pageWithName("SchedulePage");
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		return nextPage;
	}

	public Main mainPage() {
		Main nextPage = (Main) pageWithName("Main");
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		return nextPage;
	}

	public WOComponent saveChanges() {
		Session session = (Session) session();
		EOEditingContext context = session.defaultEditingContext();
		String msg;
		ErrorPage errorPage = (ErrorPage) pageWithName("ErrorPage");

		// Verify student's first name
		msg = session.isValidName(student.firstName());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		// Verify student's last name
		msg = session.isValidName(student.lastName());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		// Verify student's date of birth
		if (student.dateOfBirth() != null || error != null) {
			error = null;
			msg = session.isValidDate(student.dateOfBirth());
			if (msg != null) {
				errorPage.setErrorMsg(msg);
				errorPage.setNextPage(this);
				return (errorPage);
			}
		}

		// Verify student's street
		msg = session.isValidStreet(homeAddress.street());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		// Verify student's city
		msg = session.isValidCity(homeAddress.city());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		// Verify student's state
		msg = session.isValidState(homeAddress.state());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		// Verify student's ZIP code
		msg = session.isValidZip(homeAddress.zip());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}
		// Verify student's email address
		msg = session.isValidEmail(homeAddress.emailAddress());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}
		// Verify student's login
		msg = session.isValidLogin(student.login());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}
		// Verify student's password
		msg = session.isValidPassword(student.password());
		if (msg != null) {
			errorPage.setErrorMsg(msg);
			errorPage.setNextPage(this);
			return (errorPage);
		}

		student.setAddress(homeAddress);
		context.saveChanges();
		return null;
	}

	@Override
	public void validationFailedWithException(Throwable exception, Object value, String key) {
		error = new Exception(key);
	}

	public void setStudent(Student newStudent) {
		student = newStudent;
	}

	public Student student() {
		return student;
	}

	public void setHomeAddress(HomeAddress newHomeAddress) {
		homeAddress = newHomeAddress;
	}

	public HomeAddress homeAddress() {
		return homeAddress;
	}

}
