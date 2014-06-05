package com.fanerfeng.screentime.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MonitorService extends Service {
	static public HandleTimer handleTimer = null;
	//static public Context context = null;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		Notification notify = new Notification();
		startForeground(1,notify);
		Log.v("ScreenTime", "MonitorService.onCreate.");
		//Toast.makeText(this, "MonitorService.onCreate.", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.v("ScreenTime", "MonitorService.onDestroy.");
		super.onDestroy();
		Intent myIntent = new Intent();
		myIntent.setAction("screentime.action.MONITOR_SERVICE");
		startService(myIntent);	
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.v("ScreenTime", "MonitorService.onStartCommand.");
		flags = START_STICKY;
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.v("ScreenTime", "MonitorService.onStart.");
		//Toast.makeText(this, "MonitorService.onStart.", Toast.LENGTH_SHORT).show();
	
		if (handleTimer == null) {
			handleTimer = new HandleTimer(this);
		}
		/*	
		context = this;
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Log.v("ScreenTime", "Thread.run.");		
						//TODO			
						Thread.sleep(8000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
		});
		t.start();
		*/
	}
}
