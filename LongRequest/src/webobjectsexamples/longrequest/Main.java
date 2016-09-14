/*
 * Main.java
 * [LongRequest Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/**
 This class and the associated component are the main page for the application.
 */
package webobjectsexamples.longrequest;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;




public class Main extends WOComponent {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4151008111404304146L;
	// Instance variables for the start and stop values
    public Number stop;
    public Number start;

	// Instance variable for the refresh rate
	public Number refresh;

	// Instance variable for the e-mail address
    public String email;


	/**
	 * Constructor - sets the stop, start, and refresh values to defaults.
	 */

    public Main( WOContext aContext )  {
        super( aContext );
        stop = new Integer( 3000000 );
        start = new Integer( 0 );
		refresh = new Integer( 1 );
    }


	/**
	 * Method to jump to the simple refresh page  This page
	 * takes values for the start, stop, and refresh rate.
	 */

    public WOComponent toSimpleRefreshPage()  {
        WOComponent nextPage = pageWithName( "SimpleRefreshPage" );
        nextPage.takeValueForKey( start, "start" );
        nextPage.takeValueForKey( stop, "stop" );
		nextPage.takeValueForKey( refresh, "refresh" );
		return nextPage;
    }


	/**
	 * Method to jump to the refresh page with a status.   This page
	 * takes values for the start, stop, and refresh rate.
	 */

    public WOComponent toRefreshWithStatusPage()  {
        WOComponent nextPage = pageWithName( "RefreshWithStatusPage" );
        nextPage.takeValueForKey( start, "start" );
        nextPage.takeValueForKey( stop, "stop" );
		nextPage.takeValueForKey( refresh, "refresh" );
        return nextPage;
    }


	/**
	 * Method to jump to the refresh page with progress data.  This page
	 * takes values for the start, stop, and refresh rate.
	 */

    public WOComponent toRefreshWithDataPage()  {
        WOComponent nextPage = pageWithName( "RefreshWithDataPage" );
        nextPage.takeValueForKey(start, "start");
        nextPage.takeValueForKey(stop, "stop");
		nextPage.takeValueForKey( refresh, "refresh" );
        return nextPage;
    }


	/**
	 * Method to jump to the manual refresh page.  This page
	 * takes values for the start and stop.
	 */

    public WOComponent toManualRefreshPage()  {
        WOComponent nextPage = pageWithName( "ManualRefreshPage" );
        nextPage.takeValueForKey(start, "start");
        nextPage.takeValueForKey(stop, "stop");
        return nextPage;
    }


	/**
	 * Method to jump to the refresh page with cancel options.  This page
	 * takes values for the start, stop, and refresh rate.
	 */

    public WOComponent toRefreshWithCancelPage()  {
        WOComponent nextPage = pageWithName( "RefreshWithCancelPage" );
        nextPage.takeValueForKey(start, "start");
        nextPage.takeValueForKey(stop, "stop");
		nextPage.takeValueForKey( refresh, "refresh" );
        return nextPage;
    }


	/**
	 * Method to jump to the refresh page with email notification.  This
	 * only happens if an e-mail address has been entered.  This page
	 * takes values for the start and stop.
	 */


    public WOComponent toRefreshWithNotificationPage()  {
        WOComponent nextPage = null;
        if (email!=null && (email.length()!=0)) {
            nextPage = pageWithName( "RefreshWithNotificationPage" );
            nextPage.takeValueForKey(start, "start");
            nextPage.takeValueForKey(stop, "stop");
			nextPage.takeValueForKey(email, "mailTo");
        }
        return nextPage;
    }

}
