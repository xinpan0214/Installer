package com.szgvtv.ead.framework.packageinstaller;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class InstallerManage {
	private static InstallerManage sIM = null;					//单例
	private Context mContext = null;
	private Handler mHandler = null;
	private ArrayList<InstallerInfo> mInstallList = null;		//安装列表
	private InstallerInfo mCurInstall = null;					//当前安装
	

	private InstallThread mInstallThread = null;				//安装线程
	
	/*安装消息*/
	public static final int INSTALL_START = 1;
	public static final int INSTALL_COMPLETE = 2;
	public static final int INSTALL_PROGRESS = 3;
	public static final int DELETE_START = 4;
	public static final int DELETE_COMPLETE = 5;
	public static final int DELETE_PROGRESS = 6;
	
	
	
	private static final String TAG = "InstallerManage";

	public static synchronized InstallerManage getInstance(Context context) {
		if (sIM == null) {
			sIM = new InstallerManage(context);
		}
		return sIM;
	}

	public InstallerManage(Context context) {
		mContext = context;
		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				InstallMessage(msg);
				super.handleMessage(msg);
			}
		};
		mInstallList = new ArrayList<InstallerInfo>();
		mInstallThread = new InstallThread();
		mInstallThread.start();
	}

	public synchronized void addInstallList(InstallerInfo info) {
		if (info != null) {
			mInstallList.add(info);
		}
	}

	public synchronized void removeInstallList(InstallerInfo info) {
		if (info != null) {
			mInstallList.remove(info);
		}

	}

	public synchronized boolean existInstall() {
		if(mCurInstall != null){
			return true;
		}else {
			return false;
		}	
	}
	
	public InstallerInfo getCurInstall() {
		return mCurInstall;
	}

	public void setCurInstall(InstallerInfo mCurInstall) {
		this.mCurInstall = mCurInstall;
	}


	public synchronized void startInstall() {
		setCurInstall(mInstallList.get(0));
		new Installer(mContext, mCurInstall, mHandler);
	}

	
	public static void sendVersionInfo(Context context){
		Log.i(TAG, "==============GET_VERSION============+"+getVerName(context));
		Intent intent = new Intent();
		intent.setAction(InstallerInter.ACTION_INSTALL_VERSION_NUMBER);
		Bundle bundle = new Bundle();
		bundle.putString(InstallerInter.VERSION_NUMBER, getVerName(context));
		intent.putExtras(bundle);
		context.sendBroadcast(intent);
	}
	
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;
	}
	
	
	/**
	 * @Title: InstallMessage
	 * @Description: 所有安装消息处理
	 * @param msg
	 * @return: void
	 */
	public void InstallMessage(Message msg) {
		switch (msg.what) {
		case INSTALL_START:{
			InstallerInfo  info= (InstallerInfo)msg.obj;
			if(info != null){
				Log.i(TAG, "==============INSTALL_START============ "+info.getPkgName());
				Intent intent = new Intent();
				intent.setAction(InstallerInter.ACTION_INSTALL_START);
				Bundle bundle = new Bundle();
				bundle.putString(InstallerInter.APP_NAME, info.getAppName());
				bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);
			}	
			break;
		}
		case INSTALL_PROGRESS:{
			InstallerInfo info = (InstallerInfo) msg.obj;
			if (info != null) {
				Log.i(TAG, "=====INSTALL_PROGRESS======= "+" current_progress "+info.getCurProgress()+" total_progress"+info.getTotalProgress());
				Intent intent = new Intent();
				intent.setAction(InstallerInter.ACTION_INSTALL_PROGRESS);
				Bundle bundle = new Bundle();
				bundle.putString(InstallerInter.APP_NAME, info.getAppName());
				bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
				bundle.putInt(InstallerInter.CURRENT_PROGRESS,
						info.getCurProgress());
				bundle.putInt(InstallerInter.TOTAL_PROGRESS,
						info.getTotalProgress());
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);
			}
			break;
		}
		case INSTALL_COMPLETE:{
			InstallerInfo  info= (InstallerInfo)msg.obj;
			Log.i(TAG, "==============INSTALL_COMPLETE============ "+info.getPkgName()+msg.arg1);
			Intent intent = new Intent();
			intent.setAction(InstallerInter.ACTION_INSTALL_COMPLETE);
			Bundle bundle = new Bundle();
			bundle.putString(InstallerInter.APP_NAME, info.getAppName());
			bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
			bundle.putInt(InstallerInter.RET_CODE, msg.arg1);
			intent.putExtras(bundle);
			mContext.sendBroadcast(intent);
			
			removeInstallList(mCurInstall);
			setCurInstall(null);
			break;
		}
		
		case DELETE_START:{
			InstallerInfo  info= (InstallerInfo)msg.obj;
			if(info != null){
				Log.i(TAG, "==============DELETE_START============ "+info.getPkgName());
				Intent intent = new Intent();
				intent.setAction(InstallerInter.ACTION_DELETE_START);
				Bundle bundle = new Bundle();
				bundle.putString(InstallerInter.APP_NAME, info.getAppName());
				bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);
			}	
			break;
		}
		case DELETE_PROGRESS:{
			InstallerInfo info = (InstallerInfo) msg.obj;
			if (info != null) {
				Log.i(TAG, "=====DELETE_PROGRESS======= "+" current_progress "+info.getCurProgress()+" total_progress"+info.getTotalProgress());
				Intent intent = new Intent();
				intent.setAction(InstallerInter.ACTION_DELETE_PROGRESS);
				Bundle bundle = new Bundle();
				bundle.putString(InstallerInter.APP_NAME, info.getAppName());
				bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
				bundle.putInt(InstallerInter.CURRENT_PROGRESS,
						info.getCurProgress());
				bundle.putInt(InstallerInter.TOTAL_PROGRESS,
						info.getTotalProgress());
				intent.putExtras(bundle);
				mContext.sendBroadcast(intent);
			}
			break;
		}
		case DELETE_COMPLETE:{
			InstallerInfo  info= (InstallerInfo)msg.obj;
			Log.i(TAG, "==============DELETE_COMPLETE============ "+info.getPkgName()+msg.arg1);
			Intent intent = new Intent();
			intent.setAction(InstallerInter.ACTION_DELETE_COMPLETE);
			Bundle bundle = new Bundle();
			bundle.putString(InstallerInter.APP_NAME, info.getAppName());
			bundle.putString(InstallerInter.PACKAGE_NAME, info.getPkgName());
			bundle.putInt(InstallerInter.RET_CODE, msg.arg1);
			intent.putExtras(bundle);
			mContext.sendBroadcast(intent);
			
			removeInstallList(mCurInstall);
			setCurInstall(null);
			break;
		}

		}
	}
	
	
	/**
	 * 安装线程
	 */
	public class InstallThread extends Thread {

		public InstallThread() {
		}

		@Override
		public void run() {
			try {
				super.run();
				while (true) {
					if(!existInstall()){
						if(mInstallList.size()>0){
							//开始一个处理
							Log.i(TAG, "=======================================");
							Log.i(TAG, "==============start install============ ");
							startInstall();
						}else {
							//队列没有待处理安装
							sleep(2000);
							//android.os.Process.killProcess(android.os.Process.myPid());
						}
					}else {
						//队列安装正在处理
						Log.i(TAG, "==============installing============ ");
						sleep(200);
					}	
				}
			} catch (Exception e) {
			}
		}
	}

}
