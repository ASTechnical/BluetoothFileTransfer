package com.example.bluetoothfiletransfer.modelclasses;

import com.google.android.gms.ads.nativead.NativeAd;

public class AdModelClass {
    private NativeAd nativeAd;

    public AdModelClass(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }

    public NativeAd getNativeAd() {
        return nativeAd;
    }

    public void setNativeAd(NativeAd nativeAd) {
        this.nativeAd = nativeAd;
    }
}
