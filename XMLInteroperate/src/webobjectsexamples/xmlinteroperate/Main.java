/*
Main.java
[XMLInteroperate project]

© Copyright 2005-2007 Apple, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/**
 * This is just a normal component class.
 */
package webobjectsexamples.xmlinteroperate;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import webobjectsexamples.realestate.common.Listing;
import webobjectsexamples.realestate.common.ListingPhoto;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;


public class Main extends WOComponent {
    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	private static NSData NO_PHOTO;

    static {
        WOResourceManager mgr = WOApplication.application().resourceManager();
        InputStream stream;
        try {
            stream = mgr.inputStreamForResourceNamed("NoPhoto.gif", null, null);
             NO_PHOTO = new NSData(stream, 15694); // 15694 is the file size
            stream.close();

        }  catch (IOException e) {
            NO_PHOTO = new NSData();
        }

    }

    // Keep track of last prices
    private NSMutableArray<BigDecimal> _lastPrices;

    /** @TypeInfo webobjectsexamples.realestate.common.Listing */
    public NSArray listings;

    public Listing listing;
    public int index;
    public String lastPhoto;
    public String xmlRepresentation;

    public Main(WOContext context) {
        super(context);

        try {
            EOEditingContext ec = session().defaultEditingContext();

            EOFetchSpecification fetchSpec = new EOFetchSpecification("Listing", null, null);
            // Let's deal with only 5 properties.
            fetchSpec.setFetchLimit(5);
            listings = ec.objectsWithFetchSpecification(fetchSpec);

            if (listings != null) {
                _lastPrices = new NSMutableArray<BigDecimal>(listings.count());
                for (int i=0; i<listings.count(); i++) {
                    _lastPrices.addObject(((Listing)listings.objectAtIndex(i)).askingPrice());
                }
            } else {
                NSLog.err.appendln("No Listing");
            }
        } catch (Exception exception) {
        	NSLog.err.appendln("Loading exception " + exception);
        	exception.printStackTrace();
        }
     }

    public WOComponent getLatestPrice() {
        Interoperator interoperator = ((Session)session()).interoperator();
        xmlRepresentation = interoperator.getLatestPrice(listing);

        return null;
    }

    public String listingStreet() {
        EOEnterpriseObject addr = listing.address();
        StringBuffer buf = new StringBuffer(200);
        buf.append((String)addr.valueForKey("street"));
        String aptNum = (String)addr.valueForKey("aptNum");
        if (aptNum != null) {
            buf.append(" #");
            buf.append(aptNum);
        }

        return buf.toString();
    }

    public String listingCityStateZip() {
        EOEnterpriseObject addr = listing.address();
        StringBuffer buf = new StringBuffer(200);
        buf.append((String)addr.valueForKey("city"));
        buf.append(", ");
        buf.append((String)addr.valueForKey("state"));
        buf.append(' ');
        buf.append((String)addr.valueForKey("zip"));

        return buf.toString();
    }

    public NSData photo() {
        NSArray photos = listing.listingPhotos();
        if (photos != null) {
            for (int i=0; i<photos.count(); i++) {
                ListingPhoto p = (ListingPhoto)photos.objectAtIndex(i);
                if (p.isPrimaryPhoto().booleanValue())
                    return p.photo();
            }

            // Just return any photo.
            return ((ListingPhoto)photos.objectAtIndex(0)).photo();
        }

        return NO_PHOTO;
    }

    public boolean hasPriceChanged() {
        float lastPrice = _lastPrices.objectAtIndex(index).floatValue();
        float newPrice = listing.askingPrice().floatValue();
        if (newPrice == lastPrice)
            return false;

        _lastPrices.replaceObjectAtIndex(listing.askingPrice(), index);

        if (newPrice > lastPrice)
            lastPhoto = "up.gif";
        else
            lastPhoto = "down.gif";

        return true;
    }

    public String listingAskingPrice() {
        return listing.askingPrice().setScale(2).toString();
    }
}
