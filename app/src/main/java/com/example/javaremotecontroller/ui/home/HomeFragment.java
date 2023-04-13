package com.example.javaremotecontroller.ui.home;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.javaremotecontroller.MainActivity;
import com.example.javaremotecontroller.R;
import com.example.javaremotecontroller.adapter.BlueToothDeviceListAdapter;
import com.example.javaremotecontroller.communication.BlueToothHelper;
import com.example.javaremotecontroller.communication.InfraredHelper;
import com.example.javaremotecontroller.communication.WiFiHelper;
import com.example.javaremotecontroller.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Set;

import static android.content.Context.NOTIFICATION_SERVICE;

public class HomeFragment extends Fragment implements CompoundButton.OnCheckedChangeListener {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private InfraredHelper infraredHelper = new InfraredHelper();
    private BlueToothHelper blueToothHelper;
    private WiFiHelper wifiManager;
    private NotificationManager notificationManager;
    private Notification notification;
    private TextView wifiInfoTextView;
    RecyclerView recyclerView;
    private ArrayList<BluetoothDevice> bluetoothDeviceArrayList;
    private String TAG = "DEBUG";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // 获取连接过的蓝牙设备
        initBlueToothDevice();

        blueToothHelper = new BlueToothHelper();
        wifiManager = new WiFiHelper(getActivity());
        // 蓝牙设备列表
        recyclerView = root.findViewById(R.id.blue_tooth_device_recycler_view);
        BlueToothDeviceListAdapter blueToothDeviceListAdapter =
                new BlueToothDeviceListAdapter(getActivity(),
                        bluetoothDeviceArrayList);
        recyclerView.setAdapter(blueToothDeviceListAdapter);

        final Switch wifiSwitch = root.findViewById(R.id.wifi_switch_home);
        wifiInfoTextView = root.findViewById(R.id.wifi_info);

        wifiSwitch.setOnCheckedChangeListener(this);

        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

        // 大于 安卓 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("cid", "通知", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(getActivity(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

        notification = new NotificationCompat.Builder(getActivity(), "cid")
                .setContentTitle("标题")
                .setContentText("这是内容")
                .setSmallIcon(R.drawable.ic_baseline_settings_24)
                .setContentIntent(pendingIntent)
                .build();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initBlueToothDevice() {
//        // 不支持蓝牙
//        if (!BlueToothHelper.getInstance().isSupport()) {
//            Toast.makeText(getActivity(), "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        BlueToothHelper.getInstance().open();
//        Set<BluetoothDevice> mBondedList = BlueToothHelper.getInstance().getBondedDevices();
//        Log.e(TAG, "initBlueToothDevice: " + mBondedList.toString());
//        for (BluetoothDevice device : mBondedList) {
//            bluetoothDeviceArrayList.add(device);
//        }
        BtFoundReceiver mBtFoundReceiver = new BtFoundReceiver();
        IntentFilter filter = new IntentFilter();
        //搜索结果
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //搜索完成
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mBtFoundReceiver, filter);

        if (!BlueToothHelper.getInstance().isSupport()) {
            Toast.makeText(getActivity(), "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
            return;
        }

        BlueToothHelper.getInstance().open();

        requestPermission();
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
        BlueToothHelper.getInstance().startDiscovery();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        StringBuffer sb = new StringBuffer();
        sb.append("wifi信息:\n");
        sb.append("mac地址:" + wifiInfo.getMacAddress() + "\n");
        sb.append("接入点的BSSID：" + wifiInfo.getBSSID() + "\n");
        sb.append("IP地址（int）：" + wifiInfo.getIpAddress() + "\n");
        wifiInfoTextView.setText(sb);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    class BtFoundReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device != null) {
                    //判断是否配对过
                    if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                        bluetoothDeviceArrayList.add(device);
                        Log.e(TAG, "onReceive: " + device.getName() );
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

            }
        }
    }
}

