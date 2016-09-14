/*
 Folder.java
 [WOInheritance Project]

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

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class Folder extends EOGenericRecord {

    public Folder() {
        super();
    }

/*
    // If you implement the following constructor EOF will use it to
    // create your objects, otherwise it will use the default
    // constructor. For maximum performance, you should only
    // implement this constructor if you depend on the arguments.
    public Folder(EOEditingContext context, EOClassDescription classDesc, EOGlobalID gid) {
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

    public String name() {
        return (String)storedValueForKey("name");
    }

    public void setName(String value) {
        takeStoredValueForKey(value, "name");
    }

    public Folder parent() {
        return (Folder)storedValueForKey("parent");
    }

    public void setParent(Folder value) {
        takeStoredValueForKey(value, "parent");
    }

    public NSArray children() {
        return (NSArray)storedValueForKey("children");
    }

    public void setChildren(NSMutableArray value) {
        takeStoredValueForKey(value, "children");
    }

    public void addToChildren(Folder object) {
        NSMutableArray array = (NSMutableArray)children();

        willChange();
        array.addObject(object);
    }

    public void removeFromChildren(Folder object) {
        NSMutableArray array = (NSMutableArray)children();

        willChange();
        array.removeObject(object);
    }

    public NSArray creatures() {
        return (NSArray)storedValueForKey("creatures");
    }

    public void setCreatures(NSMutableArray value) {
        takeStoredValueForKey(value, "creatures");
    }

    public void addToCreatures(Creature object) {
        NSMutableArray array = (NSMutableArray)creatures();

        willChange();
        array.addObject(object);
    }

    public void removeFromCreatures(Creature object) {
        NSMutableArray array = (NSMutableArray)creatures();

        willChange();
        array.removeObject(object);
    }

        //****** LOGIC **********

    public static Folder rootFolder(EOEditingContext ec) {
        EOQualifier qual=new EOKeyValueQualifier("parent",EOQualifier.QualifierOperatorEqual, null);
        EOFetchSpecification fs=new EOFetchSpecification("Folder", qual, null);
        NSArray array=ec.objectsWithFetchSpecification(fs);

        if (array.count()==1)
            return (Folder) array.objectAtIndex(0);

        if (array.count()>1)
            throw new RuntimeException("More than one Root Folder");

        // Root folder does not exist, so create and save one
        Folder rootFolder=new Folder();
        ec.insertObject(rootFolder);
        rootFolder.setName("ROOT");

        ec.saveChanges();

        return rootFolder;
    }

    public String path() {

        Folder currentFolder=this;
        String path=this.name();

        // Special case for Root
        if (currentFolder.parent() == null)
            return "/";

        while (currentFolder.parent() != null) {
            currentFolder=currentFolder.parent();
            if (currentFolder.parent() == null)
                path="/"+path;
            else
                path=currentFolder.name()+"/"+path;
        }
        return path;
    }

}
