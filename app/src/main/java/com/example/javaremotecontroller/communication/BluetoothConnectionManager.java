package com.example.javaremotecontroller.communication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnectionManager {
    private static final String TAG = "BluetoothConnection";
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // 这是RFCOMM的UUID，用于串行通信

    private BluetoothAdapter bluetoothAdapter;
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private Handler mHandler;

    public BluetoothConnectionManager(Handler handler) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = handler;
    }

    public boolean isBluetoothEnabled() {
        return bluetoothAdapter != null && bluetoothAdapter.isEnabled();
    }

    public void connect(BluetoothDevice device) {
        if (bluetoothAdapter == null) {
            Log.e(TAG, "Bluetooth adapter is not available.");
            return;
        }

        // 取消当前连接线程（如果有）
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        // 取消当前数据传输线程（如果有）
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        // 开始连接线程
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public void write(byte[] data) {
        ConnectedThread thread;
        synchronized (this) {
            if (connectedThread == null) {
                return;
            }
            thread = connectedThread;
        }
        thread.write(data);
    }

    public void disconnect() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    private class ConnectThread extends Thread {
        private BluetoothSocket socket;
        private BluetoothDevice device;

        public ConnectThread(BluetoothDevice bluetoothDevice) {
            device = bluetoothDevice;
            BluetoothSocket tmp = null;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
                Log.e(TAG, "Failed to create RFComm socket: " + e.getMessage());
            }
            socket = tmp;
        }

        public void run() {
            bluetoothAdapter.cancelDiscovery();

            try {
                socket.connect();
                connected(socket, device);
            } catch (IOException connectException) {
                try {
                    socket.close();
                } catch (IOException closeException) {
                    Log.e(TAG, "Failed to close socket during connection failure: " + closeException.getMessage());
                }
                connectionFailed();
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close RFComm socket: " + e.getMessage());
            }
        }
    }

    private class ConnectedThread extends Thread {
        private BluetoothSocket socket;
        private InputStream inputStream;
        private OutputStream outputStream;
        private byte[] buffer;

        public ConnectedThread(BluetoothSocket bluetoothSocket) {
            socket = bluetoothSocket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Failed to create input/output stream: " + e.getMessage());
            }

            inputStream = tmpIn;
            outputStream = tmpOut;
        }

        public void run() {
            buffer = new byte[1024];
            int numBytes;

            while (true) {
                try {
                    numBytes = inputStream.read(buffer);
                    byte[] data = new byte[numBytes];
                    System.arraycopy(buffer, 0, data, 0, numBytes);
//                    mHandler.obtainMessage(MessageConstants.MESSAGE_READ, numBytes, -1, data).sendToTarget();
                } catch (IOException e) {
                    Log.e(TAG, "Failed to read data from input stream: " + e.getMessage());
                    connectionLost();
                    break;
                }
            }
        }

        public void write(byte[] data) {
            try {
                outputStream.write(data);
//                mHandler.obtainMessage(MessageConstants.MESSAGE_WRITE, -1, -1, data).sendToTarget();
            } catch (IOException e) {
                Log.e(TAG, "Failed to write data to output stream: " + e.getMessage());
            }
        }

        public void cancel() {
            try {
                socket.close();
            } catch (IOException e) {
                Log.e(TAG, "Failed to close RFComm socket: " + e.getMessage());
            }
        }
    }

    private synchronized void connected(BluetoothSocket socket, BluetoothDevice device) {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }

        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }

        connectedThread = new ConnectedThread(socket);
        connectedThread.start();

        // 将连接成功的消息发送给UI线程
//        mHandler.obtainMessage(MessageConstants.MESSAGE_DEVICE_CONNECTED, device).sendToTarget();
    }

    private void connectionFailed() {
        // 将连接失败的消息发送给UI线程
//        mHandler.obtainMessage(MessageConstants.MESSAGE_CONNECTION_FAILED).sendToTarget();
    }

    private void connectionLost() {
        // 将连接丢失的消息发送给UI线程
//        mHandler.obtainMessage(MessageConstants.MESSAGE_CONNECTION_LOST).sendToTarget();
    }
}
