/*
 WXFileUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

import com.webobjects.appserver.WOApplication;
import com.webobjects.appserver.WOResourceManager;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSData;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSMutableDictionary;
import com.webobjects.foundation.NSPropertyListSerialization;
import com.webobjects.foundation.NSTimestamp;

/**
WXFileUtilities includes miscellaneous methods useful when accessing files associated with WebObjects projects as well as specific kinds of commonly used files (such as plists).
*/
public class WXFileUtilities {


    /**
     * Loads properties from a text file into the Java System properties.
     * Note that this is NOT an Apple "plist" file, but a file with simple
     * properties, like "subliminal.message = WebObjects rules!" separated
     * by carriage returns.
     *
     * @param fileName		String, path to the file
     */

    public static void loadPropertiesFromPath( String fileName ) throws Exception {

        FileInputStream propertyFileInput = new FileInputStream( fileName );
        Properties prop = new Properties( System.getProperties() );
        prop.load( propertyFileInput );
        propertyFileInput.close();

        System.setProperties( prop );
        WXDebug.println( 1, "Properties: ");
        System.getProperties().list( System.out );
    }


    /**
     * Gives you back an object if you give it a java File
     * object. The object it returns is <i>usually</i> an
     * NSDictionary, but can also be an NSArray or a String.
     * The method uses NSPropertyListSerialization and assumes
     * the file is in NSFoundation plist format.
     */
    public static Object objectFromPListFile(File file) {
        Object obj = null;
        try {
            obj = NSPropertyListSerialization.propertyListFromString(stringFromFile(file));
        }
        catch(IOException ioexception) {
            WXDebug.println(1, ioexception.toString());
        }
        return obj;
    }


    /**
     * Gives you back an object, like objectFromPListFile, but does so from a
     * resource for the application.  Uses the WOResourceManager
     */

    public static Object objectFromPListResourceFile( String filename, String framework ) {

        WOResourceManager rm = WOApplication.application().resourceManager();
        URL resourcePathURL = rm.pathURLForResourceNamed( filename, framework, null );
        return WXFileUtilities.objectFromPListFile( new File( resourcePathURL.getPath() ) );
    }


    /**
     * reads data from content of a java File object.
     *
     * @param f       java File object to read from
     * @return        data from content of File
     */
    static public NSData dataFromFile(File f) throws IOException {
        if (f==null)
            throw new IOException("null file");
        int size=(int) f.length();
        FileInputStream fis=new FileInputStream(f);
        byte [] data = new byte[size];
        int bytesRead=0;
        while (bytesRead<size)
            bytesRead+=fis.read(data,bytesRead,size-bytesRead);
        fis.close();
        return new NSData(data);
    }

    /**
     * writes NSData as content of a java File object to disk.
     *
     * @param s       NSData to write to disk
     * @param f       java File object to write to
     */
    static public void writeDataToFile(NSData s, File f) throws IOException {
        if (f==null)
            throw new IOException("null file");
        int size=s.length();
        FileOutputStream fos=new FileOutputStream(f);
        byte [] data = s.bytes(0,size);
//        int bytesWritten=0;
//        while (bytesWritten < size)
//        	bytesWritten += fos.write(data, bytesWritten,size - bytesWritten);
        fos.write(data);
        fos.close();

    }



    /**
     * reads String from content of a java File object.
     *
     * @param f       java File object to read from
     * @return        String from content of File
     */
    static public String stringFromFile(File f) throws IOException {
        if (f==null)
            throw new IOException("null file");
        int size=(int) f.length();
        FileInputStream fis=new FileInputStream(f);
        byte [] data = new byte[size];
        int bytesRead=0;
        while (bytesRead<size)
            bytesRead+=fis.read(data,bytesRead,size-bytesRead);
        fis.close();
        return new String(data);
    }

    /**
     * writes String as content of a java File object to disk.
     *
     * @param s       String to write to disk
     * @param f       java File object to write to
     */
    static public void writeStringToFile(String s, File f) throws IOException {
        if (f==null)
            throw new IOException("null file");
//        int size=(int) s.length();
        FileOutputStream fos=new FileOutputStream(f);
        byte [] data = s.getBytes();
//        int bytesWritten=0;
        //while (bytesWritten < size)
        //    bytesWritten += fos.write(data, bytesWritten,size - bytesWritten);
        fos.write(data);
        fos.close();

    }

