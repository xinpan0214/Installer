package com.szgvtv.ead.framework.packageinstaller;

import java.io.File;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageParser;
import android.content.pm.PackageUserState;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;

public class UnInstallerActivity extends Activity {
	private static final String TAG = "UnInstallerActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startDelete(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		startDelete(intent);
	}

	private void startDelete(Intent intent) {
		PackageManager mPm;
		Uri mPackageURI;
		ApplicationInfo mAppInfo = null;
		mPackageURI = intent.getData();

		if (mPackageURI != null) {
			String packageName = mPackageURI.getEncodedSchemeSpecificPart();
			if (packageName == null) {
				Log.e(TAG, "=======input null package:" + packageName);
				return;
			}

			mPm = getPackageManager();
			boolean errFlag = false;
			try {
				mAppInfo = mPm.getApplicationInfo(packageName,
						PackageManager.GET_UNINSTALLED_PACKAGES);
			} catch (NameNotFoundException e) {
				errFlag = true;
				Log.e(TAG, "Invalid package name:" + packageName);
			}

			String className = mPackageURI.getFragment();
			ActivityInfo activityInfo = null;
			if (className != null) {
				try {
					activityInfo = mPm.getActivityInfo(new ComponentName(
							packageName, className), 0);
				} catch (NameNotFoundException e) {
					Log.e(TAG, "Invalid className name:" + className);
					errFlag = true;
				}
			}
			if (mAppInfo != null && !errFlag) {
				InstallerInfo info = new InstallerInfo();
				info.setPkgName(mAppInfo.packageName);
				info.setInstallFlag(false);
				InstallerManage.getInstance(getApplicationContext())
						.addInstallList(info);
			} else {
				Log.e(TAG, "Invalid packageName or componentName in "
						+ mPackageURI.toString());
			}

		}
	}
}
