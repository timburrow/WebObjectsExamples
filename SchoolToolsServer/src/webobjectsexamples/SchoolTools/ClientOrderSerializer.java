/*
 * File: ClientOrderSerializer.java
 *
 * Description: Class used to serialize and deserialize ClientOrder objects.
 *      This class is duplicated in SchoolToolsClient.
 *
 * Author: Apple Computer
 *
 * Copyright: � Copyright 2005-2007 Apple, Inc.. All rights reserved.
 *
 * Disclaimer:
 *      IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc.
 *      ("Apple") in consideration of your agreement to the following terms, and your
 *      use, installation, modification or redistribution of this Apple software
 *      constitutes acceptance of these terms.  If you do not agree with these terms,
 *      please do not use, install, modify or redistribute this Apple software.
 *
 *      In consideration of your agreement to abide by the following terms, and subject
 *      to these terms, Apple grants you a personal, non-exclusive license, under AppleUs
 *      copyrights in this original Apple software (the "Apple Software"), to use,
 *      reproduce, modify and redistribute the Apple Software, with or without
 *      modifications, in source and/or binary forms; provided that if you redistribute
 *      the Apple Software in its entirety and without modifications, you must retain
 *      this notice and the following text and disclaimers in all such redistributions of
 *      the Apple Software.  Neither the name, trademarks, service marks or logos of
 *      Apple Computer, Inc. may be used to endorse or promote products derived from the
 *      Apple Software without specific prior written permission from Apple.  Except as
 *      expressly stated in this notice, no other rights or licenses, express or implied,
 *      are granted by Apple herein, including but not limited to any patent rights that
 *      may be infringed by your derivative works or by other works in which the Apple
 *      Software may be incorporated.
 *
 *      The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO
 *      WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED
 *      WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 *      PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN
 *      COMBINATION WITH YOUR PRODUCTS.
 *
 *      IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR
 *      CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
 *      GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 *      ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION
 *      OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT
 *      (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 *      ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Change History (most recent first):
 *
 */

package webobjectsexamples.SchoolTools;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis.Constants;
import org.apache.axis.encoding.SerializationContext;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.wsdl.fromJava.Types;
import org.xml.sax.Attributes;

public class ClientOrderSerializer implements Serializer {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -546995170437685847L;
	public static final String QUANTITY = "quantity";
    public static final String PRODUCT = "product";
    public static final QName myTypeQName = new QName("wo-xml-demo", "Order");

    public void serialize(QName name, Attributes attributes,
                          Object value, SerializationContext context)
        throws IOException
    {
        if (!(value instanceof ClientOrder)) {
            throw new IOException("Can't serialize a " + value.getClass().getName() + " with a ClientOrderSerializer.");
	}

        ClientOrder clientOrder = (ClientOrder)value;

        context.startElement(name, attributes);
        context.serialize(new QName("", QUANTITY), null, new Integer(clientOrder.quantity()));
        context.serialize(new QName("", PRODUCT), null, clientOrder.product());
        context.endElement();
    }

    public String getMechanismType() { return Constants.AXIS_SAX; }

    public boolean writeSchema(Types types) throws Exception {
        return false;
    }

	public org.w3c.dom.Element writeSchema (Class javaType, Types types) throws Exception {
			return null;
	}
}

