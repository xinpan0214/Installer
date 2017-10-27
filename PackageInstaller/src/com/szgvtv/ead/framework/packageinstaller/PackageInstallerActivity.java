
package com.szgvtv.ead.framework.packageinstaller;
import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

public class PackageInstallerActivity extends Activity {
	private static final String TAG = "PackageInstallerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startInstall(getIntent());
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		startInstall(intent);
	}
	
	private void startInstall(Intent intent){
		try {
			Uri mPackageURI;
			PackageInfo mPkgInfo;
			mPackageURI = intent.getData();
			if(mPackageURI != null){
				if(mPackageURI.getScheme().equals("file")){
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
		            	InstallerManage.getInstance(getApplicationContext()).addInstallList(info);
		            }else {
		            	Log.w(TAG, "======error apk file======  "+sourceFile.getAbsolutePath());
					}
				}		
			}
		} catch (Exception e) {
			Log.w(TAG, "======error apk file======  " + e.toString());
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
}
