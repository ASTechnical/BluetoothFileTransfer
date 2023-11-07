package com.example.bluetoothfiletransfer.receivers;

import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bluetoothfiletransfer.interfaces.BltConnectStateListener;

public class BltConnectDeviceReceiver extends BroadcastReceiver {

BltConnectStateListener bltConnectStateListener;

    public BltConnectDeviceReceiver(BltConnectStateListener bltConnectStateListener) {
        this.bltConnectStateListener = bltConnectStateListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(intent.getAction())){
            BluetoothDevice bluetoothDevice=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            int connectionState=intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
            if(bluetoothDevice!=null && bltConnectStateListener!=null){
                bltConnectStateListener.onConnectionStateChanged(bluetoothDevice, connectionState);
            }
        }
    }
}
