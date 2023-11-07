package com.example.bluetoothfiletransfer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetoothfiletransfer.R;
import com.example.bluetoothfiletransfer.interfaces.ScanDeviceInterface;
import com.example.bluetoothfiletransfer.modelclasses.BltDeviceEntity;

import java.util.ArrayList;

public class ScanDeviceAdapter extends RecyclerView.Adapter<ScanDeviceAdapter.ScanDeviceHolder> {
    Context context;
    ArrayList<BltDeviceEntity> list;
    ScanDeviceInterface scanDeviceInterface;

    public ScanDeviceAdapter(Context context, ArrayList<BltDeviceEntity> list, ScanDeviceInterface scanDeviceInterface) {
        this.context = context;
        this.list = list;
        this.scanDeviceInterface = scanDeviceInterface;
    }

    @NonNull
    @Override
    public ScanDeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bluetooth_device_list_items, parent, false);
        return new ScanDeviceAdapter.ScanDeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScanDeviceHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(list.get(position).getDeviceName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanDeviceInterface.onItemClick(list.get(position).getBluetoothDevice(), position, list.get(position), list.get(position).getDeviceName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void updateViews(int position, BltDeviceEntity bltDeviceEntity) {
        list.set(position, bltDeviceEntity);
        notifyItemChanged(position);

    }

    public class ScanDeviceHolder extends RecyclerView.ViewHolder {
        TextView name;

        public ScanDeviceHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }
}
