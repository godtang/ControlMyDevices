package com.example.administrator.controlmydevices

import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.SocketException
import java.net.UnknownHostException

/**
 * Created by Administrator on 2017/10/19.
 */

class WakeThread(ip: String, macAddr: String) : Thread() {
    internal var ip: String? = null
    internal var macAddr: String? = null

    init {
        this.ip = ip
        this.macAddr = macAddr
    }

    @Override
    fun run() {
        super.run()
        wakeOnLan(ip, macAddr)
    }

    fun wakeOnLan(ip: String?, macAddr: String?) {
        var datagramSocket: DatagramSocket? = null
        try {
            val mac = getMacBytes(macAddr)
            val magic = ByteArray(6 + 16 * mac.size)
            //1.写入6个FF
            for (i in 0..5) {
                magic[i] = 0xff.toByte()
            }
            //2.写入16次mac地址
            var i = 6
            while (i < magic.size) {
                System.arraycopy(mac, 0, magic, i, mac.size)
                i += mac.size
            }
            datagramSocket = DatagramSocket()
            val datagramPacket = DatagramPacket(magic, magic.size, InetAddress.getByName(ip), 8888)
            datagramSocket.send(datagramPacket)
        } catch (e: SocketException) {
            e.printStackTrace()
        } catch (e: UnknownHostException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (datagramSocket != null)
                datagramSocket.close()
        }
    }

    @Throws(IllegalArgumentException::class)
    private fun getMacBytes(macStr: String?): ByteArray {
        val bytes = ByteArray(6)
        val hex = macStr!!.split("(\\:|\\-)")
        if (hex.size != 6) {
            throw IllegalArgumentException("Invalid MAC address.")
        }
        try {
            for (i in 0..5) {
                bytes[i] = Integer.parseInt(hex[i], 16) as Byte
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Invalid hex digit in MAC address.")
        }

        return bytes
    }
}