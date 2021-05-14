package com.example.administrator.controlmydevices;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class MainActivity extends AppCompatActivity {

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
    EditText ipEdit = null;
    EditText macEdit = null;
    Button wakeButton = null;
    Button shutdownButton = null;
    Button findDevicesButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wakeButton = (Button) findViewById(R.id.wake_button);
        ipEdit = (EditText) findViewById(R.id.ip);
        macEdit = (EditText) findViewById(R.id.mac_addr);
        wakeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lip = ipEdit.getText().toString();
                String lmacAddr = macEdit.getText().toString();
                if (lip == null) {
                    ipEdit.setText("please input ipEdit!");
                }
                Log.d("hello", lip);
                if (lmacAddr == null) {
                    macEdit.setText("please input mac!");
                }
                Log.d("hello", lmacAddr);
                if (lip != null && lmacAddr != null) {
                    new WakeThread(lip, lmacAddr).start();
                }
                Toast.makeText(MainActivity.this, "send wake package", Toast.LENGTH_SHORT).show();
                SavePage();
            }
        });
        shutdownButton = (Button) findViewById(R.id.shutdown_button);
        shutdownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lip = ipEdit.getText().toString();
                Log.d("hello", lip);
                if (lip != null) {
                    new ShutdownThread(lip).start();
                }
                Toast.makeText(MainActivity.this, "send shutdown package", Toast.LENGTH_SHORT).show();
                SavePage();
            }
        });

        findDevicesButton = (Button) findViewById(R.id.find_devices_button);
        findDevicesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Runnable rn = new Runnable() {
                    public void run() {
                        try {
                            String host = "255.255.255.255";//广播地址
                            int port = 40000;//广播的目的端口
                            String message = "test";//用于发送的字符串
                            InetAddress adds = InetAddress.getByName(host);
                            DatagramSocket ds = new DatagramSocket();
                            DatagramPacket dp = new DatagramPacket(a, a.length, adds, port);
                            ds.send(dp);
                            ds.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread test = new Thread(rn);
                test.start();
            }
        });
        InitPage();
    }

    private void InitPage() {
        SharedPreferences sharedPreferences = getSharedPreferences("ControlMyDevice", Activity.MODE_PRIVATE);
        String strIp = sharedPreferences.getString("ipEdit", "");
        String strMac = sharedPreferences.getString("mac", "");
        if (!strIp.isEmpty()) {
            ipEdit.setText(strIp);
            ;
        }
        if (!strMac.isEmpty()) {
            macEdit.setText(strMac);
            ;
        }
    }

    private void SavePage() {
        //实例化SharedPreferences对象（第一步）
        SharedPreferences mySharedPreferences = getSharedPreferences("ControlMyDevice", Activity.MODE_PRIVATE);
        //实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        //用putString的方法保存数据
        String strIp = ipEdit.getText().toString();
        String strMac = macEdit.getText().toString();
        if (!strIp.isEmpty()) {
            editor.putString("ipEdit", strIp);
        }
        if (!strMac.isEmpty()) {
            editor.putString("mac", strMac);
        }
        //提交当前数据
        editor.commit();
    }

    private void FindDevices() {

    }
}
