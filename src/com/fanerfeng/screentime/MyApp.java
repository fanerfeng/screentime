package com.fanerfeng.screentime;

import android.app.Application;

public class MyApp extends Application{
   
	private RestDialogActivity myActivity;
    
	public MyApp() {
		// TODO Auto-generated constructor stub
		myActivity = null;
	}
    
    public void setInstance(RestDialogActivity instance){
            myActivity = instance;
    }
    
    public RestDialogActivity getInstance(){
            return myActivity;
    }
}
