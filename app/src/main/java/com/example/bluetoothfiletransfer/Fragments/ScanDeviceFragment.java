package com.example.bluetoothfiletransfer.Fragments;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.bluetoothfiletransfer.Activities.PairedDevicesActivity;
import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.databinding.FragmentScanDeviceBinding;

import java.util.Set;


public class ScanDeviceFragment extends Fragment
{
    FragmentScanDeviceBinding binding;
    BluetoothAdapter bluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    public static int REQUEST_ENABLE_BT = 1;
    public static int REQUEST_DISABLE_BT = 2;
    BroadcastReceiver bluetoothReceiver;

    @SuppressLint("MissingPermission")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        binding = FragmentScanDeviceBinding.inflate(inflater, container, false);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        pairedDevices = bluetoothAdapter.getBondedDevices();
//        for (BluetoothDevice bb:pairedDevices){
//        }
        binding.pairedB.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!bluetoothAdapter.isEnabled())
                {
                    Toast.makeText(requireContext(), "Please Bluetooth On First", Toast.LENGTH_SHORT).show();
                } else if (bluetoothAdapter.isEnabled())
                {
                    startActivity(new Intent(getActivity(), PairedDevicesActivity.class));
                }

            }
        });
        binding.scanBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!bluetoothAdapter.isEnabled())
                {
                    Toast.makeText(requireContext(), "Please Bluetooth On First", Toast.LENGTH_SHORT).show();
                } else if (bluetoothAdapter.isEnabled())
                {
                    startActivity(new Intent(getActivity(), PairedDevicesActivity.class));
                }
            }
        });

        binding.bluetoothSwitchBtn.setOnClickListener(new View.OnClickListener()
        {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v)
            {
                if (!bluetoothAdapter.isEnabled())
                {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    binding.bluetoothSwitchBtn.setChecked(true);
                   // bluetoothAdapter.isEnabled();
                } else if (bluetoothAdapter.isEnabled())
                {

                    boolean isBluetoothDisabled = bluetoothAdapter.disable();

                    if (isBluetoothDisabled) {
                        // Bluetooth is successfully disabled
                        binding.bluetoothSwitchBtn.setChecked(false);
                    }

                }


            }
        });

        binding.scanBtn.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in_out));
        return binding.getRoot();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT)
        {
            if (resultCode == RESULT_OK)
            {
                // Bluetooth is successfully enabled
                Toast.makeText(requireContext(), "Bluetooth is successfully enabled", Toast.LENGTH_SHORT).show();
                binding.bluetoothSwitchBtn.setChecked(true);
            } else if (resultCode==RESULT_CANCELED)
            {
                Toast.makeText(requireContext(), "Bluetooth is successfully disabled", Toast.LENGTH_SHORT).show();
                binding.bluetoothSwitchBtn.setChecked(false);
            }

        }

    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (bluetoothAdapter.isEnabled())
        {
            binding.bluetoothSwitchBtn.setChecked(true);
        }

        if (!bluetoothAdapter.isEnabled())
        {
            binding.bluetoothSwitchBtn.setChecked(false);
        }


    }

    @Override
    public void onPause()
    {
        super.onPause();
        // Unregister the BroadcastReceiver to avoid memory leaks
        // requireContext().unregisterReceiver(bluetoothReceiver);
    }
}