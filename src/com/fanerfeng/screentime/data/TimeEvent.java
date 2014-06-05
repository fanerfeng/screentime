package com.fanerfeng.screentime.data;

public class TimeEvent {
	public Long startTime;
	public Long endTime;
	
	public TimeEvent(long startTime, long endTime){
		this.startTime = new Long(startTime);
		this.endTime = new Long(endTime);
	}
	
	public TimeEvent(Long startTime, Long endTime){
		this.startTime = new Long(startTime.longValue());
		this.endTime = new Long(endTime.longValue());
	}
	
    public static long getCurrentTime(){ 
		return System.currentTimeMillis();
    }
    /*time methods*/
    public long swapMillisToMins(long timeMillis){
    	//long t = Long.parseLong(timeStr);  
        //long time = System.currentTimeMillis() - (t*1000);  
        long sec = (long) Math.ceil(timeMillis /1000);//��ǰ  
        long minute = (long) Math.ceil(timeMillis/60/1000.0f);// ����ǰ  
        long hour = (long) Math.ceil(timeMillis/60/60/1000.0f);// Сʱ  
        long day = (long) Math.ceil(timeMillis/24/60/60/1000.0f);// ��ǰ  

        return sec;     
    }

}
