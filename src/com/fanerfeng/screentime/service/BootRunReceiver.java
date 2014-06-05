package com.fanerfeng.screentime.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

public class BootRunReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (/*Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())*/true) {
			Log.v("ScreenTime", "BootRunReceiver.onReceive("+ intent.getAction()+").");
			//Toast.makeText(context,"BootRunReceiver.onReceive.",Toast.LENGTH_SHORT).show();

			Intent myIntent = new Intent();
			myIntent.setAction("screentime.action.ALARM");
			SetAlarmTime(context, myIntent, 30000);				
			//SetAlarmTime(context, new Intent(context, xxx.class), 10);
		}
	}
	
	private void SetAlarmTime(Context context, Intent intent, long intervalSeconds){
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);

		long firstime = SystemClock.elapsedRealtime();
		AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Log.v("ScreenTime", "BootRunReceiver.OnTimer.");
		//Toast.makeText(context,"BootRunReceiver.OnTimer.",Toast.LENGTH_SHORT).show();
		am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, intervalSeconds, sender);
	}
}