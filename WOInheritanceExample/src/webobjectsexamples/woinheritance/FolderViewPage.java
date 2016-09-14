/*
 * FolderViewPage.java
 * [WOInheritance Project]
 *
 * © Copyright 2001-2007 Apple, Inc. All rights reserved.
 *
 * IMPORTANT:  This Apple software is supplied to you by Apple Computer,
 * Inc. (“Apple”) in consideration of your agreement to the following
 * terms, and your use, installation, modification or redistribution of
 * this Apple software constitutes acceptance of these terms.  If you do
 * not agree with these terms, please do not use, install, modify or
 * redistribute this Apple software.
 *
 * In consideration of your agreement to abide by the following terms,
 * and subject to these terms, Apple grants you a personal, non-
 * exclusive license, under Apple’s copyrights in this original Apple
 * software (the “Apple Software”), to use, reproduce, modify and
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

import java.util.Enumeration;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WODisplayGroup;
import com.webobjects.eoaccess.EOEntity;
import com.webobjects.eoaccess.EOModel;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.foundation.NSMutableArray;

public class FolderViewPage extends WOComponent {
    protected Folder rootFolder;
    protected Folder selectedFolder;
    protected Creature creature;
    protected Folder aFolder;
    protected boolean renameFolder=false;
    protected WODisplayGroup creatureDisplayGroup;
    protected Creature selectedCreature;
    protected boolean isAddingCreature=false;
    protected boolean isCreatingNewCreature=false;

    /** @TypeInfo EOEntity */
    protected NSMutableArray nonAbstractCreatureEntities=null;
    protected EOEntity anEntity;
    protected EOEntity selectedEntity;

    public FolderViewPage(WOContext context) {
        super(context);
    }

    public Folder rootFolder()
    {
        return rootFolder;
    }
    public void setRootFolder(Folder newRootFolder)
    {
        rootFolder = newRootFolder;
        selectedFolder=rootFolder;
    }

    public WOComponent newFolder()
    {
        if (selectedFolder == null)
            return null;

        EOEditingContext ec=session().defaultEditingContext();
        Folder newFolder=new Folder();
        newFolder.setName("New Folder");

        ec.insertObject(newFolder);
        selectedFolder.addObjectToBothSidesOfRelationshipWithKey(newFolder, "children");

        ec.saveChanges();

        return null;
    }

    public EOEntity anEntity()
    {
        return anEntity;
    }

    public void setAnEntity(EOEntity entity)
    {
        anEntity = entity;
    }

    public EOEntity selectedEntity()
    {
        return selectedEntity;
    }

    public void setSelectedEntity(EOEntity entity)
    {
        selectedEntity = entity;
    }

    public boolean renameFolder()
    {
        return renameFolder;
    }

    public void setRenameFolder(boolean yn)
    {
        renameFolder = yn;
    }

    public String getSelectedModelType()
    {
        return (String) application().valueForKey("selectedModelType");
    }

    public WODisplayGroup getCreatureDisplayGroup()
    {
        return creatureDisplayGroup;
    }

    public void setCreatureDisplayGroup(WODisplayGroup newDisplayGroup)
    {
         creatureDisplayGroup = newDisplayGroup;
    }

    public Folder getSelectedFolder()
    {
        return selectedFolder;
    }

    public Folder aFolder()
    {
        return aFolder;
    }

    public void setAFolder(Folder newFolder)
    {
        aFolder = newFolder;
    }

    public Creature creature()
    {
        return creature;
    }

    public void setCreature(Creature newCreature)
    {
        creature = newCreature;
    }

    public boolean isAddingCreature()
    {
        return isAddingCreature;
    }

    public boolean isCreatingNewCreature()
    {
        return isCreatingNewCreature;
    }

    public Creature selectedCreature()
    {
        return selectedCreature;
    }

    public WOComponent addReplaceCreatures()
    {
        isAddingCreature=true;
        return null;
    }

    public WOComponent editName()
    {
        renameFolder=true;

        return null;
    }

    public WOComponent creatureClicked()
    {
        selectedCreature=creature;
        isAddingCreature=false;
        return null;
    }

    public void setSelectedFolder(Folder aFolder) {
        if (selectedFolder == aFolder)
            return;

        selectedFolder=aFolder;
        selectedCreature=null;
        isAddingCreature=false;
        isCreatingNewCreature=false;
    }

    public WOComponent cancel()
    {
        session().defaultEditingContext().revert();
        isAddingCreature=false;
        isCreatingNewCreature=false;
        selectedCreature=null;
        return null;
    }

    public WOComponent save() {
        session().defaultEditingContext().saveChanges();

        isAddingCreature=false;
        isCreatingNewCreature=false;
        return null;
    }


    public WOComponent newCreature()
    {
        isCreatingNewCreature=true;
        return null;
    }

    /** @TypeInfo EOEntity */
    public NSMutableArray nonAbstractCreatureEntities()
    {
        if (nonAbstractCreatureEntities != null)
            return nonAbstractCreatureEntities;

        // Look at EOEntities and figure out which ones are subentities to Creature and are not Abstract
        nonAbstractCreatureEntities=new NSMutableArray();

        // Assume only one model is in this app
        EOModel model=(EOModel) EOModelGroup.globalModelGroup().models().objectAtIndex(0);
        Enumeration anEnum=model.entities().objectEnumerator();
        EOEntity creatureEntity=model.entityNamed("Creature");

        while (anEnum.hasMoreElements()) {
            EOEntity entity=(EOEntity) anEnum.nextElement();

            if (entity.isAbstractEntity())
                continue;

            // see if this entity is a subentity of Creature
            EOEntity tempEntity=entity.parentEntity();
            while (tempEntity != null) {
                if (tempEntity == creatureEntity) {
                    nonAbstractCreatureEntities.addObject(entity);
                    break;
                }
                tempEntity=tempEntity.parentEntity();
            }
        }

        return nonAbstractCreatureEntities;
    }


    public WOComponent editNewCreature()
    {
        EOEditingContext ec=session().defaultEditingContext();

        // Create new and add to selected folder
        selectedCreature= (Creature)selectedEntity.classDescriptionForInstances().createInstanceWithEditingContext(ec, null);

        // put Creature in session's editingContext
        ec.insertObject(selectedCreature);

        // Add creature to folder
        selectedFolder.addObjectToBothSidesOfRelationshipWithKey(selectedCreature, "creatures");
        isAddingCreature=false;
        isCreatingNewCreature=false;
        return null;
    }

    public WOComponent deleteCreature()
    {
        EOEditingContext ec=session().defaultEditingContext();

        // If the model is set up correctly the creature should also disappear from all the folders it is in
        ec.deleteObject(selectedCreature);

        ec.saveChanges();

        selectedCreature=null;
        isAddingCreature=false;
        isCreatingNewCreature=false;

        return null;
    }

    public WOComponent deleteSelectedFolder()
    {
        EOEditingContext ec=session().defaultEditingContext();

        // If the model is setup correctly the creature should NOT be deleted from the database
        ec.deleteObject(selectedFolder);

        ec.saveChanges();

        selectedFolder=null;
        isAddingCreature=false;
        isCreatingNewCreature=false;
        return null;
    }

    public WOComponent selectRootFolder()
    {
        setSelectedFolder(rootFolder);
        return null;
    }

    public boolean isRootFolderSelected()
    {
        return (selectedFolder==rootFolder);
    }
}
