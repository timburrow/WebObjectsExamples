/*
 PCDictionaryRepetitionPage.java
 [WOExamples Project]

© Copyright 2005-2007 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.woexamples;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;


/*
 This class and the associated component demonstrates the WODictionaryRepetition
 dynamic element (which can be found in the WOExtensions framework.
 */

public class PCDictionaryRepetitionPage extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6345355941304215025L;

	// Instance variable for the current key
    public String currentKey;

    // Instance variable for the current item
    public String currentItem;

    // Instance variable fot the dictionary
    public NSDictionary dictionary;


    /**
     * Constructor for the component - used to instantiate the component
     * and populate the dictionary with basic information for the
     * demonstration of the component.
     */

    public PCDictionaryRepetitionPage( WOContext aContext )  {

        // Init
        super(aContext);

        // Create a temp dictionary of contents
        NSMutableDictionary aDictionary = new NSMutableDictionary();
        aDictionary.setObjectForKey("Alpha", "alpha");
        aDictionary.setObjectForKey("Beta", "beta");
        aDictionary.setObjectForKey("Delta", "delta");
        aDictionary.setObjectForKey("Gamma", "gamma");

        // Set the local dictionary with the values
        dictionary = (NSDictionary)aDictionary;
    }
}

