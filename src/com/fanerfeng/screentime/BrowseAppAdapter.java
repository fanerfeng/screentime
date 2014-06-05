package com.fanerfeng.screentime;

import java.util.List;

import com.fanerfeng.screentime.R;

import com.fanerfeng.screentime.data.AppInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class BrowseAppAdapter extends BaseAdapter   {
	
	private List<AppInfo> appInfoList = null;
	
	LayoutInflater infater = null;
    
	public BrowseAppAdapter(Context context,  List<AppInfo> apps) {
		infater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		appInfoList = apps ;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		System.out.println("size" + appInfoList.size());
		return appInfoList.size();
	}
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appInfoList.get(position);
	}
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public View getView(int position, View convertview, ViewGroup arg2) {
		//System.out.println("getView at " + position);
		View view = null;
		ViewHolder holder = null;
		if (convertview == null || convertview.getTag() == null) {
			view = infater.inflate(R.layout.browseappinfoitem, null);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} 
		else{
			view = convertview ;
			holder = (ViewHolder) convertview.getTag() ;
		}
		
		AppInfo appInfo = (AppInfo) getItem(position);
		holder.appIcon.setImageDrawable(appInfo.appIcon);
		holder.appName.setText(appInfo.appName) ;
		holder.packageName.setText(appInfo.packageName);
        holder.check.setChecked(appInfo.check); 
        
		return view;
	}

	class ViewHolder {
		ImageView appIcon ;             //���ID
		TextView appName ;             //�û�ID   
		TextView packageName ;  //���ռ���ڴ��С 
		CheckBox check;
		public ViewHolder(View view) {
			this.appIcon = (ImageView)view.findViewById(R.id.imageAppIcon) ;
			this.appName = (TextView) view.findViewById(R.id.tvAppName);
			this.packageName = (TextView) view.findViewById(R.id.tvPackageName);
			this.check = (CheckBox) view.findViewById(R.id.cbMonitored);
		}
	}

}