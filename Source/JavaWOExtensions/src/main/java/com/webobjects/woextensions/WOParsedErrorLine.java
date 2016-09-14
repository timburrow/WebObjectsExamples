/*
 * WOParsedErrorLine.java
 * [JavaWOExtensions Project]
 *
 * ©Copyright 2001 - 2007 Apple, Inc. All rights reserved.
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

package com.webobjects.woextensions;

/* WOParsedErrorLine.java created on Thu 29-Apr-1999 */

public class WOParsedErrorLine extends Object {
    private static final String DEFAULT_PACKAGE = "Default Package";
    
    protected StackTraceElement _element;
    protected boolean _ignorePackage; // if true, then it will not be possible to display an hyperlink

    public WOParsedErrorLine(StackTraceElement element) {
        _element = element;
        // By default we handle all packages
        _ignorePackage = false;
    }

    public String packageName() {
        String className = _element.getClassName();
        if(className != null) {
            int index = className.lastIndexOf('.');
            if(index == -1) {
                return DEFAULT_PACKAGE;
            } else {
                return className.substring(0,index);
            }
        }
        return "";
    }

    public String className() {
        String className = _element.getClassName();
        if(className != null) {
            int index = className.lastIndexOf('.');
            if(index == -1) {
                return className;
            } else {
                return className.substring(index+1);
            }
        }
        return "";
    }

    public String packageClassPath() {
        return _element.getClassName();
    }

    public String methodName() {
        return _element.getMethodName();
    }

    public boolean isDisable() {
        return (line() < 0 || _ignorePackage);
    }

    protected void setIgnorePackage(boolean yn) { _ignorePackage = yn; }
    
    public String fileName() {
        String fileName = _element.getFileName();
        if(fileName == null) {
            fileName = className();
            if(fileName != null) {
                fileName = fileName+".java";
            } else {
                fileName = "Unknown Source";
            }
        }
        return fileName;
    }

    public String lineNumber() {
        int line = _element.getLineNumber();
        if (line >= 0) {
            return String.valueOf(line);
        }
        return "NA";
    }

    public int line() {
        return _element.getLineNumber();
    }
    
    @Override
	public String toString() {
	    int line = line();
        String lineInfo = (line >= 0) ? String.valueOf(line) : "No line info due to compiled code";
        String fileInfo = (line >= 0) ? _element.getFileName() : "Compiled code no file info";
        String packageName = packageName();
        
        if(DEFAULT_PACKAGE.equals(packageName)) {
            return "class : " + className() + ": " + methodName() + " in file :" + fileInfo + " - line :" + lineInfo;
        } else {
            return "In package : " + packageName + ", class : " + className() + " method : " + methodName() + " in file :" + fileInfo + " - line :" + lineInfo;
        }
    }
}
