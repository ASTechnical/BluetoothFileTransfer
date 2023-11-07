package com.example.bluetoothfiletransfer;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.databinding.FragmentScanDeviceBinding;

import java.util.Set;


public class ScanDeviceFragment extends Fragment {
    FragmentScanDeviceBinding binding;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    int REQUEST_ENABLE_BT = 1;
    public static int REQUSET_ENABLE_BT = 1;

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentScanDeviceBinding.inflate(inflater, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        pairedDevices = bluetoothAdapter.getBondedDevices();
//        for (BluetoothDevice bb:pairedDevices){
//        }
        binding.pairedB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PairedDevicesActivity.class));
            }
        });
        binding.scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PairedDevicesActivity.class));
            }
        });
        binding.bluetoothSwitchBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (!bluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else if (bluetoothAdapter.isEnabled()) {
                    bluetoothAdapter.disable();
                    binding.bluetoothSwitchBtn.setChecked(false);
//                    }
                }


            }
        });
        binding.scanBtn.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_out));
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_ENABLE_BT) {
            if (resultCode == RESULT_OK) {

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if (bluetoothAdapter.isEnabled()) {
            binding.bluetoothSwitchBtn.setChecked(true);
        }
        if (!bluetoothAdapter.isEnabled()) {
            binding.bluetoothSwitchBtn.setChecked(false);
        }


    }
}