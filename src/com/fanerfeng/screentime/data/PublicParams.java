package com.fanerfeng.screentime.data;

public class PublicParams {
	public final static String SHARED_TABLE="shared_table"; 
	public final static String SECS_RUNNING="running_secs"; 
	public final static String SECS_RESTING="resting_secs";
	public final static String SECS_SAVERANGE="save_range_secs"; 
	
	private static PublicParams publicParams;
	
	public synchronized static PublicParams getInstance() {
		if (publicParams == null)
			publicParams = new PublicParams();
		return publicParams;
	}
}
