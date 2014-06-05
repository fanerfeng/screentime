package com.fanerfeng.screentime;

import com.fanerfeng.screentime.R;
import com.fanerfeng.screentime.MyApp;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class RestDialogActivity extends Activity {
	private ActivityManager mActivityManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.restdialog);

		((MyApp) getApplication()).setInstance(this);

		mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		Intent intent = getIntent();

		if (intent != null) {
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				String packagename = bundle.getString("packagename");

				if (packagename != null) {
					forceTerminateActivity(packagename);
					Log.v("ScreenTime", "terminate(" + packagename + ")");
					Toast.makeText(this, "terminate(" + packagename + ")",Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	protected void onDestroy() {
		((MyApp) getApplication()).setInstance(null);
		Log.v("ScreenTime", "terminate rest-dialog self");
		Toast.makeText(this, "terminate rest-dialog self", Toast.LENGTH_SHORT)
				.show();
		super.onDestroy();
	}

	private void forceTerminateActivity(String packageName) {

		mActivityManager.killBackgroundProcesses(packageName);
	}
}