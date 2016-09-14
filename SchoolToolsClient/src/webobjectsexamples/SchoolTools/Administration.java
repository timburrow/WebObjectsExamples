/*
 * File: Administration.java
 *
 * Description: Java file associated with Administration.wo
 *
 * Author: Apple Computer
 *
 * Copyright: © Copyright 2005-2007 Apple, Inc. All rights reserved.
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.jspservlet.WOServletContext;

public class Administration extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2017478130184334828L;
	protected String         department;
    protected NSMutableArray departmentList  = new NSMutableArray();
    protected boolean        error           = false;
    protected String         orderCount      = "0";
    protected ClientOrder    orderItem;
    protected NSMutableArray orderList       = new NSMutableArray();
    protected ClientProduct  product;
    protected NSMutableArray productList     = new NSMutableArray();
    protected ClientProduct  productSelected;
    protected int            quantity;
    protected double         total;
    protected String         vendor;
    protected NSMutableArray vendorList      = new NSMutableArray();

    public Administration(WOContext context) {
        super(context);

        /* Populate the list with values represent preferred Vendors */

        vendorList.addObject("The Little MacDealer");
        vendorList.addObject("Barry's Office Supplies");
        vendorList.addObject("Red Ball Sporting Goods");
        vendorList.addObject("Andersons Office Furniture");

        /* School Department list */
        departmentList.addObject("Math Department");
        departmentList.addObject("English Department");
        departmentList.addObject("Social Sciences");
        departmentList.addObject("History Department");
        departmentList.addObject("Art Department");
        departmentList.addObject("Athletic Department");
        departmentList.addObject("Janitorial Department");
        departmentList.addObject("Administration Department");

        /* Product List */
        productList.addObject(new ClientProduct("iBook 500-Mhz", "M7710LL/A", 1299.00));
        productList.addObject(new ClientProduct("iMac Special Edition 600-Mhz", "M145345/LLA", 1499.00));
        productList.addObject(new ClientProduct("iMac 500-Mhz", "M145345/LLA", 1199.00));
        productList.addObject(new ClientProduct("iMac 400-Mhz", "M145345/LLA", 899.00));
        productList.addObject(new ClientProduct("PowerBook G4 500-Mhz", "M7710LL/A", 3997.00));
        productList.addObject(new ClientProduct("PowerMac G4 733-Mhz", "M8451LL/A", 3499.00));
        productList.addObject(new ClientProduct("PowerMac G4 533-Mhz", "M7688LL/A", 2199.00));
        productList.addObject(new ClientProduct("PowerMac G4 Dual 533-Mhz", "M7688LL/A", 2599.00));
        productList.addObject(new ClientProduct("PowerMac G4 466-Mhz", "M7627LL/A", 1699.00));
    }

    public Administration addItem()
    {
        error = false;

        if (quantity == 0) {
            error = true;
            return null;
        }

        ClientOrder currentOrderItem = new ClientOrder(productSelected, quantity);
        orderList.addObject(currentOrderItem);
        orderCount = String.valueOf(orderList.count());

        // save this stuff in the servlet session so that the SoapHook can grab a hold of it
        HttpSession session = ((WOServletContext)context()).httpServletRequest().getSession();
        session.setAttribute("orderList",  orderList.vector());
        session.setAttribute("pin",        ((Session)session()).getPin());
        session.setAttribute("wsClient",   ((Session)session()).getWSClient());

        return null;
    }

    public String getDepartment() {
        return department;
    }

    public NSArray getDepartmentList() {
        return departmentList;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public ClientOrder getOrderItem() {
        return orderItem;
    }

    public NSArray getOrderList() {
        return orderList;
    }

    public ClientProduct getProduct() {
        return product;
    }

    public NSArray getProductList() {
        return productList;
    }

    public ClientProduct getProductSelected() {
        return productSelected;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotal() {
        return total += quantity * productSelected.getPrice();
    }

    public String getVendor() {
        return vendor;
    }

    public NSArray getVendorList() {
        return vendorList;
    }

    public String resultJSPPageHref() {
        HttpServletRequest httpRequest = ((WOServletContext)context()).httpServletRequest();

        String result = httpRequest.getContextPath() + "/jsp/Result.jsp";

        HttpSession servletSession = httpRequest.getSession(false);
        if (servletSession != null) {
            result = result + ";jsessionid=" + servletSession.getId();
        }

        return result;
    }

    public void setDepartment(String aDepartment) {
        department = aDepartment;
    }

    public void setOrderItem(ClientOrder anOrderItem) {
        orderItem = anOrderItem;
    }

    public void setProduct(ClientProduct aProduct) {
        product = aProduct;
    }

    public void setProductSelected(ClientProduct aProductSelected) {
        productSelected = aProductSelected;
    }

    public void setQuantity(int aQuantity) {
        quantity = aQuantity;
    }

    public void setVendor(String aVendor) {
        vendor = aVendor;
    }
}