    /**
     * reads String from file at the passed in path.
     *
     * @param s       String to write to disk
     * @param path    destination path including filename
     */
    static public String stringFromPath(String path) throws IOException {
        File file = new File(path);
        return stringFromFile(file);
    }

    /**
     * writes String as content of a file to passed in path.
     *
     * @param s       String to write to disk
     * @param path    destination path including filename
     */
    static public void writeStringToPath(String s, String path) throws IOException {
        File file = new File(path);
        writeStringToFile (s, file);
    }

    /**
     * loads file from disk and returns its contents as a string. Same as
     * stringFromPath but does not throw an exception.
     *
     * @param path    path including filename
     * @return        contents of the file at path as a String
     */
    public static String loadFileAtPath(String path){
        String contentString = null;
        try{
            contentString = stringFromPath(path);
        }catch(IOException e){
            WXDebug.println(1, "WXFileUtilities: loadConfigFileAtPath: failed for path:"+path);
        }
        return contentString;
    }





    /**
     * Assumes that the file passed in is a directory and attempts to
     * loop over all its members, deleting each one.
     *
     * @param f          File object representing dir to delete
     * @param handler    handler object to deal with errors
     * @param userInfo   dict used to feed error data to handler object
     * @return           <code>true</code> if all the members of the folder could be
     *                   removed.
     */
    private static boolean removeDirectoryContentsWithHandlerUserInfo(File f, WXFileUtilitiesHandler handler, NSMutableDictionary userInfo){
        String files[] = f.list();
        Enumeration enumerator = new NSArray<String>(files).objectEnumerator();
        boolean success = true;
        while (enumerator.hasMoreElements()) {
            String file = (String)enumerator.nextElement();
            String parentPath = f.getAbsolutePath();
            if(parentPath != null) file = parentPath+File.separator+file;
            if(!removeFileAtPathHandlerUserInfo(file, handler, userInfo)){
                success = false;
            }
        }
        return success;
    }


    /**
     * Primitive file removal method. This method may get called recursively.
     *
     * @param filePath   path representing the File to delete
     * @param handler    handler object to deal with errors
     * @param userInfo   dict used to feed error data to handler object
     * @return           <code>true</code> if all the file was removed
     *                   (or not but the handler approved anyway).
     */
    private static boolean removeFileAtPathHandlerUserInfo(String filePath, WXFileUtilitiesHandler handler, NSMutableDictionary userInfo){
        File f = new File(filePath);
        boolean success = false;
        if(f.exists()){
            if(f.canWrite()){
                boolean dirRemovalSuccess = true;
                if(f.isDirectory()){
                    dirRemovalSuccess = removeDirectoryContentsWithHandlerUserInfo(f, handler, userInfo);
                    if(!dirRemovalSuccess){
                        userInfo.setObjectForKey("An error occurred deleting contents of folder", "Error");
                        userInfo.setObjectForKey(filePath, "Path");
                    }
                }
                if(dirRemovalSuccess){
                    if(handler != null){
                        handler.willProcessPath(f, filePath);
                    }
                    success = f.delete();
                    if(!success){
                        userInfo.setObjectForKey("An error occurred deleting file", "Error");
                        userInfo.setObjectForKey(filePath, "Path");
                    }
                }
            }else{
                userInfo.setObjectForKey("File does not exist", "Error");
                userInfo.setObjectForKey(filePath, "Path");
            }
        }else{
            success = true;
        }
        if(!success && handler != null){
            return handler.shouldProceedAfterError(f, userInfo);
        }
        return success;
    }




    /**
     * Copies the a directory assuming from and to Files are the
     * exact absolute paths of two directories.
     * This method is recursively called. First it creates the directory
     * at toFile. Then copies to the toFile folder, all files found in fromFile.
     */
    private static boolean copyDirectoryContentsWithHandlerUserInfo(File fromFile, File toFile, WXFileUtilitiesHandler handler, NSMutableDictionary userInfo){
        boolean success = true;
        if(handler != null){
            handler.willProcessPath(fromFile, toFile.getAbsolutePath());
        }
        success = toFile.mkdir();
        if(!success){
            userInfo.setObjectForKey("An error occurred copying file when creating folder at: "+toFile.getAbsolutePath(), "Error");
            userInfo.setObjectForKey(fromFile.getAbsolutePath(), "Path");
        }else{
            String files[] = fromFile.list();
            Enumeration<String> enumerator = new NSArray<String>(files).objectEnumerator();
            while (enumerator.hasMoreElements()) {
                String nextFromFile = enumerator.nextElement();
                String nextToFile = ""+nextFromFile;
                String parentFromPath = fromFile.getAbsolutePath();
                String parentToPath = toFile.getAbsolutePath();
                if(parentFromPath != null) nextFromFile = parentFromPath + File.separator + nextFromFile;
                if(parentToPath != null) nextToFile = parentToPath + File.separator + nextToFile;
                if(!copyPathToPathHandlerUserInfo(nextFromFile, nextToFile, handler, userInfo)){
                    success = false;
                }
            }
        }
        return success;
    }


