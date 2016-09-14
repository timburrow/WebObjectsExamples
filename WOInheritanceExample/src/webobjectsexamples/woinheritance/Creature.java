/*
 * Creature.java
 * [WOInheritance Project]
 *
 * © Copyright 2001-2007 Apple, Inc. All rights reserved.
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

package webobjectsexamples.woinheritance;

import com.webobjects.eocontrol.EOClassDescription;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.foundation.NSTimestamp;

public class Creature extends EOGenericRecord {
    public Creature() {
        super();

        System.out.println(" ********* "+getClass().getName()+" instantiated ");
    }

    private static short _hasSingleTableType = 0;

    public static boolean hasSingleTableType() {
        final String rootEntityName = "Creature";
        final String rootEntityTypeKey = "type";
        if (_hasSingleTableType == 0) {
            EOClassDescription cd = EOClassDescription.classDescriptionForEntityName(rootEntityName);
            if (cd.attributeKeys().containsObject(rootEntityTypeKey)) {
                _hasSingleTableType = 1;
            } else {
                _hasSingleTableType = -1;
            }
        }
        return (_hasSingleTableType == 1);
    }

    public void awakeFromInsertion (EOEditingContext ec) {
        super.awakeFromInsertion(ec);

        System.out.println("******* >> "+ getClass().getName()+" awakeFromInsertion, ec="+ec);
        // this is needed because client side changes seem to be applied first and we
        // would override them
        if (firstName() == null)
            setFirstName(getClass().getName());

        // This is only actually used when Single Table Inheritance is used
        // All other times this value never makes it into the Database
        // Single table inheritance will have an extra column called type in the
        // database that will be used to figure out what entity the row belongs to
        if (Creature.hasSingleTableType()) {
            String className = getClass().getName();
            int dot = className.lastIndexOf('.');
            if (dot != -1) {
                className = className.substring(dot+1);
            }
            setType(className);
        }
    }

/*
    // If you implement the following constructor EOF will use it to
    // create your objects, otherwise it will use the default
    // constructor. For maximum performance, you should only
    // implement this constructor if you depend on the arguments.
    public Creature(EOEditingContext context, EOClassDescription classDesc, EOGlobalID gid) {
        super(context, classDesc, gid);
    }

    // If you add instance variables to store property values you
    // should add empty implementions of the Serialization methods
    // to avoid unnecessary overhead (the properties will be
    // serialized for you in the superclass).
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    }
*/

    public Number numberOfLegs() {
        return (Number)storedValueForKey("numberOfLegs");
    }

    public void setNumberOfLegs(Number value) {
        takeStoredValueForKey(value, "numberOfLegs");
    }

    public NSTimestamp dateOfBirth() {
        return (NSTimestamp)storedValueForKey("dateOfBirth");
    }

    public void setDateOfBirth(NSTimestamp value) {
        takeStoredValueForKey(value, "dateOfBirth");
    }

    public String firstName() {
        return (String)storedValueForKey("firstName");
    }

    public void setFirstName(String value) {
        takeStoredValueForKey(value, "firstName");
    }

    // **** LOGIC ***

    public String toString() {
        return customDescription();
    }

    public String customDescription() {
        String namePath = getClass().getName();
        if (namePath != null) {
            int index = namePath.lastIndexOf(".");
            if (index >= 0) {
                return (index < namePath.length() - 1) ? namePath.substring(index + 1)+" - "+firstName() : firstName();
            }
	    return namePath +" - "+firstName();
        }
        return firstName();
    }

    // The type is only saved to the DB when using SingleTable inheritance.
    // For Vertical and Horizontal we will compute it on the fly and denote NotInDB in the string
    public String type() {
        String type="NotInDB:"+getClass().getName();
        if (Creature.hasSingleTableType()) {
            type=(String) storedValueForKey("type");
        }
        return type;
    }

    // The type is only saved to the DB when using SingleTabe inheritance.
    // For Vertical and Horizontal we will compute it on the fly in the type() method and denote NotInDB in the string
    public void setType(String aType) {
        if (Creature.hasSingleTableType()) {
            takeStoredValueForKey(aType,"type");
        }
    }

}
