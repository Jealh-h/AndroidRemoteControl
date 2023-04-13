package com.example.javaremotecontroller.adapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlueToothDeviceListAdapter extends RecyclerView.Adapter<BlueToothDeviceListAdapter.MyViewHolder> {

    Context context;
    ArrayList<BluetoothDevice> devicesList;

    public BlueToothDeviceListAdapter(Context context, ArrayList<BluetoothDevice> devicesList) {
        this.context = context;
        this.devicesList = devicesList;
    }

    @NonNull
    @Override
    public BlueToothDeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.context).inflate(R.layout.blue_tooth_device_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BlueToothDeviceListAdapter.MyViewHolder holder, int position) {
        // 设置数据
        holder.deviceName.setText(devicesList.get(position).getName());
        holder.deviceIp.setText(devicesList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return this.devicesList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName, deviceIp;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.blue_tooth_device_name);
            deviceIp = itemView.findViewById(R.id.blue_tooth_device_ip);
        }
    }
}