    /**
     * Primitive file copying method.
     * Assumes that the paths are both the exact paths we wish to write from and to.
     * Method reads the file a small chunk of bytes at a time and then writes out
     * that chunk. Hence, only a small number of bytes are in memory at any given
     * time. Thus you don't need several MB of memory to copy several MB of files.
     * <p>
     * This code was based on an example in the book <i>Java Examples</i> by David Flanagan.
     */
    private static boolean copyFileWithHandlerUserInfo(File fromFile, File toFile, WXFileUtilitiesHandler handler, NSMutableDictionary userInfo){
        boolean success = true;
        FileInputStream from = null;
        FileOutputStream to = null;
        try{
            byte[] buffer = new byte[4096];
            from = new FileInputStream(fromFile);
            to = new FileOutputStream(toFile);
            int bytes_read;
            // Read chunk of bytes into buffer, then write them out, looping until
            // we reach the end of the file which is when read() returns -1.
            while((bytes_read = from.read(buffer)) != -1){
                to.write(buffer, 0, bytes_read);
            }
        }catch(FileNotFoundException e){
            success = false;
            userInfo.setObjectForKey("FileNotFoundException occurred copying file bytes", "Error");
            userInfo.setObjectForKey(fromFile.getAbsolutePath(), "Path");
        }catch(IOException e){
            success = false;
            userInfo.setObjectForKey("IOException occurred copying file bytes", "Error");
            userInfo.setObjectForKey(fromFile.getAbsolutePath(), "Path");
        }finally{
            // Always close stream even if exceptions thrown.
            if(from != null){
                try {
                    from.close();
                }catch(IOException e){
                    // The error has already been reported
                }
            }
            if(to != null){
                try {
                    to.close();
                }catch(IOException e){
                    // The error has already been reported
                }
            }
        }
        return success;
    }

    /**
     * This method is recursively called and represents of the meat of the file
     * copying code. The method performs a number of tests to make sure everything
     * is in order.
     */
    private static boolean copyPathToPathHandlerUserInfo(String fromPath, String toPath, WXFileUtilitiesHandler handler, NSMutableDictionary userInfo){
        File fromFile = new File(fromPath);
        File toFile = new File(toPath);
        boolean success = false;
        // First make sure the source file exists. If not abort (exit with an error).
        if(fromFile.exists()){
            // Then make sure we can read from it. If not abort.
            if(fromFile.canRead()){
                // Now that the source is OK, we check the destination. I use a flag
                // set initially to true. If any test fails, the flag can be switched
                // to false. If proceed flag is false, we abort.
                boolean proceed = true;
                if(toFile.isDirectory()){
                    // If the destination is a directory, use the source filename as
                    // the destination filename within the destination directory.
                    // Note that if we can tell the file is a directory, we know it
                    // exists.
                    toFile = new File(toFile, fromFile.getName());
                }

                // Now we check if the toFile exists and if so if we can write to
                // it which means overwrite it. If the file doesn't exist, we see if
                // its parent directory exists, if it is indeed a directory, and if
                // we can write to it; if not to any of them, we abort.
                if(toFile.exists()){
                    if(!toFile.canWrite()){
                        proceed = false;
                        userInfo.setObjectForKey("Cannot write to destination", "Error");
                        userInfo.setObjectForKey(toPath, "Path");
                    }
                }else{
                    String parent = toFile.getParent();
                    if(parent == null){
                        proceed = false;
                        userInfo.setObjectForKey("No parent available and destination does not exist", "Error");
                        userInfo.setObjectForKey(toPath, "Path");
                    }else{
                        File dir = new File(parent);
                        if(!dir.exists()){
                            proceed = false;
                            userInfo.setObjectForKey("Parent of destination does not exist", "Error");
                            userInfo.setObjectForKey(toPath, "Path");
                        }else if(dir.isFile()){
                            proceed = false;
                            userInfo.setObjectForKey("Parent of destination is not a directory", "Error");
                            userInfo.setObjectForKey(toPath, "Path");
                        }else if(!dir.canWrite()){
                            proceed = false;
                            userInfo.setObjectForKey("Parent of destination is not writable", "Error");
                            userInfo.setObjectForKey(toPath, "Path");
                        }
                    }
                }

                // If everything is in order we can proceed.
                if(proceed){
                    // If the fromFile is a folder, we call the directory copying
                    // primitive to copy the directory. If a file, we call the file
                    // copying primitive.
                    if(fromFile.isDirectory()){
                        success = copyDirectoryContentsWithHandlerUserInfo(fromFile, toFile, handler, userInfo);
                        if(!success){
                            userInfo.setObjectForKey("An error occurred copying contents of folder", "Error");
                            userInfo.setObjectForKey(fromPath, "Path");
                        }
                    }else{
                        if(handler != null){
                            handler.willProcessPath(fromFile, fromPath);
                        }
                        success = copyFileWithHandlerUserInfo(fromFile, toFile, handler, userInfo);
                        if(!success){
                            userInfo.setObjectForKey("An error occurred copying file", "Error");
                            userInfo.setObjectForKey(fromPath, "Path");
                        }
                    }
                }
            }else{
                userInfo.setObjectForKey("Cannot read from-file", "Error");
                userInfo.setObjectForKey(fromPath, "Path");
            }
        }else{
            userInfo.setObjectForKey("From-file does not exist", "Error");
            userInfo.setObjectForKey(fromPath, "Path");
        }
        if(!success && handler != null){
            return handler.shouldProceedAfterError(fromFile, userInfo);
        }
        return success;
    }

