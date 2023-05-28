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

    public static boolean isRegistry = false;
    public static String HID_DEBUG_TAG = "BluetoothHidHelper";
    public static int MAX_BYTE_DECIMAL = (int) Math.pow(2, 7) - 1;

    private static Context context;
    private static BluetoothAdapter bluetoothAdapter;
    public static BluetoothHidDevice hidDevice;
    public static BluetoothDevice bluetoothDevice;
    public static boolean isConnected = false;
    private BluetoothProfile bluetoothProfile;

    public final static String NAME = "JEALH_REMOTE_APP_NAME";
    public final static String DESCRIPTION = "JEALH_REMOTE_APP_DESCRIPTION";
    public final static String PROVIDER = "JEALH_REMOTE_APP_PROVIDER";

    public final static byte[] MOUSE_KEYBOARD_COMBO = {
            // keyboard
            (byte) 0x05, (byte) 0x01, // Usage page (Generic Desktop)
            (byte) 0x09, (byte) 0x06, // Usage (Keyboard)
            (byte) 0xA1, (byte) 0x01, // Collection (Application)
            (byte) 0x85, (byte) 0x02, //    Report ID
            (byte) 0x05, (byte) 0x07, //       Usage page (Key Codes)
            (byte) 0x19, (byte) 0xE0, //       Usage minimum (224)
            (byte) 0x29, (byte) 0xE7, //       Usage maximum (231)
            (byte) 0x15, (byte) 0x00, //       Logical minimum (0)
            (byte) 0x25, (byte) 0x01, //       Logical maximum (1)
            (byte) 0x75, (byte) 0x01, //       Report size (1)
            (byte) 0x95, (byte) 0x08, //       Report count (8)
            (byte) 0x81, (byte) 0x02, //       Input (Data, Variable, Absolute) ; Modifier byte
            (byte) 0x75, (byte) 0x08, //       Report size (8)
            (byte) 0x95, (byte) 0x01, //       Report count (1)
            (byte) 0x81, (byte) 0x01, //       Input (Constant)                 ; Reserved byte
            (byte) 0x75, (byte) 0x08, //       Report size (8)
            (byte) 0x95, (byte) 0x06, //       Report count (6)
            (byte) 0x15, (byte) 0x00, //       Logical Minimum (0)
            (byte) 0x25, (byte) 0x65, //       Logical Maximum (101)
            (byte) 0x05, (byte) 0x07, //       Usage page (Key Codes)
            (byte) 0x19, (byte) 0x00, //       Usage Minimum (0)
            (byte) 0x29, (byte) 0x65, //       Usage Maximum (101)
            (byte) 0x81, (byte) 0x00, //       Input (Data, Array)              ; Key array (6 keys)
            (byte) 0xC0,              // End Collection

            // Mouse
            (byte) 0x05, (byte) 0x01, // Usage Page (Generic Desktop)
            (byte) 0x09, (byte) 0x02, // Usage (Mouse)
            (byte) 0xA1, (byte) 0x01, // Collection (Application)
            (byte) 0x85, (byte) 0x01, //    Report ID
            (byte) 0x09, (byte) 0x01, //    Usage (Pointer)
            (byte) 0xA1, (byte) 0x00, //    Collection (Physical)
            (byte) 0x05, (byte) 0x09, //       Usage Page (Buttons)
            (byte) 0x19, (byte) 0x01, //       Usage minimum (1)
            (byte) 0x29, (byte) 0x03, //       Usage maximum (3)
            (byte) 0x15, (byte) 0x00, //       Logical minimum (0)
            (byte) 0x25, (byte) 0x01, //       Logical maximum (1)
            (byte) 0x75, (byte) 0x01, //       Report size (1)
            (byte) 0x95, (byte) 0x03, //       Report count (3)
            (byte) 0x81, (byte) 0x02, //       Input (Data, Variable, Absolute)
            (byte) 0x75, (byte) 0x05, //       Report size (5)
            (byte) 0x95, (byte) 0x01, //       Report count (1)
            (byte) 0x81, (byte) 0x01, //       Input (constant)                 ; 5 bit padding
            (byte) 0x05, (byte) 0x01, //       Usage page (Generic Desktop)
            (byte) 0x09, (byte) 0x30, //       Usage (X)
            (byte) 0x09, (byte) 0x31, //       Usage (Y)
            (byte) 0x09, (byte) 0x38, //       Usage (Wheel)
            (byte) 0x15, (byte) 0x81, //       Logical minimum (-127)
            (byte) 0x25, (byte) 0x7F, //       Logical maximum (127)
            (byte) 0x75, (byte) 0x08, //       Report size (8)
            (byte) 0x95, (byte) 0x03, //       Report count (3)
            (byte) 0x81, (byte) 0x06, //       Input (Data, Variable, Relative)
            (byte) 0xC0,              //    End Collection
            (byte) 0xC0,              // End Collection

            // consumer Devices
            (byte)0x05, (byte)0x0C,        // USAGE_PAGE (Consumer Devices)
            (byte)0x09, (byte)0x01,        // USAGE (Consumer Control)
            (byte)0xA1, (byte)0x01,        // COLLECTION (Application)
            (byte)0x85, (byte)0x03,        // REPORT_ID = 3
            (byte)0x75, (byte)0x10,        // REPORT_SIZE (16)
            (byte)0x95, (byte)0x02,        // REPORT_COUNT (2)
            (byte)0x15, (byte)0x01,        // LOGICAL_MIN (1)
            (byte)0x26, (byte)0xFF, (byte)0x02,  // LOGICAL_MAX (767)
            (byte)0x19, (byte)0x01,        // USAGE_MIN (1)
            (byte)0x2A, (byte)0xFF, (byte)0x02,  // USAGE_MAX (767)
            (byte)0x81, (byte)0x00,        // INPUT (Data Ary Abs)
            (byte)0xC0,                     // END_COLLECTION

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
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            return;
        hidMessage.sendState = HidMessage.State.Sending;
        boolean result = BluetoothHidHelper.hidDevice.sendReport(BluetoothHidHelper.bluetoothDevice, hidMessage.reportId, hidMessage.reportData);
        if (!result)
            hidMessage.sendState = HidMessage.State.Failed;
        else
            hidMessage.sendState = HidMessage.State.Succeed;
        Log.e(HID_DEBUG_TAG, hidMessage.reportId + ":" + util.bytesToHex(hidMessage.reportData) + ":" + BluetoothHidHelper.bluetoothDevice.getName());
    }

    public static boolean connect(BluetoothDevice device) {
        if (device == null) {
            return false;
        }
        boolean result = hidDevice.connect(device);
        Log.e(HID_DEBUG_TAG, hidDevice.getConnectionState(device) + "connect onServiceConnected: " + result + device.getName());
        if (result) {
            bluetoothDevice = device;
            isConnected = true;
        } else
            isConnected = false;
        return result;
    }

    public static boolean connect(String deviceAddress) {
        if (TextUtils.isEmpty(deviceAddress)) {
            ToastUtils.showToast(context, "获取mac地址失败");
            return false;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothDevice == null) {
            bluetoothDevice = BlueToothHelper.getInstance().getRemoteDevice(deviceAddress);
        }
        boolean result = hidDevice.connect(bluetoothDevice);
        Log.e(HID_DEBUG_TAG, "connect onServiceConnected: " + result);
        if (result) {
            isConnected = true;
        } else
            isConnected = false;
        return result;
    }

    public static boolean isConnect() {
        return isConnected;
    }

    public BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            Log.e(HID_DEBUG_TAG, "onServiceConnected: " + profile + "--" + BluetoothProfile.HID_DEVICE);
            bluetoothProfile = proxy;
            if (profile == BluetoothProfile.HID_DEVICE) {
                hidDevice = (BluetoothHidDevice) proxy;
                BluetoothHidDeviceAppSdpSettings sdp =
                        new BluetoothHidDeviceAppSdpSettings(NAME, DESCRIPTION, PROVIDER, BluetoothHidDevice.SUBCLASS2_UNCATEGORIZED, MOUSE_KEYBOARD_COMBO);
                hidDevice.registerApp(sdp, null, null, Executors.newCachedThreadPool(), hidDeviceCallBack);
            }
        }

        @Override
        public void onServiceDisconnected(int profile) {
            if (profile == BluetoothProfile.HID_DEVICE)
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
}
