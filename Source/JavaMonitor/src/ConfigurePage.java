/*
� Copyright 2006- 2007 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. 

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF 
SUCH DAMAGE.
 */
import com.webobjects.appserver.WOComponent;
import com.webobjects.foundation.NSArray;
import com.webobjects.monitor._private.MObject;
import com.webobjects.monitor._private.String_Extensions;

public class ConfigurePage extends MonitorComponent  {

    /**
	 * serialVersionUID
	 */
	private static final long	serialVersionUID	= -3447899695208425947L;

	/********** HTTP Server Section **********/
    public WOComponent HTTPServerUpdateClicked() {
        theApplication._lock.startReading();
        try {
            if (theApplication.siteConfig().hostArray().count() != 0) {
                sendUpdateSiteToWotaskds();
            }
        } finally {
            theApplication._lock.endReading();
        }

        ConfigurePage aPage = (ConfigurePage) pageWithName("ConfigurePage");
        return aPage;
    }
    /**********/

    /********** Email Section **********/
    public WOComponent emailUpdateClicked() {
        theApplication._lock.startReading();
        try {
            if (theApplication.siteConfig().hostArray().count() != 0) {
                sendUpdateSiteToWotaskds();
            }
        } finally {
            theApplication._lock.endReading();
        }

        ConfigurePage aPage = (ConfigurePage) pageWithName("ConfigurePage");
        return aPage;
    }
    /**********/


    /********** Adaptor Section **********/
    String _loadSchedulerSelection = null;;
    String loadSchedulerItem;
    NSArray loadSchedulerList = MObject.loadSchedulerArray;

    Integer urlVersionItem;
    NSArray urlVersionList = MObject.urlVersionArray;

    String customSchedulerName;

    String loadSchedulerSelection() {
        if ( (theApplication != null) && (theApplication.siteConfig().scheduler() != null) ) {
            int indexOfScheduler = MObject.loadSchedulerArrayValues.indexOfObject(theApplication.siteConfig().scheduler());
            if (indexOfScheduler != -1) {
                _loadSchedulerSelection = (String) loadSchedulerList.objectAtIndex(indexOfScheduler);
            } else {
                // Custom scheduler
                _loadSchedulerSelection = (String) loadSchedulerList.objectAtIndex(loadSchedulerList.count()-1);
                customSchedulerName = theApplication.siteConfig().scheduler();
            }
        }
        return _loadSchedulerSelection;
    }
    void setLoadSchedulerSelection(String value) {
        _loadSchedulerSelection = value;
    }

    Integer urlVersionSelection() {
        if (theApplication != null) return theApplication.siteConfig().urlVersion();
        return null;
    }
    void setUrlVersionSelection(Integer value) {
        if (theApplication != null) theApplication.siteConfig().setUrlVersion(value);
    }

    public WOComponent adaptorUpdateClicked() {
        String newValue;

        int i = loadSchedulerList.indexOfObject(_loadSchedulerSelection);
        if (i == 0) {
            newValue = null;
        } else if (i == (loadSchedulerList.count()-1)) {
            newValue = customSchedulerName;
            if (!String_Extensions.isValidXMLString(newValue)) {
                newValue = null;
            }
        } else {
            newValue = (String) MObject.loadSchedulerArrayValues.objectAtIndex(i);
        }
        theApplication.siteConfig().setScheduler(newValue);

        theApplication._lock.startReading();
        try {
            if (theApplication.siteConfig().hostArray().count() != 0) {
                sendUpdateSiteToWotaskds();
            }
        } finally {
            theApplication._lock.endReading();
        }

        ConfigurePage aPage = (ConfigurePage) pageWithName("ConfigurePage");
        return aPage;
    }
    /**********/
    
}
