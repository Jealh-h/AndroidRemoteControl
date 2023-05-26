package com.example.javaremotecontroller.adapter;


import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.BlueToothDeviceDetail;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.communication.BluetoothHidHelper;
import com.example.javaremotecontroller.ui.activity.ComputerActivity;
import com.example.javaremotecontroller.util.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BlueToothDeviceListAdapter extends RecyclerView.Adapter<BlueToothDeviceListAdapter.MyViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<BluetoothDevice> devicesList;
    RecyclerView recyclerView;
    private ListItemClick listItemClick;

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
        BluetoothDevice device = devicesList.get(position);
        // 设置数据
        holder.deviceName.setText(device.getName() == null ?
                "未知设备" : device.getName());
        holder.deviceIp.setText(device.getAddress());
        BluetoothClass bluetoothClass = device.getBluetoothClass();
        int deviceClass = bluetoothClass.getDeviceClass();
        Log.e("TAG", "onBindViewHolder: " + deviceClass + "--Name: " + device.getName());
        switch (deviceClass){
            case BluetoothClass.Device.COMPUTER_DESKTOP:
            case BluetoothClass.Device.COMPUTER_LAPTOP:
            case BluetoothClass.Device.COMPUTER_HANDHELD_PC_PDA:
                holder.imageView.setImageResource(R.drawable.ic_baseline_laptop_mac_24);
                break;
            case BluetoothClass.Device.PHONE_SMART:
                holder.imageView.setImageResource(R.drawable.ic_baseline_phone_iphone_24);
                break;
            case BluetoothClass.Device.AUDIO_VIDEO_HEADPHONES:
            case BluetoothClass.Device.AUDIO_VIDEO_WEARABLE_HEADSET:
                holder.imageView.setImageResource(R.drawable.ic_baseline_headset_24);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return this.devicesList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        BluetoothDevice device = devicesList.get(position);

        if(this.listItemClick != null) {
            this.listItemClick.onClick(v, devicesList, device);
        }else {
            BluetoothHidHelper.connect(device);
            Intent intent = new Intent(context, ComputerActivity.class);
            intent.putExtra(util.BLUE_TOOTH_DEVICE_CARRY_DATA_KEY, device.getAddress());
            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setOnItemClick(ListItemClick listItemClick) {
        this.listItemClick = listItemClick;
    }

    public interface ListItemClick {
        void onClick(View v, ArrayList<BluetoothDevice> devList, BluetoothDevice device);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName, deviceIp;
        ImageView imageView;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.blue_tooth_device_name);
            deviceIp = itemView.findViewById(R.id.blue_tooth_device_ip);
            imageView = itemView.findViewById(R.id.blue_tooth_device_type);
        }
    }
}
