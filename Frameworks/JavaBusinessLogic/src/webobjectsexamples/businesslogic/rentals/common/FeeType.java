/*
 * FeeType.java [JavaBusinessLogic Project] � Copyright 2005 Apple Computer, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (�Apple�) in consideration of your agreement to the following terms, and your use, installation, modification or
 * redistribution of this Apple software constitutes acceptance of these terms. If you do not agree with these terms, please do not use, install, modify or redistribute this Apple software. In consideration of your agreement to abide by the following terms, and subject to these terms, Apple grants
 * you a personal, non-exclusive license, under Apple�s copyrights in this original Apple software (the �Apple Software�), to use, reproduce, modify and redistribute the Apple Software, with or without modifications, in source and/or binary forms; provided that if you redistribute the Apple Software
 * in its entirety and without modifications, you must retain this notice and the following text and disclaimers in all such redistributions of the Apple Software. Neither the name, trademarks, service marks or logos of Apple Computer, Inc. may be used to endorse or promote products derived from the
 * Apple Software without specific prior written permission from Apple. Except as expressly stated in this notice, no other rights or licenses, express or implied, are granted by Apple herein, including but not limited to any patent rights that may be infringed by your derivative works or by other
 * works in which the Apple Software may be incorporated. The Apple Software is provided by Apple on an "AS IS" basis. APPLE MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE,
 * REGARDING THE APPLE SOFTWARE OR ITS USE AND OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS. IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION, MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE), STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package webobjectsexamples.businesslogic.rentals.common;

import com.webobjects.eocontrol.EOEditingContext;
import com.webobjects.eocontrol.EOEnterpriseObject;
import com.webobjects.eocontrol.EOFetchSpecification;
import com.webobjects.eocontrol.EOGenericRecord;
import com.webobjects.eocontrol.EOGlobalID;
import com.webobjects.eocontrol.EOKeyValueQualifier;
import com.webobjects.eocontrol.EOQualifier;
import com.webobjects.foundation.NSArray;

public class FeeType extends EOGenericRecord {
	private static final long	serialVersionUID		= -3924679005498502385L;

	public static final String	EnabledKey				= "enabled";

	public static final String	FeeTypeKey				= "feeType";

	public static final String	OrderByKey				= "orderBy";

	public static final String	FeeTypeEntityName		= "FeeType";

	public static final String	FeeTypeIDKey			= "feeTypeID";

	private static EOGlobalID	_defaultFeeTypeGlobalID	= null;

	private static EOGlobalID	_lateFeeTypeGlobalID	= null;

	public FeeType() {
		super();
	}

	private static EOGlobalID _globalIDForPrimaryKey(int primaryKey, EOEditingContext editingContext) {
		EOFetchSpecification fetchSpecification = new EOFetchSpecification(FeeTypeEntityName, new EOKeyValueQualifier(FeeTypeIDKey, EOQualifier.QualifierOperatorEqual, new Integer(primaryKey)), null);
		NSArray objects = editingContext.objectsWithFetchSpecification(fetchSpecification);
		return (objects.count() > 0) ? editingContext.globalIDForObject((EOEnterpriseObject) (objects.objectAtIndex(0))) : null;
	}

	public static FeeType defaultFeeType(EOEditingContext editingContext) {
		if (_defaultFeeTypeGlobalID == null) {
			// default fee type is the object with primary key = 1
			_defaultFeeTypeGlobalID = _globalIDForPrimaryKey(1, editingContext);
		}
		return (_defaultFeeTypeGlobalID != null) ? (FeeType) (editingContext.faultForGlobalID(_defaultFeeTypeGlobalID, editingContext)) : null;
	}

	public static FeeType lateFeeType(EOEditingContext editingContext) {
		if (_lateFeeTypeGlobalID == null) {
			// late fee type is the object with primary key = 2
			_lateFeeTypeGlobalID = _globalIDForPrimaryKey(2, editingContext);
		}
		return (_lateFeeTypeGlobalID != null) ? (FeeType) (editingContext.faultForGlobalID(_lateFeeTypeGlobalID, editingContext)) : null;
	}
}
