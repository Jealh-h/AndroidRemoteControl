package com.example.javaremotecontroller.communication;
import android.app.Service;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class WiFiHelper {
    private WifiManager wifiManager;
    private Context context;

    public WiFiHelper(Context ctx) {
        context = ctx;
        wifiManager = (WifiManager) ctx.getSystemService(Service.WIFI_SERVICE);
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
            Log.e("/proc/net/arp", totalText );
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
