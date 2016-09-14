/*
© Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */
//
// PetStoreComponent.java
// Project PetStoreWOJava
//
// subclass of WOComponent for the Petstore app. Has usefull methods for all parent components
package webobjectsexamples.petstorewojava;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjectsexamples.petstore.Account;

public class PetStoreComponent extends WOComponent {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1083094842226336908L;
	public String pageDisplayName;
    public WOComponent lastPage;

    public PetStoreComponent(WOContext cxt) {
        super(cxt);
      }

    public Session sess(){
        return (Session)session();
    }

    public boolean isLoggedIn(){
        return sess().currentAccount()==null?false:true;
    }

    public Account currentAccount(){
        return sess().currentAccount();
    }

    public Cart cart(){
        return sess().cart();
    }

    public WOComponent goBackToLastPage(){
        return lastPage;
    }

    public WOComponent lastPage(){
        return lastPage;
    }

    public boolean hasLastPage(){
        //hepler for conditionals that  display a "last page" link
        return (lastPage==null?false:true);
    }

    public WOComponent noActionSubmit(){
        //this is a convienience method for submitting "updates"
        return null;
    }

    public String pageDisplayName(){
        //If the subclass has a PAGE_DISPLAY_NAME ivar declared,  then this is
        //what the "last page" link will display. This can be overridden
        //to display a more custom name
        if((pageDisplayName = (String)valueForKey("PAGE_DISPLAY_NAME"))==null)
            return name();
        else
            return pageDisplayName;
    }

    @Override
	public WOComponent pageWithName(String name){
    //this custom implementation of pageWithName automatically handles
    //setting the last page for a "go back" link.
        WOComponent page = super.pageWithName(name);

        if(name.equals(this.name()))
            page.takeValueForKey(this.lastPage(),"lastPage");
        else
            page.takeValueForKey(this,"lastPage");

        return page;
    }

    @Override
	public boolean synchronizesVariablesWithBindings(){
        return false;
    }

}
