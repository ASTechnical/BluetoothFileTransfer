package com.example.bluetoothfiletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.adapters.ScanDeviceAdapter;
import com.example.bluetoothfiletransfer.databinding.ActivityScanDeviceBinding;
import com.example.bluetoothfiletransfer.interfaces.BltConnectStateListener;
import com.example.bluetoothfiletransfer.interfaces.ScanDeviceInterface;
import com.example.bluetoothfiletransfer.modelclasses.BltDeviceEntity;
import com.example.bluetoothfiletransfer.receivers.BltConnectDeviceReceiver;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ScanDeviceActivity extends AppCompatActivity implements ScanDeviceInterface {
    ActivityScanDeviceBinding binding;
    BluetoothAdapter bluetoothAdapter;
    ScanDeviceAdapter scanDeviceAdapter;
    ArrayList<BltDeviceEntity> data;
    BltConnectDeviceReceiver bltConnectDeviceReceiver;
    boolean result;
    BluetoothDevice device;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScanDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        data = new ArrayList<>();
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();

    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Toast.makeText(context, "reciever" + action, Toast.LENGTH_SHORT).show();
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                //discovery starts, we can show progress dialog or perform other tasks
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                //discovery finishes, dismis progress dialog
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                //bluetooth device found
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                data.add(new BltDeviceEntity(device.getName(), device.getAddress(), device));

                setRecyclerViewData(data);

            }
        }
    };

    public void setRecyclerViewData(ArrayList<BltDeviceEntity> dataa) {
        scanDeviceAdapter = new ScanDeviceAdapter(ScanDeviceActivity.this, dataa, this);
        scanDeviceAdapter.notifyDataSetChanged();
        binding.recyclerView.setAdapter(scanDeviceAdapter);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onItemClick(BluetoothDevice bluetoothDevice, int position, BltDeviceEntity scanDeviceEntity, String address) {
        int pos = position;
        bltConnectDeviceReceiver = new BltConnectDeviceReceiver(new BltConnectStateListener() {
            @Override
            public void onConnectionStateChanged(BluetoothDevice bluetoothDevice, int bondState) {
                switch (bondState) {
                    case BluetoothDevice.BOND_BONDED:
                        scanDeviceAdapter.updateViews(position, new BltDeviceEntity(device.getName(), device.getAddress(), device));
                        data.remove(position);
                        scanDeviceAdapter.notifyItemRemoved(position);
                        // Bluetooth pairing successful
//                Log.d("TAG", "Bluetooth pairing successful for device: " + device.getName() + "position" + position);
                        Toast.makeText(ScanDeviceActivity.this, "Bluetooth pairing successful for device: ", Toast.LENGTH_SHORT).show();

                        break;
                    case BluetoothDevice.BOND_BONDING:
                        // Bluetooth pairing in progress
//                Log.d("TAG", "Bluetooth pairing in progress for device: " + device.getName());
                        Toast.makeText(ScanDeviceActivity.this, "Bluetooth pairing in progress for device: ", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        // Bluetooth pairing failed
//                Log.d("TAG", "Bluetooth pairing failed for device: " + device.getName());
                        Toast.makeText(ScanDeviceActivity.this, "Bluetooth pairing failed for device:", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(bltConnectDeviceReceiver, filter);
        if (device.getBondState() == device.BOND_BONDED) {
            Log.d("TAG", "onItemClick: bonded");
            BluetoothDevice bluetoothDevice1 = bluetoothAdapter.getRemoteDevice(address);
            if (bluetoothDevice1 != null) {
                unPair(bluetoothDevice1, position);

            }
        } else {
            Log.d("TAG", "onItemClick: not bonded" + bluetoothDevice);
            result = bluetoothDevice.createBond();
            if (bluetoothDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                Log.d("TAG", "onItemClick: not bonded");
            }
//

        }

    }

    @SuppressLint("MissingPermission")
    public void unPair(BluetoothDevice device, int position) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);
            Log.d("TAG", "Unpaired from device: " + device.getName());
            Toast.makeText(this, "Unpaired from device: " + device.getName(), Toast.LENGTH_SHORT).show();
            scanDeviceAdapter.updateViews(position, new BltDeviceEntity(device.getName(), device.getAddress(), device));

        } catch (Exception e) {
            Log.e("TAG", "Failed to unpair from device: " + device.getName(), e);
        }

    }
}
