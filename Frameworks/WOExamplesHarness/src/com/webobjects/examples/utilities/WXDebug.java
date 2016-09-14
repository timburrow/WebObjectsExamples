/*
 WXDebug.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

/**
WXDebug class provides a convenient way to generate reams of debugging information from your application. Instead of using a logging method like System.out.println, you might use the class method:
<pre>
WXDebug.println(1, "foo");
</pre>
Everything is set to a reasonable default out of the box (so to speak). If you want more control over your debugging output, there are methods for setting the debug threshold (an integer which your passed in level must be greater than or equal to in order for output to be generated). <p>

Depending on the ``mode'' that WXDebug is in, you get different messages printed. Given the above statement  <code>WXDebug.println(1, "foo");</code> called from your Session's awake method, here's what you'll see printed for each mode:

 <dl>
 <dt><b>TERSE</b>:
 <dd>foo

 <dt><b>NSLOG</b>:
 <dd>Mar 02 10:26:33 javaApp[395] foo

 <dt><b>VERBOSE</b>:
 <dd>Session.awake - foo

 <dt><b>PONTIFICAL</b>:
 <dd>Mar 02 10:26:33 javaApp[395] - Session.awake - foo
 </dl>

 Usually you develop in <b>PONTIFICAL</b> mode and deploy in <b>TERSE</b> mode with a class <code>debugLevel</code> of 1.<p>

 I also like to set some general numbering standards for my debugging output. I (usually) use the following guidelines for debugging levels:
<pre>
  1      General application logging
  2 - 10 Controller level debugging in action methods
 11 - 15 Controller level debugging in setters, getters, takeValuesFromRequest,
         invokeAction, and appendToResponse
 16 - 25 Framework level debugging stuff
 25 - up Messages that I'm going to want to see when I'm totally lost in a bug
         and grasping at straws. One step away from tossing my workstation out
         the window.
 </pre>
 Note also that the ``mode'' is set to <b>PONTIFICAL</b> by default. It is  recommended that you set the mode to <b>NSLOG</b> or <b>TERSE</b> when you deploy your app. <p>

 The <b>PONTIFICAL</b> and <b>NSLOG</b> modes print out the date, name of the app ("javaApp" by default) and a "pseudo" PID. This is merely to allow  you to discern which app is printing what if you are running multiple instances of an app and funneling their log output to a single file. <p>

 The <b>PONTIFICAL</b> and <b>VERBOSE</b> modes also print out the Class and  method where WXDebug.println was invoked. While this was a real  piece of cake in Objective C, thanks to the lack of a true  preprocessor, it's a real pain in Java. In order to get the class  and method, we need to generate a stack trace and muck through it  frame by frame to find out who our caller is. <p>

By default, the class prints to the STDERR  outputstream (System.err), but you can set it to STDOUT or a file or whatever you want. Just give it a printstream. Go nuts.

  Added support for tags and direct support for exception printing. println( int, string ) will continue to function as above.  However, new support for tags has been introducted.  WXDebug now maintains a list of tags in addition to a current debug level.

  If the tagList is empty, WXDebug funcitons as above except if a tag is provided with a debug message, then it is also printed with the message.  If no tag is provided then the message is printed as described above.

  If the tagList is not empty and a tag is provided, then if the tag is in the list the message is printed, but if it is not in the list then it is not printed.

  Lastly, if there is a tag list, but there is no tag provided with a message, then the message is still printed.

  The debug level is evaluated before the tagList is.

    DebugLevel = 25
    Mode = TERSE
    TagList = { "fitz" }

println( 30, "fitz", "this is a test that you won't see" );
println( 20, "fitz", "now you see me." );
println( 20, "bob", "you don't see me." );
println( 20, "this is the message, not the tag" );

Example:

<dl>
<dd>now you see me.
<dd>this is the message, not the tag
</dl>
*/
package com.webobjects.examples.utilities;