    /**
     * Deletes the file or directory (including, recusively, all subdirectories and
     * files in the directory) identified by path. If the removal operation is successful,
     * the method returns <code>true</code>. If the operation is not successful, but
     * the handler method <code>shouldProceedAfterError</code> returns <code>true</code>,
     * this method also returns <code>true</code>; otherwise it returns <code>false</code>.
     * <p>
     * The argument handler identifies an object that implements the WXFileUtilitiesHandler
     * interface which includes the callback message <code>shouldProceedAfterError</code>.
     * You can specify null for handler; if you specify null and an error occurs, it is like your
     * <code>shouldProceedAfterError</code> automatically returns <code>false</code>.
     * <p>
     * Since the removal of directory contents is so thorough and final, be careful when
     * using this method.
     *
     * @param filePath   path representing the File to delete
     * @param handler    handler object to deal with errors and other delegation
     * @return           <code>true</code> if all the file was removed
     *                   (or not but the handler approved anyway).
     * @see WXFileUtilitiesHandler
     */
    public static boolean removeFileAtPathHandler( String filePath, WXFileUtilitiesHandler handler ){
        boolean success = true;
        WXDebug.println(1, "WXFileUtilities: removeFileAtPathHandler:"+filePath);
        if(filePath != null){
            NSMutableDictionary userInfo = new NSMutableDictionary();
            success = removeFileAtPathHandlerUserInfo(filePath, handler, userInfo);
        }
        return success;
    }


    /**
     * Checks if the destination path resides within the source path. If the source
     * path is a directory and the destination is within this source path, then the
     * method returns true.
     */
    private static boolean destinationIsWithinSource(String fromPath, String toPath){
        boolean error = false;
        File f = new File(fromPath);
        if(f.isDirectory()){
            NSArray parts = NSArray.componentsSeparatedByString(toPath, fromPath);
            //WXDebug.println(1, "WXFileUtilities: destinationIsWithinSource: parts:"+parts);
            if(parts.count() > 1) error = true;
        }
        return error;
    }

