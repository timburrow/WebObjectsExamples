/*
 WXUtilities.java
 [WOExamplesHarness Project]

© Copyright 2001-2007 Apple Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package com.webobjects.examples.utilities;

import java.net.InetAddress;
import java.rmi.server.UID;
import java.util.Random;

import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSDictionary;

/**
WXUtilities includes miscellaneous methods based on just the JDK and/or the Foundation framework. All methods must be static (class) methods. If you find that you need several class variables, several instance variables, or several methods to complete your utility function, you should not put your utility code in this class, but instead add your own class to this framework.
<p>

*/
public class WXUtilities {

    /**
     * This method is a convenient means of comparing two host strings to
     * see if they represent the same internet address. The method
     * first checks if the passed in hostname strings match, but it will
     * also try creating Java InetAddress objects from the host strings
     * and see if they represent the same internet address. For example
     * one string might be an IP address and the other might be a hostname;
     * Their strings wouldn't match in this case, but they still could
     * represent the same internet address. Also note that if "foo" and
     * "foo.apple.com" point to the same address, these strings would also
     * return <code>true</code> for a match.
     * <p>
     * Throws a runtime exception if either passed in string is null.
     * <p>
     * Note that "localhost" and "foo" won't match even if "foo" is the
     * local machine because these are different internet addresses. Also
     * note that a machine can have more than one internet address so this
     * method does not indicate that two machines are the same.
     *
     * @param thisHost    a hostname string to test
     * @param thatHost    another hostname string to test
     * @return  <code>true</code> if the passed in hostname strings
     *          represent the same internate address.
     */
    public static boolean doHostsMatch(String thisHost, String thatHost){
        if(thisHost == null || thatHost == null)
            throw new RuntimeException("WXUtilities: doHostsMatch: thisHost:"+thisHost+"  thatHost:"+thatHost+" CANNOT be null");
        boolean hostsMatch = false;
        if (thisHost.toLowerCase().equals(thatHost)){
            hostsMatch = true;
        }
        if (!hostsMatch) {
            try {
                InetAddress thatHostIA = InetAddress.getByName(thatHost);
                InetAddress thisHostIA = InetAddress.getByName(thisHost);
                if (thisHostIA.equals(thatHostIA)){
                    hostsMatch = true;
                }
            }catch(java.net.UnknownHostException e) {
                WXDebug.println(1, "WXUtilities: doHostsMatch: Unable to get host inet address");
            }
        }
        return hostsMatch;
    }




    private static final String URLArgJoin = "&";

    /**
     * A constant representing the commonly used empty string,
     * "".
     */
    public static final String EmptyString = "";

    /**
     * Returns a string of URL form value encoded key value pairs as
     * pulled from the passed in dictionary. This is also known as a GET
     * style URL string. The form of such a URL string is:
     * key1=value1&key2=value2
     *
     * @param aDict   the dictionary of key value paris to transform
     *                into a GET-style form value URL string.
     * @return a GET-style URL string.
     */
    public static String formValueURIFromDictionary(NSDictionary aDict){
        if (aDict != null) {
            String results = "";
            NSArray keys = aDict.allKeys();
            int max = keys.count();
            for (int i = 0; i < max; i++) {
                String join = ((i == (max - 1)) ? EmptyString : URLArgJoin);
                String key = (String)keys.objectAtIndex(i);
                Object value = aDict.objectForKey(key);
                if (value instanceof NSArray) {
                    NSArray vals = (NSArray)value;
                    int counter = vals.count();
                    for (int j = 0; j < counter; j++) {
                        Object iVal = vals.objectAtIndex(j);
                        String jJoin = ((j == (counter - 1)) ? join : URLArgJoin);
                        results = results+WXCodecUtilities.urlEncode(key)+"="+WXCodecUtilities.urlEncode(iVal.toString())+jJoin;
                    }
                }else{
                    results = results+WXCodecUtilities.urlEncode(key)+"="+WXCodecUtilities.urlEncode(value.toString())+join;
                }
            }
            return results;
        }
        return EmptyString;
    }

    static private Random _randomNumberGenerator;

    /**
     * Returns a shared random number generator object that can used
     * throughout your application. The generator is seeded with the
     * current date.
     *
     * @return a shared random number generator.
     */
    static public Random randomNumberGenerator(){
        if(_randomNumberGenerator == null){
        	int seed = (int)(System.currentTimeMillis() / 1000L);
             _randomNumberGenerator = new Random(seed);
        }
        return _randomNumberGenerator;
    }


    /**
     * Returns an global ID string that is unique with respect to the
     * host on which it is generated.  This GUID is unique under the
     * following conditions: a) the machine takes more than one second
     * to reboot, and b) the machine's clock is never set backward.
     * The method uses the java.rmi.UID class to create a unique number
     * in a name space limited to your machine and then combines this
     * string with your machine's internet address.
     *
     * @return the GUID
     */
    static public String generateGUID() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (java.net.UnknownHostException e) {
			WXDebug.println(1, "WXUtilities: GUID: generateGUID: e:" + e);
		}
		UID uid = new UID();
		String guid = uid.toString() + "|" + (ia != null ? ia.toString() : "localhost");
		return guid;
	}



}
