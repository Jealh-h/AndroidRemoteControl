package com.example.javaremotecontroller.communication;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.javaremotecontroller.util.util;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MediaRemoteControl {
    public static byte modifier = 0x00;

    public static void onKeyBoardKeyDown(String key) {
        byte k = parseKey(key);
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(new HidMessage(
                    HidMessage.DeviceType.Keyboard,
                    HidMessage.KeyBoardReportID,
                    new byte[]{modifier, 0, k, 0, 0, 0, 0, 0}));
        }
    }

    public static void onKeyBoardKeyUp(String key) {
        byte k = parseKey(key);
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(new HidMessage(
                    HidMessage.DeviceType.Keyboard,
                    HidMessage.KeyBoardReportID,
                    new byte[]{modifier, 0, 0, 0, 0, 0, 0, 0}));
        }
    }

    public static void onModifierDown(String key) {
        byte k = parseKey(key);
        modifier = k;
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(new HidMessage(
                    HidMessage.DeviceType.Keyboard,
                    HidMessage.KeyBoardReportID,
                    new byte[]{modifier, 0, 0, 0, 0, 0, 0, 0}));
        }
    }

    public static void onModifierUp(String key) {
        byte k = parseKey(key);
        modifier = 0x00;
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(new HidMessage(
                    HidMessage.DeviceType.Keyboard,
                    HidMessage.KeyBoardReportID,
                    new byte[]{modifier, 0, 0, 0, 0, 0, 0, 0}));
        }
    }

    public static byte parseKey(String key) {
        byte keycode = (byte) Integer.parseInt(key);
        return keycode;
    }

    public static void onMediaBtnClick(String key, int delay) {
        byte k = parseKey(key);
        Log.e("BluetoothHidHelper", "onMediaBtnClick: " + k );
        onMediaDown(k);
        util.startDelayTask(new Runnable() {
            @Override
            public void run() {
                onMediaUp();
            }
        }, delay);
    }

    public static void onMediaDown(byte key) {
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(
                    new HidMessage(
                            HidMessage.DeviceType.Media,
                            HidMessage.MediaReportID,
                            new byte[]{key, 0, 0, 0}));
        }
    }

    public static void onMediaUp() {
        synchronized (MediaRemoteControl.class) {
            BluetoothHidHelper.postReport(
                    new HidMessage(
                            HidMessage.DeviceType.Media,
                            HidMessage.MediaReportID,
                            new byte[]{0, 0, 0, 0}));
        }
    }
}
