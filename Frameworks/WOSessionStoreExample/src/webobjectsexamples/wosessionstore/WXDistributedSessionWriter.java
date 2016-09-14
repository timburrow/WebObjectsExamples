/*
 WXDistributedSessionWriter.java
 [WOSessionStoreExampleX Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wosessionstore;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOHTTPConnection;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;

public class WXDistributedSessionWriter extends Object  {

    private String _dssHost;
    private int _dssPort;
    private long _nextTry;
    private long _retries;
    WOHTTPConnection _remoteConnection;
    WOHTTPConnection _localConnection;


    static boolean debug_server = false;
    static boolean debug_async = false;
    static int debug_failover_count = 1;
    static int lastQueueSize = 0;
    static long _RETRY_BACKOFF = 60;
    // one minute
    static long _MAX_BACKOFF = 6000;
    // ten minutes
    static String getSessionURLPrefix = "/cgi-bin/WebObjects/DistributedSessionStoreServerJava.woa/wa/getSession?sessionID=";
    static NSDictionary getSessionHeaderDict = null;
    static String putSessionURLPrefix = "/cgi-bin/WebObjects/DistributedSessionStoreServerJava.woa/wa/storeSession";
    static NSDictionary putSessionHeaderDict = null;
    static String delSessionURLPrefix = "/cgi-bin/WebObjects/DistributedSessionStoreServerJava.woa/wa/deleteSession?sessionID=";
    static NSDictionary delSessionHeaderDict = null;

    static  {
        try {
            getSessionHeaderDict = new NSDictionary<Object, Object>(new Object[]{"WXDistributedSessionStore" , "octet/stream"},
                                                    new Object[]{"User-Agent", "Accept"});
            putSessionHeaderDict = new NSDictionary<Object, Object>(new Object[]{"WXDistributedSessionStore", "octet/stream"},
                                                    new Object[]{"User-Agent", "Content-Type"});
            delSessionHeaderDict = new NSDictionary<Object, Object>(new Object[]{"WXDistributedSessionStore", "text/html"},
                                                    new Object[]{"User-Agent", "Accept"});
        } catch(Throwable localException) {
            // You should always catch all exceptions occuring in a static initializer. Otherwise you get very
            // strange errors at runtime.
            System.out.println("Exception occured in static initializer...\n"+localException.toString());
            localException.printStackTrace();
        }
    }

    public WXDistributedSessionWriter(String hostName, int port) {
        super();

//        NSUserDefaults defaults = NSUserDefaults.standardUserDefaults();
        _dssPort = port;
        _dssHost = new String(hostName);
        _remoteConnection = null;
        // connection to remote session store server made lazily on demand
        _localConnection = null;
        // connection to local session store server made lazily on demand
        _retries = 0;
        _nextTry = System.currentTimeMillis();

        /*
        debug_server = defaults.boolForKey("WXDSSDebugEnabled");

        if (debug_server != null) {
            debug_failover_count = defaults.integerForKey("WXDSSDebugFailoverCount");

            if (debug_failover_count < 1) {
                debug_failover_count = 1;
            }
        }
         */

        return;
    }


    public void  finalize() throws Throwable {
        super.finalize();
    }

    public WOHTTPConnection  remoteServerConnection() {
        if ((_remoteConnection != null) && (!_remoteConnection.isConnected())) {
            _remoteConnection = null;
        }

        if (_remoteConnection == null) {
            _remoteConnection = new WOHTTPConnection(_dssHost, _dssPort);
            _remoteConnection.setKeepAliveEnabled(true);
        }

        return _remoteConnection;
    }


    public WOHTTPConnection  localServerConnection() {
        if ((_localConnection != null) && (!_localConnection.isConnected())) {
            _localConnection = null;
        }

        if (_localConnection == null) {
            // When local and remote are the same thing, we pretend we have no local connection.
            if (_dssHost.equals("localhost")) {
                return null;
            }

            _localConnection = new WOHTTPConnection("localhost", _dssPort);
            _localConnection.setKeepAliveEnabled(true);
        }

        return _localConnection;
    }


    public NSData  _getDataForSessionIDFromServer(String aSessionID, WOHTTPConnection serverConnection) {
        // Use a DirectAction
        WORequest getRequest;
        WOResponse response;
        NSData content = null;
        getRequest = new WORequest("GET", getSessionURLPrefix.concat(aSessionID), "HTTP/1.1", getSessionHeaderDict, null, null);
        if(WOApplication.application().isDebuggingEnabled()) {
            System.out.println("+++ DistributedSessionStore->_getDataForSessionID: INFO - sending request: <" + getRequest + "> +++");
        }

        if (serverConnection.sendRequest(getRequest)) {
            response = serverConnection.readResponse();
            if (response != null) {
                content = response.content();
                if(content != null) {
                    if(content.length() == 0) {
                        _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
                        content = null;
                    }
                }
            } else {
                _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
                content = null;
            }
        } else {
            _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
            content = null;
        }

        if (content != null) {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("+++ DistributedSessionStore->_getDataForSessionID: INFO - session " + aSessionID
                                   + " content length = " + content.length() + " +++");
            }
        } else {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("--- DistributedSessionStore->_getDataForSessionID: WARN - could not retrieve session " +
                                   aSessionID + " (could be timed out remotely) ---");
            }
        }
        return content;
    }

    public boolean  _putDataForSessionIDToServer(NSData content, String aSessionID, WOHTTPConnection serverConnection) {
        WORequest putRequest;
        WOResponse response;
        boolean rc;
        putRequest = new WORequest("POST", putSessionURLPrefix, "HTTP/1.1", putSessionHeaderDict, content, null);
        putRequest.setHeader(aSessionID, "x-wosessionid");
        if(WOApplication.application().isDebuggingEnabled()) {
            System.out.println("+++ DistributedSessionStore->_putData: INFO - sending request: <" + putRequest +
                               "> +++");
        }

        if (serverConnection.sendRequest(putRequest)) {
            response = serverConnection.readResponse();
            if (response != null) {
                rc = true;
            } else {
                rc = false;
                _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
            }
        } else {
            rc = false;
            _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
        }

        if (rc) {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("+++ DistributedSessionStore->_putData: INFO - session " + aSessionID + " +++");
            }
        } else {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("*** DistributedSessionStore->_putData: ERROR - could not store session " + aSessionID + " ***");
            }
        }
        return rc;
    }


    public boolean  _deleteDataForSessionIDFromServer(String  aSessionID, WOHTTPConnection  serverConnection) {
        // Use a DirectAction
        WORequest deleteRequest;
        WOResponse response;
        NSData content = null;

        deleteRequest = new WORequest("GET", delSessionURLPrefix.concat(aSessionID), "HTTP/1.1", delSessionHeaderDict, null, null);
        if(WOApplication.application().isDebuggingEnabled()) {
            System.out.println("+++ DistributedSessionStore->_deleteDataForSessionID: INFO - sending request: <" + deleteRequest + "> +++");
        }

        if (serverConnection.sendRequest(deleteRequest)) {
            response = serverConnection.readResponse();

            if (response != null) {
                content = response.content();
            } else {
                _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
            }

        } else {
            _nextTry = System.currentTimeMillis() + _RETRY_BACKOFF;
        }

        if (content != null) {
            return true;
        } else {
            return false;
        }

    }


    public void  removeSessionArchiveFromServerWithID(String  aSessionID) {
        WOHTTPConnection serverConnection = remoteServerConnection();
        boolean rc = _deleteDataForSessionIDFromServer(aSessionID, serverConnection);

        if (rc) {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("+++ DistributedSessionStore: INFO - session " + aSessionID +
                                   " deleted on server +++");
            }
        } else {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("--- DistributedSessionStore: WARN - could not delete session with ID: " +
                                   aSessionID + " on server ---");
            }
        }

    }


    public NSData  restoreSessionArchiveFromServerWithID(String  aSessionId) {
        NSData anArchivedSessionData = null;
        int attempts;

        WOHTTPConnection serverConnection;

        // Go ask (assuming it's connected) our remote session store server for the session.
        for (attempts = 2; attempts > 0; attempts--) {
            serverConnection = remoteServerConnection();
            anArchivedSessionData = _getDataForSessionIDFromServer(aSessionId, serverConnection);
            if (anArchivedSessionData != null) {
                break;
            }
        }
        return anArchivedSessionData;
    }

    // This is called by the subclasses to invoke the actual send to the server. This may be synchronous or asynchronous.
    public boolean  performStoreSessionArchiveToServerWithID(NSData  aSessionArchiveData, String  aSessionId) {
        WOHTTPConnection serverConnection = null;
        boolean didWrite = false;
        int attempts;

        for (attempts = 2; attempts > 0; attempts--) {
            serverConnection = remoteServerConnection();
            didWrite = _putDataForSessionIDToServer(aSessionArchiveData, aSessionId, serverConnection);

            if (didWrite) {
                break;
            }
        }

        if (didWrite) {
            // If debugging is switched on, verify we can get the session data back. Optionally repeat this
            // a large number of times to simulate failover
            if (debug_server) {
                int i;
                boolean failure = false;

                for (i = 0; (i < debug_failover_count) && (!failure); i++) {
                    NSData newData = _getDataForSessionIDFromServer(aSessionId, serverConnection);

                    if (newData.length() == aSessionArchiveData.length()) {
                        int length = newData.length();
                        byte[] oldSessionData = aSessionArchiveData.bytes();
                        byte[] newSessionData = newData.bytes();
                        int j;

                        for (j = 0; j < length; j++) {
                            if (oldSessionData[j] != newSessionData[j]) {
                                if(WOApplication.application().isDebuggingEnabled()) {
                                    System.out.println("storeSession: *** Bytes differ at " + j + " ***");
                                }
                                failure = true;
                                break;
                            }

                        }

                    } else {
                        if(WOApplication.application().isDebuggingEnabled()) {
                            System.out.println("storeSession: *** Data lengths differ ***");
                        }
                        failure = true;
                    }

                }

            }

        } else {
            if(WOApplication.application().isDebuggingEnabled()) {
                System.out.println("*** WARN: could not save session data " + aSessionId + " to server " +
                        _dssHost + " ***");
            }
            // Just log for now.
//[NSException raise:NSGenericException format:@"%@: could not save session data to server \"%@\".", [self class], _dssHost];
        }

        return didWrite;
    }


    // This is overridden by the subclasses to be synchronous or asynchronous.
    public boolean  storeSessionArchiveToServerWithID(NSData  aSessionArchiveData, String  aSessionId) {
        return performStoreSessionArchiveToServerWithID(aSessionArchiveData, aSessionId);
    }


    /************** Start Of SubclassHooks ***************/

}