package com.example.javaremotecontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.ui.activity.WiFiStopWatch;
import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.javaremotecontroller.databinding.ActivityBlueToothConnectStateBinding;

import java.util.ArrayList;

public class BlueToothConnectState extends AppCompatActivity implements View.OnClickListener {

    private ActivityBlueToothConnectStateBinding binding;
    private RecyclerView recyclerView;
    private RecyclerView connectedRecycleView;
    private BlueToothDeviceListAdapter blueToothDeviceListAdapter;
    private BlueToothDeviceListAdapter blueToothDeviceConnectedListAdapter;
    private String TAG = "DEBUG_LIFE_CYCLE";
    private ArrayList<BluetoothDevice> scanDevicesList = new ArrayList<>();
    private ArrayList<BluetoothDevice> connectedDevicesList = new ArrayList<>();
    private TextView connectedCountView;
    private CardView refreshCircle;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBlueToothConnectStateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        util.immersionStatusBar(this);

        Toolbar toolbar = binding.blueToothConnectStateToolbar;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void init() {
        Context context = this;
        recyclerView = findViewById(R.id.blue_tooth_scan_device_recycler_view);
        refreshCircle = findViewById(R.id.refresh_blue_device_list);
        connectedRecycleView = findViewById(R.id.blue_tooth_connected_device_list);
        connectedCountView = findViewById(R.id.bt_connect_count);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        registerReceiver(mReceiver, intentFilter);

        blueToothDeviceListAdapter = new BlueToothDeviceListAdapter(BlueToothConnectState.this, scanDevicesList);
        blueToothDeviceListAdapter.setOnItemClick(new BlueToothDeviceListAdapter.ListItemClick() {
            @Override
            public void onClick(View v, ArrayList<BluetoothDevice> devList, BluetoothDevice device) {
                ToastUtils.showToast(context, "配对：" + device.getName());
                BlueToothHelper.getInstance().createBond(device);
            }
        });
        recyclerView.setAdapter(blueToothDeviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(BlueToothConnectState.this));

        blueToothDeviceConnectedListAdapter = new BlueToothDeviceListAdapter(BlueToothConnectState.this, connectedDevicesList);
        connectedRecycleView.setAdapter(blueToothDeviceConnectedListAdapter);
        connectedRecycleView.setLayoutManager(new LinearLayoutManager(BlueToothConnectState.this));

        connectedDevicesList.clear();
        connectedDevicesList.addAll(BlueToothHelper.getInstance().getBondedDevices());
        updateConnectedView();

        BlueToothHelper.getInstance().startDiscovery(this);
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

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e(TAG, "onReceive: " + action);
            switch (action) {
                case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                    ToastUtils.showToast(BlueToothConnectState.this, "扫描开始");
                    Animation animation = AnimationUtils.loadAnimation(BlueToothConnectState.this, R.anim.rotate);
                    refreshCircle.startAnimation(animation);
                    blueToothDeviceListAdapter.notifyDataSetChanged();
                    break;
                case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                    refreshCircle.clearAnimation();
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    // 已连接
//                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                    connectedDevicesList.add(device);
//                    updateConnectedView();
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    BluetoothDevice disconnectDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    ArrayList newList = new ArrayList();
                    Log.e(TAG, "ACTION_ACL_DISCONNECTED: " + disconnectDevice.getAddress());
                    for(BluetoothDevice b : connectedDevicesList) {
                        Log.e(TAG, String.valueOf("ACTION_ACL_DISCONNECTED: " + b.getAddress() + b.getAddress() == disconnectDevice.getAddress()));
                        if(!b.getAddress().equals(disconnectDevice.getAddress())){
                            newList.add(b);
                        }
                    }
                    connectedDevicesList.clear();
                    connectedDevicesList.addAll(newList);
                    updateConnectedView();
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                    Log.e(TAG, "ACTION_ACL_DISCONNECT_REQUESTED: ");
                    break;
                case BluetoothDevice.ACTION_FOUND:
                    // 已扫描到
                    BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(device1.getBondState() != BluetoothDevice.BOND_BONDED){
                        scanDevicesList.add(device1);
                        blueToothDeviceListAdapter.notifyDataSetChanged();
                    }
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    if(remoteDevice == null) {
                        ToastUtils.showToast(BlueToothConnectState.this,"无设备");
                        return;
                    }
                    int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                    if(status == BluetoothDevice.BOND_BONDED) {
                        ToastUtils.showToast(BlueToothConnectState.this,"已绑定" + remoteDevice.getName());
                    } else if(status == BluetoothDevice.BOND_BONDING) {
                        ToastUtils.showToast(BlueToothConnectState.this,"正在绑定" + remoteDevice.getName());
                    } else if(status == BluetoothDevice.BOND_NONE) {
                        ToastUtils.showToast(BlueToothConnectState.this,"未绑定" + remoteDevice.getName());
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e(TAG, "onRestart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        BlueToothHelper.getInstance().cancelDiscovery();
        Log.e(TAG, "onPause: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop: ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        BlueToothHelper.getInstance().cancelDiscovery();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refresh_blue_device_list:
                BlueToothHelper.getInstance().startDiscovery(this);
                scanDevicesList.clear();
                blueToothDeviceListAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void updateConnectedView() {
        blueToothDeviceConnectedListAdapter.notifyDataSetChanged();
        try {
            connectedCountView.setText(connectedDevicesList.size() + "");
        } catch (Exception e) {
            Log.e(TAG, "ErrorReceive:" + e.toString());
        }
    }
}