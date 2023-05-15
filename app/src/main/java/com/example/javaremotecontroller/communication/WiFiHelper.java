package com.example.javaremotecontroller.communication;
import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.AsyncTask;
import android.os.Build;
import android.os.PatternMatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class WiFiHelper {
    private WifiManager wifiManager;
    private Context context;
    private ConnectivityManager mConnectivityManager;

    public WiFiHelper(Context ctx) {
        context = ctx;
        wifiManager = (WifiManager) ctx.getSystemService(Service.WIFI_SERVICE);
        mConnectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public boolean setWifiEnabled() {
        return wifiManager.setWifiEnabled(true);
    }

    public boolean setWifiDisabled() {
        // android api 28 及以下有效
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.P){
            return wifiManager.setWifiEnabled(false);
        }else {
            Toast.makeText(context, "wifi 未打开，请打开 wifi", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    /**
     * wifi 开关状态
     * @return
     */
    public int getWifiState() {
        return wifiManager.getWifiState();
    }

    /**
     * wifi 是否连接
     * @return
     */
    public boolean isWifiConnect() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.e("DEBUG", "isWifiConnect: " + wifiInfo.toString());
        return wifiInfo != null;
    }

    /**
     * 获取连接信息
     * @return WifiInfo
     */
    public WifiInfo getConnectionInfo() {
        return wifiManager.getConnectionInfo();
    }

    /**
     * 获取SSID
     * @return
     */
    public String getSSID() {
        if(Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1){
            ConnectivityManager connManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo()!=null){
                    return networkInfo.getExtraInfo().replace("\"","");
                }
            }
        }
        WifiManager mWifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        assert mWifiManager != null;
        WifiInfo info = mWifiManager.getConnectionInfo();
        return info.getSSID().replace("\"", "");
    }

    /**
     * 获取设备周围的Wi-Fi网络列表
     * @return
     */
    public List<ScanResult> getScanResults() {
        return wifiManager.getScanResults();
    }

    public class connectThread extends Thread {

        private String ipAddress;
        private int port;
        private String text;

        public connectThread(String ipAddress, int port, String text) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.text = text;
        }

        @Override
        public void run() {
            try {
                Socket socket = new Socket(ipAddress, port);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(text.getBytes());
                outputStream.flush();
                outputStream.close();
                socket.close();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList getConnectedIPs() {
        ArrayList connectedIP = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/net/arp"));
            String line,totalText = "";
            while((line = br.readLine())!=null){
                totalText += line;
                String[] splitted = line.split(" +");
                if(splitted !=null && splitted.length ==4){
                    String ip = splitted[0];
                    connectedIP.add(ip);
                }
            }
            Log.e("wifi->getConnectedIPs", totalText );
        }catch (Exception e){
            Log.e("wifi->getConnectedIPs", e.toString());
        }
        return connectedIP;
    }
}

class HttpRequestTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        String url = params[0];
        String result = "";
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // 发送GET请求
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // 打印响应结果
            result = response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = "Error: " + e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
//        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
    }
}
