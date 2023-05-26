package com.example.javaremotecontroller.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.javaremotecontroller.util.ToastUtils;
import com.example.javaremotecontroller.util.util;

import java.util.Arrays;
import java.util.concurrent.Executors;

@RequiresApi(api = Build.VERSION_CODES.P)
public class BluetoothHidHelper {
    private static final byte MOUSE_BUTTON_LEFT = (byte) 0x01;
    private static final byte MOUSE_BUTTON_RIGHT = (byte) 0x02;
    private static final byte MOUSE_BUTTON_MIDDLE = (byte) 0x04;
    public static boolean isRegistry = false;
    public static String HID_DEBUG_TAG = "BluetoothHidHelper";
    public static int MAX_BYTE_DECIMAL = (int) Math.pow(2,7) - 1;

    private static Context context;
    private static BluetoothAdapter bluetoothAdapter;
    public static BluetoothHidDevice hidDevice;
    public static BluetoothDevice bluetoothDevice;
    public static boolean isConnected = false;
    private BluetoothHidDevice.Callback hidDeviceCallback;
    private BluetoothProfile bluetoothProfile;

    public final static String NAME = "JEALH_REMOTE_APP_NAME";
    public final static String DESCRIPTION = "JEALH_REMOTE_APP_DESCRIPTION";
    public final static String PROVIDER = "JEALH_REMOTE_APP_PROVIDER";
    public final static byte[] Descriptor = {
            (byte) 0x05, (byte) 0x01, (byte) 0x09, (byte) 0x02, (byte) 0xa1, (byte) 0x01, (byte) 0x09, (byte) 0x01, (byte) 0xa1, (byte) 0x00,
            (byte) 0x85, (byte) 0x01, (byte) 0x05, (byte) 0x09, (byte) 0x19, (byte) 0x01, (byte) 0x29, (byte) 0x03, (byte) 0x15, (byte) 0x00, (byte) 0x25, (byte) 0x01,
            (byte) 0x95, (byte) 0x03, (byte) 0x75, (byte) 0x01, (byte) 0x81, (byte) 0x02, (byte) 0x95, (byte) 0x01, (byte) 0x75, (byte) 0x05, (byte) 0x81, (byte) 0x03,
            (byte) 0x05, (byte) 0x01, (byte) 0x09, (byte) 0x30, (byte) 0x09, (byte) 0x31, (byte) 0x09, (byte) 0x38, (byte) 0x15, (byte) 0x81, (byte) 0x25, (byte) 0x7f,
            (byte) 0x75, (byte) 0x08, (byte) 0x95, (byte) 0x03, (byte) 0x81, (byte) 0x06, (byte) 0xc0, (byte) 0xc0, (byte) 0x05, (byte) 0x01, (byte) 0x09, (byte) 0x06,
            (byte) 0xa1, (byte) 0x01, (byte) 0x85, (byte) 0x02, (byte) 0x05, (byte) 0x07, (byte) 0x19, (byte) 0xE0, (byte) 0x29, (byte) 0xE7, (byte) 0x15, (byte) 0x00,
            (byte) 0x25, (byte) 0x01, (byte) 0x75, (byte) 0x01, (byte) 0x95, (byte) 0x08, (byte) 0x81, (byte) 0x02, (byte) 0x95, (byte) 0x01, (byte) 0x75, (byte) 0x08,
            (byte) 0x15, (byte) 0x00, (byte) 0x25, (byte) 0x65, (byte) 0x19, (byte) 0x00, (byte) 0x29, (byte) 0x65, (byte) 0x81, (byte) 0x00, (byte) 0x05, (byte) 0x08,
            (byte) 0x95, (byte) 0x05, (byte) 0x75, (byte) 0x01, (byte) 0x19, (byte) 0x01, (byte) 0x29, (byte) 0x05,
            (byte) 0x91, (byte) 0x02, (byte) 0x95, (byte) 0x01, (byte) 0x75, (byte) 0x03, (byte) 0x91, (byte) 0x03,
            (byte) 0xc0
    };


    public BluetoothHidHelper(Context context) {
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        RegistryHIDApp();
    }

    public void RegistryHIDApp() {
        try {
            if (!isRegistry)
                BluetoothAdapter
                        .getDefaultAdapter()
                        .getProfileProxy(
                                context,
                                mProfileServiceListener,
                                BluetoothProfile.HID_DEVICE);
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast(context, "您的手机不支持蓝牙遥控");
        }
    }

