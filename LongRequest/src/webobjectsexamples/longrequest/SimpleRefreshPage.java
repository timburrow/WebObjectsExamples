/*
 * SimpleRefreshPage.java
 * [LongRequest Project]

� Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
 /**
 This class and the associated component creates a simple long request page.
 This page takes the information from the main page and performs the calculation
 of the prime numbers.  Note that for a long request a developer must inherit
 from the WOLongResponse page and, in this case, implement methods for the
 long task, the refresh rate, and the task to perform when the long request is
 complete.
 */
package webobjectsexamples.longrequest;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResponse;
import com.webobjects.woextensions.WOLongResponsePage;

public class SimpleRefreshPage extends WOLongResponsePage {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1078557534740097106L;
	// Instance variables for start and stop
    public Number stop;
    public Number start;

	// Instance variable for the refresh rate
	public Number refresh;

	// Instance variables for the number of primes and the progress
    public int count;
    public Number value;


	/**
	 * Constructor - initializes the instance variables to default
	 * values, which will be overridden by the passed in arguments.  This
	 * is done to ensure that if any are not set by the arguments the example
	 * can continue.
	 */

    public SimpleRefreshPage( WOContext context ) {
        super( context );
        count = 0;
        start = new Integer( 0 );
        stop = new Integer( 5000000 );
        value = new Integer( 10 );
    }


	/**
	 * Override of appendToResponse - this method increments the count (the total
	 * number of refreshes) and sets the refresh interval to the passed in
	 * value.
	 */

    @Override
	public void appendToResponse( WOResponse aResponse, WOContext aContext )  {
        count++;
        setRefreshInterval( refresh.intValue() );
        super.appendToResponse( aResponse, aContext );
    }


	/**
	 * Override of performAction method - this is where the main computation is done
	 * for the example.  By placing the computation in invokeAction is is automatically
	 * performed when the component loads.  Here we use the current values of the
	 * start and stop values to calculate all of the primes.
	 */

    @Override
	public Object performAction()  {

        WOPrimeNumberComputer pnc = new WOPrimeNumberComputer();

        /* This class does a setStatus: on the refresh page as it is computing
          * another way of setting the status would be to implement
          * -returnStatusPage, and in there, to have the main thread pole this object
          * (first keep this object as an ivar) for the status.
          * but then pnc needs a thread safe -status method.
          */

        return pnc.calculatePrimes( start, stop, "status", this );
    }


	/**
	 * Override of the method to return the page that is displayed while the
	 * long-running computation is running. This page displays the current
	 * status of the computation, which we are working with here.
	 */

    @Override
	public WOComponent refreshPageForStatus( Object status ) {
        value = new Integer( value.intValue()+(25/count) );
        return this;
    }


	/**
	 * Method to return the result page when the computation is complete.  This
	 * methods sets the result page, passes all of the computation information,
	 * and then returns the page.
	 */

    @Override
	public WOComponent pageForResult( Object result )  {

        WOComponent resultPage = pageWithName( "ResultPage" );
        resultPage.takeValueForKey( start, "start" );
        resultPage.takeValueForKey( stop, "stop" );
        resultPage.takeValueForKey( result, "result" );
        return resultPage;
    }

}
