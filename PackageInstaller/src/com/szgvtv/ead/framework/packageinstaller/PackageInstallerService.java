package com.szgvtv.ead.framework.packageinstaller;
import java.io.File;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

public class PackageInstallerService extends Service {
	private static final String TAG = "PackageInstallerService";

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "======PackageInstallerService onCreate=====");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//return super.onStartCommand(intent, flags, startId);
		Log.i(TAG, "======PackageInstallerService onStartCommand=====");
		if(intent != null){
			InstallerManage.sendVersionInfo(getApplicationContext());
			Uri mPackageURI = intent.getData();
			if(mPackageURI != null){
				if(mPackageURI.getScheme().equals("file")){
					startInstall(intent);
				}
				else if(mPackageURI.getScheme().equals("package")){
					startDelete(intent);
				}
			}	
		}
		
		super.onStart(intent, startId);
		return Service.START_NOT_STICKY;
		//return super.onStartCommand(intent, flags, startId);
	}
	
	private void startInstall(Intent intent){
		try {
			Log.i(TAG, "======startInstall=====");
			Uri mPackageURI;
			PackageInfo mPkgInfo;
			mPackageURI = intent.getData();
			if(mPackageURI != null){
				if(mPackageURI.getScheme().equals("file")){
					final File sourceFile = new File(mPackageURI.getPath());
					String archiveFilePath=sourceFile.getAbsolutePath();//安装包路径  
			        PackageManager pm = getPackageManager();    
			        mPkgInfo = pm.getPackageArchiveInfo(archiveFilePath, PackageManager.GET_ACTIVITIES); 
			        if(mPkgInfo != null){    
			            ApplicationInfo appInfo = mPkgInfo.applicationInfo;  
			            appInfo.sourceDir = archiveFilePath;
			            appInfo.publicSourceDir = archiveFilePath;
			            String appName = pm.getApplicationLabel(appInfo).toString();    
			          
			            InstallerInfo info = new InstallerInfo();
		            	info.setPath(sourceFile.getAbsolutePath());
		            	info.setPkgName(mPkgInfo.packageName);
		            	info.setInstallFlag(true);
		            	info.setAppName(appName);
		            	Log.w(TAG, "======startInstall====="+appName);
		            	
		            	InstallerManage.getInstance(getApplicationContext()).addInstallList(info);
			            
			        }else {
			        	Log.w(TAG, "======error apk file======  "+sourceFile.getAbsolutePath());
					}  
					
					/*
					final File sourceFile = new File(mPackageURI.getPath());
		            PackageParser.Package parsed = getPackageInfo(sourceFile);        
		            mPkgInfo = PackageParser.generatePackageInfo(parsed, null,
		                    PackageManager.GET_PERMISSIONS, 0, 0, null,
		                    new PackageUserState());
		            if(mPkgInfo != null){
		            	InstallerInfo info = new InstallerInfo();
		            	info.setPath(sourceFile.getAbsolutePath());
		            	info.setPkgName(mPkgInfo.packageName);
		            	info.setInstallFlag(true);
		            	String appnameString = getPackageManager().getApplicationLabel(mPkgInfo.applicationInfo).toString();
		            	info.setAppName(appnameString);
		            	Log.w(TAG, "======startInstall====="+appnameString);
		            	info.setAppName(getPackageManager().getApplicationLabel(mPkgInfo.applicationInfo).toString());
		            	
		            	InstallerManage.getInstance(getApplicationContext()).addInstallList(info);
		            }else {
		            	Log.w(TAG, "======error apk file======  "+sourceFile.getAbsolutePath());
					}
				*/}	
			}
		} catch (Exception e) {
			Log.w(TAG, "======error apk file======  " + e.toString());
		}
	}
	
	
	private void startDelete(Intent intent) {
		Log.w(TAG, "======startDelete=====");
		PackageManager mPm;
		Uri mPackageURI;
		ApplicationInfo mAppInfo = null;
		mPackageURI = intent.getData();

		if (mPackageURI != null) {
			String packageName = mPackageURI.getEncodedSchemeSpecificPart();
			if (packageName == null) {
				Log.w(TAG, "=======input null package:" + packageName);
				return;
			}

			mPm = getPackageManager();
			boolean errFlag = false;
			try {
				mAppInfo = mPm.getApplicationInfo(packageName,
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
				errFlag = true;
				Log.w(TAG, "Invalid package name:" + packageName);
			}

			String className = mPackageURI.getFragment();
			ActivityInfo activityInfo = null;
			if (className != null) {
				try {
					activityInfo = mPm.getActivityInfo(new ComponentName(
							packageName, className), 0);
				} catch (NameNotFoundException e) {
					Log.w(TAG, "Invalid className name:" + className);
					errFlag = true;
				}
			}
			if (mAppInfo != null && !errFlag) {
				InstallerInfo info = new InstallerInfo();
				info.setPkgName(mAppInfo.packageName);
				info.setInstallFlag(false);
				info.setAppName(mAppInfo.loadLabel(getPackageManager()).toString());
				InstallerManage.getInstance(getApplicationContext())
						.addInstallList(info);
			} else {
				Log.w(TAG, "Invalid packageName or componentName in "
						+ mPackageURI.toString());
			}

		}
	}
	
	 public static PackageParser.Package getPackageInfo(File sourceFile) {
	        final String archiveFilePath = sourceFile.getAbsolutePath();
	        PackageParser packageParser = new PackageParser(archiveFilePath);
	        DisplayMetrics metrics = new DisplayMetrics();
	        metrics.setToDefaults();
	        PackageParser.Package pkg =  packageParser.parsePackage(sourceFile,
	                archiveFilePath, metrics, 0);
	        // Nuke the parser reference.
	        packageParser = null;
	        return pkg;
	    }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
