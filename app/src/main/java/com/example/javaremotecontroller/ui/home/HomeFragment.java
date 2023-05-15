package com.example.javaremotecontroller.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.BlueToothConnectState;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.WifiDeviceConnectState;
import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.communication.WiFiHelper;
import com.example.javaremotecontroller.databinding.FragmentHomeBinding;
import com.example.javaremotecontroller.util.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private FragmentHomeBinding binding;
    private BlueToothHelper blueToothHelper;
    private WiFiHelper wifiHelper;
    private NotificationManager notificationManager;
    RecyclerView recyclerView;
    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();
    private String TAG = "DEBUG";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        blueToothHelper = new BlueToothHelper();
        wifiHelper = new WiFiHelper(getActivity());
        // 蓝牙设备列表
        recyclerView = root.findViewById(R.id.blue_tooth_device_recycler_view);
        BlueToothDeviceListAdapter blueToothDeviceListAdapter =
                new BlueToothDeviceListAdapter(getActivity(),
                        bluetoothDeviceArrayList);
        recyclerView.setAdapter(blueToothDeviceListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

//        final Switch wifiSwitch = root.findViewById(R.id.wifi_switch_home);
//        wifiInfoTextView = root.findViewById(R.id.wifi_info);

//        wifiSwitch.setOnCheckedChangeListener(this);

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        // 大于 安卓 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("cid", "通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        initEvent();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
            } else {
                startDiscovery();
            }
        } else {
            startDiscovery();
        }
    }

    private void startDiscovery() {
        Log.e(TAG, "startDiscovery: ");
        BlueToothHelper.getInstance().startDiscovery(getActivity());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        blueToothHelper.open();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NotNull int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            if (permissions[i].equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                int result = grantResults[i];
                if (result == PackageManager.PERMISSION_GRANTED) {
                    startDiscovery();
                } else {
                    requestPermission();
                }
            }
        }
    }

    /**
     * 初始化 wifi 连接状态文案
     */
    private void initWifiState() {
        View root = binding.getRoot();
        TextView wifiConnectState = root.findViewById(R.id.wifi_connect_state_text);
        TextView bluetoothConnectState = root.findViewById(R.id.bluetooth_connect_state_text);
        if(wifiHelper.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
            wifiConnectState.setText("已关闭");
        }else if(wifiHelper.isWifiConnect()) {
            wifiConnectState.setText("已连接");
        }else {
            wifiConnectState.setText("未连接");
        }
        bluetoothConnectState.setText(BlueToothHelper.getInstance().isEnabled()?"已开启" : "未开启");
    }

    private void initEvent() {
        View root = binding.getRoot();
        CardView blueToothCard = root.findViewById(R.id.bluetooth_home_card_view);
        CardView wifiCard = root.findViewById(R.id.wifi_home_card_view);
        blueToothCard.setOnClickListener(this);
        wifiCard.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wifi_home_card_view:
                Intent wifi_intent = new Intent(getActivity(), WifiDeviceConnectState.class);
                startActivity(wifi_intent);
                break;
            case R.id.bluetooth_home_card_view:
                Intent intent = new Intent(getActivity(), BlueToothConnectState.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    class BtFoundReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e(TAG, "onReceive: " + device.toString() );
                //判断是否配对过
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    bluetoothDeviceArrayList.add(device);
                    Log.e(TAG, "onReceive: " + device.getName() );
                }
            }else if(BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.ACTION_ACL_CONNECTED);
                Log.e(TAG, "onReceive: " + device.getName() );
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initWifiState();
    }
}

