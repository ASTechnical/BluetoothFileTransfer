package com.example.bluetoothfiletransfer.utils;

import android.util.Log;

import java.io.File;
import java.text.DecimalFormat;

public class Constants {
    public static final String APPS = "apps";
    public static final String FILES = "files";
    public static final String MUSIC = "music";
    public static final String PICS = "pics";
    public static final String VIDEOS = "video";

    public static String getFileSize(String str) {
        String stringSizeLengthFile = getStringSizeLengthFile(new File(str).length());
        Log.d("size", "getFileSize: size:  " + stringSizeLengthFile);
        return stringSizeLengthFile;
    }

    public static String getStringSizeLengthFile(long j) {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        DecimalFormat decimalFormat2 = new DecimalFormat("0");
        float f = (float) j;
        if (f < 1048576.0f) {
            return decimalFormat2.format((double) (f / 1024.0f)) + " Kb";
        }
        if (f < 1.07374182E9f) {
            return decimalFormat.format((double) (f / 1048576.0f)) + " Mb";
        }
        return f < 1.09951163E12f ? decimalFormat.format((double) (f / 1.07374182E9f)) + " Gb" : "";
    }
}
