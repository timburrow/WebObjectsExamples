/*
ListingXMLDelegate.java
[XMLInteroperate project]

© Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/**
* This delegate class controls how a Listing object is serialized. There are 2 interesting things
 * to note:
 *
 * 1. We maintain a static table of the delegate objects that were written out so that we don't
 *   write out another delegate for the same source object. This is important during
 *   serialization of cyclic graph references. If you return the same delegate for the same
 *   source object, NSXMLOutputStream takes care of the references (and would not go into
 *   infinite loop).
 *
 * 2. The only relation that we are interested in writing out is the agent responsible for this
 *   listing. We are only writing out "askingPrice" again (as a string) because the original
 *   attribute is written out in a generic manner that is hard to transform. See the console
 *   for an output of the original XML string.
 */
package webobjectsexamples.xmlinteroperate;

import java.io.IOException;
import java.io.ObjectOutput;
import java.math.BigDecimal;

import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSMutableDictionary;


public class ListingXMLDelegate extends XMLDelegate {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4728646116646846356L;
	private static NSMutableDictionary writtenTable = new NSMutableDictionary();
    static void reset() {
        writtenTable.removeAllObjects();
    }
    static Object objectForKey(Object key) {
        Object obj = writtenTable.objectForKey(key);
        if (obj == null) {
            obj = new ListingXMLDelegate((EOEnterpriseObject)key);
            writtenTable.setObjectForKey(obj, key);
        }
        return obj;
    }

    private ListingXMLDelegate(EOEnterpriseObject eo) {
        super(eo);
    }

    @Override
	public void writeExternal(ObjectOutput out) throws IOException {
        WOXMLCoderOutputStream os = (WOXMLCoderOutputStream)out;
        super.writeExternal(os);

        BigDecimal price = ((BigDecimal)_eo.valueForKey("askingPrice")).setScale(2);
        os.writeObject(price.toString(), "askingPrice");
        os.writeObject(AgentXMLDelegate.objectForKey(_eo.valueForKey("agent")), "agent");
    }
}
