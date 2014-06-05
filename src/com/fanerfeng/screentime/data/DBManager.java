package com.fanerfeng.screentime.data;

import java.util.ArrayList;
import java.util.List;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBManager {
	private DBHelper helper;
	private SQLiteDatabase db;

	public DBManager(Context context) {
		helper = new DBHelper(context);
		db = helper.getWritableDatabase();
	}

	public void add(AppInfo appinfo) {
		db.beginTransaction(); 
		try {
			db.execSQL("INSERT INTO " + DBHelper.DBTABLE_NAME +
					" VALUES(null, ?, ?, ?, ?, ?)",
					new Object[] {appinfo.packageName,appinfo.appName,
					appinfo.check, appinfo.locked,appinfo.lockedTime });
			db.setTransactionSuccessful(); 
		} finally {
			db.endTransaction();
		}
	}
	public void add(List<AppInfo> appinfos) {
		db.beginTransaction(); 
		try {
			for (AppInfo appinfo : appinfos) {
				db.execSQL("INSERT INTO "  + DBHelper.DBTABLE_NAME +" VALUES(null, ?, ?, ?, ?)",
						new Object[] {appinfo.appName, appinfo.packageName,
						appinfo.check, appinfo.locked,appinfo.lockedTime });
			}
			db.setTransactionSuccessful(); 
		} finally {
			db.endTransaction(); 
		}
	}

	public void updateCheckTag(String packageName, boolean check) {
		ContentValues cv = new ContentValues();
		cv.put("checked", check? 1:0);
		db.update(DBHelper.DBTABLE_NAME, cv, "packageName = ?", new String[] { packageName });
	}

	public void updateLockedTag(String packageName, boolean locked) {
		ContentValues cv = new ContentValues();
		cv.put("locked", locked? 1:0);
		if (!locked){
			cv.put("lockedTime", 0);
		}
		db.update(DBHelper.DBTABLE_NAME, cv, "packageName = ?", new String[] { packageName });
	}
	public void updateLockedTime(String packageName, long lockedTime) {
		ContentValues cv = new ContentValues();
		cv.put("lockedTime", lockedTime);
		db.update(DBHelper.DBTABLE_NAME, cv, "packageName = ?", new String[] { packageName });
	}

/*
	public void deleteOldPerson(Person person) {
		db.delete("person", "age >= ?",
				new String[] { String.valueOf(person.age) });
	}
*/
	public List<AppInfo> query() {
		ArrayList<AppInfo> appinfos = new ArrayList<AppInfo>();
		Cursor c = db.rawQuery("SELECT * FROM "+ DBHelper.DBTABLE_NAME, null);
		while (c.moveToNext()) {
			AppInfo appinfo = new AppInfo();
			appinfo.packageName = c.getString(c.getColumnIndex("packageName"));
			appinfo.appName 	= c.getString(c.getColumnIndex("appName"));
			appinfo.check 		= (c.getInt(c.getColumnIndex("checked")) == 1) ? true : false;
			appinfo.locked 		= (c.getInt(c.getColumnIndex("locked")) == 1) ? true : false;
			appinfo.lockedTime 	= c.getLong(c.getColumnIndex("lockedTime"));
			appinfos.add(appinfo);
		}
		c.close();		
		return appinfos;
	}
	
	public List<AppInfo> query(boolean checked) {		
		Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.DBTABLE_NAME+" WHERE checked = ?", new String[]{Integer.toString(checked?1:0)});  
		ArrayList<AppInfo> appinfos = new ArrayList<AppInfo>();		
	    while (c.moveToNext()) {
	    	AppInfo appinfo = new AppInfo();
			appinfo.packageName = c.getString(c.getColumnIndex("packageName"));
			appinfo.appName 	= c.getString(c.getColumnIndex("appName"));
			appinfo.check 		= (c.getInt(c.getColumnIndex("checked")) == 1) ? true : false;
			appinfo.locked 		= (c.getInt(c.getColumnIndex("locked")) == 1) ? true : false;
			appinfo.lockedTime 	= c.getLong(c.getColumnIndex("lockedTime"));			
			appinfos.add(appinfo);
		}
	    c.close();	    
	    return appinfos;
	}
	
	public AppInfo query(String packagename) {		
		Cursor c = db.rawQuery("SELECT * FROM "+DBHelper.DBTABLE_NAME+" WHERE packageName = ?", new String[]{packagename});  
		AppInfo appinfo = null;		
	    if (c.moveToNext()) {
	    	appinfo = new AppInfo();
			appinfo.packageName = c.getString(c.getColumnIndex("packageName"));
			appinfo.appName 	= c.getString(c.getColumnIndex("appName"));
			appinfo.check 		= (c.getInt(c.getColumnIndex("checked")) == 1) ? true : false;
			appinfo.locked 		= (c.getInt(c.getColumnIndex("locked")) == 1) ? true : false;
			appinfo.lockedTime 	= c.getLong(c.getColumnIndex("lockedTime"));
		}
	    c.close();	    
	    return appinfo;
	}

	public void print(){
		ArrayList<AppInfo> appinfos = (ArrayList<AppInfo>) this.query();	
		int ii = 0;
	    for (AppInfo appinfo : appinfos) {
	    	Log.i("ScreenTime", "DBManager." +ii + "||" +
	    			appinfo.appName + "||" +
	    			appinfo.packageName + "||" +
					appinfo.check + "||" +
					appinfo.locked + ".");
	    	ii++;
		}
	}
	
	public void closeDB() {
		db.close();
	}
}
