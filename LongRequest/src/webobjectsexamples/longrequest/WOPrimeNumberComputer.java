/*
 * WOPrimeNumberComputer.java
 * [LongRequest Project]

© Copyright 2005 Apple Computer, Inc. All rights reserved.

IMPORTANT:  This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or redistribution of this Apple software constitutes acceptance of these terms.  If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software.

In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants you a personal, non-exclusive license, under Apple’s copyrights in this original Apple software (the “Apple Software”), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software.  Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the Apple Software without specific prior written permission from Apple.  Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other works in which the Apple Software may be incorporated.

The Apple Software is provided by Apple on an "AS IS" basis.  APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.

IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN  ADVISED OF THE POSSIBILITY OF
SUCH DAMAGE.
 */

package webobjectsexamples.longrequest;

import com.webobjects.appserver.WOComponent;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;

public class WOPrimeNumberComputer extends Object {

    // The calculatePrimes method is more interesting than the countPrimes method.
    // It does a takeValueForKey early to put a vector in the status object shared
    // between the 2 threads. Then it keeps updating this vector.
    // Both threads see the same NSArray being updated, without needing to be synchronized.
    // in the mean time, this method regularly does other takeValues to set the prime number count.

    public NSArray calculatePrimes(Number aStart, Number aStop, String aKey, WOComponent target) {
        NSMutableArray primeArray = new NSMutableArray();
        int i, j;
        int start = aStart.intValue();
        int stop = aStop.intValue();
        int count = 0;

        NSMutableArray value = null;

        if (start <= 3) {
            count ++;
            primeArray.addObject(new Integer(2));	// 2 is prime
            start = 3;
        }
        // initialize the status (for RefreshPage2 only).
        target.takeValueForKey(primeArray, aKey);

        for (i= start; i < stop; i += 2) {
            boolean prime = true;
            for (j = 3; j*j <= i; j += 2) {
                if (i % j == 0) {
                    prime = false;
                    break;
                }
            }
            if (prime) {
                count ++;
                primeArray.addObject(new Integer(i));
                if (primeArray.count() > 20) {
                    primeArray.removeObjectAtIndex(0);
                }
                if (count % 100 == 0) {
                     // every 10000 primes we send them back.
                    value = new NSMutableArray();
                     value.addObject(new Integer(count));
                     value.addObject(new Integer(i));
                     target.takeValueForKey(value, aKey);
                 }
            }
            if (i % 2 == 0) i--; // That's to make sure we are odd from now on.
        }
        value = new NSMutableArray();
        value.addObject(new Integer(count));
        value.addObject(new Integer(i));
        return value;
    }
}
