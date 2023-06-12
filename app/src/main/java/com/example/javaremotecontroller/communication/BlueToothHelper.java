package com.example.javaremotecontroller.communication;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.javaremotecontroller.util.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 单例类
 */
public class BlueToothHelper {
    private static BlueToothHelper mInstance = null;
    private BluetoothAdapter mBluetoothAdapter;
    private ConnectTread connectTread;
    private static final UUID APP_UUID = UUID.fromString("a-b-c-d-e");
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 9999;
    private String TAG = "BLUE_TOOTH_HELPER";
    private BluetoothHidDevice hidDevice;
    private BluetoothHidDevice.Callback hidDeviceCallback;

    public BlueToothHelper() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BlueToothHelper getInstance() {
        if(mInstance==null){
            synchronized (BlueToothHelper.class) {
                if(mInstance == null)
                    mInstance = new BlueToothHelper();
            }
        }
        return mInstance;
    }

    /**
     * 是否支持
     * @return
     */
    public boolean isSupport() {
        return mBluetoothAdapter != null;
    }

    /**
     * 是否打开
     *
     * @return
     */
    public boolean isEnabled() {
        assert(mBluetoothAdapter!=null);
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 开关 无提示
     */
    public void open() {
        if (!isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }

    /**
     * 开关 有提示
     *
     * @param mActivity
     * @param requestCode
     */
    public void open(Activity mActivity, int requestCode) {
        if (!isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 获取已配对列表
     *
     * @return Set
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    /**
     * 是否在搜索
     *
     * @return boolean
     */
    public boolean isDiscovering() {
        return mBluetoothAdapter.isDiscovering();
    }

    public void enabledBluetooth(Activity activity) {
        if(!mBluetoothAdapter.isEnabled()){
            mBluetoothAdapter.enable();
        }
        if(mBluetoothAdapter.getScanMode()!=BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoveryIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoveryIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            activity.startActivity(discoveryIntent);
        }

    }

    public ArrayList<BluetoothDevice> getConnectedDevices() {
        Set<BluetoothDevice> bondedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<BluetoothDevice> bluetoothDeviceArrayList = new ArrayList<>();

        for (BluetoothDevice device : bondedDevices) {
            int headset = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET);
            int a2dp = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP);
            int health = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH);
            if (BluetoothProfile.STATE_CONNECTED == headset) {
                // device is connected via A2DP profile
                bluetoothDeviceArrayList.add(device);
            } else if (BluetoothProfile.STATE_CONNECTED == mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET)) {
                // device is connected via Headset profile
                bluetoothDeviceArrayList.add(device);
            } else if (BluetoothProfile.STATE_CONNECTED == mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH)) {
                // device is connected via Health profile
                bluetoothDeviceArrayList.add(device);
            }
        }
        return bluetoothDeviceArrayList;
    }

    /**
     * 开始搜索
     */
    public void startDiscovery(Activity activity) {
       if (isDiscovering()) {
            return;
        }
       if(!isEnabled()) {
           enabledBluetooth(activity);
       }
        if(!util.LocationStateCheck(activity)) {
            util.openLocationSetting(activity);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 如果没有定位权限，则向用户请求权限
                activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            } else {
                // 如果有定位权限，进行相应操作
                // ...
                mBluetoothAdapter.startDiscovery();
            }
        }
        Log.e("DEBUG_LIFE_CYCLE", "startDiscovery:2 " + isDiscovering());
    }

    /**
     * 停止搜索
     */
    public void cancelDiscovery() {
        if (isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    /**
     * 获取远程设备
     *
     * @param address
     * @return BluetoothDevice
     */
    public BluetoothDevice getRemoteDevice(String address) {
        return mBluetoothAdapter.getRemoteDevice(address);
    }

    /**
     * 配对
     * @param device
     * @return
     */
    public boolean createBond(BluetoothDevice device) {
        cancelDiscovery();
        if(device.getBondState()==BluetoothDevice.BOND_NONE){
            device.createBond();
        }else if(device.getBondState() == BluetoothDevice.BOND_BONDED) {

        }
        return device.createBond();
    }


    /**
     * 获取客户端设备
     * @param name
     * @param uuid
     * @return BluetoothServerSocket
     */
    public BluetoothServerSocket listenUsingRfcommWithServiceRecord(String name, UUID uuid) {
        try {
            return mBluetoothAdapter.listenUsingRfcommWithServiceRecord(name, uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void connectSocket(BluetoothDevice device) {
        connectTread = new ConnectTread(device);
        connectTread.start();
    }

    public void sendCommand(byte[] command) {
        if(connectTread!=null) {
            connectTread.sendByte(command);
        }
    }

    private class ConnectTread extends Thread {
        private BluetoothSocket socket;
        private BluetoothDevice device;

        public ConnectTread(BluetoothDevice device) {
            this.device = device;

            BluetoothSocket tmp = null;
            try {
                tmp = this.device.createRfcommSocketToServiceRecord(APP_UUID);
            } catch (IOException e){
                Log.e("connect->ConnectedTread", e.toString());
            }
            socket = tmp;
        }

        public void run() {
            try {
                socket.connect();
            } catch (IOException e){
                Log.e("ConnectThread->connect", e.toString());

                try {
                    socket.close();
                    socket = null;
                } catch (IOException e1){
                    Log.e("ConnectThread->close", e1.toString());
                }
                connectionFailed();
                return;
            }
        }

        public void cancel() {
            try {
                socket.close();
                socket = null;
            } catch (IOException e){
                Log.e("Connect -> cancel", e.toString());
            }
        }

        public void sendByte(byte[] bytes) {
            if(socket!=null) {
                try  {
                    OutputStream outputStream = socket.getOutputStream();
                    if(outputStream!=null){
                        outputStream.write(bytes);
                    }
                } catch (Exception e) {
                    Log.e(TAG, "sendByte: " + e.toString());
                }
            }
        }
    }

    private synchronized void connectionFailed() {
        Log.e("TAG", "connectionFailed: ");
    }

}