    public static void postReport(HidMessage hidMessage) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            return;
        Log.e(HID_DEBUG_TAG, "post Report:" + util.bytesToHex(hidMessage.reportData) + BluetoothHidHelper.bluetoothDevice.getName());
        hidMessage.sendState = HidMessage.State.Sending;
        boolean result = BluetoothHidHelper.hidDevice.sendReport(BluetoothHidHelper.bluetoothDevice, hidMessage.reportId,hidMessage.reportData);
        if(!result)
            hidMessage.sendState = HidMessage.State.Failed;
        else
            hidMessage.sendState = HidMessage.State.Succeed;
    }

    public static boolean connect(BluetoothDevice device) {
        boolean result = hidDevice.connect(device);
        Log.e(HID_DEBUG_TAG, "connect onServiceConnected: " + result);
        if(result) {
            bluetoothDevice = device;
            isConnected = true;
        }else
            isConnected = false;
        return result;
    }

    public static boolean connect(String deviceAddress) {
        if(TextUtils.isEmpty(deviceAddress)){
            ToastUtils.showToast(context, "获取mac地址失败");
            return false;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothDevice == null) {
            bluetoothDevice = BlueToothHelper.getInstance().getRemoteDevice(deviceAddress);
        }
        boolean result = hidDevice.connect(bluetoothDevice);
        Log.e(HID_DEBUG_TAG, "connect onServiceConnected: " + result);
        if(result) {
            isConnected = true;
        }else
            isConnected = false;
        return result;
    }

    public static boolean isConnect() {
        return isConnected;
    }

    public BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.e(HID_DEBUG_TAG, "onServiceConnected: " + profile +"--" + BluetoothProfile.HID_DEVICE );
            bluetoothProfile = proxy;
            if (profile == BluetoothProfile.HID_DEVICE) {
                hidDevice = (BluetoothHidDevice) proxy;
                BluetoothHidDeviceAppSdpSettings sdp =
                        new BluetoothHidDeviceAppSdpSettings(NAME, DESCRIPTION, PROVIDER, BluetoothHidDevice.SUBCLASS1_COMBO, Descriptor);
                hidDevice.registerApp(sdp, null, null, Executors.newCachedThreadPool(), hidDeviceCallBack);
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if(profile == BluetoothProfile.HID_DEVICE)
                isConnected = false;
        }
    };

    private BluetoothHidDevice.Callback hidDeviceCallBack = new BluetoothHidDevice.Callback() {
        @Override
        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            super.onAppStatusChanged(pluggedDevice, registered);
            isRegistry = registered;
        }

        @Override
        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            super.onConnectionStateChanged(device, state);
        }

        @Override
        public void onGetReport(BluetoothDevice device, byte type, byte id, int bufferSize) {
            super.onGetReport(device, type, id, bufferSize);
        }

        @Override
        public void onSetReport(BluetoothDevice device, byte type, byte id, byte[] data) {
            super.onSetReport(device, type, id, data);
        }

        @Override
        public void onSetProtocol(BluetoothDevice device, byte protocol) {
            super.onSetProtocol(device, protocol);
        }

        @Override
        public void onInterruptData(BluetoothDevice device, byte reportId, byte[] data) {
            super.onInterruptData(device, reportId, data);
        }

        @Override
        public void onVirtualCableUnplug(BluetoothDevice device) {
            super.onVirtualCableUnplug(device);
        }
    };

    private void initialize() {
        // 检查设备是否支持蓝牙HID设备模式
        if (!context.getPackageManager().hasSystemFeature("android.hardware.bluetooth_hci")) {
            Log.e(HID_DEBUG_TAG, "设备不支持蓝牙HID设备模式");
            return;
        }

        // 检查是否已启用蓝牙
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Log.e(HID_DEBUG_TAG, "蓝牙未启用");
            return;
        }

        // 获取HID设备配置
//        hidDeviceCallback = createHidDeviceCallBack();
//        BluetoothHidDeviceAppConfiguration configuration = new BluetoothHidDeviceAppConfiguration(
//                "Mouse Controller",
//                "OpenAI",
//                "1.0",
//                BluetoothHidDevice.SUBCLASS1_MOUSE,
//                BluetoothHidDevice.SUBCLASS1_KEYBOARD,
//                BluetoothHidDevice.SUBCLASS1_NONE
//        );

        // 注册HID设备
//        bluetoothAdapter.getProfileProxy(context, hidDeviceCallback, BluetoothProfile.HID_DEVICE);

        // 设置HID设备配置
//        hidDevice = BluetoothHidDevice.getProfileProxy(context);
//        if (hidDevice != null) {
//            hidDevice.registerApp(configuration, null);
//        }
    }
}
