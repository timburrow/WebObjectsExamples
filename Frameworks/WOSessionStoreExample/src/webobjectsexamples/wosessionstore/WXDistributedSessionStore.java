/*
 WXDistributedSessionStore.java
 [WOSessionStoreExampleX Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wosessionstore;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOSession;
import com.webobjects.appserver.WOSessionStore;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSNotification;
import com.webobjects.foundation.NSNotificationCenter;

public abstract class WXDistributedSessionStore extends WOSessionStore {

    WOSessionStore _serverSessionStore;
    WXDistributedSessionWriter _sessionWriter;

    // We default to the simpler distributed session writer.
    public WXDistributedSessionStore() {
        super();
        _serverSessionStore = WOSessionStore.serverSessionStore();
        _sessionWriter = new WXSynchronousDistributedSessionWriter(hostName(), port());
    }

    // This will be called by subclasses with null for both arguments.  The subclasses set the ivars directly.
    public WXDistributedSessionStore(WOSessionStore aServerSessionStore, WXDistributedSessionWriter aSessionWriter) {
        super();
        _serverSessionStore = aServerSessionStore;
        _sessionWriter = aSessionWriter;
    }

    public void  finalize() throws Throwable {
        NSNotificationCenter.defaultCenter().removeObserver(this);
        super.finalize();
    }

    // We attempt to restore the session from the WOServerSessionStore.  If that fails we try to restore the session
    // from the distributed session store.
    public WOSession  restoreSessionWithID(String  aSessionId, WORequest  aRequest) {
        WOSession aSession = _serverSessionStore.checkOutSessionWithID(aSessionId, aRequest);

        // For testing purposes, set aSession to null here to force retrieval of the session from the
        // distributed session store.
//        aSession = null;

        if (aSession == null) {
            NSData aSessionArchive = _sessionWriter.restoreSessionArchiveFromServerWithID(aSessionId);
            if (aSessionArchive != null) {
                try {
                    byte[] sessionBytes;
                    ByteArrayInputStream byteArrayInputStream;
                    ObjectInputStream objectInputStream;

                    sessionBytes = aSessionArchive.bytes();
                    byteArrayInputStream = new ByteArrayInputStream(sessionBytes);
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    aSession = (WOSession)objectInputStream.readObject();
                    if (aSession != null) {
                        if(WOApplication.application().isDebuggingEnabled()) {
                            System.out.println("+++ Restored session " + aSessionId + " from server +++");
                        }
                    }

                } catch(Throwable localException) {
                    // Swallow the exception as we'll deal with it later since aSession will be null.
                    if(WOApplication.application().isDebuggingEnabled()) {
                        System.out.println("" + (getClass()) + ": " + (localException));
                    }

                    aSession = null;
                }
            }
        }
        return aSession;
    }

    // Save the session to the server session store and then for failover purposes save it also to the
    // distributed session store.
    public void saveSessionForContext(WOContext aContext) {
        WOSession aSession = aContext.session();
        String aSessionID = aSession.sessionID();
        ByteArrayOutputStream byteArrayOutputStream;
        ObjectOutputStream objectOutputStream;
        byte[] sessionBytes;
        NSData sessionData;

        byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(aSession);
            objectOutputStream.flush();
            sessionBytes = byteArrayOutputStream.toByteArray();
            sessionData = new NSData(sessionBytes);

            // Once we have the session archived, we can free up the serverSessionStore by checking in the session.
            // This is because, the main thread (which runs main run loop and the socket timeout Timer),
            // is blocked by a lock until we do so.
            _serverSessionStore.checkInSessionForContext(aContext);

            if (_sessionWriter.storeSessionArchiveToServerWithID(sessionData, aSessionID)) {
                if(WOApplication.application().isDebuggingEnabled()) {
                    System.out.println("+++ INFO: Wrote session " + aSessionID + " to server +++");
                }
            } else {
                if(WOApplication.application().isDebuggingEnabled()) {
                    System.out.println("--- WARN: Write of session " + aSessionID + " to server FAILED ---");
                }
            }
        } catch (IOException ioException) {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("--- WARN: Write of session " + aSessionID + " to server FAILED ---");
            }
        }
    }

    public void  removeSession(NSNotification  notification) {
        String sessionID = (String )notification.object();
        _sessionWriter.removeSessionArchiveFromServerWithID(sessionID);
    }


    // *************************************************************************
    // These methods were formerly in a category on WXDistributedSessionStore.
    // *************************************************************************
    static public String  hostName() {
        // Defaults to localhost only because that means we can test with a single machine.
        String hostName = System.getProperty("WXDistributedSessionStoreHost");

        if (hostName == null) {
            hostName = "localhost";
        }

        return hostName;
    }

    static public int port() {
        int port;
        String portString;

        portString = System.getProperty("WXDistributedSessionStorePort");

        if(portString != null) {
            port = Integer.parseInt(portString);
        } else {
            port = 9999;
        }

        return port;
    }

    /*
    static public WOSessionStore synchronousDistributedSessionStore() {
        WOSessionStore aServerSessionStore = serverSessionStore();
        WXDistributedSessionWriter aSessionWriter = new WXSynchronousDistributedSessionWriter(hostName(), port());
        WOSessionStore aDistributedSessionStore = new WXDistributedSessionStore(aServerSessionStore, aSessionWriter);
        return aDistributedSessionStore;
    }
     */

    /*
    static public WOSessionStore  as() {
        WOSessionStore aServerSessionStore = serverSessionStore();
        WXDistributedSessionWriter aSessionWriter = new WXAsynchronousDistributedSessionWriter(hostName(), port());
        WOSessionStore aDistributedSessionStore = new WXDistributedSessionStore(aServerSessionStore, aSessionWriter);
        return aDistributedSessionStore;
    }
     */

}
