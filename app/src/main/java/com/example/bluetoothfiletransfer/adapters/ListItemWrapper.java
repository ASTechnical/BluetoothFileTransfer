package com.example.bluetoothfiletransfer.adapters;

import com.example.bluetoothfiletransfer.modelclasses.AppsModelClass;
import com.google.android.gms.ads.nativead.NativeAd;

public class ListItemWrapper {
    // Constants for view types
    public static final int VIEW_TYPE_APPS = 0;
    public static final int VIEW_TYPE_NATIVE_AD = 1;

    private AppsModelClass appsModel;
    private NativeAd nativeAd;
    private int viewType;

    public ListItemWrapper(AppsModelClass appsModel) {
        this.appsModel = appsModel;
        this.viewType = VIEW_TYPE_APPS;
    }

    public ListItemWrapper(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
        this.viewType = VIEW_TYPE_NATIVE_AD;
    }

    public AppsModelClass getAppsModel() {
        return appsModel;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public int getViewType() {
        return viewType;
    }
}
