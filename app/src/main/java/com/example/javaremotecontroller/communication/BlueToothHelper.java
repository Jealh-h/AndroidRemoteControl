package com.example.javaremotecontroller.communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
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

    /**
     * 开始搜索
     */
    public void startDiscovery(Activity activity) {
        if (isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
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

    public void connect(BluetoothDevice device) {
        connectTread = new ConnectTread(device);
        connectTread.start();
    }

    private class ConnectTread extends Thread {
        private BluetoothSocket socket;
        private BluetoothDevice device;

        public ConnectTread(BluetoothDevice device) {
            this.device = device;

            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(APP_UUID);
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
            } catch (IOException e){
                Log.e("Connect -> cancel", e.toString());
            }
        }
    }

    private synchronized void connectionFailed() {
        Log.e("TAG", "connectionFailed: ");
    }

    private void connected(BluetoothDevice device){
        if(connectTread != null) {
            connectTread.cancel();
            connectTread = null;
        }

//        Message message = handler.obtainMessage();
//        Bundle bundle = new Bundle();
//        bundle.putString("DEVICE_NAME", device.getName());
//        message.setData(bundle);
//
//        hundler.sendMessage(message);
//
//        setState("handle");
    }
}
