package com.example.javaremotecontroller.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.BlueToothDeviceDetail;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.util.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class WifiDeviceListAdapter extends RecyclerView.Adapter<WifiDeviceListAdapter.MyViewHolder> implements View.OnClickListener {

    Context context;
    ArrayList<ScanResult> devicesList;
    RecyclerView recyclerView;
    private ListItemClick listItemClick;

    public WifiDeviceListAdapter(Context context, ArrayList<ScanResult> devicesList) {
        this.context = context;
        this.devicesList = devicesList;
    }

    @NonNull
    @Override
    public WifiDeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if(recyclerView==null){
            recyclerView = (RecyclerView) parent;
        }
        View view = LayoutInflater.from(this.context).inflate(R.layout.wifi_device_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull WifiDeviceListAdapter.MyViewHolder holder, int position) {
        // 设置数据
        holder.deviceName.setText(devicesList.get(position).SSID == "" ?
                "未知设备" : devicesList.get(position).SSID);
        holder.deviceIp.setText(devicesList.get(position).BSSID);
    }

    @Override
    public int getItemCount() {
        return this.devicesList.size();
    }

    @Override
    public void onClick(View v) {
        if(this.listItemClick != null) {
            this.listItemClick.onClick(v, devicesList);
        }else {
            int position = recyclerView.getChildAdapterPosition(v);
            Toast.makeText(context, devicesList.get(position).SSID, Toast.LENGTH_SHORT).show();
//            Intent intent = new Intent(context, BlueToothDeviceDetail.class);
//            intent.putExtra(util.BLUE_TOOTH_DEVICE_CARRY_DATA_KEY, devicesList.get(position).BSSID);
//            context.startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setOnItemClick(ListItemClick listItemClick) {
        this.listItemClick = listItemClick;
    }

    public interface ListItemClick {
        void onClick(View v, ArrayList<ScanResult> devList);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView deviceName, deviceIp;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.wifi_device_name);
            deviceIp = itemView.findViewById(R.id.wifi_device_ip);
        }
    }
}

