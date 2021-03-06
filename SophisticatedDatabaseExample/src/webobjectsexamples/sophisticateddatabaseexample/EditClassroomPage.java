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

public class EditClassroomPage extends WOComponent {
	/**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= -3673339119263176919L;

	protected Classroom			classroom;

	protected Classroom			selectedClassroom;

	public EditClassroomPage(WOContext context) {
		super(context);
		// Add new Classroom to editing context
		classroom = new Classroom();
		session().defaultEditingContext().insertObject(classroom);
	}

	/** @TypeInfo Classroom */
	public NSMutableArray getClassrooms() {
		return EOUtilities.objectsForEntityNamed(this.session().defaultEditingContext(), "Classroom").mutableClone();
	}

	/**
	 * Save changes to database
	 */
	public WOComponent saveChanges() {
		String check1 = classroom.room();
		String check2 = classroom.building();
		if (check1 != null && check2 != null) {
			EOEditingContext context = session().defaultEditingContext();
			// save changes permanently in database
			context.saveChanges();
		}
		// Add new Classroom to editing context
		classroom = new Classroom();
		session().defaultEditingContext().insertObject(classroom);
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

	public WOComponent deleteClassroom() {
		EOEditingContext context = session().defaultEditingContext();
		// delete selected object
		context.deleteObject(classroom);
		saveChanges();
		return null;
	}

	public WOComponent selectClassroom() {
		EOEditingContext context = session().defaultEditingContext();
		context.revert();
		classroom = selectedClassroom;
		return null;
	}

	public AdministrationPage administrationPage() {
		AdministrationPage nextPage = (AdministrationPage) pageWithName("AdministrationPage");

		// Initialize your component here

		return nextPage;
	}

	public void setClassroom(Classroom newClassroom) {
		classroom = newClassroom;
	}

	public Classroom classroom() {
		return classroom;
	}

	public void setSelectedClassroom(Classroom newSelectedClassroom) {
		selectedClassroom = newSelectedClassroom;
	}

	public Classroom selectedClassroom() {
		return selectedClassroom;
	}
}
