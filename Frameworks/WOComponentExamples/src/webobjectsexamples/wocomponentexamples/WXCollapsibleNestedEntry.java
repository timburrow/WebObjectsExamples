/*
 WXCollapsibleNestedEntry.java
 [WOComponentExamples Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wocomponentexamples;

import java.io.Serializable;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class WXCollapsibleNestedEntry extends WOComponent implements Serializable {

    /**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5178329528973354100L;
	public static Integer WXNO = new Integer(0);
    public static Integer WXYES = new Integer(1);

    private boolean _isExpanded;


    /**
     * Constructor for the component, which also sets the URL for
     * the component action from the current WOContext. This method takes
     * one argument, the current WOContext.
     *
     * @param aContext		the current WOContext
     * @return			an instance of this component (WXCollapsibleNestedList)
     */

    public WXCollapsibleNestedEntry(WOContext aContext)  {
        super(aContext);
    }


    /**
     * Method to turn off variable synchronization between this component
     * and the parent to this component.
     */

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    public Integer isExpanded() {
        // ** This is here to avoid converting BOOL <-> NSNumber when accessed by wod.
        return _isExpanded ? WXYES : WXNO;
    }

    public WOComponent toggleExpansion() {
        if (_isExpanded) {
            _isExpanded = false;
        } else {
            _isExpanded = true;
        }
        return null;
    }

    public String currentToggleImageName() {
        String anImageName = null;
        if (_isExpanded) {
            anImageName = "DownTriangle.gif";
        } else {
            anImageName = "RightTriangle.gif";
        }
        return anImageName;
    }
}
