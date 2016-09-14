/*
 Session.java
 [VirtualStoreLiteX Project]

© Copyright 2005-2007 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


package webobjectsexamples.virtualstorelite;

import java.util.Enumeration;

import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;


public class Session extends WOSession {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8250612020295781024L;
	public NSMutableArray shoppingCart;
    public NSDictionary selectedProduct;

    public Session() {
        // Create an empty shopping cart for the user
        shoppingCart = new NSMutableArray();
    }

    public int quantityForCurrentProduct() {
        // Return the quantity if the selected product is already in the shopping cart
        Enumeration e;
        NSDictionary detailOrder;
        e = shoppingCart.objectEnumerator();
        while (e.hasMoreElements()) {
            detailOrder = (NSDictionary) e.nextElement();
            if ((NSDictionary) detailOrder.objectForKey("Product") == selectedProduct)
                return ((Integer) detailOrder.objectForKey("Quantity")).intValue();
        }
        // The selected product is not in the shopping cart so we return a quantity of one
        return 1;
    }

    public void addCurrentProductToShoppingCart( int quantity) {
        Enumeration e;
        NSMutableDictionary detailOrder = null;
        // Before setting the quantity we need to check if the selected product
        // is not already in the shopping cart
        e = shoppingCart.objectEnumerator();
        while (e.hasMoreElements()) {
            detailOrder = (NSMutableDictionary) e.nextElement();
            if ((NSDictionary)detailOrder.objectForKey("Product") == selectedProduct) {
                // This product is already in the shopping cart
                if (quantity == 0) {
                    // Zero quantity mean remove the product from the shopping cart
                    shoppingCart.removeObject( detailOrder);
                }
                else {
                    // We set here the new quantity
                    detailOrder.setObjectForKey(new Integer( quantity), "Quantity");
                }
                break;
            }
			detailOrder = null;
        }
        if (detailOrder == null) {
            // The product was not in the shopping cart. We should add it
            detailOrder = new NSMutableDictionary();
            detailOrder.setObjectForKey(selectedProduct,"Product");
            detailOrder.setObjectForKey(new Integer( quantity), "Quantity");
            shoppingCart.addObject( detailOrder);
        }
    }

}
