package com.example.javaremotecontroller.adapter;


import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlueToothDeviceListAdapter extends RecyclerView.Adapter<BlueToothDeviceListAdapter.MyViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<BluetoothDevice> devicesList;
    RecyclerView recyclerView;

    public BlueToothDeviceListAdapter(Context context, ArrayList<BluetoothDevice> devicesList) {
        this.context = context;
        this.devicesList = devicesList;
        Log.e("TAG", "BlueToothDeviceListAdapter: "+ devicesList);
    }

    @NonNull
    @Override
    public BlueToothDeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(recyclerView==null){
            recyclerView = (RecyclerView) parent;
        }
        View view = LayoutInflater.from(this.context).inflate(R.layout.blue_tooth_device_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BlueToothDeviceListAdapter.MyViewHolder holder, int position) {
        // 设置数据
        holder.deviceName.setText(devicesList.get(position).getName() == null ?
                "未知设备" : devicesList.get(position).getName());
        holder.deviceIp.setText(devicesList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return this.devicesList.size();
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        Toast.makeText(context, devicesList.get(position).getAddress(), Toast.LENGTH_SHORT).show();
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
