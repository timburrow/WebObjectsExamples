/*
 * TreeWalker.java
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
 Class to walk the tree of a website, including a number of convenience methods
 for starting with a node or a document.
 */
package webobjectsexamples.relatedlinks;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.webobjects.foundation.NSLog;

public class TreeWalker extends Object {

    /**
     * Convenience method to walk the tree specifying a document and
     * a mapping file to process the node with.
     *
     * @param doc	Document for the elements
     * @param mapping	Mapping to process the nodes with
     */

    public static void walkTree( Document doc, Mapping mapping ) {
        walkTree( doc.getDocumentElement(), 0, mapping );
    }

    /**
     * Convenience class for printing each node
     */

    public static class PrintNodes implements Mapping {

        /*
         * Method to handle each node, taking the node and level as arguments.
         *
         * @param node		the node to handle
         * @param level		the level of the node
         */

        public void handleNode( Node node, int level )  {

            String info = "";
            short type = node.getNodeType();
            boolean isText = ( type == Node.TEXT_NODE || type == Node.COMMENT_NODE );
            boolean isEmptyText = ( isText && node.getNodeValue().trim().length() == 0 );

            if ( !isText || !isEmptyText ) {
                if ( type != Node.TEXT_NODE ) {
                    for (int i = 0; i < level; i++)
                        info = info + "\t";
                    info = "\n" + info + node.getNodeName() + ": " ;
                    NamedNodeMap attributes = node.getAttributes();
                    if (attributes != null) {
                        int length = attributes.getLength();
                        if (length > 0) {
                            info = info + "(";
                            for (int i = 0; i < length; i++) {
                                String name = attributes.item(i).getNodeName();
                                String value = "";
                                Attr attr = ((Element)node).getAttributeNode(name);
                                if (attr != null)
                                    value = attr.getValue();
                                info = info + name + ":"+ value;
                                if (i < length - 1)
                                    info = info + " ";
                            }
                            info = info + ") ";
                       }
                    }
                }

                if ( !isEmptyText && node.getNodeValue() != null )
                        info = info + " " + node.getNodeValue();
                printInfo( info );
            }
        }

        /**
         * Method to print information to the console
         * @param info		the information to print
         */

        public void printInfo( String info ) {
            NSLog.out.appendln( info );
        }
    }


    /** Node mapping interface definition */

    public interface Mapping {
        public void handleNode(Node node, int level);
    }


    /**
     * Method to recursively walk the tree beginning at a the specified node.
     * For each note, the handleNode() method is called from the instance of
     * the passed-in Mapping argument.
     *
     * @param node	Node to start walking from
     * @param level	level of the node
     * @param mapping	Mapping to use to handle the node
     */

    public static void walkTree( Node node, int level, Mapping mapping ) {

        // use the mapping to handle the node
        mapping.handleNode(node, level);

        // get the children for the nodes and walk the tree from there
        Node child = node.getFirstChild();
        while ( child != null ) {
            walkTree( child, level+1, mapping );
            child = child.getNextSibling();
        }
    }
}
