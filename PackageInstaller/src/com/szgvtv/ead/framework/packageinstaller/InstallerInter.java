package com.szgvtv.ead.framework.packageinstaller;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

/**
 * 供调用者查看
 *
 */
public class InstallerInter {
	/*发送给APP广播*/
	/*安装开始*/
	public static final String ACTION_INSTALL_START = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_START";
	/*安装完成，判断返回值*/
	public static final String ACTION_INSTALL_COMPLETE = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_COMPLETE";
	/*安装进度*/
	public static final String ACTION_INSTALL_PROGRESS = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_PROGRESS";
	/*卸载开始*/
	public static final String ACTION_DELETE_START = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_START";
	/*卸载完成，判断返回值*/
	public static final String ACTION_DELETE_COMPLETE = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_COMPLETE";
	/*卸载进度*/
	public static final String ACTION_DELETE_PROGRESS = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_PROGRESS";
	/*得到版本号*/
	public static final String ACTION_INSTALL_VERSION_NUMBER = "com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_VERSION_NUMBER";
	
	/*广播action接收数据*/
	public static final String APP_NAME = "app_name";						/*所有广播传*/
	public static final String PACKAGE_NAME = "package_name";				/*所有广播传*/
	public static final String CURRENT_PROGRESS = "current_progress";		/*ACTION_INSTALL_PROGRESS ACTION_DELETE_PROGRESS*/
	public static final String TOTAL_PROGRESS = "total_progress";			/*ACTION_INSTALL_PROGRESS ACTION_DELETE_PROGRESS*/
	public static final String RET_CODE = "ret_code";						/*ACTION_INSTALL_COMPLETE ACTION_DELETE_COMPLETE*/
	public static final String VERSION_NUMBER = "version_number";			/*ACTION_GET_INSTALL_VERSION*/

	/*安装返回值类型，其它都是错误 ret_code返回值类型*/
	public static final int INSTALL_SUCCEEDED = 1;							/*安装成功*/
	public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;		/*空间不足*/
	
	/*卸载返回值类型，其它都是错误 ret_code返回值类型*/
	public static final int DELETE_SUCCEEDED = 1;							/*卸载成功*/
	public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;		/*系统应用*/
	
	/*默认总进度*/
	public static final int INSTALL_TOTAL_PROGRESS = 40;
	public static final int DELETE_TOTAL_PROGRESS = 5;
	
	/*安装apk*/
	private void installApk(Context context,String apkpath)
	{
		try 
		{
			Intent intent = new Intent();
		    intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(apkpath)), 
					"gv_application/vnd.android.package-archive");
			context.startService(intent);
		} 
		catch (Exception e) 
		{
			Log.i("MainActivity", "error"+e.toString());
		}
	}
	
	/*删除apk*/
	private void deleteApk(Context context,String pkgname)
	{
		try {
			Intent intent = new Intent();
		    intent.setAction(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.parse("package:"+pkgname), "gv_application/vnd.android.package-archive");
			context.startService(intent);
		} 
		catch (Exception e) 
		{
			Log.i("MainActivity", "error"+e.toString());
		}
	}
	
	/*返回值广播*/
	private DownStatusIntentReceiver mReceiver = new DownStatusIntentReceiver();
	private class DownStatusIntentReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_START")) {	
				/*name.setText(intent.getStringExtra("app_name")+" 开始安装");
				progressBar.setProgress(0);
				progressBar.setMax(40);*/
			}else if(intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_PROGRESS")){
				/*name.setText(intent.getStringExtra("app_name")+" 正在安装");
				progressBar.setProgress(intent.getIntExtra("current_progress", 0));
				progressBar.setMax(intent.getIntExtra("total_progress", 40));*/
			}else if(intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_COMPLETE")){
				/*progressBar.setProgress(40);
				progressBar.setMax(40);
				name.setText(intent.getStringExtra("app_name")+" 安装完成");*/
			}
			else if(intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_START")){
			}
			else if(intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_COMPLETE")){
			}
			else if(intent.getAction().equals("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_PROGRESS")){
			}
		}
	}
	
	/*注册广播*/
	private void registerIntentReceivers(Context context) {
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_START");
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_COMPLETE");
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.INSTALL_PROGRESS");
		
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_START");
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_COMPLETE");
		filter.addAction("com.szgvtv.ead.framework.packageinstaller.InstallerInter.DELETE_PROGRESS");
		context.registerReceiver(mReceiver, filter);
	}
	
	/*取消广播*/
	private void unregisterIntentReceivers(Context context) {
		if (mReceiver != null) {
			context.unregisterReceiver(mReceiver);
			mReceiver = null;
		}
	}
}
