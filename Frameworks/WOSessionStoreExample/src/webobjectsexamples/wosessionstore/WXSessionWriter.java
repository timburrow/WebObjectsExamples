/*
 WXSessionWriter.java
 [WOSessionStoreExampleX Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wosessionstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPathUtilities;

public abstract class WXSessionWriter extends Object {
    private String _archivedSessionsPath;
    private WXArchiveTimeOutManager _archiveTimeOutManager;


    static private String
    _sessionArchivesPath()  {
        String aSessionArchivesPath = System.getProperty("WOSessionArchivesPath");
        if (aSessionArchivesPath==null) {
            String aPath = NSBundle.mainBundle().bundlePathURL().getPath();
            aSessionArchivesPath = NSPathUtilities.stringByAppendingPathComponent(aPath, "SessionArchives");
        }
        return aSessionArchivesPath;
    }

    private String
    _createSessionArchivesDirectory() throws IOException {
        String aSessionArchivesPath = _sessionArchivesPath();
        File aFile = new File(aSessionArchivesPath);
        if (!aFile.exists()) {
            boolean wasDirectoryCreated = aFile.mkdir();
            if (!wasDirectoryCreated) {
                throw new IOException("<"+getClass().getName()+">:  Could not create SessionArchives directory \""+aSessionArchivesPath+"\".  Check permissions.");
            }
        }
        return aSessionArchivesPath;
    }

    public WXSessionWriter() throws IOException {
        super();
        _archivedSessionsPath = _createSessionArchivesDirectory();
        _archiveTimeOutManager = new WXArchiveTimeOutManager(_archivedSessionsPath);
    }

    public String pathForSessionID(String aSessionID) {
        String aFullPath = NSPathUtilities.stringByAppendingPathComponent(_archivedSessionsPath, aSessionID);
        return aFullPath;
    }

    public WOSession readSessionFromDiskWithID(String aSessionId)  {
        WOSession aSession = null;
        String aSessionFilePath = pathForSessionID(aSessionId);
        try {
            FileInputStream fin = new FileInputStream(aSessionFilePath);
            ObjectInputStream in = new ObjectInputStream(fin);
            aSession = (WOSession)in.readObject();
            in.close();
            if (aSession!=null) {
                NSLog.debug.appendln("<"+getClass().getName()+"> Restored session "+aSessionId+" from disk.");
            }
        } catch (Exception e) {
            // ** swallow the exception as we'll deal with it later since aSession will be nil
        	NSLog.err.appendln("<"+getClass().getName()+">: "+e);
            if (WOApplication.application().isDebuggingEnabled()) {
                e.printStackTrace();
            }
            aSession = null;

        }
        return aSession;
    }

    public abstract void writeSessionToDisk(WOSession aSession) throws IOException;
}
