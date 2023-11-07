package com.example.bluetoothfiletransfer.modelclasses;

public class AllItemModelClass {
    boolean isSelected;
    String itemName;
    String itemPath;
    String itemSize;

    public String getImgPath() {
        return this.itemPath;
    }

    public void setImgPath(String str) {
        this.itemPath = str;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }

    public AllItemModelClass(String str, String str2, boolean z, String str3) {
        this.itemPath = str;
        this.isSelected = z;
        this.itemSize = str2;
        this.itemName = str3;
    }

    public String getItemSize() {
        return this.itemSize;
    }

    public void setItemSize(String str) {
        this.itemSize = str;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String str) {
        this.itemName = str;
    }
}
