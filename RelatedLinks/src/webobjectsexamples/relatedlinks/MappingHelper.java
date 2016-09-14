/*
 * MappingHelper.java
 * [Related Links Project]
 *
 * © Copyright 2005-2007 Apple, Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (“Apple”) in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms,
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under Apple’s copyrights in this original Apple
 * software (the “Apple Software”), to use, reproduce, modify and
 * redistribute the Apple Software, with or without modifications, in
 * source and/or binary forms; provided that if you redistribute the
 * Apple Software in its entirety and without modifications, you must
 * retain this notice and the following text and disclaimers in all such
 * redistributions of the Apple Software.  Neither the name, trademarks,
 * service marks or logos of Apple Computer, Inc. may be used to endorse
 * or promote products derived from the Apple Software without specific
 * prior written permission from Apple.  Except as expressly stated in
 * this notice, no other rights or licenses, express or implied, are
 * granted by Apple herein, including but not limited to any patent
 * rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated.
 *
 * The Apple Software is provided by Apple on an "AS IS" basis.  APPLE
 * MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 * THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS
 * USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 *
 * IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT,
 * INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE,
 * REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE,
 * HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING
 * NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.relatedlinks;

import com.webobjects.appserver.xml.WOXMLDecoder;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSMutableArray;


/**
 This class uses a mapping method to instantiate objects from the XML
 that was obtained from the related links site.
 */

public class MappingHelper {

    // Instance variable for the XML decoder
    static WOXMLDecoder decoder = null;


    /**
     * Method to return related links as an array of NSDictionaries given a mapping
     * file URl and a website.
     *
     * @param mappingFileURL	url to the mapping file
     * @param website		url to the website to parse
     * @return			NSArray of dictionaries (each is a link)
     */

    public static NSArray relatedLinks(String mappingFileURL, String webSite) {

        NSArray links = null;
        String xmlData = ContentHelper.relatedLinksContent(webSite, true);
        // In this example, the mapping file doesn't change, so we only need to create the decoder once.
        if (decoder == null) {
            decoder = WOXMLDecoder.decoderWithMapping(mappingFileURL);

            // We are stuck in the non namespace world for now.
            try {
                decoder.xmlReader().setFeature("http://xml.org/sax/features/namespaces", false);
            }
            catch (Exception ex) {
                // We tried, nothing we can do here.
            }
        }

        Object obj= decoder.decodeRootObject(new NSData(xmlData.getBytes()));
        if (obj.getClass().equals(MappingHelper.RDF.class)) {
            links = ((MappingHelper.RDF)obj).links.relatedLinks;
        }
        return links;
    }


    /**
     * Mapping model helpers -- these must be public and static since
     * they are innner classes
     */

    public static class RelatedLinks {
        public NSMutableArray relatedLinks;
    }

    public static class RDF {
        public MappingHelper.RelatedLinks links;
    }
}
