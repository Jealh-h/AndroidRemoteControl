package com.example.javaremotecontroller.communication;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.content.Intent;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * 单例类
 */
public class BlueToothHelper {
    private static BlueToothHelper mInstance = null;
    private BluetoothAdapter mBluetoothAdapter;

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

    /**
     * 开始搜索
     */
    public void startDiscovery() {
        if (!isDiscovering()) {
            mBluetoothAdapter.startDiscovery();
        }
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

}
