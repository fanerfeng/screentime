package com.fanerfeng.screentime;

import com.fanerfeng.screentime.R;
import com.fanerfeng.screentime.data.PublicParams;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnCheckedChangeListener
{	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Button btnSelectApps;
		RadioGroup rg;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		rg = (RadioGroup) this.findViewById(R.id.rgTimeSelection);
		btnSelectApps = (Button) this.findViewById(R.id.btnSelectApps);
		
		rg.setOnCheckedChangeListener((OnCheckedChangeListener) this);
		btnSelectApps.setOnClickListener(this);		
		
		SharedPreferences userInfo = getSharedPreferences(PublicParams.SHARED_TABLE, 0);  
		int runningMins = userInfo.getInt(PublicParams.SECS_RUNNING, 0)/60;
		
		int rbId;
		switch(runningMins){
			case 10: rbId = R.id.rbTimeSelection1;break;
			case 20: rbId = R.id.rbTimeSelection2;break;
			case 30: rbId = R.id.rbTimeSelection3;break;
			case 40: rbId = R.id.rbTimeSelection4;break;
			case 50: rbId = R.id.rbTimeSelection5;break;
			case 60: rbId = R.id.rbTimeSelection6;break;
			default: rbId = R.id.rbTimeSelection0;break;
		}
		rg.check(rbId);
		
		Log.v("ScreenTime", "MainActivity.onCreate.mins("+runningMins+").");
		//Toast.makeText(this,"MainActivity.onCreate.mins("+runningMins+").",Toast.LENGTH_SHORT).show();
	}
	
    protected void onDestroy() {  
        super.onDestroy();  
    }
    
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId)
	{
		int time = 0;
		switch(checkedId){
			case R.id.rbTimeSelection1: time = 10;break;
			case R.id.rbTimeSelection2: time = 20;break;
			case R.id.rbTimeSelection3: time = 30;break;
			case R.id.rbTimeSelection4: time = 40;break;
			case R.id.rbTimeSelection5: time = 50;break;
			case R.id.rbTimeSelection6: time = 60;break;
			default: time = 0;break;
		}
		
		time = time*60;
		SharedPreferences userInfo = getSharedPreferences(PublicParams.SHARED_TABLE, 0);  
		userInfo.edit().putInt(PublicParams.SECS_RUNNING, time).commit();		
		userInfo.edit().putInt(PublicParams.SECS_RESTING, time).commit();
		userInfo.edit().putInt(PublicParams.SECS_SAVERANGE, time*2).commit();	
		if (time != 0){
			Intent testIntent = new Intent();
			testIntent.setAction("screentime.action.BOOT_COMPLETED");			
			sendBroadcast(testIntent);			
		}
		Log.v("ScreenTime", "MainActivity.onCheckedChanged.mins("+time+").");
		//Toast.makeText(this,"MainActivity.onCheckedChanged.mins("+time+").",Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onClick(View source)
	{
		if (source.getId() == R.id.btnSelectApps)
		{
			Intent myIntent = new Intent(this, BrowseAppActivity.class);
			startActivity(myIntent);
			
			Intent testIntent = new Intent();
			testIntent.setAction("screentime.action.BOOT_COMPLETED");			
			sendBroadcast(testIntent);		
		}
	}
}