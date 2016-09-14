/*
 * WOExceptionParser.java [JavaWOExtensions Project] ©Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non- exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple
 * Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived
 * from the Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or
 * by other works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.webobjects.woextensions;

/**
 * WOExceptionParser parse the stack trace of a Java exception (in fact the parse is really made in WOParsedErrorLine). The stack trace is set in an NSArray that will be used in the UI in the exception page.
 */
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;

import com.webobjects.appserver.WOApplication;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSBundle;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSLog;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSPropertyListSerialization;

public class WOExceptionParser extends Object {
    private static NSArray<String> ignoredPackages = null;

    public static NSArray<WOParsedException> parseException(Throwable exception) {
        NSMutableArray<WOParsedException> exceptions = new NSMutableArray<WOParsedException>();
        _parseException(exception, null, exceptions);
        return exceptions;
    }
    
    @SuppressWarnings("unchecked")
    private static NSArray<String> _ignoredPackages() {
        // Build it once
        if(ignoredPackages == null) {
            NSBundle bundle;
            URL pathURL;
            NSArray<String> tmpArray;
            NSDictionary dic;
            NSMutableArray<NSBundle> allBundles = NSBundle.frameworkBundles().mutableClone();
            NSMutableArray<String> ignored = new NSMutableArray<String>();

            for (Iterator<NSBundle> iterator = allBundles.iterator(); iterator.hasNext();) {
                bundle = iterator.next();
                pathURL = WOApplication.application().resourceManager().pathURLForResourceNamed("WOIgnoredPackage.plist", bundle.name(), null);
                if (pathURL != null) {
                    dic = (NSDictionary) NSPropertyListSerialization.propertyListWithPathURL(pathURL);
                    tmpArray = (NSArray) dic.objectForKey("ignoredPackages");
                    if (tmpArray != null && tmpArray.count() > 0) {
                        ignored.addObjectsFromArray(tmpArray);
                    }
                }
            }
            
            ignoredPackages = ignored;
        }
        return ignoredPackages;
    }

    private static void _verifyPackageForLine(WOParsedErrorLine line) {
        NSArray<String>packages = _ignoredPackages();
        String linePackageName = line.packageName();

        for (String ignoredPackageName : packages) {
            if (linePackageName.startsWith(ignoredPackageName)) {
                line.setIgnorePackage(true);
                break;
            }
        }
    }

    
    private static void _parseException(Throwable exception, Throwable cause, NSMutableArray<WOParsedException> exceptions) {
        try {
            WOParsedException parsedException;
            StackTraceElement[] elements = exception.getStackTrace();
            int maxFrame = elements.length - 1;
            int commonFrames = 0;
            
            if(cause != null) {
                parsedException = new WOParsedException("Caused by: "+exception.toString());
                StackTraceElement[] causedElements = cause.getStackTrace();
                int currentCausedFrame = causedElements.length - 1;
                
                while(maxFrame >= 0 && currentCausedFrame >=0 && elements[maxFrame].equals(causedElements[currentCausedFrame])) {
                    maxFrame--;
                    currentCausedFrame--;
                    commonFrames++;
                }
            } else {
                parsedException = new WOParsedException(exception.toString());
            }

            NSMutableArray<WOParsedErrorLine> currentExceptionFrames = new NSMutableArray<WOParsedErrorLine>();
            for (int i=0; i <= maxFrame; i++) {
                WOParsedErrorLine aLine = new WOParsedErrorLine(elements[i]);
                _verifyPackageForLine(aLine);
                currentExceptionFrames.addObject(aLine);
            }
            parsedException.setFrames(currentExceptionFrames);
            
            if(commonFrames > 0) {
                parsedException.setCommonFrames(commonFrames);
            }

            exceptions.add(parsedException);
            
            Throwable previousCause = exception.getCause();
            if(previousCause != null) {
                _parseException(previousCause, exception, exceptions);
            }
        } catch (Throwable e) {
            NSLog.err.appendln("WOExceptionParser - exception collecting backtrace data " + e + " - Empty backtrace.");
            NSLog.err.appendln(e);
        }
    }
}
