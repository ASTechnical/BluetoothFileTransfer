package com.example.bluetoothfiletransfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.adapters.PairedDeviceAdapter;
import com.example.bluetoothfiletransfer.adapters.ScanDeviceAdapter;
import com.example.bluetoothfiletransfer.databinding.ActivityPairedDevicesBinding;
import com.example.bluetoothfiletransfer.interfaces.BltConnectStateListener;
import com.example.bluetoothfiletransfer.interfaces.PairedDeviceInterface;
import com.example.bluetoothfiletransfer.interfaces.ScanDeviceInterface;
import com.example.bluetoothfiletransfer.modelclasses.BltDeviceEntity;
import com.example.bluetoothfiletransfer.receivers.BltConnectDeviceReceiver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

public class PairedDevicesActivity extends AppCompatActivity implements PairedDeviceInterface, ScanDeviceInterface {
    ActivityPairedDevicesBinding binding;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    public static PairedDeviceAdapter pairedDeviceAdapter;
    public static ArrayList<BltDeviceEntity> data;
    ScanDeviceAdapter scanDeviceAdapter;
    ArrayList<BltDeviceEntity> data2;
    BltConnectDeviceReceiver bltConnectDeviceReceiver;
    boolean result;
    BluetoothDevice device;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPairedDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        data = new ArrayList<>();
        data2 = new ArrayList<>();
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!bluetoothAdapter.isEnabled()) {
            binding.deviceName.setVisibility(View.GONE);
            binding.deviceName2.setVisibility(View.GONE);
        }
        pairedDevices = bluetoothAdapter.getBondedDevices();
        IntentFilter filter = new IntentFilter();

        filter.addAction(BluetoothDevice.ACTION_FOUND);

        registerReceiver(mReceiver, filter);
        bluetoothAdapter.startDiscovery();
        for (BluetoothDevice bb : pairedDevices) {
            data.add(new BltDeviceEntity(bb));
            pairedDeviceAdapter = new PairedDeviceAdapter(this, data, this);
            binding.recyclerView.setAdapter(pairedDeviceAdapter);
//            binding.recyclerView2.setAdapter(pairedDeviceAdapter);
        }
//        binding.forgetDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onClickDevice(BluetoothDevice bluetoothDevice, int pos) {
        binding.recyclerView.setVisibility(View.GONE);
//        binding.txt.setVisibility(View.VISIBLE);
//        binding.forgetDevice.setVisibility(View.VISIBLE);
        binding.deviceName.setText(bluetoothDevice.getName());
//        binding.forgetDevice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
        forgetDevice(bluetoothDevice);
////                pairedDeviceAdapter.notifyDataSetChanged();
//                data.remove(pos);
//                pairedDeviceAdapter.notifyItemRemoved(pos);
//                binding.recyclerView.setVisibility(View.VISIBLE);
//                binding.txt.setVisibility(View.GONE);
//                binding.deviceName.setText("Paired Devices");
//                binding.forgetDevice.setVisibility(View.GONE);
////                finish();
//            }
//        });

    }

    public void forgetDevice(BluetoothDevice bluetoothDevice) {
        Method method = null;
        try {
            method = bluetoothDevice.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(bluetoothDevice, (Object[]) null);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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
                data2.add(new BltDeviceEntity(device.getName(), device.getAddress(), device));

                setRecyclerViewData(data2);

            }
        }
    };

    public void setRecyclerViewData(ArrayList<BltDeviceEntity> dataa) {
        scanDeviceAdapter = new ScanDeviceAdapter(PairedDevicesActivity.this, dataa, this);
        scanDeviceAdapter.notifyDataSetChanged();
        binding.recyclerView2.setAdapter(scanDeviceAdapter);
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
                        data2.remove(position);
                        data.add(new BltDeviceEntity(bluetoothDevice));
                        scanDeviceAdapter.notifyItemRemoved(position);
                        pairedDeviceAdapter.notifyDataSetChanged();
                        // Bluetooth pairing successful
//                Log.d("TAG", "Bluetooth pairing successful for device: " + device.getName() + "position" + position);
                        Toast.makeText(PairedDevicesActivity.this, "Bluetooth pairing successful for device: ", Toast.LENGTH_SHORT).show();

                        break;
                    case BluetoothDevice.BOND_BONDING:
                        // Bluetooth pairing in progress
//                Log.d("TAG", "Bluetooth pairing in progress for device: " + device.getName());
                        Toast.makeText(PairedDevicesActivity.this, "Bluetooth pairing in progress for device: ", Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        // Bluetooth pairing failed
//                Log.d("TAG", "Bluetooth pairing failed for device: " + device.getName());
                        Toast.makeText(PairedDevicesActivity.this, "Bluetooth pairing failed for device:", Toast.LENGTH_SHORT).show();
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