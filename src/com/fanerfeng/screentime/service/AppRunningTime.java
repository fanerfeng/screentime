package com.fanerfeng.screentime.service;

public class AppRunningTime {
	String lastPackage;
	String currPackage;
	long startTime;
	long endTime;
	
	public AppRunningTime(){
		lastPackage = null;
		currPackage = null;
		startTime = 0;
		endTime = 0;
	}
}
