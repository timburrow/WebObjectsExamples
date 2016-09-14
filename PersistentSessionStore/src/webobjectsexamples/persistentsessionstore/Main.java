/*
 * Main.java
 * [PersistentSessionStore Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.persistentsessionstore;

import com.webobjects.foundation.*;
import com.webobjects.appserver.*;


public class Main extends WOComponent {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	// Instance variable for the current index
    public int currentIndex;

	// Instance variable for the correct guess condition
    public boolean isGuessCorrect = false;

    // Instance variable for the list of numbers and the
    // item used to iterate.
    public NSArray list;
    public Object listItem;


	/**
	 * Constructor - used to set the current index to 0
	 */

    public Main( WOContext aContext )  {
        super( aContext );
        currentIndex = 0;
		list = new NSArray( new Object[] { "1","2","3","4","5","6","7","8","9","10" } );
    }


	/**
	 * Method to return the current number value.  Note that this method is using
	 * the current index and adding one, since indexing starts at 0.
	 */

    public Number currentNumber() {
        return new Integer(currentIndex + 1);
    }


	/**
	 * Method performed when a number is selected.  Each time a number is selected
	 * the value is added to the list of guesses (which is stored in the session)
	 * and compared to the "correct" number  (which is always "8").  If the guess is
	 * correct, then the boolean for the correct guess is set to true;  otherwise it
	 * is left as false.
	 */

    public WOComponent numberClicked()  {

        WOComponent aNewPage = (WOComponent)pageWithName( "Main" );
        NSMutableArray aGuessesArray = ( (Session)session() ).guesses;
        Number aGuess = currentNumber();

		aGuessesArray.addObject( aGuess );
        if ( aGuess.intValue() == 8 ) {
            aNewPage.takeValueForKey( new Boolean(true), "isGuessCorrect" );
        }
        return aNewPage;
    }

}
