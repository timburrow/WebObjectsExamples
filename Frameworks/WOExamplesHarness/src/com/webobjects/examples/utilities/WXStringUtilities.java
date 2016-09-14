/*
 WXStringUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

/**
WXStringUtilities provides String munging, generation, and manipulation methods.
 */

public class WXStringUtilities extends Object /* JC_WARNING - Please advise: this could be a subclass of EOCustomObject. If needed, add 'implements NSKeyValueCoding' and implementations for valueForKey & takeValueForKey. */ {

    private static final String URL_CHARS = "abcdefghijklmnopqrstuvwxyz0123456789-_.";
    private static final String KEY_CHARS = "abcdefghijklmnopqrstuvwxyz" +
    "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "012345678901234567890123456789" +
    "012345678901234567890123456789";

    /**
     * Characters to use in creating passwords.
     * Missing zero, capital Oh, lowercase ell, and numerical one to
     * avoid end-user confusion.
     */
    private static String _passwordChars =
    "ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789-_!?*.+^#$&@%=";

    /**
     * Sets the characters that are used for generating passwords in
     * <code>passwordWithLength(int)</code>. The string passed in should
     * be something like: "abcdefg", basically the individual characters
     * you want in passwords, but all together without spaces as one string.
     */
    public synchronized static void setPasswordChars(String value) {
        _passwordChars = value;
    }

    /**
     * Returns the characters that are used for generating passwords
     * in ChiStringUtils.passwordWithLength(int). The default value is:
     * "ABCDEFGHIJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789-_!?*.+^#$&@%="
     */
    public static String passwordChars() {
        return _passwordChars;
    }

    /**
     * Returns a random string from a set of characters including all
     * upper and lower case letters in the English alphabet. All arabic
     * digits from 0-9 are also included. The number of characters in
     * the string is set by the interger passed in.
     *
     * @param length   number of characters in returned string
     * @return string made up of random alphanumeric characters
     */
    public static String randomKeyWithLength(int length) {
        return randomStringWithLengthFromCharset(length, KEY_CHARS);
    }

    /**
     * Returns a machine generated password using the character set
     * made up of the characters returned by <code>passwordChars()</code>.
     * Good for making up random (but not non-deterministic)
     * passwords for users. Not terribly secure, but adequate for
     * lightweight protection. You can change the set of characters
     * used to generate these passwords by feeding a string of
     * characters to ChiStringUtils.setPasswordChars(java.lang.String)
     *
     * @param length   number of characters in returned password
     * @return string made up of random alphanumeric characters
     */
    public static String passwordWithLength(int length) {
        return randomStringWithLengthFromCharset(length, passwordChars());
    }

    /**
     * Returns a random string from a passed in set of characters.
     * This is the primitive method used by the other random string
     * generator methods in the class.
     *
     * @param length    number of characters in returned string
     * @param charset   character set to pick random characters from
     * @return string made up of random alphanumeric characters
     */
    public static String randomStringWithLengthFromCharset(int length, String charset) {
        int someNumber;
        char[] password = new char[length];

        for(int i = 0; i < length; i++) {
            short num = (short)WXUtilities.randomNumberGenerator().nextInt();
            if (num < 0) { num += 32768; } // Piece of crap only returns signed ints.
            someNumber = num % charset.length();
            password[i] = charset.charAt(someNumber);
        }
        return new String(password);
    }


    /**
     * Make something url/http normal without URLEncoding. Nice to
     * have around if you want to scrub all the cruft out of a string
     * and make it into a filename or url or something something.
     *
     * @param aString  string to make lower case and strip out any
     *                 characters that would have to be URL encoded
     * @return all lowercase string without any characters needing
     *         escaping
     */
    public static String toLowerAndNormalize( String aString ) {
        return WXStringUtilities.normalizeStringFromString( aString.toLowerCase(), URL_CHARS );
    }


    /**
     * Method to create a "normalized" string from a base set of characters.  This can be
     * used to make something url/http normal without URLEncoding.
     *
     * @param aString  		string to parse characters from
     * @param baseString	base set of "acceptable" characters to check against
     * @return 				tring without any characters not in the base set
     */

    public static String normalizeStringFromString( String aString, String baseString ) {

        StringBuffer sb = new StringBuffer( aString );
        int i;
        char c;

        for ( i = 0; i < sb.length(); i++ ) {
            c = sb.charAt( i );
            if ( baseString.indexOf(c) < 0 ) {
                sb.setCharAt(i, '_');
            }
        }

        return sb.toString();
    }


