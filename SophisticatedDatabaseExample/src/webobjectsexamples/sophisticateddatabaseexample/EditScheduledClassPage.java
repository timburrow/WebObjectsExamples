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

import java.text.SimpleDateFormat;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.eoaccess.EOUtilities;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSTimestamp;

public class EditScheduledClassPage extends WOComponent {

	/**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= -8581232258020795532L;

	private static String[]		dayList				= { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday" };

	private static String[]		timeList			= { "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM", "2:00 PM", "3:00 PM", "4:00 PM" };

	protected ScheduledClass	scheduledClass;

	protected Schedule			schedule;

	protected ScheduledClass	selectedScheduledClass;

	protected Course			selectedCourse;

	protected Classroom			selectedClassroom;

	protected String			selectedDay;

	protected String			selectedTime;

	protected Teacher			selectedTeacher;

	public EditScheduledClassPage(WOContext context) {
		super(context);
		// Add new ScheduledClass to editing context
		scheduledClass = new ScheduledClass();
		schedule = new Schedule();
		scheduledClass.setSchedule(schedule);
		session().defaultEditingContext().insertObject(schedule);
		session().defaultEditingContext().insertObject(scheduledClass);
	}

	/** @TypeInfo ScheduledClass */
	public NSMutableArray getScheduledClasses() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "ScheduledClass").mutableClone();
	}

	/**
	 * Save changes to database
	 */
	public WOComponent saveChanges() {
		Classroom check1 = scheduledClass.classroom();
		Course check2 = scheduledClass.course();
		if (check1 != null && check2 != null) {
			EOEditingContext context = session().defaultEditingContext();
			schedule = scheduledClass.schedule();
			schedule.setDay(selectedDay);
			try {
				SimpleDateFormat format = new SimpleDateFormat();
				String fullDate = "1/1/01 " + selectedTime + ", PDT";
				schedule.setTime(new NSTimestamp(format.parse(fullDate)));
			} catch (java.text.ParseException pe) {
				return (null);
			}
			context.saveChanges();
		}
		// Add new ScheduledClass to editing context
		scheduledClass = new ScheduledClass();
		Schedule aSchedule = new Schedule();

		aSchedule.setDay(selectedDay);
		scheduledClass.setSchedule(aSchedule);
		session().defaultEditingContext().insertObject(aSchedule);
		session().defaultEditingContext().insertObject(scheduledClass);
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

	public WOComponent deleteScheduledClass() {
		EOEditingContext context = session().defaultEditingContext();
		// delete selected object
		context.deleteObject(scheduledClass);
		saveChanges();
		return null;
	}

	public WOComponent selectScheduledClass() {
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		scheduledClass = selectedScheduledClass;
		schedule = scheduledClass.schedule();
		return null;
	}

	/** @TypeInfo Course */
	public NSArray getAvailableCourses() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "Course");
	}

	/** @TypeInfo Classroom */
	public NSArray getAvailableClassrooms() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "Classroom");
	}

	/** @TypeInfo java.lang.String */
	public NSArray<String> getAvailableDays() {
		return new NSArray<String>(dayList);
	}

	/** @TypeInfo java.lang.String */
	public NSArray<String> getAvailiableTimes() {
		return new NSArray<String>(timeList);
	}

	/** @TypeInfo Teacher */
	public NSArray getAvailableTeachers() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "Teacher");
	}

	public void setSelectedCourse(Course newCourse) {
		selectedCourse = newCourse;
	}

	public Course selectedCourse() {
		return selectedCourse;
	}

	public void setScheduledClass(ScheduledClass newScheduledClass) {
		scheduledClass = newScheduledClass;
	}

	public ScheduledClass scheduledClass() {
		return scheduledClass;
	}

	public void setSelectedScheduledClass(ScheduledClass newSelectedScheduledClass) {
		selectedScheduledClass = newSelectedScheduledClass;
	}

	public ScheduledClass selectedScheduledClass() {
		return selectedScheduledClass;
	}

	public void setSchedule(Schedule newSchedule) {
		schedule = newSchedule;
	}

	public Schedule schedule() {
		return schedule;
	}

	public void setSelectedClassroom(Classroom newSelectedClassroom) {
		selectedClassroom = newSelectedClassroom;
	}

	public Classroom selectedClassroom() {
		return selectedClassroom;
	}

	public void setSelectedDay(String newSelectedDay) {
		selectedDay = newSelectedDay;
	}

	public String selectedDay() {
		return selectedDay;
	}

	public void setSelectedTime(String newSelectedTime) {
		selectedTime = newSelectedTime;
	}

	public String selectedTime() {
		return selectedTime;
	}

	public void setSelectedTeacher(Teacher newSelectedTeacher) {
		selectedTeacher = newSelectedTeacher;
	}

	public Teacher selectedTeacher() {
		return selectedTeacher;
	}
}
