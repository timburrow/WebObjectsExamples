/*
 * FolderViewComponent.java
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

import java.util.Hashtable;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;

public class FolderViewComponent extends WOComponent {
    protected Folder aFolder;
    protected boolean isShowingChildrenFolders=false;
    protected Hashtable showingChildrenHashMap=new Hashtable();

    public FolderViewComponent(WOContext context) {
        super(context);
    }

    public Folder aFolder() {
        return aFolder;
    }

    public void setAFolder(Folder newFolder) {
         aFolder = newFolder;
    }

    public boolean synchronizesVariablesWithBindings() {
        return false;
    }

    public String nextLevelString()
    {
        String currentLevelString=(String) valueForBinding("currentLevelString");
        String levelIncrementString=(String) valueForBinding("levelIncrementString");
        return currentLevelString+levelIncrementString;
    }
    public boolean isShowingChildrenFolders() {
        if (aFolder == null)
            return false;

        // Hashtables can have an object as the key, NSDictionary needs String as key
        Boolean value=(Boolean) showingChildrenHashMap.get(aFolder);
        if (value==null || value.booleanValue()==false)
            return false;

        return true;
    }

    public WOComponent folderClicked()
    {
        setValueForBinding(aFolder, "selectedFolder");
        return null;
    }

    public WOComponent toggleVisibility()
    {
        boolean value=!isShowingChildrenFolders();

        showingChildrenHashMap.put(aFolder, new Boolean(value));

        return null;
    }

    public WOComponent updateName()
    {
        setValueForBinding(new Boolean(false), "editName");

        session().defaultEditingContext().saveChanges();

        return null;
    }

    public boolean isOneToEdit()
    {
        Folder selectedFolder=(Folder) valueForBinding("selectedFolder");

        if (aFolder != selectedFolder)
            return false;

        Boolean shouldEdit=(Boolean) valueForBinding("editName");

        if (shouldEdit != null && shouldEdit.booleanValue())
            return true;

        return false;
    }

    public boolean isSelectedFolder()
    {
        Folder selectedFolder=(Folder) valueForBinding("selectedFolder");

        if (aFolder != selectedFolder)
            return false;

        return true;
    }
}
