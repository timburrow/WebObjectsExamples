/*
XMLDelegate.java
[XMLInteroperate project]

© Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

/**
 * If you have source code access to a custom EO class then you don't need to create an
 * delegate class to do the serialization. You would customize the writeObject() and
 * readObject() methods in the custom EO class itself.
 * <p>
 * In this example project, we pretend that we don't have source code access. The other case
 * is when you just have an EOGenericRecord or EOCustomObject, then you would need an delegate
 * class.
 * <p>
 * We implements java.io.Externalizable to demonstrate a different approach.
 *
 */
package webobjectsexamples.xmlinteroperate;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

abstract public class XMLDelegate implements Externalizable {
    protected EOEnterpriseObject _eo;

    public XMLDelegate(EOEnterpriseObject eo) {
        _eo = eo;
    }

    /**
     * Writes out the non-relationship based attributes. Relationships are more complex and
     * decisions on whether they should be written out are made in the subclasses.
     */
    public void writeExternal(ObjectOutput out) throws IOException {
        WOXMLCoderOutputStream os = (WOXMLCoderOutputStream)out;

        EOClassDescription classDescription = _eo.classDescription();
        NSArray attributeKeys = classDescription.attributeKeys();
        int count = attributeKeys.count();
        for(int i=0; i<count; i++) {
            String attributeKey = (String)attributeKeys.objectAtIndex(i);
            Object attributeValue = _eo.valueForKey(attributeKey);
            os.writeObject(attributeValue, attributeKey);
        }
    }

    /**
     * In this example we are not doing deserialization.
     */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
    	// Not used
    }

}
