package com.example.javaremotecontroller.communication;

public class HidMessage {
    public byte reportId;
    public DeviceType deviceType;
    public static State sendState = State.None;
    public byte[] reportData;

    public HidMessage(DeviceType deviceType, byte reportId, byte[] data){
        this.deviceType = deviceType;
        this.reportData = data;
        this.reportId = reportId;
    }

    public enum DeviceType {
        None,Mouse,Keyboard
    }

    public enum State {
        None,Sending,Succeed,Failed
    }
}
