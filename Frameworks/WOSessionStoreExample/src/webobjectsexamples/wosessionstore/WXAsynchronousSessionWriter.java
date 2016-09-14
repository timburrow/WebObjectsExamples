/*
 WXAsynchronousSessionWriter.java
 [WOSessionStoreExampleX Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.wosessionstore;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOSession;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;

public class WXAsynchronousSessionWriter extends WXSessionWriter implements Runnable {
	NSMutableArray _writeQueue;

	private void _writeSessionArchiveToDisk(byte[] sessionData, String aSessionId) throws IOException {
		String aSessionFilePath = pathForSessionID(aSessionId);
		try {
			FileOutputStream fout = new FileOutputStream(aSessionFilePath);
			fout.write(sessionData);
			NSLog.debug.appendln( "<"+getClass().getName()+"> Wrote session "+aSessionId+" to disk.");
		} catch (IOException e) {
			if (WOApplication.application().isDebuggingEnabled()) {
				e.printStackTrace();
			}
			throw new IOException("<"+getClass().getName()+">: could not save session data to archive file \""+aSessionFilePath+"\".");
		}
	}

	private void writeQueuedArchivesToDisk() throws IOException {
		Object[] aSessionIdArchiveDataPair = null;
		byte[] aSessionArchiveData = null;
		String aSessionId = null;

		// ** No exception handling required here because there's no way we can get an exception on any of the code between the locks.
		synchronized(this) {
			while (_writeQueue.count() == 0) {
				try {
					wait();
				} catch(InterruptedException e) {}
			}
			// ** must retain/autorelease here so aSessionIdArchiveDataPair can survive past the point where we remove it from the _writeQueue
			aSessionIdArchiveDataPair = (Object[])_writeQueue.objectAtIndex(0);
			_writeQueue.removeObjectAtIndex(0);
		}

		aSessionId = (String)aSessionIdArchiveDataPair[0];
		aSessionArchiveData = (byte[])aSessionIdArchiveDataPair[1];
		_writeSessionArchiveToDisk(aSessionArchiveData, aSessionId);
	}

	public void run() {
		while (true) {
			try {
				writeQueuedArchivesToDisk();
			} catch (Exception e) {
				NSLog.err.appendln( "<"+getClass().getName()+"> Exception occured in session writer thread: "+e);
				if (WOApplication.application().isDebuggingEnabled()) {
					e.printStackTrace();
				}
			}
		}
	}

	public WXAsynchronousSessionWriter() throws IOException {
		super();
		_writeQueue = new NSMutableArray();

		try {
			Thread t = new Thread(this);
			t.start();
		} catch (IllegalThreadStateException localException) {
			throw new IllegalThreadStateException ("<"+getClass().getName()+"> Exception occured while creating Disk Cleaner Thread: "+localException.toString());

		}
	}

	public void writeSessionToDisk( WOSession aSession ) throws IOException {
		String aSessionId = aSession.sessionID();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		Object[] aSessionIdArchiveDataPair;

		try {
			out = new ObjectOutputStream(bout);
			out.writeObject( aSession );
			out.close();
			NSLog.debug.appendln( "<"+getClass().getName()+"> Archived session "+aSessionId+" in memory.");
		} catch (IOException e) {
			if (WOApplication.application().isDebuggingEnabled()) {
				e.printStackTrace();
			}
			throw new IOException("<"+getClass().getName()+">: could not archive session \""+aSessionId+"\".");
		}

		if (bout!=null) {
			byte[] aSessionArchiveData = bout.toByteArray();
			aSessionIdArchiveDataPair = new Object[2];
			aSessionIdArchiveDataPair[0] = aSessionId;
			aSessionIdArchiveDataPair[1] = aSessionArchiveData;

			// ** No exception handling required here because there's no way we can get an exception on any of the code between the locks.
			synchronized(this) {
				_writeQueue.addObject(aSessionIdArchiveDataPair);
				notify();
			}
		}
	}
}
