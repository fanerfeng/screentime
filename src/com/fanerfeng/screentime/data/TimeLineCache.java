package com.fanerfeng.screentime.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import android.util.Log;


public class TimeLineCache {
	private static final String TAG = "ScreenTime";
	private static TimeLineCache timeLineCache;
	private static long timeWindowMills = 0;

	private Map<String, TreeMap<Long, TimeEvent>> allTimeLine;

	private TimeLineCache() {
		allTimeLine = new HashMap<String, TreeMap<Long, TimeEvent>>();
		//timeWindowMills = getSharedPreferences(PublicParams.SHARED_TABLE, 0).getInt(PublicParams.SECS_SAVERANGE, 0);

	}
	public synchronized static TimeLineCache getInstance() {
		if (timeLineCache == null)
			timeLineCache = new TimeLineCache();
		return timeLineCache;
	}
	
	public void setTimeWindow(long timeMillis) {
		timeWindowMills = timeMillis;
	}
	
	public synchronized List<TimeEvent> getEvents(String packageName, Long begin, Long end) {
		TreeMap<Long, TimeEvent> TimeLineData = allTimeLine.get(packageName);
		if (TimeLineData == null || TimeLineData.size() == 0) {
			Log.e(TAG, "can't get data, packageName=" + packageName);
			return null;
		}
		
		long early = 0;
		long elapsedPoint = TimeEvent.getCurrentTime() - timeWindowMills;

		SortedMap<Long, TimeEvent> map = TimeLineData.subMap(early, elapsedPoint);
		
		Log.i(TAG,"auto delete expire TimeEvent, count=" + map.size()
							+ ", delete time begin=" + early
							+ ",end=" + elapsedPoint);
		map.clear();

		
		List<TimeEvent> events = new ArrayList<TimeEvent>();
		Iterator<Entry<Long, TimeEvent>> it = TimeLineData.subMap(begin, end).entrySet().iterator();		
		while (it.hasNext()) {
			Entry<Long, TimeEvent> entry = it.next();
			events.add(entry.getValue());
		}
		
		Log.i(TAG, "getEvents size=" + events.size());
		
		return events;
	}
	
	public synchronized TimeEvent getLastEvent(String packageName) {
		TreeMap<Long, TimeEvent> TimeLineData = allTimeLine.get(packageName);
		if (TimeLineData == null || TimeLineData.size() == 0) {
			Log.e(TAG, "can't get data, packageName=" + packageName);
			return null;
		}

		return TimeLineData.lastEntry().getValue();		
	}

	public synchronized boolean addTimeEvent(String packageName, TimeEvent timeEvent) {
		Log.i(TAG, "addTimeEvent Time:" + timeEvent.endTime);
				
		TreeMap<Long, TimeEvent> TimeLineData = allTimeLine.get(packageName);
		if (TimeLineData == null) {
			TimeLineData = new TreeMap<Long, TimeEvent>();
			allTimeLine.put(packageName, TimeLineData);
		}
/*
		if (TimeLineData.containsKey(date)) {
			TimeLineData.remove(date);
		}
*/		
		TimeLineData.put(timeEvent.endTime, timeEvent);

		/*
		Iterator<Entry<Date, TimeEvent>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Date, TimeEvent> entry = it.next();
			Log.i(TAG, "addTimeEvent  after:" + entry.getKey().getTime() + ", event:" + entry.getValue().getName());
		}
		*/
		return true;
	}
	
	public synchronized boolean cleanTimeEvent(String packageName) {
		TreeMap<Long, TimeEvent> TimeLineData = allTimeLine.get(packageName);
		if (TimeLineData == null || TimeLineData.size() == 0) {
			return false;
		}

		TimeLineData.clear();		
		return true;
	}

	public synchronized void cleanAll() {
		Iterator<Entry<String, TreeMap<Long, TimeEvent>>> it = allTimeLine.entrySet().iterator();
		while (it.hasNext()) {
			it.next().getValue().clear();
		}
		allTimeLine.clear();
	}
}
