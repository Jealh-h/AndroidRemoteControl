package com.example.javaremotecontroller;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.adapter.WifiDeviceListAdapter;
import com.example.javaremotecontroller.communication.WiFiHelper;
import com.example.javaremotecontroller.util.util;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.javaremotecontroller.databinding.ActivityWifiDeviceConnectStateBinding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WifiDeviceConnectState extends AppCompatActivity implements View.OnClickListener {

    private ActivityWifiDeviceConnectStateBinding binding;
    private WiFiHelper wifiHelper;
    private TextView wifiInfoTextView;
    private ArrayList<ScanResult> wifiList = new ArrayList();
    private WifiDeviceListAdapter wifiDeviceListAdapter;
    private RecyclerView recyclerView;
    private TextView wifiStateTextView;
    private String TAG = "WIFI_DEVICE_CONNECT_STATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityWifiDeviceConnectStateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        init();
    }

    private void init() {
        wifiHelper = new WiFiHelper(this);
        wifiInfoTextView = findViewById(R.id.wifi_info);
        wifiHelper.getConnectedIPs();
        wifiStateTextView = findViewById(R.id.wifi_detail_connect_state_text_view);
        recyclerView = findViewById(R.id.wifi_scan_device_recycler_view);


        wifiDeviceListAdapter = new WifiDeviceListAdapter(WifiDeviceConnectState.this, wifiList);
        recyclerView.setAdapter(wifiDeviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(WifiDeviceConnectState.this));

        if(wifiHelper.isWifiConnect()) {
            WifiInfo wifiInfo = wifiHelper.getConnectionInfo();
            StringBuilder sb = new StringBuilder();
            sb.append("mac地址:").append(wifiInfo.getMacAddress()).append("\n");
            sb.append("wifi名称:").append(wifiHelper.getSSID()).append("\n");
            sb.append("接入点的BSSID：").append(wifiInfo.getBSSID()).append("\n");
            sb.append("IP地址:").append(util.int2Decimal(wifiInfo.getIpAddress())).append("\n");
            wifiStateTextView.setText("已连接 WiFi 网络");
            wifiInfoTextView.setText(sb);
            wifiInfoTextView.setVisibility(View.VISIBLE);
        }

        List<ScanResult> scanList = wifiHelper.getScanResults();
        wifiList.addAll(scanList);
        wifiDeviceListAdapter.notifyDataSetChanged();
    }

    private void sendToESP(String msg) {
        WifiInfo wifiInfo = wifiHelper.getConnectionInfo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    InetAddress serverAddr = InetAddress.getByName("192.168.4.1"); // 输入ESP8266的IP地址
                    Socket socket = new Socket(serverAddr, 80);
                    // 发送指令
                    OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
                    writer.write(msg);
                    writer.flush();

                    // 读取响应
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = reader.readLine();

                    reader.close();
                    writer.close();
                    socket.close();
                    Log.e(TAG, "run: " + msg + response );
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "run: " + msg + e.toString() );
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.clean) {
            sendToESP("clean");
        }else {
            sendToESP("安卓发送时间：" + new Date().toString());
        }
    }
}