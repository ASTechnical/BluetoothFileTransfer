package com.example.bluetoothfiletransfer.modelclasses;

import java.util.ArrayList;

public class SelectedItemsArray {
    static int itemCount = 0;
    static ArrayList<SelectedItems> items = new ArrayList<>();

    public static SelectedItems getItemAt(int i) {
        return items.get(i);
    }

    public static void setSelectedItemByName(int i, SelectedItems selectedItems) {
        items.set(i, selectedItems);
    }

    public static void addItem(SelectedItems selectedItems) {
        items.add(selectedItems);
    }

    public static void removeItem(SelectedItems selectedItems) {
        String imgPath = selectedItems.getImgPath();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getImgPath().equals(imgPath)) {
                items.remove(i);
                return;
            }
        }
    }

    public static ArrayList<SelectedItems> getAllSelectedItems() {
        return items;
    }

    public static int getArraySize() {
        return items.size();
    }

    public static int getItemCount() {
        return itemCount;
    }

    public static void addItemCount() {
        itemCount++;
    }

    public static void minusItemCount() {
        int i = itemCount;
        if (i != 0) {
            itemCount = i - 1;
        }
    }

    public static void clearSelectedItemArray() {
        items.clear();
    }

    public static void setItemCountZero() {
        itemCount = 0;
    }
}
