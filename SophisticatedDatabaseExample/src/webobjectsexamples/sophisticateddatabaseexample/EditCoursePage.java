/*******************************************************************************
 *
 * [SophisticatedDatabaseExample Project]
 *
 * � 2005, 2007 Apple Computer, Inc.. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.
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
import com.webobjects.foundation.NSMutableArray;

public class EditCoursePage extends WOComponent {
	/**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= -2564078607052210084L;

	protected Course			course;

	protected Course			selectedCourse;

	public EditCoursePage(WOContext context) {
		super(context);
		// Add new Course to editing context
		course = new Course();
		session().defaultEditingContext().insertObject(course);
	}

	/** @TypeInfo Course */
	public NSMutableArray getCourses() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "Course").mutableClone();
	}

	/**
	 * Save changes to database
	 */
	public WOComponent saveChanges() {
		String check1 = course.name();
		String check2 = course.shortDescription();
		if (check1 != null && check2 != null) {
			EOEditingContext context = session().defaultEditingContext();
			// save changes permanently in database
			context.saveChanges();
		}
		// Add new Course to editing context
		course = new Course();
		session().defaultEditingContext().insertObject(course);
		return null;
	}

	/**
	 * Go back to main page
	 */
	public Main mainPage() {
		Main nextPage = (Main) pageWithName("Main");
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		return nextPage;
	}

	/**
	 * Go back to administration page
	 */
	public AdministrationPage administrationPage() {
		AdministrationPage nextPage = (AdministrationPage) pageWithName("AdministrationPage");
		EOEditingContext context = session().defaultEditingContext();
		// throw away any unsaved changes
		context.revert();
		return nextPage;
	}

	public WOComponent deleteCourse() {
		EOEditingContext context = session().defaultEditingContext();
		// delete selected object
		context.deleteObject(course);
		saveChanges();
		return null;
	}

	public WOComponent selectCourse() {
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		course = selectedCourse;
		return null;
	}

	public void setCourse(Course newCourse) {
		course = newCourse;
	}

	public Course course() {
		return course;
	}

	public void setSelectedCourse(Course newCourse) {
		selectedCourse = newCourse;
	}

	public Course selectedCourse() {
		return selectedCourse;
	}

}
