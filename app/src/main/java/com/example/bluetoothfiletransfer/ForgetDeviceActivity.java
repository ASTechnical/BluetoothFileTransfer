package com.example.bluetoothfiletransfer;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.view.View;

import com.example.bluetoothfiletransfer.databinding.ActivityForgetDeviceBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ForgetDeviceActivity extends AppCompatActivity {
    ActivityForgetDeviceBinding binding;
    BluetoothDevice bluetoothDevice;
    String getevice;
    int getPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgetDeviceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bluetoothDevice = getIntent().getParcelableExtra("device");
        getPosition = getIntent().getIntExtra("position", 0);
        binding.forgetDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgetDevice(bluetoothDevice);
                PairedDevicesActivity.pairedDeviceAdapter.notifyItemRemoved(getPosition);
                PairedDevicesActivity.data.remove(getPosition);
                finish();
            }
        });

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
}