    /**
     * Copies the file or directory (including, recusively, all subdirectories and
     * files in the directory) identified by the from-path to the to-path.
     * If the removal operation is successful,
     * the method returns <code>true</code>. If the operation is not successful, but
     * the handler method <code>shouldProceedAfterError</code> returns <code>true</code>,
     * this method also returns <code>true</code>; otherwise it returns <code>false</code>.
     * <p>
     * The argument handler identifies a delegate that implements the
     * WXFileUtilitiesHandler interface. You can specify null for the handler.
     * If you specify null and an error occurs, it is like the delegate method
     * <code>shouldProceedAfterError</code> automatically returns <code>false</code>.
     * <p>
     * The method does nothing if the source and destination are the same. It also does nothing
     * if the source is a folder and the destination lies within this source.
     * <p>
     * If you are copying files, and the destination is folder, a file with the same
     * name as the source is created within the destination folder. If the destination is
     * an existant (or non-existant filename with an existant parent folder),
     * the source file is copied with the same name as
     * the destination file (thus over-writing the destination file if present).
     * <p>
     * If you are copying folders, and the destination is a folder, a new
     * folder with the same name as the source is made under the destination. For example:
     * copying /foo/bar/source_dir to /dest_dir results in /dest_dir/source_dir. If the
     * destination exists and is a file instead of folder, the copy fails. If the destination
     * does not exist (but its parent folder does), the source folder is copied with
     * the same name as the named destination. For example: copying /foo/bar/source_dir
     * to a non-existant /dest_dir results in a new /dest_dir whose contents match
     * those of source_dir.
     *
     * @param fromPath   path representing the File to copy
     * @param toPath     path representing the destination of the File to copy
     * @param handler    handler object to deal with errors and other delegation
     * @return           <code>true</code> if all the file was removed
     *                   (or not but the handler approved anyway).
     * @see WXFileUtilitiesHandler
     */
    public static boolean copyPathToPathHandler(String fromPath, String toPath, WXFileUtilitiesHandler handler){
        boolean success = true;
        WXDebug.println(1, "WXFileUtilities: copyPathToPathHandler:"+fromPath);

        if(fromPath != null && toPath != null){
            if(fromPath.equals(toPath)){
                success = true;
            }else if(destinationIsWithinSource(fromPath, toPath)){
                WXDebug.println(1, "WXFileUtilities: copyPathToPathHandler: destinationIsWithinSource");
                success = false;
            }else{
                NSMutableDictionary userInfo = new NSMutableDictionary();
                success = copyPathToPathHandlerUserInfo(fromPath, toPath, handler, userInfo);
            }
        }
        return success;
    }


    /**
     * Moves the file or directory (including, recusively, all subdirectories and
     * files in the directory) identified by the from-path to the to-path.
     * If the removal operation is successful,
     * the method returns <code>true</code>. If the operation is not successful, but
     * the handler method <code>shouldProceedAfterError</code> returns <code>true</code>,
     * this method also returns <code>true</code>; otherwise it returns <code>false</code>.
     * <p>
     * The argument handler identifies a delegate that implements the
     * WXFileUtilitiesHandler interface. You can specify null for the handler.
     * If you specify null and an error occurs, it is like the delegate method
     * <code>shouldProceedAfterError</code> automatically returns <code>false</code>.
     * <p>
     * The method does nothing if the source and destination are the same. It also does nothing
     * if the source is a folder and the destination lies within this source. Finally
     * it does nothing if the source file cannot be written (deleted).
     * <p>
     * If you are moving files, and the destination is folder, a file with the same
     * name as the source is created within the destination folder. If the destination is
     * an existant (or non-existant filename with an existant parent folder),
     * the source file is copied with the same name as
     * the destination file (thus over-writing the destination file if present).
     * <p>
     * If you are moving folders, and the destination is a folder, a new
     * folder with the same name as the source is made under the destination. For example:
     * copying /foo/bar/source_dir to /dest_dir results in /dest_dir/source_dir. If the
     * destination exists and is a file instead of folder, the copy fails. If the destination
     * does not exist (but its parent folder does), the source folder is moved with
     * the same name as the named destination. For example: copying /foo/bar/source_dir
     * to a non-existant /dest_dir results in a new /dest_dir whose contents match
     * those of source_dir.
     * <p>
     * Deletion of the source does not begin at all if the initial copy was not successful.
     * This means that it is possible for a copy of the source to occur and still leave
     * all of the source intact.  This could happen if the permission on the source
     * were to shift after the move call began.
     *
     * @param fromPath   path representing the File to move
     * @param toPath     path representing the destination of the File to move
     * @param handler    handler object to deal with errors and other delegation
     * @return           <code>true</code> if all the file was removed
     *                   (or not but the handler approved anyway).
     * @see WXFileUtilitiesHandler
     */
    public static boolean movePathToPathHandler(String fromPath, String toPath, WXFileUtilitiesHandler handler){
        boolean success = true;
        WXDebug.println(1, "WXFileUtilities: movePathToPathHandler:"+fromPath);
        if(fromPath == null || toPath == null) return false;
        if(!fromPath.equals(toPath)){
            File f = new File(fromPath);
            if(f.canWrite()){
                success = copyPathToPathHandler(fromPath, toPath, handler);
                if(success){
                    WXDebug.println(1, "WXFileUtilities: movePathToPathHandler: copy phase succeeded");
                    WXDebug.println(1, "WXFileUtilities: movePathToPathHandler: about to remove from file");
                    success = removeFileAtPathHandler(fromPath, handler);
                }
            }
        }
        return success;
    }

