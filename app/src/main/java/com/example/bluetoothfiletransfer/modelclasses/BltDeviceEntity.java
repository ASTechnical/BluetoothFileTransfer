package com.example.bluetoothfiletransfer.modelclasses;

import android.bluetooth.BluetoothDevice;

public class BltDeviceEntity {
    String deviceName, deviceAddress;
BluetoothDevice bluetoothDevice;

    public BltDeviceEntity(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }


    public BltDeviceEntity(String deviceName, String deviceAddress, BluetoothDevice bluetoothDevice) {
        this.deviceName = deviceName;
        this.deviceAddress = deviceAddress;
        this.bluetoothDevice = bluetoothDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
