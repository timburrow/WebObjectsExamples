/*
 * Main.java [Related Links Project] © Copyright 2005-2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal,
 * non- exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its
 * entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple
 * Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works
 * in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE
 * APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */

package webobjectsexamples.relatedlinks;

import java.net.URL;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;

public class Main extends WOComponent {

	/**
	 * serialVersionUID
	 */
	private static final long		serialVersionUID	= 9106492352087117868L;

	// Instance variable for website (with default value)
	public String					webSite				= "www.apple.com";

	// Instance variables for related link viewing
	public String					relatedLinksInfo;

	public NSArray					relatedLinks;

	public Object					currLink;

	public RelatedLink				currTopicLink;

	// Instance variables for mapping URL and mapping file
	public static String			mappingURL;

	public final static String		mappingFile			= "relatedMapping.xml";

	public final NonMappingHelper	nonMappingHelper;

	/**
	 * Constructor
	 */

	public Main(WOContext aContext) {
		super(aContext);
		nonMappingHelper = new NonMappingHelper();
	}

	/**
	 * Method to walk the related links xml tree and display the results as hyperlinks Returns to the same page (null).
	 */

	public WOComponent relatedLinksAsHyperlinks() {
		reset();
		relatedLinks = nonMappingHelper.relatedLinks(webSite);
		return null;
	}

	/**
	 * Method to walk the related links xml tree and display the results as formatted text Returns to the same page (null).
	 */

	public WOComponent relatedLinksAsFormattedText() {
		reset();
		relatedLinksInfo = nonMappingHelper.relatedLinksInfo(webSite);
		return null;
	}

	/**
	 * Method to use the mapping model to decode the related link XML information and display the results as hyperlinks. Returns to the same page (null).
	 */

	public WOComponent linksViaMapping() {
		reset();
		if (mappingURL == null) {
			URL anURL = application().resourceManager().pathURLForResourceNamed(mappingFile, null, null);
			if (anURL != null)
				mappingURL = anURL.toString();
		}
		try {
			relatedLinks = MappingHelper.relatedLinks(mappingURL, webSite);
		} catch (Throwable e) {
			relatedLinksInfo = "The Related Link data for this website doesn't contain proper XML";
		}
		return null;
	}

	/**
	 * Method to display the results (related links) as raw XML Returns to the same page (null).
	 */

	public WOComponent relatedLinksAsRawXML() {
		reset();
		relatedLinksInfo = ContentHelper.relatedLinksContent(webSite, false);
		return null;
	}

	/**
	 * Method to reset the page display instance variables (called before each display method).
	 */

	@Override
	public void reset() {
		relatedLinksInfo = null;
		relatedLinks = null;
		nonMappingHelper.reset();
	}
}
