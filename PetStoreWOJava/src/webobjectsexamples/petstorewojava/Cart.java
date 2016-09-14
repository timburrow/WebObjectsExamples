/*
© Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (ÒAppleÓ) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under AppleÕs copyrights in this original Apple software (the ÒApple SoftwareÓ), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//
// Cart.java
// Project PetStoreWOJava
//
//This is a Shopping Cart. Its an NSArray of NSDictionary's
//Each dictionary holds a "cartItem", consisting of: item, qty, and precalculated total
//The cart also holds the running total for all the cart items
package webobjectsexamples.petstorewojava;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjectsexamples.petstore.Item;

public class Cart {

    NSMutableArray cartItems;
            double cartTotal;

    public Cart(){
        initCart();
    }

    void initCart(){
        cartItems = new NSMutableArray();
        cartTotal = 0.0;
    }

    public void clearCart(){ initCart(); }

    public NSMutableArray cartItems() { return cartItems; }

    public double cartTotal() { return cartTotal; }

    public boolean  hasCartItems() {
        return cartItems.count()>0?true:false;
    }

    public void removeCartItem(NSDictionary cartItem){
        cartItems.removeObject(cartItem);
    }

    public void addCartItem(Item item, int qty){
        NSMutableDictionary ci=null;

        //check to see if item is already in cartItems, if so, bump up the qty
        if( (ci=cartItemContainingItem(item))!=null){
            addQtyToCartItem(ci, qty);
        }
        else{
            //This is a new item, not already in the cartItems array
            ci= new NSMutableDictionary();
            ci.setObjectForKey(item, "item");
            ci.setObjectForKey(new Integer(qty), "qty");
            ci.setObjectForKey(new Double(item.listPrice().doubleValue() * qty), "total");

            //add the cart item to the cart
            cartItems.addObject(ci);
        }
        //update the cart total
        cartTotal += item.listPrice().doubleValue() * qty;
    }

    public void addQtyToCartItem(NSMutableDictionary cartItem, int qty){
        Integer existingQty = (Integer)cartItem.objectForKey("qty");
        Integer newQty = new Integer(existingQty.intValue()+qty);

        cartItem.setObjectForKey(newQty, "qty");
    }

    public NSMutableDictionary cartItemContainingItem(Item item){
        //This  method returns the CartItem, containing a given Item
        for(int i=0;i<cartItems.count();i++){
            NSMutableDictionary ci=(NSMutableDictionary)cartItems.objectAtIndex(i);
            if(ci.objectForKey("item").equals(item))
            return ci;
        }
        return null;
    }
}
