package com.fanerfeng.screentime;

import java.util.ArrayList;
import java.util.List;

import com.fanerfeng.screentime.R;

import com.fanerfeng.screentime.data.AppInfo;
import com.fanerfeng.screentime.data.DBManager;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class BrowseAppActivity extends Activity  implements OnItemClickListener{
	private List<AppInfo> appInfoList = null;
	private ListView lvAppInfoList;
	private TextView tvAppInfoListTitle;
	private DBManager dbmgr;
	private BrowseAppAdapter appInfoAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browseappinfo);

		lvAppInfoList = (ListView) findViewById(R.id.lvAppInfoList);
		tvAppInfoListTitle = (TextView) findViewById(R.id.txtAppInfoListTitle);
		lvAppInfoList.setOnItemClickListener(this);
		this.registerForContextMenu(lvAppInfoList);
		
		dbmgr = new DBManager(this);
		InitInstalledAppInfo();
		InitListViewAdaptor();  
		tvAppInfoListTitle.setText(getString(R.string.installedApps) + appInfoList.size());
	}

	protected void onDestroy() {
		super.onDestroy();
		dbmgr.closeDB();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
       	appInfoList.get(arg2).check = !appInfoList.get(arg2).check;
       	InitListViewAdaptor();
        dbmgr.updateCheckTag(appInfoList.get(arg2).packageName,appInfoList.get(arg2).check);
	}

	public boolean onContextItemSelected(MenuItem item) {
		return super.onContextItemSelected(item);
	}
	public void InitListViewAdaptor(){
		 if (appInfoAdapter == null) {  
			 appInfoAdapter = new BrowseAppAdapter(this,appInfoList);
			 lvAppInfoList.setAdapter(appInfoAdapter);
		 } 
		 else {         
			 appInfoAdapter.notifyDataSetChanged();  
	     }
	}
	
	private void InitInstalledAppInfo() {
		appInfoList = new ArrayList<AppInfo>();
		List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);

		for (PackageInfo packageinfo : packages) {
			if ((packageinfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
				AppInfo appInfo = new AppInfo();
				appInfo.packageName = packageinfo.packageName;
				appInfo.appName = packageinfo.applicationInfo.loadLabel(getPackageManager()).toString();
				appInfo.appIcon = packageinfo.applicationInfo.loadIcon(getPackageManager());
				appInfo.check = false;
				appInfo.locked = false;
				appInfo.lockedTime = 0;
				
				appInfoList.add(appInfo);

				AppInfo tempAppInfo = dbmgr.query(appInfo.packageName);
				if (tempAppInfo != null) {//数据库中已有:此处appInfo是实时获取到的，是否考虑从DB获取？
					appInfo.check = tempAppInfo.check;
				}
				else{
					dbmgr.add(appInfo);
				}
			}
		}
	}
}
