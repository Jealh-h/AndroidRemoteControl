package com.example.javaremotecontroller.communication;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.Context;

import java.util.UUID;

public class BluetoothGattHelper {
    // 创建一个BluetoothGattCharacteristic变量来存储音量级别
    private BluetoothGattCharacteristic characteristic;
    private BluetoothGatt bluetoothGatt;

    // 获取Volume Control Service的UUID
    private static final UUID VOLUME_CONTROL_SERVICE_UUID = UUID.fromString("00001801-0000-1000-8000-00805f9b34fb");
    // 获取Volume Control Characteristic的UUID
    private static final UUID VOLUME_CONTROL_CHARACTERISTIC_UUID = UUID.fromString("00002a05-0000-1000-8000-00805f9b34fb");

    private Context context;

    public BluetoothGattHelper(Context context) {
        this.context = context;
    }

    public void connectDeviceByGatt(BluetoothDevice device) {
        BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null) {
            bluetoothGatt = device.connectGatt(context, false, gattCallback);
        }
    }

    public void disconnectDeviceByGatt() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
        }
    }

    // BluetoothGatt回调
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        // 当发现服务时调用
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                BluetoothGattService volumeService = gatt.getService(VOLUME_CONTROL_SERVICE_UUID);
                if (volumeService != null) {
                    characteristic = volumeService.getCharacteristic(VOLUME_CONTROL_CHARACTERISTIC_UUID);
                }
            }
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 写入特征值成功的处理
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // 读取特征值成功的处理
                byte[] data = characteristic.getValue();
                // 处理收到的数据
            }
        }
    };

    public void writeCharacteristicValue(byte[] data) {
        if(bluetoothGatt != null && characteristic!=null) {
            characteristic.setValue(data);
            bluetoothGatt.writeCharacteristic(characteristic);
        }
    }

    public void readCharacteristicValue() {
        if (bluetoothGatt != null && characteristic != null) {
            bluetoothGatt.readCharacteristic(characteristic);
        }
    }
}