    /**
     * Returns a date object from the long interger returned by the java.io.File
     * object's <code>lastModified()</code> method. From the JDK documentation
     * for java.io.File:<br>
     * <i>The return value is system dependent and should only be used to
     * compare with other values returned by last modified. It should not be
     * interpreted as an absolute time</i>.
     * <p>
     * After some research, the returned integer was found to be the number of milliseconds
     * since January 1, 1970. This research was done on Windows 2000 Pro.
     * It's quite possible other operating systems (and/or VMs) do not use this convention.
     * If not this method will need to detect the OS/VM and use a different translation
     * constant between NSDate's interval convention and the local JDK's. Or you
     * can change two defaults that the current method uses to decide what the ref
     * date and precision are. These defaults are:
     * <p>
     * <code>WX_JAVA_FILE_DATE_INTERVAL_PRECISION</code> = Precision of the java.io.File
     * <code>modifiedDate</code> interval relative to one second. If the JVM is using a precision of seconds
     * this is "1.0"; if it is using milliseconds as most appear to, this value is "1000.0".
     * Basically this value is whatever it takes to convert your <code>modifiedDate</code> interval
     * into seconds.
     * <p>
     * <code>WX_JAVA_FILE_REF_DATE_DELTA_SEC</code> = Seconds to the reference date used
     * by the JVM's java.io.File <code>modifiedDate</code> relative to that used by
     * NSDate (which is January 1, 2001). For Windows 2000 at least this value is slightly
     * more than 31 years or 978307227.5 +/- 60 seconds.
     *
     * @param interval   the return value from a java.io.File object's lastModified
     *                   method
     * @return a date based on the passed in interval, its assumed precision, and
     *         an assumed reference date.
     */
    public static NSTimestamp dateFromJavaLastModifiedInterval(long interval){
        double intervalD = interval;
        double precD = 1000.0;
        String precisionRelativeToSeconds = System.getProperty("WX_JAVA_FILE_DATE_INTERVAL_PRECISION");
        if(precisionRelativeToSeconds != null){
            precD = new BigDecimal(precisionRelativeToSeconds).doubleValue();
        }
        double refDeltaD = 978307227.5;
        String secondsToJavaFileBaseDateRelativeToNSDateRefDate = System.getProperty("WX_JAVA_FILE_REF_DATE_DELTA_SEC");
        if(secondsToJavaFileBaseDateRelativeToNSDateRefDate != null){
            refDeltaD = new BigDecimal(secondsToJavaFileBaseDateRelativeToNSDateRefDate).doubleValue();
        }
        double nsDateInterval = intervalD/precD - refDeltaD;
        long nsDateIntervalMS = 0+(long)nsDateInterval*1000;
        NSTimestamp dt = new NSTimestamp (nsDateIntervalMS);
        return dt;
    }


