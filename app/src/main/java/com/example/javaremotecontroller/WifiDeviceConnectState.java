package com.example.javaremotecontroller;

import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.os.Bundle;

import com.example.javaremotecontroller.adapter.WifiDeviceListAdapter;
import com.example.javaremotecontroller.communication.WiFiHelper;
import com.example.javaremotecontroller.ui.activity.WiFiStopWatch;
import com.example.javaremotecontroller.util.Esp8266Helper;
import com.example.javaremotecontroller.util.util;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.javaremotecontroller.databinding.ActivityWifiDeviceConnectStateBinding;

import java.util.ArrayList;
import java.util.List;

public class WifiDeviceConnectState extends AppCompatActivity {

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

        Toolbar toolbar = binding.wifiConnectStateToolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view -> finish());

        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_single_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, WiFiStopWatch.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
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

        util.immersionStatusBar(this);
    }
}