    /**
     * Return a specifically sized substring of an existing string, specifying the length of the string,
     * optional extra characters to append to the end (like "..."), and an optional string of characters
     * to break on the last instance of BEFORE the specified length.  The breaking characters allow you to
     * take a string of words and specify a "max" length but break the string in between words, thereby not
     * chopping a word up.
     *
     * @param theString				the source string
     * @param theLength				int, the length (maximum) of the string
     * @param extraCharacters		characters to append to the end of the string
     * @param breakCharacters		characters to break on before the length is met.
     * @return						a sized string
     */

    public static String sizedStringFromString( String theString, int theLength, String extraCharacters, String breakCharacters ) {

    	 String aString = theString;

        // Determine if there is a string value, and if it needs to be modified
        if ( aString != null && aString.length() > 0 ) {

            // Check to make sure the string is longer than the specified length
            if ( theLength < aString.length() ) {

                // Return value for the component
                int extraCharLength = 0;
                int breakCharIndex = 0;

                // Truncate the string to the max size ...
                aString = new String ( aString.substring ( 0, theLength ) );

                // Check for defined values
                if ( extraCharacters != null && extraCharacters.length() > 0 )
                    extraCharLength = extraCharacters.length();
                if ( breakCharacters != null && breakCharacters.length() > 0 )
                    breakCharIndex = aString.lastIndexOf( breakCharacters );

                // If a match exists
                if ( breakCharIndex > 0 ) {

                    // Check that the extra characters will fit:  the extraCharacter length must be less
                    // than the difference between the string length and the index of the breaking characters.
                    // If not, check for an earlier match of the breaking characters.
                    while ( ( extraCharLength > ( theLength - breakCharIndex ) ) &&
                        ( aString.substring( 0, breakCharIndex ).indexOf( breakCharacters ) > 0 ) ) {

                        breakCharIndex = ( aString.substring( 0, breakCharIndex) ).lastIndexOf( breakCharacters );
                    }

                    // Truncate the string to the breakCharIndex.
                    aString = aString.substring( 0, breakCharIndex );
                }

                // Determine how much (if not all) of the extra characters to append
                if ( extraCharLength > ( theLength - breakCharIndex) )
                    extraCharLength = theLength - breakCharIndex;

                // Ensure the string will not be over the max length
				if (aString.length() == theLength && extraCharLength > 0)
					aString = aString.substring(0, (theLength - extraCharLength));

				// Append the extra characters
				if ((extraCharacters != null) && (extraCharLength > 0))
					aString = aString + extraCharacters.substring(0, extraCharLength);
            }
        }

        // Send back the string value
        return aString;
    }




    /**
     * Get the last element of a path (AKA, the
     * filename). Warning. This does not use the pathSeparator system
     * property.  This is for files uploaded through a browser, so it
     * accomodates forward slash AND backslash pathSeparators.
     * If you are dealing with files on your local file system, use NSPathUtilities
     * <code>lastPathComponent</code>.
     */
    public static String lastPathComponentXPlatform(String wholePath) {
        int lastDirIndex = wholePath.lastIndexOf("\\");

        WXDebug.println(20,"Got filename:" + wholePath);

        if ( lastDirIndex < 0) {
            // Try forwardSlash
            WXDebug.println(20,"Can't find a backSlash, Trying forwardSlash");
            lastDirIndex = wholePath.lastIndexOf("/");
        }
        else if ( lastDirIndex > -1) {
            //trim just the filename out
            String tmp = wholePath.substring(lastDirIndex + 1);
            WXDebug.println(20,"Munged to:   " + tmp);
            return tmp;
        }
        WXDebug.println(20,"Untouched:   " + wholePath);
        return wholePath;
    }


    /**
     * Method to get the contents of a URL and to place them into a String.  This method
     * uses the normal Java URLConnection and pulls in the information.  Currently there is
     * no error status returned after an exception is found - a null string is returned
     *
     * @param aURLString		String of the fully-qualified URL to get the contents of
     * @return					contents of the URL
     */

    public static String stringWithContentsOfURL(String aURLString) throws Exception {

        // Instance variables
        URLConnection uc;
        BufferedReader br;
        boolean stillMoreData = true;
        StringBuffer sb = new StringBuffer();

        // Open a connection and get the information
        try {

            uc = new URL( aURLString).openConnection();
            br = new BufferedReader( new InputStreamReader(uc.getInputStream()) );

            while( stillMoreData ) {

                String str = br.readLine();

                if( str == null ) {
                    stillMoreData = false;
                } else {
                    sb.append( str );
                }
            }

            return sb.toString();

        }

        // Catch any errors
        catch( Exception e ) {

            WXDebug.println( 1, "stringFromURL.contentsOfURL(): caught exception: " + e.toString() );
            e.printStackTrace();
        }

        // Should this be null or should I return the http error? ahorovit 3/1/2001
        return null;
    }


