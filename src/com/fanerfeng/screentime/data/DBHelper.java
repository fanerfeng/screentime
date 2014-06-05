package com.fanerfeng.screentime.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
  	private static final String DATABASE_NAME = "ApplicationInfo.db";
	private static final int DATABASE_VERSION = 3;
	public static final String DBTABLE_NAME = "APPINFO";

	public DBHelper(Context context) {
		// CursorFactory����Ϊnull,ʹ��Ĭ��ֵ
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// ��ݿ��һ�α�����ʱonCreate�ᱻ����
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + DBTABLE_NAME +
				"(_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
				"packageName VARCHAR, appName VARCHAR, " +
				"checked INTEGER, locked INTEGER, " +
				"lockedTime INTEGER)");
	}

	// ���DATABASE_VERSIONֵ����Ϊ2,ϵͳ����������ݿ�汾��ͬ,�������onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("ALTER TABLE "+ DBTABLE_NAME + " ADD COLUMN other STRING");
	}
}