    /**
     * Returns an NSDictionary containing various objects that represent (some of)
     * the POSIX attributes of the file specified at path. Meant to mimic
     * the NSFileManager method of the same name.
     * You access these objects using the keys:
     * <ol>
     *     <li>"NSFileType"</li>
     *     <li>"NSFileSize"</li>
     *     <li>"NSFileModificationDate"</li>
     *     <li>"NSFileReferenceCount"</li>
     * </ol>
     * "NSFileType" is currently only one of two things: "NSFileTypeDirectory" or
     * "NSFileTypeRegular". The limited types are a reflection of limitations of the
     * java.io.File class (the basis for this method) which doesn't know about
     * "NSFileTypeSymbolicLink" or any of the other five types recognized by NSFileManager.
     * "NSFileReferenceCount" is currently always one since the java.io.File class
     * does not know about links. In fact, java.io.File is so limited
     * that several items available from NSFileManager are not available here including:
     * <ol>
     *     <li>"NSFileOwnerAccountName"</li>
     *     <li>"NSFileGroupOwnerAccountName"</li>
     *     <li>"NSFileIdentifier"</li>
     *     <li>"NSFileDeviceIdentifier"</li>
     *     <li>"NSFilePosixPermissions"</li>
     * </ol>
     * Another trickiness is that java.io.File does not actually return a date for its
     * lastModified method. Instead it returns an interval that is not defined except
     * via comparisons to other java.io.File lastModified values. We make our best shot
     * here by estimating the date by little bit of reverse engineering.
     * <p>
     * To get more information at the degree available via NSFileManager, we either have
     * write native code and maintain that on a platform specific basis or wait for better
     * JVM/JDK implementations.
     *
     * @see dateFromJavaLastModifiedInterval
     * @param path   Path to get file system stats on
     * @param flag   flag indicating whether to traverse link (ignored this version)
     * @return dictionary of stats
     *
     */
    public static NSDictionary fileAttributesAtPathTraverseLink(String path, boolean flag){
        NSMutableDictionary d = null;
        if(path != null){
            d = new NSMutableDictionary();
            File f = new File(path);
            if(f.isDirectory()){
                d.setObjectForKey("NSFileTypeDirectory", "NSFileType");
            }else if(f.isFile()){
                d.setObjectForKey("NSFileTypeRegular", "NSFileType");
            }else{
                d.setObjectForKey("NSFileTypeRegular", "NSFileType");
            }
            d.setObjectForKey(new Long(f.length()), "NSFileSize");
            d.setObjectForKey(dateFromJavaLastModifiedInterval(f.lastModified()), "NSFileModificationDate");
            d.setObjectForKey(new Integer(1), "NSFileReferenceCount");
        }
        return d;
    }


    /**
     * Returns a dictionary representing the plist in the file with the passed in
     * filename located in the passed in framework name.
     * <p>
     * Returns null of no file is found or if the filename passed in is null.
     *
     * @param filename         name of the filename with the plist in both app and framework
     * @param frameworkName    name of your framwork that uses the config file
     * return                  a dictionary representing the contents of the plist.
     */
    public static NSDictionary loadConfigFile(String filename, String frameworkName){
        if (filename == null)
			return null;
		URL localConfigPathURL = WOApplication.application().resourceManager().pathURLForResourceNamed(filename, frameworkName, null);

		if (localConfigPathURL == null) {
			WXDebug.println(1, "localConfigPath was missing:" + filename);
			return null;
		}
		String localConfigString = WXFileUtilities.loadFileAtPath(localConfigPathURL.getPath());
		NSDictionary d = (NSDictionary) NSPropertyListSerialization.propertyListFromString(localConfigString);
		WXDebug.println(1, "d:" + d);
		return d;
    }



