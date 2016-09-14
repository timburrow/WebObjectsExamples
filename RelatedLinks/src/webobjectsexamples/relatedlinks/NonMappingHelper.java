/*
 * NonMappingHelper.java
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
/**
 * Class that uses the XML tree walking method to instantiate objects from the
 * XML returned for a related link site
 */

package webobjectsexamples.relatedlinks;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;



public class NonMappingHelper extends Object {

    // Instance variable for link information
    String relLinksInfo;

    // Instance variable for the array of related links
    NSMutableArray relLinks;


    /**
     * Static method to return the information for the related links
     */

    public String relLinksInfo() {
        if (relLinksInfo == null)
            relLinksInfo = new String();
        return relLinksInfo;
    }


    /**
     * Static method to return the information array of related links
     */

    public NSMutableArray relLinks() {
        if (relLinks == null)
            relLinks = new NSMutableArray();
        return relLinks;
    }


    /**
     *  method to, given a website, return formatted related links info
     *
     * @param website		the website to process
     * @return			String of formatted link information
     */

    public String relatedLinksInfo( String webSite ) {
        walkRelatedLinks( webSite, new PrintNodes() );
        return relLinksInfo();
    }


    /**
     * method to, given a website, return all of the related links
     * an an NSArray of NSDictionaries (one for each link)
     *
     * @param website		the website to process
     * @return			NSArray of link information (in dictionary format)
     */

    public NSArray relatedLinks( String webSite ) {
        walkRelatedLinks( webSite, new HyperlinkMapper() );
        return relLinks();
    }


    /**
     * method to walk the related links XML tree, calling the passed in
     * mapper handleNode() method on each node.
     *
     * @param website		the website to process
     * @param mapper		mapper to use to handle the nodes
     */

    public void walkRelatedLinks( String webSite, TreeWalker.Mapping mapper ) {

        String xmlData = ContentHelper.relatedLinksContent(webSite, true);
        if (xmlData == null) {
            return;
        }
        // convert to content back into a DOM document.  if we didn't have to
        // correct the XML, would be preferable to obtain directly by calling
        // contentAsDOMDocument on the initial response.
        WOResponse response = new WOResponse();
        response.setContent(xmlData);
        Document xmlDoc = response.contentAsDOMDocument();
        // walk the xml tree with our custom mapping class
        TreeWalker.walkTree(xmlDoc, mapper);
    }


    /**
     * Printing sublcass which saves rather than prints the node information
     */

    public class PrintNodes extends TreeWalker.PrintNodes {
        @Override
		public void printInfo( String info ) {
            relLinksInfo = relLinksInfo() + info;
        }
    }


    /**
     * Mapping class for obtaining href information from the nodes
     */

    public class HyperlinkMapper implements TreeWalker.Mapping {

        public void handleNode( Node node, int level )  {

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String name = ((Element)node).getAttribute("name");
                if (!name.equals(""))  {
                    if (node.getNodeName().equals("Topic")) {
                        relLinks().addObject(new Topic(name));
                    }
                    else {
                        String href = ((Element)node).getAttribute("href");
                        if (!href.equals("")) {
                            RelatedLink newLink = new RelatedLink(name, href);
                            if (node.getParentNode().getNodeName().equals("Topic")) {
                                ((Topic)relLinks().lastObject()).addRelatedLink(newLink);
                            }
                            else
                                relLinks().addObject(newLink);
                        }
                    }
                }
            }
        }
    }


    /**
     * method to reset the information known for the related links information
     * and the array of related links.  Simple sets the local instance variables to null.
     */

    public void reset() {
        relLinksInfo = null;
        relLinks = null;
    }
}
