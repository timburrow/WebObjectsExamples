/*
� Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
// Generated by the WebObjects Wizard Wed Dec 06 15:00:20 America/Los_Angeles 2000
package webobjectsexamples.petstorewojava;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSValidation;
import com.webobjectsexamples.petstore.Account;
import com.webobjectsexamples.petstore.Order;

public class BillingDetails extends PetStoreComponent {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4443706380991573096L;

	protected String PAGE_DISPLAY_NAME="Billing Details Page";

    protected NSArray creditCardTypes=Order.CreditCardTypes;
    protected NSArray months=Order.Months;
    protected NSArray years=Order.Years;
    protected NSArray countries=Account.Countries;
    protected NSArray states=Account.States;

    protected boolean shipToBilling = true;
    protected boolean isConfirmShipping;
    protected String creditCardType;
    protected String month, year;
    protected String firstname,lastname,addr1, addr2,city,state,zip,country;
    protected boolean editCCInfo;
    protected boolean hasEntryError;
    protected String errorMessage;
      private Order order;


    public BillingDetails(WOContext cxt) {
        super(cxt);

        order = Order.createAndInsertOrder(currentAccount(), cart().cartItems(), sess().defaultEditingContext());

        initCCInfo();
        editCCInfo = true;
    }

    void initCCInfo(){
        Account account = currentAccount();
        firstname = account.firstname();
        lastname = account.lastname();
        addr1 = account.addr1();
        addr2 = account.addr2();
        city = account.city();
        state = account.state();
        zip = account.zip();
        country = account.country();
    }

    protected Order order(){
        return order;
    }

    public WOComponent confirmShippingData() {
        if(!shipToBilling && editCCInfo){
            editCCInfo=false;

            firstname = null;
            lastname = null;
            addr1 = null;
            addr2 = null;
            city = null;
            zip = null;

            return null;
        }

        isConfirmShipping = true;
        Account account = currentAccount();

        try{
            order.setExprdate(month + "/" + year);
            order.setCourier("UPS");
            order.setBilladdr1(account.addr1());
            order.setBilladdr2(account.addr2());
            order.setBillcity(account.city());
            order.setBillstate(account.state());
            order.setBillzip(account.zip());
            order.setBillcountry(account.country());
            order.setBilltofirstname(account.firstname());
            order.setBilltolastname(account.lastname());
            order.setShipaddr1(addr1);
            order.setShipaddr2(addr2);
            order.setShipcity(city);
            order.setShipstate(state);
            order.setShipzip(zip);
            order.setShipcountry(country);
            order.setShiptofirstname(firstname);
            order.setShiptolastname(lastname);
        }catch(NSValidation.ValidationException ex){
            hasEntryError = true;
            errorMessage = ex.getMessage();
        }
            hasEntryError = false;
            return null;
    }

    public WOComponent finishOrder() {
        WOComponent page = null;
        try{
            sess().defaultEditingContext().saveChanges();
            //now that the order has been committed, clear the cart
            cart().clearCart();

            page = pageWithName("OrderCompletion");
            page.takeValueForKey(order,"order");

        }catch(Exception ex){
            hasEntryError = true;
            errorMessage = ex.getMessage();
        }

        return page;
    }

    public WOComponent editBillingInfo()
    {
        isConfirmShipping = false;
        initCCInfo();
        editCCInfo = true;
        return null;
    }
}