package com.example.bluetoothfiletransfer.interfaces;

import android.bluetooth.BluetoothDevice;

public interface BltConnectStateListener {
    void onConnectionStateChanged(BluetoothDevice bluetoothDevice, int bondState);
}
