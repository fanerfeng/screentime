package com.fanerfeng.screentime.service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.fanerfeng.screentime.MyApp;
import com.fanerfeng.screentime.RestDialogActivity;
import com.fanerfeng.screentime.data.AppInfo;
import com.fanerfeng.screentime.data.DBManager;
import com.fanerfeng.screentime.data.PublicParams;
import com.fanerfeng.screentime.data.TimeEvent;
import com.fanerfeng.screentime.data.TimeLineCache;

public class HandleTimer {
	static private final int timeInterval =  8000;
	private Timer timer;

	private Context context;
	private String topPackage;
	private String secondPackage;
	int runningSecs;
	int saveRangeSecs;
	int restingSecs;
	long currentTimeMillis;

	private DBManager dbmgr;  

	
	public HandleTimer(Context context) {
		// TODO Auto-generated constructor stubtimer;		
		this.context = context.getApplicationContext();

		timer = new Timer();
		timer.schedule(new RemindTask(), 0, timeInterval);
		
	}
	
	class RemindTask extends TimerTask{
		public void run(){
			topPackage = getRunningActivity(0);
			secondPackage = getRunningActivity(1);
	
			runningSecs = context.getSharedPreferences(PublicParams.SHARED_TABLE, 0).getInt(PublicParams.SECS_RUNNING, 0);
	 		if (runningSecs == 0) 
	 			return;
	 		
			saveRangeSecs = context.getSharedPreferences(PublicParams.SHARED_TABLE, 0).getInt(PublicParams.SECS_SAVERANGE, 0);
			restingSecs = context.getSharedPreferences(PublicParams.SHARED_TABLE, 0).getInt(PublicParams.SECS_RESTING, 0);
			currentTimeMillis = TimeEvent.getCurrentTime();
					
			TimeLineCache timeLineCache = TimeLineCache.getInstance();
			timeLineCache.setTimeWindow(saveRangeSecs*1000);
			
	    	dbmgr = new DBManager(context);
	    	List<AppInfo> appinfos = dbmgr.query(true);
	    	dbmgr.closeDB();

			int ergodicResult = 0;
			long ergodicElapsedTime = 0;
			
			Log.v("ScreenTime", "HandleTimer Entry");
			
	   		for (AppInfo appinfo: appinfos){
				Log.v("ScreenTime", "(" + appinfo.appName + "/" + 
						appinfo.check + "/" + 
						appinfo.locked + "/" + 
						(appinfo.locked?(currentTimeMillis - appinfo.lockedTime)/1000:appinfo.lockedTime) + "/" +
						")");

	   			if (appinfo.locked){
	   				if (appinfo.lockedTime + restingSecs*1000 < currentTimeMillis){
	   					dbmgr = new DBManager(context);
	   					dbmgr.updateLockedTag(appinfo.packageName, false);
	   					dbmgr.updateLockedTime(appinfo.packageName, 0);
	   					dbmgr.closeDB();
	   					
	   					timeLineCache.cleanTimeEvent(appinfo.packageName);
	   					
	   					if (secondPackage.equals(appinfo.packageName) && topPackage.equals(RestDialogActivity.class.getPackage().getName())){
	   						ergodicResult = 2;
	   						/*For print*/
	   						ergodicElapsedTime = currentTimeMillis - appinfo.lockedTime;
	   					}
	    			}
	   				else if (topPackage.equals(appinfo.packageName)){
	   					ergodicResult = 1;
	   				}
	   				/*For print*/
	   				if (topPackage.equals(appinfo.packageName)){
	   					ergodicElapsedTime = currentTimeMillis - appinfo.lockedTime;
	   				}
	   			}
	   			else if (topPackage.equals(appinfo.packageName)){
	   				timeLineCache.addTimeEvent(appinfo.packageName, new TimeEvent(currentTimeMillis - timeInterval, currentTimeMillis));
	   				//连续合并TimeEvent::
	   				
	   				long totalRunningTime = 0;   		 		
	   		    	List<TimeEvent> timeEventList = timeLineCache.getEvents(appinfo.packageName, new Long(currentTimeMillis - saveRangeSecs*1000), currentTimeMillis);
	   		    	if(timeEventList != null){
	   			    	for (TimeEvent timeEvent : timeEventList) {
	   			    		totalRunningTime += (timeEvent.endTime.longValue() - timeEvent.startTime.longValue());
	   			    	}
	   		    	}
	   		    	if (totalRunningTime >= runningSecs*1000){
	   		    		dbmgr = new DBManager(context);
	   					dbmgr.updateLockedTag(appinfo.packageName, true);
	   					dbmgr.updateLockedTime(appinfo.packageName, currentTimeMillis);
	   					dbmgr.closeDB();		
	    					
	   					ergodicResult = 3;	 
	   		    	}
	   		    	/*For print*/
  					ergodicElapsedTime = totalRunningTime;
	   			}	   				
	   		}
	   		
			Log.v("ScreenTime", "HandleTimer:(ergodic/elapsed/top/second)=(" + 
					ergodicResult + "/" +
					ergodicElapsedTime/1000 + "/" +
					getApplicationName(topPackage) + "/" +
					getApplicationName(secondPackage) + ")");
			/*
			Toast.makeText(context,"HandleTimer(res/ela/top/sec)=(" +
					ergodicResult + "/" +
					ergodicElapsedTime + "/" +
					getApplicationName(topPackage) + "/" +
					getApplicationName(secondPackage) + ")",
					Toast.LENGTH_LONG).show();
			*/
	   		switch(ergodicResult){
	   		case 0:break;
	   		case 1:startRestDialog(topPackage);break;
	   		case 3:startRestDialog(topPackage);break;
	   		case 2:finishRestDialog();break;
	   		}   		
		}
	}
	
    private String getRunningActivity(int pos)
    {
    	ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

    	List<RunningTaskInfo> runningTaskInfos = mActivityManager.getRunningTasks(pos+1);
    	if(runningTaskInfos != null && runningTaskInfos.size() > pos){
    		try{
    			RunningTaskInfo runningTaskInfo = runningTaskInfos.get(pos);
	       		if (runningTaskInfo != null){
	       			ComponentName cn = runningTaskInfo.topActivity;
	       			return cn.getPackageName();
	       		}
    		}
    		catch(Exception e){}
    	}
    	
    	return null ;
    }
    
    private String getApplicationName(String packageName) {
    	PackageManager packageManager = null;
    	ApplicationInfo applicationInfo = null;
    	try {
    		packageManager = context.getApplicationContext().getPackageManager();    	
    		applicationInfo = packageManager.getApplicationInfo(packageName, 0);
    	} 
    	catch (PackageManager.NameNotFoundException e) {
    		   applicationInfo = null;
    	}
    	if (applicationInfo != null)
    		return (String) packageManager.getApplicationLabel(applicationInfo);
    	else
    		return null;
   	}   
    
	private void finishRestDialog() {
		//setForeground(true);
		android.os.Debug.waitForDebugger();
		RestDialogActivity myActivity = ((MyApp) ( (Activity) context).getApplication()).getInstance();		

		if (myActivity !=null){
			myActivity.finish();
		}
	}
	
	private void startRestDialog(String extrasMsg) {
		Intent myIntent = new Intent(context.getApplicationContext(), RestDialogActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		Bundle bundle=new Bundle();
		bundle.putString("packagename", extrasMsg);
		myIntent.putExtras(bundle); 
		context.startActivity(myIntent);
	}
}
