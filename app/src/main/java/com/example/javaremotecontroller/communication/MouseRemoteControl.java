package com.example.javaremotecontroller.communication;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.javaremotecontroller.util.util;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MouseRemoteControl {
    public static HidMessage MouseReport = new HidMessage(HidMessage.DeviceType.Mouse, (byte) HidMessage.MouseReportID, new byte[]{0,0,0,0});

    public static void onMouseLeftDown() {
        MouseReport.reportData[0] |= 1;
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseLeftUp() {
        MouseRemoteControl.MouseReport.reportData[0] &= (~1);
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseRightDown() {
        MouseReport.reportData[0] |= 2;
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseRightUp() {
        MouseReport.reportData[0] &= (~2);
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseMove(int deltaX, int deltaY,int wheel) {
        if(MouseReport.sendState.equals(HidMessage.State.Sending))
            return;
        deltaX = deltaX > BluetoothHidHelper.MAX_BYTE_DECIMAL ? BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaX;
        deltaX = deltaX < -BluetoothHidHelper.MAX_BYTE_DECIMAL ? -BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaX;
        deltaY = deltaY > BluetoothHidHelper.MAX_BYTE_DECIMAL ? BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaY;
        deltaY = deltaY < -BluetoothHidHelper.MAX_BYTE_DECIMAL ? -BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaY;
        wheel = wheel > BluetoothHidHelper.MAX_BYTE_DECIMAL ? BluetoothHidHelper.MAX_BYTE_DECIMAL : wheel;
        wheel = wheel < -BluetoothHidHelper.MAX_BYTE_DECIMAL ? -BluetoothHidHelper.MAX_BYTE_DECIMAL : wheel;

        MouseReport.reportData[1] = (byte) deltaX;
        MouseReport.reportData[2] = (byte) deltaY;
        MouseReport.reportData[3] = (byte) wheel;

        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void simulatedLeftClick(int delay) {
        onMouseLeftDown();
        util.startDelayTask(new Runnable() {
            @Override
            public void run() {
                onMouseLeftUp();
            }
        }, delay);
    }

}
