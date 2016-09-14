/*
 * User.java
 * [JavaRealEstate Project]
 *
 * © Copyright 2001-2007 Apple Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (ÒAppleÓ) in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms,
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under AppleÕs copyrights in this original Apple
 * software (the ÒApple SoftwareÓ), to use, reproduce, modify and
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

package webobjectsexamples.realestate.server;

import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.foundation.NSArray;

public abstract class User extends webobjectsexamples.realestate.common.User {
    public static final String PasswordKey = "password";
    public static final String UserTypeKey = "userType";
    public static final String DefaultsKey = "defaults";

    public static final String DefaultsEntity = "UserDefaults";

    public static final Integer AgentUserType = new Integer(1);
    public static final Integer CustomerUserType = new Integer(2);
    public static final Integer AdministratorUserType = new Integer(9);

    public User() {
        super();
    }

    abstract Integer _userType();

    @Override
	public void awakeFromInsertion(EOEditingContext editingContext) {
        super.awakeFromInsertion(editingContext);
        setUserType(_userType());
    }

    public String password() {
        return (String)storedValueForKey(PasswordKey);
    }

    public void setPassword(String value) {
        takeStoredValueForKey(value, PasswordKey);
    }

    public void setUserType(Number value) {
        takeStoredValueForKey(value, UserTypeKey);
    }

    public Number userType() {
        return (Number)storedValueForKey(UserTypeKey);
    }

    public NSArray defaults() {
        return (NSArray)storedValueForKey(DefaultsKey);
    }

    public void setDefaults(NSArray value) {
        takeStoredValueForKey(value, DefaultsKey);
    }

    public void addToDefaults(EOEnterpriseObject object) {
        includeObjectIntoPropertyWithKey(object, DefaultsKey);
    }

    public void removeFromDefaults(EOEnterpriseObject object) {
        excludeObjectFromPropertyWithKey(object, DefaultsKey);
    }

    private EOEnterpriseObject _mainDefaults(boolean createIfNecessary) {
        NSArray defaults = defaults();
        if ((defaults != null) && (defaults.count() > 0)) {
            return (EOEnterpriseObject)(defaults().objectAtIndex(0));
        }

        if (createIfNecessary) {
            EOEditingContext editingContext = editingContext();
            EOClassDescription classDescription = EOClassDescription.classDescriptionForEntityName(DefaultsEntity);
            EOEnterpriseObject object = classDescription.createInstanceWithEditingContext(editingContext, null);
            editingContext().insertObject(object);
            addObjectToBothSidesOfRelationshipWithKey(object, DefaultsKey);
            return object;
        }
        return null;
    }

    public EOEnterpriseObject mainDefaults() {
        return _mainDefaults(false);
    }

    public EOEnterpriseObject mainDefaultsGuaranteed() {
        return _mainDefaults(true);
    }
}
