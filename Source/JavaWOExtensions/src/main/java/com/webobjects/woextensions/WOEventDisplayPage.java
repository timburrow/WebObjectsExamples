/*
 * WOEventDisplayPage.java [JavaWOExtensions Project] ©Copyright 2001 - 2007 Apple, Inc. All rights reserved. IMPORTANT: This Apple software is supplied to you by Apple Computer, Inc. (“Apple”) in consideration of your agreement to the following terms, and your use, installation, modification or
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

import java.util.Iterator;

import com.webobjects.appserver.WOComponent;
import com.webobjects.appserver.WOContext;
import com.webobjects.appserver.WOEvent;
import com.webobjects.eocontrol.EOEvent;
import com.webobjects.eocontrol.EOEventCenter;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSComparator;
import com.webobjects.foundation.NSForwardException;
import com.webobjects.foundation.NSMutableArray;
import com.webobjects.foundation.NSMutableDictionary;

public class WOEventDisplayPage extends WOEventPage {
	private static final long								serialVersionUID	= 1169474085544058231L;

	public EOEvent											currentEvent;

	public NSArray<EOEvent>									selectionPath;

	public int												_displayMode;

	public NSArray<EOEvent>									events;

	public NSMutableDictionary<EOEvent, NSArray<EOEvent>>	cache;

	public NSMutableArray<EOEvent>							webEvents, eofEvents;

	public WOEventDisplayPage								_self				= this;

	private _EventComparator								_eventAscendingComparator;

	public WOEventDisplayPage(WOContext aContext) {
		super(aContext);
		selectionPath = new NSMutableArray<EOEvent>();
		_displayMode = 1;
		cache = new NSMutableDictionary<EOEvent, NSArray<EOEvent>>();
		// we sort these things descending, i.e. the opposite of normal sorting order
		_eventAscendingComparator = new _EventComparator(EOSortOrdering.CompareDescending, this);
	}

	public int displayMode() {
		return _displayMode;
	}

	public void setDisplayMode(Object ick) {
		if (ick == null) {
			_displayMode = 0;
		} else if (ick instanceof Number) {
			_displayMode = ((Number) ick).intValue();
		} else {
			try {
				_displayMode = Integer.parseInt(ick.toString());
			} catch (NumberFormatException e) {
				_displayMode = 0;
			}
		}
	}

	public int displayLevelForEvent(EOEvent e) {
		int index, i, n;
		NSArray<EOEvent> children;

		index = selectionPath.indexOfObject(e);
		if (index != NSArray.NotFound)
			return index;

		children = rootEventList();
		if (children.containsObject(e))
			return 0;

		int count = selectionPath.count();
		for (i = 0, n = count; i < n; i++) {
			children = cache.objectForKey(selectionPath.objectAtIndex(i));
			if (null == children)
				break;

			if (children.containsObject(e))
				return i + 1;
		}

		return -1;
	}

	public NSArray<EOEvent> filterEvents(NSArray<EOEvent> evs, int level) {
		int i, n;
		NSArray<EOEvent> filtered;
		if (evs == null) {
			return NSArray.emptyArray();
		}
		// in the general case, it is sufficient to sort the events
		// by their plain duration, which is what the default implementation does.
		try {
			if (_displayMode != 4 || level != 0) {
				try {
					filtered = evs.sortedArrayUsingComparator(_eventAscendingComparator);
				} catch (IllegalStateException ex) {
					filtered = evs;
				}
			} else {

				// For association mode, we need to filter out unwanted events,
				// i.e. those which are not related to associations.
				int count = evs.count();
				NSMutableArray<EOEvent> mutableFiltered = new NSMutableArray<EOEvent>(count);

				for (i = 0, n = count; i < n; i++) {
					if (childrenForEvent(evs.objectAtIndex(i)).count() != 0)
						mutableFiltered.addObject(evs.objectAtIndex(i));
				}

				mutableFiltered.sortUsingComparator(_eventAscendingComparator);

				filtered = mutableFiltered;
			}

		} catch (NSComparator.ComparisonException e) {
			throw NSForwardException._runtimeExceptionForThrowable(e);
		}

		return filtered;
	}

	public int groupTagForDisplayLevel(int level) {
		switch (_displayMode) {
			case 0:
				return -1;

			case 1:
				return -1;

			case 2:
				switch (level) {
					case 0:
						return 2;

					case 1:
						return 1;

					default:
						return -1;
				}

			case 3:
				if (level == 0)
					return 2;
				else
					return -1;

			case 4:
				if (level == 0)
					return 2;
				else
					return -1;

			default: // bogus
				return -1;
		}
	}

	public int aggregateTagForDisplayLevel(int level) {
		switch (_displayMode) {
			case 0:
				return -1;

			case 1:
				return 0;

			case 2:
				if (level <= 1)
					return -1;
				else
					return 0;

			case 3:
				if (level == 0)
					return -1;
				else
					return 0;

			case 4:
				if (level == 0)
					return -1;
				else
					return 3;

			default: // bogus
				return -1;
		}
	}

	public NSArray<EOEvent> rootEventList() {
		if (null == events) {
			switch (_displayMode) {
				case 0:
				case 1:
					events = EOEventCenter.rootEventsForAllCenters();
					break;

				default:
					events = EOEventCenter.allEventsForAllCenters();
					break;
			}

			events = EOEvent.groupEvents(events, groupTagForDisplayLevel(0));

			events = EOEvent.aggregateEvents(events, aggregateTagForDisplayLevel(0));

			events = filterEvents(events, 0);
		}
		return events;
	}

	public EOEvent object() {
		return currentEvent;
	}

	public NSArray<EOEvent> childrenForEvent(EOEvent event) {
		NSArray<EOEvent> anArray;
		int level, tag;

		anArray = cache.objectForKey(event);
		if (null != anArray) {
			if (anArray.count() == 0)
				return null;
			else
				return anArray;
		}

		anArray = event.subevents();

		if (anArray == null || (anArray.count() == 0)) {
			cache.setObjectForKey(NSArray.EmptyArray, event);
			return null;
		}

		level = displayLevelForEvent(event) + 1;
		if (level == -1)
			level = selectionPath.count() + 1;

		tag = groupTagForDisplayLevel(level);
		if (tag >= 0)
			anArray = EOEvent.groupEvents(anArray, tag);

		tag = aggregateTagForDisplayLevel(level);
		if (tag >= 0)
			anArray = EOEvent.aggregateEvents(anArray, tag);

		anArray = filterEvents(anArray, level);

		cache.setObjectForKey(anArray, event);

		return anArray;
	}

	public NSArray<EOEvent> currentEventChildren() {
		NSArray<EOEvent> result = childrenForEvent(currentEvent);
		if (result != null) {
			return result;
		} else {
			return NSArray.emptyArray();
		}
	}

	public boolean isDirectory() {
		return currentEventChildren().count() > 0;
	}

	public WOComponent resetLoggingClicked() {
		EOEventCenter.resetLoggingForAllCenters();
		return refreshLoggingClicked();
	}

	public WOComponent refreshLoggingClicked() {
		cache.removeAllObjects();
		selectionPath = new NSMutableArray<EOEvent>();
		events = null;
		webEvents = null;
		eofEvents = null;

		return null;
	}

	public String displayComponentName() {
		if (currentEvent == null) {
			return "";
		}
		return currentEvent.displayComponentName();
	}

	public int eventCount() {
		return EOEventCenter.allEventsForAllCenters().count();
	}

	public long topmostDurationValue() {
		NSArray<EOEvent> roots;

		roots = rootEventList();
		if (roots == null || (roots.count() == 0))
			return 0;
		else
			return durationOfEvent(roots.objectAtIndex(0));
	}

	public long durationOfEvent(EOEvent e) {
		int i, n;
		long sum;
		NSArray kids;

		// mode 4 is in so far special as we need to filter out events which will not
		// be displayed, even if they may have a duration.
		if (_displayMode != 4) {
			sum = e.duration();
			return sum;
		}

		// if an event has no kids, but we are still here, it means that it is
		// an association event (because otherwise it would be filtered out).
		kids = childrenForEvent(e);
		if (kids == null) {
			kids = NSArray.EmptyArray;
		}
		n = kids.count();
		if (n != 0) {
			for (i = 0, sum = 0; i < n; i++) {
				sum += ((EOEvent) kids.objectAtIndex(i)).duration();
			}
		} else {
			sum = e.duration();
		}

		return sum;
	}

	public void _cacheWebEofEvents() {
		if (webEvents != null)
			return;

		NSArray<EOEvent> allCenters = EOEventCenter.allEventsForAllCenters();
		int halfCount = allCenters.count() / 2;
		webEvents = new NSMutableArray<EOEvent>(halfCount);
		eofEvents = new NSMutableArray<EOEvent>(halfCount);

		for (Iterator<EOEvent> iterator = allCenters.iterator(); iterator.hasNext();) {
			EOEvent e = iterator.next();
			if (e instanceof WOEvent)
				webEvents.addObject(e);
			else
				eofEvents.addObject(e);
		}
	}

	public int webEventDuration() {
		int i, n, time;

		_cacheWebEofEvents();

		n = (webEvents != null) ? webEvents.count() : 0;

		for (i = 0, time = 0; i < n; i++) {
			EOEvent e = webEvents.objectAtIndex(i);
			if (e instanceof WOEvent)
				time = time + (int) e.durationWithoutSubevents();
		}

		return time;
	}

	public int eofEventDuration() {
		int i, n, time;

		_cacheWebEofEvents();

		n = (eofEvents != null) ? eofEvents.count() : 0;
		for (i = 0, time = 0; i < n; i++) {
			EOEvent e = eofEvents.objectAtIndex(i);
			if (!(e instanceof WOEvent))
				time = time + (int) e.durationWithoutSubevents();
		}

		return time;
	}

	public int webEventCount() {
		_cacheWebEofEvents();
		return (webEvents != null) ? webEvents.count() : 0;
	}

	public int eofEventCount() {
		_cacheWebEofEvents();
		return (eofEvents != null) ? eofEvents.count() : 0;
	}

	public boolean isEmpty() {
		return eventCount() == 0;
	}
}