    /**
	 * Returns a dictionary representing the plist in the file with the passed in filename, merged with the contents of the plist in a file by the same name in the framework with the passed in name. This lets you create a sort of inheritence with your config files. You can put defaults in the
	 * framework, and let the app's file's entries override only those defaults if they are present.
	 * <p>
	 * The framework version of the config file must be present if frameworkName arg is non-null. Furthermore, the config file must be a dictionary in Foundation style property list format.
	 * <p>
	 * Defines a user default "WX_WOUTILS_CONFIG_FILE_ROOT" which is a path on your local system to look for the Application's config file instead of the app's wrapper folder. It first looks for a file in a subdirectory of this path of the same name as the application, and if that is not there,
	 * looks for a file in the root folder of the default itself. If found the plist here has its contents merged on top of the previous merge of the app-wrapper config on top of the framework config.
	 *
	 * @param filename
	 *            name of the filename with the plist in both app and framework
	 * @param frameworkName
	 *            name of your framwork that uses the config file return a dictionary representing the contents of the plist.
	 */
	public static NSDictionary loadConfigAndMergeFiles(String filename, String frameworkName){
        WXDebug.println(1, "ENTEREDs");

         WOResourceManager rm = WOApplication.application().resourceManager();
		URL localConfigPathURL = rm.pathURLForResourceNamed(filename, null, null);
		String systemConfigPath = null;

		String systemPath = System.getProperty("WX_WOUTILS_CONFIG_FILE_ROOT");
        WXDebug.println(1, "systemPath:"+systemPath);
        if(systemPath != null){
            File sysPath = new File(systemPath);
            if(sysPath.exists() && sysPath.isDirectory()){
                String appSysPath = systemPath+"/"+WOApplication.application().name();
                WXDebug.println(1, "appSysPath:"+appSysPath);

                String sysPathFilename =  sysPath+"/"+filename;
                WXDebug.println(1, "sysPathFilename:"+sysPathFilename);
                File sysPathFile = new File(sysPathFilename);

                String appSysPathFilename =  appSysPath+"/"+filename;
                WXDebug.println(1, "appSysPathFilename:"+appSysPathFilename);
                File appSysPathFile = new File(appSysPathFilename);

                if(appSysPathFile.exists()){
                    WXDebug.println(1, "appSysPathFile exists");
                    systemConfigPath = appSysPathFilename;
                }else if(sysPathFile.exists()){
                    WXDebug.println(1, "sysPathFile exists and appSysPathFile doesn't exist");
                    systemConfigPath = sysPathFilename;
                }

            }
        }

        if(localConfigPathURL == null){
            WXDebug.println(1, "localConfigPath was missing:"+filename);
        }

        NSMutableDictionary d = new NSMutableDictionary();
		if (frameworkName != null) {
			URL globalConfigPathURL = rm.pathURLForResourceNamed(filename, frameworkName, null);
			if (globalConfigPathURL == null)
				throw new RuntimeException("Missing file:" + filename + "   in framework:" + frameworkName);

			WXDebug.println(1, "localConfigPath:" + (localConfigPathURL != null ? localConfigPathURL.getPath() : "null"));
			WXDebug.println(1, "globalConfigPath:" + globalConfigPathURL.getPath());
			String globalConfigString = WXFileUtilities.loadFileAtPath(globalConfigPathURL.getPath());
            NSDictionary parentD = (NSDictionary)NSPropertyListSerialization.propertyListFromString(globalConfigString);
            d = parentD.mutableClone();
        }

        if(localConfigPathURL != null){
            String localConfigString = WXFileUtilities.loadFileAtPath(localConfigPathURL.getPath());
            WXDebug.println(1, "localConfigString:"+localConfigString);
            if(localConfigString != null){
                NSDictionary childD = (NSDictionary)NSPropertyListSerialization.propertyListFromString(localConfigString);
                d.addEntriesFromDictionary(childD);
            }
        }

        if(systemConfigPath != null){
            String systemConfigString = WXFileUtilities.loadFileAtPath(systemConfigPath);
            WXDebug.println(1, "systemConfigString:"+systemConfigString);
            if(systemConfigString != null){
                NSDictionary childD = (NSDictionary)NSPropertyListSerialization.propertyListFromString(systemConfigString);
                d.addEntriesFromDictionary(childD);
            }
        }
        return d;
    }

    private static NSDictionary _mimeTypesDict = null;

    /**
     * Returns an NSDictionary to use for looking up MIME types per filename extension.
     * The default MIME type mapping is stored in a file named "WXMimeTypes.plist" in the
     * framework. You can override these settings by placing your own "WXMimeTypes.plist"
     * into your application's resources. A single entry in the plist looks like this:
     * <pre>
     * pdf = "application/pdf";
     * </pre>
     * Once called, the method does not reload from the disk.
     *
     * return              The mime type lookup dictionary.
     */
    public static NSDictionary mimeTypesDict(){
        if(_mimeTypesDict == null){
            _mimeTypesDict = loadConfigAndMergeFiles("WXMimeTypes.plist","WOExamplesHarness");
        }
        return _mimeTypesDict;
    }

    /**
     * Given a path, this method grabs the ending and compares it to
     * a list of known file endings (taken from mimeTypes.plist) and
     * returns the mimeType if it can determine it. If not, it returns
     * null. This is very nice for determining the mimeType of a file
     * that you get via a WOFileUpload. Of course, if the file ending
     * is incorrect, you're going to get the wrong mimeType.
     *
     * @param path         path to extract a file extension from an
     *                     do a mime type lookup on it
     * return              The mime type for the path's extension.
     */
    public static String mimeTypeFromPath(String path) {

        NSDictionary mimeTypes;
        String mimeType;
        String pathExtension;
        int dotIndex = path.lastIndexOf(".");

        if (dotIndex < 1) {
            return null; // no dot, no soup for you!
        } else {
            mimeTypes = mimeTypesDict();
        }

        // OK. Now we have the mimeTypes Dict. What were we doing here anyway?
        pathExtension = path.substring(dotIndex + 1).toLowerCase();
        WXDebug.println(20,"Got file extension '" + pathExtension + "'");
        mimeType = (String)mimeTypes.objectForKey(pathExtension);
        if (mimeType != null) {
            WXDebug.println(20,"Deduced mime type as '" +  mimeType + "'");
            return mimeType;
        } else {
            WXDebug.println(20,"Unable to find mimeType corresponding to '" +
                             pathExtension + "' path extension");
            return null;
        }
    }


}