    /**
     * Class for Exception thrown by WXWOUtilities objects instead of a plain RuntimeException.
     * The only difference is the addition of a type string for the exception
     * instead of only the message.
     */
    static public class WXWOValidationException extends RuntimeException{
  		private static final long	serialVersionUID	= 8867777425391924464L;
		String _type = null;
        public WXWOValidationException(){
            super();
        }
        public WXWOValidationException(String reason, String t){
            super(reason);
            _type = t;
        }
        public String type(){
            return _type;
        }
    }

    /**
     * Class for Exception thrown by WXUtilities objects instead of a plain RuntimeException.
     */
    static public class ValidationException extends RuntimeException{
 		private static final long	serialVersionUID	= 2684618517394394777L;
		public ValidationException(){
            super();
        }
        public ValidationException(String v){
            super(v);
        }
    }


    /**
     * Checks that the passed in string represents an email address in a valid
     * format. The checks include:
     * <ul>
     * <li>email too short (shortest e-mail is a@b.au)</li>
     * <li>contains no username before the '@'</li>
     * <li>contains an invalid domain name (shortest domain is 4 chars)</li>
     * <li>does not contain a '.' in the domain</li>
     * <li>contains an invalid top level domain (the top level domain - the only
     *     thing after the last '.' - must be at least two characters)</li>
     * <li>does not contain a domain name (must have a domain name between the '@' and the '.')</li>
     * <li>email cannot be null</li>
     * </ul>
     * <p>
     * Throws an ValidationException if there is any problem with the email format.
     *
     * @param value string to check for proper email address format.
     */
    public static void validateEmailAddressFormat(String value) throws WXStringUtilities.ValidationException{

        if( value == null ) throw new ValidationException( "The email address cannot be null.");

        try {

            // Get the length of the address, the index of the @, and the last .
            int length = value.length();
            int at = value.lastIndexOf('@');
            int dot = value.lastIndexOf('.');

            // The shortest e-mail is a@b.au
            if( length < 6 ) throw new ValidationException( "The email address " + value +
                                                               " is too short.");

            // A user name must be specified before the @
            if( at < 1 ) throw new ValidationException( "The email address " + value +
                                                           " contains no username before the @.");

            // The shortest domain is 4 chars
            if( at > (length - 5) ) throw new ValidationException( "The email address " + value +
                                                                      " contains an invalid domain name.");

            // There must be a . in the domain which follows the @
            if( dot < at ) throw new ValidationException( "The email address " + value +
                                                             " does not contain a '.' in the domain.");

            // The top level domain (the only thing after the last .) must be two characters
            if( dot >= length - 2 ) throw new ValidationException( "The email address " + value +
                                                                      " contains an invalid top level domain.");

            // Must have a domain name between the @ and the .
            if( dot == at + 1 ) throw new ValidationException( "The email address " + value +
                                                                  " does not contain a domain name.");
        }

        catch( Exception e ) {

            // Catch all other errors in handling ...
            throw new ValidationException( "The email address " + value + " is not valid." );
        }
    }

    public static NSArray pathComponents(String path){
        // Note I can't use java.io.File.pathSeparator since on OS X
        // this returns ":" which isn't the kind of paths I'm dealing with here
        String separator = "/";
        if (System.getProperty("os.name").startsWith("Windows")) // adjust prefix for Windows style paths
            separator = "\\";
        WXDebug.println(1, "separator:"+separator);
        return pathComponents( path,  separator);
    }

    public static NSArray<String> pathComponents(String path, String separator){
        if(path == null) return null;
        NSArray<String> arr = NSArray.componentsSeparatedByString(path, separator);
        NSMutableArray<String> pathComponents = arr.mutableClone();
        int cnt = arr.count();
        String lastComp = arr.objectAtIndex(cnt-1);
        if(lastComp.equals("")) pathComponents.removeObjectAtIndex(cnt-1);
        int cntAfter = pathComponents.count();
        if(cntAfter > 0){
            String firstComp = pathComponents.objectAtIndex(0);
            if(firstComp.equals("")) pathComponents.removeObjectAtIndex(0);
        }
        return pathComponents.immutableClone();
    }

    public static boolean isStringEmpty(String v){
        if(v == null) return true;
        NSArray a = NSArray.componentsSeparatedByString(v, " ");
        String v2 = a.componentsJoinedByString("");
        if("".equals(v2)) return true;
        return false;
    }

    public static String stringNoSpaces(String v){
        if(v == null) return null;
        NSArray a = NSArray.componentsSeparatedByString(v, " ");
        String v2 = a.componentsJoinedByString("");
        return v2;
    }


}
