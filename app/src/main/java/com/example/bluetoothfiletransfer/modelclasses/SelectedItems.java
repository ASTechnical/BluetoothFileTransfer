package com.example.bluetoothfiletransfer.modelclasses;

import android.graphics.drawable.Drawable;

public class SelectedItems {
    private int adapterPosition;
    private String fragName;
    private String imgPath;
    private Drawable itemIcon;
    private String itemName;
    private String itemSize;

    public SelectedItems(String str, int i, String str2, String str3) {
        this.imgPath = str;
        this.adapterPosition = i;
        this.fragName = str2;
        this.itemSize = str3;
    }

    public String getImgPath() {
        return this.imgPath;
    }

    public void setImgPath(String str) {
        this.imgPath = str;
    }

    public int getAdapterPosition() {
        return this.adapterPosition;
    }

    public void setAdapterPosition(int i) {
        this.adapterPosition = i;
    }

    public String getFragName() {
        return this.fragName;
    }

    public void setFragName(String str) {
        this.fragName = str;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String str) {
        this.itemName = str;
    }

    public String getItemSize() {
        return this.itemSize;
    }

    public void setItemSize(String str) {
        this.itemSize = str;
    }

    public Drawable getItemIcon() {
        return this.itemIcon;
    }

    public void setItemIcon(Drawable drawable) {
        this.itemIcon = drawable;
    }
}
