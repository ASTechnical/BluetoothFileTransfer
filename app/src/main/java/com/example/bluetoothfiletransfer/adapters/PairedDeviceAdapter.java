package com.example.bluetoothfiletransfer.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothfiletransfer.Activities.ForgetDeviceActivity;
import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.interfaces.PairedDeviceInterface;
import com.example.bluetoothfiletransfer.modelclasses.BltDeviceEntity;

import java.util.ArrayList;

public class PairedDeviceAdapter extends RecyclerView.Adapter<PairedDeviceAdapter.PairedDeviceHolder> {
    Context context;
    ArrayList<BltDeviceEntity> list;
    PairedDeviceInterface pairedDeviceInterface;

    public PairedDeviceAdapter(Context context, ArrayList<BltDeviceEntity> list, PairedDeviceInterface pairedDeviceInterface) {
        this.context = context;
        this.list = list;
        this.pairedDeviceInterface = pairedDeviceInterface;
    }

    @NonNull
    @Override
    public PairedDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_list_items, parent, false);
        return new PairedDeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PairedDeviceHolder holder, @SuppressLint("RecyclerView") int position) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        holder.name.setText(list.get(position).getBluetoothDevice().getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(context, ForgetDeviceActivity.class);
                in.putExtra("device", list.get(position).getBluetoothDevice());
                in.putExtra("position",position);
                context.startActivity(in);
//                pairedDeviceInterface.onClickDevice(list.get(position).getBluetoothDevice(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PairedDeviceHolder extends RecyclerView.ViewHolder {
        TextView name;

        public PairedDeviceHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
