package com.example.bluetoothfiletransfer.interfaces;

import android.bluetooth.BluetoothDevice;

public interface PairedDeviceInterface {
    void onClickDevice(BluetoothDevice bluetoothDevice, int pos);
}
