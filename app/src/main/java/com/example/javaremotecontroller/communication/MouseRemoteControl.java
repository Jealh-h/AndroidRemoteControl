package com.example.javaremotecontroller.communication;

import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.P)
public class MouseRemoteControl {
    public static HidMessage MouseReport = new HidMessage(HidMessage.DeviceType.Mouse, (byte)0x01, new byte[]{0,0,0,0});

    public static void onMouseLeftDown() {
        MouseReport.reportData[0] |= 1;
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseLeftUp() {
        MouseRemoteControl.MouseReport.reportData[0] &= (~1);
        BluetoothHidHelper.postReport(MouseReport);
    }

    public static void onMouseMove(int deltaX, int deltaY, int rightButton) {
        if(MouseReport.sendState.equals(HidMessage.State.Sending))
            return;
        deltaX = deltaX > BluetoothHidHelper.MAX_BYTE_DECIMAL ? BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaX;
        deltaX = deltaX < -BluetoothHidHelper.MAX_BYTE_DECIMAL ? -BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaX;
        deltaY = deltaY < BluetoothHidHelper.MAX_BYTE_DECIMAL ? BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaY;
        deltaY = deltaY < -BluetoothHidHelper.MAX_BYTE_DECIMAL ? -BluetoothHidHelper.MAX_BYTE_DECIMAL : deltaY;

        MouseReport.reportData[1] = (byte) deltaX;
        MouseReport.reportData[2] = (byte) deltaY;

        BluetoothHidHelper.postReport(MouseReport);
    }
}
