/*
 * Interoperator.java [XMLInteroperate project] © Copyright 2005-2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software
 * in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the
 * Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE,
 * REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

/**
 * This is the middleman class that interface between this project and an exchange that returns the latest price for a property. See the readme file for a more detail description for how this project is supposed to work. Briefly, this project is an application for users to get the latest price for a
 * property but the latest price is kept by an exchange. This class sends the exchange data about the property and the exchange returns the latest price.
 */
package webobjectsexamples.xmlinteroperate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;

import org.xml.sax.InputSource;

import webobjectsexamples.realestate.common.Listing;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.xml.WOXMLDecoder;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.xml.NSXMLOutputFormat;
import com.webobjects.foundation.xml.NSXMLOutputStream;

public class Interoperator {

	/**
	 * Because our aim is not to write a client/server system at the backend, we choose to simplify the irrelevant part of the example by using a fake exchange. The exchange only understands XML data that can be decoded using WOXMLDecoder and the head of the exchange decided not to upgrade (for demo
	 * purpose).
	 */
	static class FakeExchange {

		BigDecimal evaluateListing(String XMLRepresentation) {

			URL mapURL = WOApplication.application().resourceManager().pathURLForResourceNamed("Mapping.xml", null, null);
			WOXMLDecoder decoder = WOXMLDecoder.decoderWithMapping(mapURL.getPath());
			InputSource is = new InputSource(new StringReader(XMLRepresentation));

			// Recreating the listing object from the XML data.
			Listing newP = (Listing) decoder.decodeRootObject(is);

			// Artificially create a new price.
			java.util.Random gen = new java.util.Random(System.currentTimeMillis());
			// Price increases by a greater amount :-)
			BigDecimal addition = (gen.nextDouble() < 0.5) ? new BigDecimal(10000.00) : new BigDecimal(-5000.00);

			return newP.askingPrice().add(addition);
		}
	}

	private FakeExchange	_exchange;

	public Interoperator() {
		_exchange = new FakeExchange();
	}

	public String getLatestPrice(Listing listing) {

		// This is where we transform the XML data to a format that the exchange can
		// work with. A large part of the magic is in the stylesheet that is supplied to
		// NSXMLOutputStream.
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		NSXMLOutputStream out;
		try {
			InputStream trxStylesheet = WOApplication.application().resourceManager().inputStreamForResourceNamed("TransformKey.xsl", null, null);
			out = new WOXMLCoderOutputStream(baos, new InputSource(trxStylesheet));
			// nice tabbed output
			out.transformer().setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			Object obj = ListingXMLDelegate.objectForKey(listing);
			out.writeObject(obj, "Listing");
			out.close();
			trxStylesheet.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String xml = baos.toString();

		BigDecimal newPrice = _exchange.evaluateListing(xml);
		listing.setAskingPrice(newPrice);

		// We do this just to show the original XML output without transformation.
		try {
			baos.reset();
			out = new WOXMLCoderOutputStream(baos);
			out.setOutputFormat(new NSXMLOutputFormat()); // nice tabbed output
			Object obj = ListingXMLDelegate.objectForKey(listing);
			out.writeObject(obj, "Listing");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		NSLog.out.appendln("Original XML output: \n" + baos.toString());

		return xml;
	}
}
