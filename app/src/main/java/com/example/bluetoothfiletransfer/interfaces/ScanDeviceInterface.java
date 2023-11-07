package com.example.bluetoothfiletransfer.interfaces;

import android.bluetooth.BluetoothDevice;

import com.example.bluetoothfiletransfer.modelclasses.BltDeviceEntity;

public interface ScanDeviceInterface {
    void onItemClick(BluetoothDevice bluetoothDevice, int position, BltDeviceEntity bltDeviceEntity, String address);
}
