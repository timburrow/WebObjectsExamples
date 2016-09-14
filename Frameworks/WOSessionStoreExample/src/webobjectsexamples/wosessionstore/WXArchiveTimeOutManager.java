/*
 WXArchiveTimeOutManager.java
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

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOTimer;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSPathUtilities;

// ** This class sets up an WOTimer which is periodically invoked (600 secs) to start the disk cleanup process.  The user default WOSessionArchiveTimeOut can be set to some number of seconds (1 day by default) and, if a given session archive hasn't been touched for this period, it will be removed.  A separate thread is established to scan the _archivedSessionsPath for stale session archives.  The WOSessionArchiveTimeOut should be significantly greater than WOSessionTimeOut.  Hopefully, the time it takes to do a scan and purge stale files is less than 600 seconds, or else this will always be scanning and purging.  If it takes 600 seconds to scan/purge, some serious rethinking should occur!

public class WXArchiveTimeOutManager extends Object implements Runnable {

    private String _archivedSessionsPath; // directory
    private long _sessionArchiveTimeOut; // milliseconds
    private int _cleanupRequestCount;

    private static long DISK_CLEANUP_INTERVAL= 600 * 1000;

    public void _cleanupSessionArchives() {
        File aFileManager = new File(_archivedSessionsPath);
        String[] anArchiveFileNameArray = aFileManager.list();
        String anArchiveFileName = null;
        int count = anArchiveFileNameArray.length;
        for (int i=0; i < count; i++) {
            anArchiveFileName = anArchiveFileNameArray[i];
            String anArchiveFilePath = NSPathUtilities.stringByAppendingPathComponent(_archivedSessionsPath, anArchiveFileName);
            long aTimeStamp;
            File aPath = new File(anArchiveFilePath);
            aTimeStamp = aPath.lastModified();

            if (System.currentTimeMillis() - aTimeStamp >= _sessionArchiveTimeOut) {
                boolean didRemove = aPath.delete();
                if (!didRemove) {
                    NSLog.debug.appendln("<"+getClass().getName()+">: Unable to removed archive for session \""+anArchiveFileName+"\"");
                } else {
                	NSLog.debug.appendln("<"+getClass().getName()+">: Removed archive for session \""+anArchiveFileName+"\"");
                }
            } else {
            	NSLog.debug.appendln("<"+getClass().getName()+">: \""+anArchiveFileName+"\" hasn't yet become stale.");
            }
        }
    }

    public void run() {
        while(true) {
            synchronized(this) {
                while(_cleanupRequestCount == 0) { // wait for something to clean up...
                    try {
                        wait();
                    } catch(InterruptedException e) {}
                }
                _cleanupRequestCount -= 1;
            }
            _cleanupSessionArchives();
        }
    }

    public synchronized void handleSessionArchiveTimer() {
        // ** This is called form the main thread by the timer created in -init...
        _cleanupRequestCount += 1;
        notify();
    }

    private long _initSessionArchiveTimeOut() {
        String aTimeOutMillisString = System.getProperty("WOSessionArchiveTimeOut");
        // ** Default is 1 day
        long aTimeOutMillis = 24 * 60 * 60*1000;
        if (aTimeOutMillisString!=null) {
            aTimeOutMillis = Long.parseLong(aTimeOutMillisString)*1000;
        }
        return aTimeOutMillis;
    }

    private void _initTimer()  {
        String aCleanupPeriodMillisString = System.getProperty("WOSessionArchiveCleanupInterval");

        // ** Default is 1 day
        long aCleanupPeriodMillis = DISK_CLEANUP_INTERVAL;
        if (aCleanupPeriodMillisString!=null) {
            aCleanupPeriodMillis = Long.parseLong(aCleanupPeriodMillisString)*1000;
        }

        WOApplication app = WOApplication.application();
        //TODO: switch this to a java.util.Timer
        WOTimer aTimer = new WOTimer(aCleanupPeriodMillis, this, "handleSessionArchiveTimer", null, null, true);

        synchronized(app) {
            aTimer.schedule();
        }
        NSLog.debug.appendln("<"+getClass().getName()+">: Archived sessions ("+_archivedSessionsPath+") will be scanned for stale sessions every "+(aCleanupPeriodMillis/1000)+" seconds.  (A stale session is > "+(_sessionArchiveTimeOut/1000)+" seconds old.)");
    }

    public WXArchiveTimeOutManager(String anArchivedSessionsPath) {
        super();
        _archivedSessionsPath = anArchivedSessionsPath;
        _sessionArchiveTimeOut = _initSessionArchiveTimeOut();

        try {
            Thread t = new Thread(this);
            t.start();
        } catch (IllegalThreadStateException localException) {
            throw new IllegalThreadStateException ("<"+getClass().getName()+"> Exception occured while creating Disk Cleaner Thread: "+localException.toString());
        }

        _initTimer();
    }
}
