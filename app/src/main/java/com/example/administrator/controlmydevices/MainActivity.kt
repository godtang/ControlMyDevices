package com.example.administrator.controlmydevices

import android.app.Activity
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    //    @Override
    //    protected void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //        setContentView(R.layout.activity_main);
    //    }
    internal var ipEdit: EditText? = null
    internal var macEdit: EditText? = null
    internal var wakeButton: Button? = null
    internal var shutdownButton: Button? = null
    @Override
    protected fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        wakeButton = findViewById(R.id.wake_button) as Button
        ipEdit = findViewById(R.id.ip) as EditText
        macEdit = findViewById(R.id.mac_addr) as EditText
        wakeButton!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                val lip = ipEdit!!.text.toString()
                val lmacAddr = macEdit!!.text.toString()
                if (lip == null) {
                    ipEdit!!.setText("please input ipEdit!")
                }
                Log.d("hello", lip)
                if (lmacAddr == null) {
                    macEdit!!.setText("please input mac!")
                }
                Log.d("hello", lmacAddr)
                if (lip != null && lmacAddr != null) {
                    WakeThread(lip, lmacAddr).start()
                }
                Toast.makeText(this@MainActivity, "send wake package", Toast.LENGTH_SHORT).show()
                SavePage()
            }
        })
        shutdownButton = findViewById(R.id.shutdown_button) as Button
        shutdownButton!!.setOnClickListener(object : View.OnClickListener() {
            @Override
            fun onClick(v: View) {
                val lip = ipEdit!!.text.toString()
                Log.d("hello", lip)
                if (lip != null) {
                    ShutdownThread(lip).start()
                }
                Toast.makeText(this@MainActivity, "send shutdown package", Toast.LENGTH_SHORT).show()
                SavePage()
            }
        })
        InitPage()
    }

    private fun InitPage() {
        val sharedPreferences = getSharedPreferences("ControlMyDevice", Activity.MODE_PRIVATE)
        val strIp = sharedPreferences.getString("ipEdit", "")
        val strMac = sharedPreferences.getString("mac", "")
        if (!strIp.isEmpty()) {
            ipEdit!!.setText(strIp)
        }
        if (!strMac.isEmpty()) {
            macEdit!!.setText(strMac)
        }
    }

    private fun SavePage() {
        //实例化SharedPreferences对象（第一步）
        val mySharedPreferences = getSharedPreferences("ControlMyDevice", Activity.MODE_PRIVATE)
        //实例化SharedPreferences.Editor对象（第二步）
        val editor = mySharedPreferences.edit()
        //用putString的方法保存数据
        val strIp = ipEdit!!.text.toString()
        val strMac = macEdit!!.text.toString()
        if (!strIp.isEmpty()) {
            editor.putString("ipEdit", strIp)
        }
        if (!strMac.isEmpty()) {
            editor.putString("mac", strMac)
        }
        //提交当前数据
        editor.commit()
    }
}
