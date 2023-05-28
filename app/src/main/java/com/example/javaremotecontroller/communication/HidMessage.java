package com.example.javaremotecontroller.communication;

public class HidMessage {
    public byte reportId;
    public DeviceType deviceType;
    public static State sendState = State.None;
    public byte[] reportData;
    public static final byte KeyBoardReportID = 0x02;
    public static final byte MouseReportID = 0x01;
    public static final byte MediaReportID = 0x03;


    public HidMessage(DeviceType deviceType, byte reportId, byte[] data){
        this.deviceType = deviceType;
        this.reportData = data;
        this.reportId = reportId;
    }

    public enum DeviceType {
        None,Mouse,Keyboard,Media
    }

    public enum State {
        None,Sending,Succeed,Failed
    }
}
