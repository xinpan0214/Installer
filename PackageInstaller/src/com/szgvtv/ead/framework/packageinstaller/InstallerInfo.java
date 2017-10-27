package com.szgvtv.ead.framework.packageinstaller;


public class InstallerInfo {
	private String mAppName;				//应用名
	private String mPkgName;				//包名	
	private String mPath;					//安装包路径
	private boolean mInstallFlag = true;	//安装标志
	private int mCurProgress = 0;			//当前进度
	private int mTotalProgress = 0;		//总进度
	
	public String getAppName() {
		return mAppName;
	}
	public void setAppName(String mAppName) {
		this.mAppName = mAppName;
	}	
	public String getPkgName() {
		return mPkgName;
	}
	public void setPkgName(String mPkgName) {
		this.mPkgName = mPkgName;
	}
	public String getPath() {
		return mPath;
	}
	public void setPath(String mPath) {
		this.mPath = mPath;
	}
	public boolean isInstallFlag() {
		return mInstallFlag;
	}
	public void setInstallFlag(boolean mInstallFlag) {
		this.mInstallFlag = mInstallFlag;
	}
	public int getCurProgress() {
		return mCurProgress;
	}
	public void setCurProgress(int mCurProgress) {
		this.mCurProgress = mCurProgress;
	}
	public int getTotalProgress() {
		return mTotalProgress;
	}
	public void setTotalProgress(int mTotalProgress) {
		this.mTotalProgress = mTotalProgress;
	}
}
