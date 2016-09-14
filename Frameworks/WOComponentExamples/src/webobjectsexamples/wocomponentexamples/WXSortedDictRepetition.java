/*
 WXSortedDictRepetition.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
/**
 This class demonstrates the creation of a dictionary repetition that parses
 it's content by sorting the list of keys in ascending order.  This component
 requires two bindings:

 dictionary		the dictionary to parse through
 key			an instance variable to set the keys to

 This component does not display anything - after sorting the keys, it sets each
 key into the parent (peforming setValueForBinding() with each value).  The
 parent component should then take the key value and obtain the appropriate value
 for the key.
 */
package webobjectsexamples.wocomponentexamples;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSLog;


public class WXSortedDictRepetition extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 6660124958893403028L;

	// Instance variable for the dictionary
    private NSDictionary _dictionary;

    // Instance variable for the array of keys
    private NSArray _keyList;


    /**
     * Constructor for the component takeing one argument, the current WOContext.
     */

    public WXSortedDictRepetition( WOContext aContext ) {
        super( aContext );
    }


    /**
     * Method to turn off variable synchronization between this component
     * and the parent to this component.
     */

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }


    /**
     * Accessor method for the dictionary:  if the local instance variable is
     * null, the value is pulled from the binding ("dictionary") for the component.
     */

    public NSDictionary dictionary() {
        if (_dictionary == null) {
            _dictionary = (NSDictionary)valueForBinding("dictionary");
        }
        return _dictionary;
    }


    /**
     * Method to return the list of keys for the dictionary.  If the local instance
     * variable is null, then the array of keys is taken from the dictionary.  The
     * keys of the dictionary are then (attempted to be) sorted in ascending order
     * and set into the local instance variable.
     */

    public NSArray keyList() {
        if (_keyList == null) {
            NSArray keys = dictionary().allKeys();
            try {
                _keyList = keys.sortedArrayUsingComparator(NSComparator.AscendingStringComparator);
            } catch (NSComparator.ComparisonException ex) {
                if ( NSLog.debugLoggingAllowedForLevelAndGroups( NSLog.DebugLevelInformational, NSLog.DebugGroupWebObjects)) {
                    NSLog.debug.appendln("Could not sort the keys for the \'" + name() + "\' component of the type " + getClass().getName() + ".  The keys are:\n" + keys);
                }
                _keyList = keys; // for some reason we cannot sort them.
            }
        }
        return _keyList;
    }


    /**
     * Method to return the current key in the list (by returning the value from the
     * binding for "key" ).
     */

    public String currentKey() {
        return (String)valueForBinding("key");
    }


    /**
     * Method to set the current key AND object from the list:  using the passed in
     * argument (aKey) the corresponding object is obtained from the dictionary.  If
     * the object is not null, both the object and key are set into the parent.
     */

    public void setCurrentKey(String aKey) {
        Object anObject = dictionary().objectForKey( aKey );
        if ( anObject != null ) {
            setValueForBinding( aKey, "key" );
            setValueForBinding( anObject, "item" );
        }
    }
}
