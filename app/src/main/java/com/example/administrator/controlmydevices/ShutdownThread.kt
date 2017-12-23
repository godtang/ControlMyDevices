package com.example.administrator.controlmydevices

//package com.ispring.httpurlconnection;

import android.text.TextUtils
import android.text.format.Time
import android.util.Base64

import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.URL
import java.net.URLEncoder
import java.net.UnknownHostException
import java.net.HttpURLConnection
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * Created by Administrator on 2017/10/19.
 */

class ShutdownThread(ip: String) : Thread() {
    internal var ip: String? = null

    init {
        this.ip = ip
    }

    @Override
    fun run() {
        super.run()
        ShutdownDevices(ip)
    }

    fun ShutdownDevices(ip: String?) {
        var url: URL? = null//请求的URL地址
        var conn: HttpURLConnection? = null
        val requestHeader: String? = null//请求头
        val requestBody: ByteArray? = null//请求体
        val responseHeader: String? = null//响应头
        val responseBody: ByteArray? = null//响应体

        try {
            var i = 0
            val time = Time()
            time.setToNow()
            val strTime = time.format("%Y-%m-%d %H:%M:%S")
            var strEncodeTime = Base64.encodeToString(strTime.getBytes(), Base64.DEFAULT)
            strEncodeTime = strEncodeTime.substring(0, strEncodeTime.length() - 1)
            val strReq = "time=" + URLEncoder.encode(strTime, "UTF-8") + "&sign=" + GetMD5(strEncodeTime)
            url = URL("http://$ip/shutdown?$strReq")
            conn = url.openConnection() as HttpURLConnection
            val code = conn.responseCode
            if (code == 200) {
                // 4.利用链接成功的 conn 得到输入流
                val `is` = conn.inputStream// png的图片
                val strRecv = convertStreamToString(`is`)
                i = 1234 + i

            } else {
                // 请求失败
                i = 2222 + i
            }
        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            //if(datagramSocket != null) datagramSocket.close();
        }
    }

    private fun getBytesByInputStream(`is`: InputStream): ByteArray? {
        var bytes: ByteArray? = null
        val bis = BufferedInputStream(`is`)
        val baos = ByteArrayOutputStream()
        val bos = BufferedOutputStream(baos)
        val buffer = ByteArray(1024 * 8)
        var length = 0
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length)
            }
            bos.flush()
            bytes = baos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                bos.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            try {
                bis.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        return bytes
    }

    private fun getResponseHeader(conn: HttpURLConnection): String {
        val responseHeaderMap = conn.headerFields
        val size = responseHeaderMap.size()
        val sbResponseHeader = StringBuilder()
        for (i in 0 until size) {
            val responseHeaderKey = conn.getHeaderFieldKey(i)
            val responseHeaderValue = conn.getHeaderField(i)
            sbResponseHeader.append(responseHeaderKey)
            sbResponseHeader.append(":")
            sbResponseHeader.append(responseHeaderValue)
            sbResponseHeader.append("\n")
        }
        return sbResponseHeader.toString()
    }

    fun convertStreamToString(`is`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String? = null
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line!! + "/n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return sb.toString()
    }

    companion object {
        fun GetMD5(string: String): String {
            if (TextUtils.isEmpty(string)) {
                return ""
            }
            var md5: MessageDigest? = null
            try {
                md5 = MessageDigest.getInstance("MD5")
                val bytes = md5!!.digest(string.getBytes())
                var result = ""
                for (b in bytes) {
                    var temp = Integer.toHexString(b and 0xff)
                    if (temp.length() === 1) {
                        temp = "0" + temp
                    }
                    result += temp
                }
                return result
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
            }

            return ""
        }
    }
}