import java.io.CharArrayWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import com.webobjects.appserver.WOApplication;
import com.webobjects.foundation.NSMutableDictionary;


public class WXDebug extends Object {

    public static final int TERSE = 0;
    public static final int NSLOG = 1;
    public static final int VERBOSE = 2;
    public static final int PONTIFICAL = 3;

    static String _applicationName = new String( (WOApplication.application()).name() );
    static int _debugLevel         = 1;
    static int _pseudoPID          = WXDebug._genPseudoPID();
    static int _mode               = PONTIFICAL;
    static PrintStream out         = System.err;
    static NSMutableDictionary tagList = null;

    /**
     * This is the method that you'll use to log anything with the
     * WXDebug class. Just make sure that the appDebugLevel is lower
     * than WXDebug.debugLevel if you want the message to print out.
     *
     * @param int appDebugLevel Prints out the message if the
     * application's debug level is greater than or equal to this number
     * @param String message The message that you would like to print.
     * @see #setDebugLevel
     * @see #setMode
     *
     */
    public static void println ( int appDebugLevel, String message ) {
        println( appDebugLevel, null, message, 4 );
    }

    public static void println ( int appDebugLevel, String theTag, String message ) {
        println( appDebugLevel, theTag, message, 4 );
    }

    private static void println ( int appDebugLevel, String theTag, String message, int frame ) {

        String formattedTag = "";

        if (appDebugLevel > WXDebug.debugLevel())
            return;

        //  if there is a tag list, and there is a tag, and the tag isn't listed then return.
        //
        if( ( tagList != null ) && ( theTag != null ) && !isTagSet( theTag ) )
            return;

        //  If there is a tag format it.
        //
        if( theTag != null )
            formattedTag = "<" + theTag + "> ";

        //  Chop excessively long messages because Java Strings or PrintStreams suck
        //
        //  I'm not positive if this is an issue w/ Stirngs or PrintStreams
        //  But for loging, 3,072 characters should be enough for a single message
        //  -Bob Frank 2/26/01
        //
        String aMessage = message;
        if (aMessage.length() > 3 * 1024 )
            aMessage = aMessage.substring(0, 3 * 1024 - 5) + " ...";

        switch(_mode) {
            case WXDebug.TERSE:
                WXDebug.out.println( formattedTag + aMessage );
                break;

            case WXDebug.NSLOG:
                WXDebug.out.println( WXDebug._formattedDate() + " " +
                                     _applicationName +
                                     "[" + _pseudoPID + "] " +
                                     formattedTag +
                                     aMessage );
                break;

            case WXDebug.VERBOSE:
                WXDebug.out.println( formattedTag + WXDebug._callerDescription(frame) + " " +
                                     aMessage );
                break;

            case WXDebug.PONTIFICAL:
                WXDebug.out.println( WXDebug._formattedDate() + " " +
                                     _applicationName +
                                     "[" + _pseudoPID + "] - " +
                                     formattedTag +
                                     WXDebug._callerDescription(frame) + " - " +
                                     aMessage );
        }
    }

    public static void println( int appDebugLevel, String message, Exception e ) {
        println( appDebugLevel, null, message, e );
    }

    public static void println( int appDebugLevel, String tag, String message, Exception e ) {
        if (appDebugLevel > WXDebug._debugLevel)
            return;

        //  if there is a tag list, and there is a tag, and the tag isn't listed then return.
        //
        if( ( tagList != null ) && ( tag != null ) && !isTagSet( tag ) )
            return;

        println( appDebugLevel, tag, message );
        println( appDebugLevel, tag, e.getMessage() );
        e.printStackTrace( out() );
    }

