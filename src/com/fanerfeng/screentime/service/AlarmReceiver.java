package com.fanerfeng.screentime.service;

import java.util.List;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
	private ActivityManager mActivityManager = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (/*intent.getAction().equals("screentime.action.MONITOR_SERVICE")*/true) {
			Intent myIntent = new Intent();
			myIntent.setAction("screentime.action.MONITOR_SERVICE");
			
			//Log.v("ScreenTime", "AlarmReceiver.onReceive("+ intent.getAction()+").");
			//Toast.makeText(context,"AlarmReceiver.onReceive.",Toast.LENGTH_SHORT).show();
			
			context.startService(myIntent);	
		}
	}	
}
 