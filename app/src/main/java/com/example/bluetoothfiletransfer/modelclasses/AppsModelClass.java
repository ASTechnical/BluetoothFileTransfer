package com.example.bluetoothfiletransfer.modelclasses;

import android.graphics.drawable.Drawable;

import com.google.android.gms.ads.nativead.NativeAd;

public class AppsModelClass {
    private Drawable appIcon;
    private String appName;
    private String appPkgName;
    private String appSize;
    boolean isSelected;

    public AppsModelClass() {
    }

    public AppsModelClass(String str, String str2, Drawable drawable, String str3, boolean z) {
        this.appName = str;
        this.appSize = str2;
        this.appIcon = drawable;
        this.appPkgName = str3;
        this.isSelected = z;
    }


    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String str) {
        this.appName = str;
    }

    public String getAppSize() {
        return this.appSize;
    }

    public void setAppSize(String str) {
        this.appSize = str;
    }

    public Drawable getAppIcon() {
        return this.appIcon;
    }

    public void setAppIcon(Drawable drawable) {
        this.appIcon = drawable;
    }

    public String getAppPkgName() {
        return this.appPkgName;
    }

    public void setAppPkgName(String str) {
        this.appPkgName = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