    static String _callerDescription(int frm) {
        String stackTrace = null;
        Throwable except = new Throwable("debug");
        CharArrayWriter cw = new CharArrayWriter();
        PrintWriter pw = new PrintWriter(cw);
        String frame = null;

        except.printStackTrace(pw);
        stackTrace = cw.toString();

        // String scrubbing. Muck through stackTrace for our frame
        if (stackTrace != null) {
            int i, index;
            int whichFrame = frm; // The frame that we want to get Class.method from.
            String tmp = null;

            // Weird lossage here. After touching EOControl, the
            // stackTrace has an extra frame at the beginning. See
            // comment at bottom of this class for an example
            if (stackTrace.indexOf("at java.lang.Throwable") > -1)
                whichFrame++;

            tmp = stackTrace;
            for (i = 0; i < whichFrame; i++) {
                index = tmp.indexOf("at ");
                frame = tmp.substring(index + 3);
                tmp = frame;
                //System.out.println("i:"+i+" tmp: "+tmp);
            }
            if (frame != null) {
                index = frame.indexOf("(");
                frame = tmp.substring(0, index);
            }
       }
        return frame;
    }

    static String _formattedDate() {
        // Format the current time the way I like it.
        SimpleDateFormat formatter = new SimpleDateFormat ("MMM dd hh:mm:ss");
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    static int _genPseudoPID() {
        BigInteger theNumber = null;

        try {
            theNumber = new BigInteger(10, new Random());
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        if (theNumber != null) {
            return theNumber.intValue();
        }
        return 0;
    }

    /* Setters and Getters ***********************************************/

    /**
     * Returns the application name that is printed in some of debugging
     * modes. Default is "javaApp".
     */
    public static String applicationName() {
        return _applicationName;
    }

    /**
     * Sets the application name that is printed in some of debugging
     * modes. Application.init is a good place for this. (Note that
     * WOChiApplication does this for you!)
     */
    public static void setApplicationName(String value) {
        _applicationName = value;
    }

    /**
     * The application-wide debug level. Calls to WXDebug.println
     * with the first parameter less than this will be printed.
     */
    public static int debugLevel() {
        return _debugLevel;
    }

    /**
     * Sets the application-wide debug level.
     */
    public static void setDebugLevel(int value) {
        _debugLevel = value;
    }

    /**
     * The debugging mode that you are in. Choices are <b>TERSE</b>, <b>NSLOG</b>,
     * <b>VERBOSE</b>, and <b>PONTIFICAL</b>. The mode determines the format that
     * your messages are printed in. It is recommended that you do not
     * use <b>VERBOSE</b> or <b>PONTIFICAL</b> in Deployment for performance
     * reasons. Default is <b>PONTIFICAL</b>.
     */
    public static int mode() {
        return _mode;
    }

    /**
     * Sets the debugging mode which determines the format that your
     * messages are printed in. Choices are <b>TERSE</b>, <b>NSLOG</b>, <b>VERBOSE</b>, and
     * <b>PONTIFICAL</b>. Default is <b>PONTIFICAL</b>.
     */
    public static void setMode(int value) {
        _mode = value;
    }

    /**
     * The printstream where you would like your output to
     * be sent. Default is System.err.
     */
    public static PrintStream out() {
        return out;
    }

    /**
     * Sets the printstream where you would like your output to be
     *  sent. Default is System.err, but you can set it to any
     *  Printstream that you like, including System.out.
     */
    public static void setOut(PrintStream value) {
        out = value;
    }

    /**
     * Simple accessor method to test if a tag is set.
     */
    public static boolean isTagSet( String aTag ) {
        return tagList != null && tagList.objectForKey( aTag ) != null;
    }

    /**
     * Creates the tagList if it doesn't exist and adds the tag.
     */
    public static void addTag( String aTag ) {
        if( tagList == null )
            tagList = new NSMutableDictionary();

        tagList.setObjectForKey( aTag, aTag );
    }

    /**
     * Removes the tag if its in the tagList
     */
    public static void removeTag( String aTag ) {
        if( tagList != null && !isTagSet( aTag ) )
            tagList.removeObjectForKey( aTag );
    }

    /**
     * Resets the tagList and removes all tags from it.
     */
    public static void resetTagList() {
        tagList = null;
    }
}
