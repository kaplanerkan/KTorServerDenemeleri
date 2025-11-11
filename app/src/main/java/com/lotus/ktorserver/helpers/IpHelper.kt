package com.lotus.ktorserver.helpers

import android.content.Context
import android.net.wifi.WifiManager
import android.text.format.Formatter
import java.net.NetworkInterface
import java.util.Collections

object IpHelper {

    // EN İYİ VE EN GÜVENİLİR YÖNTEM (2025'te hala çalışıyor)
    fun getDeviceIpAddress(context: Context): String {
        try {
            val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            val ipInt = wifiInfo.ipAddress
            return Formatter.formatIpAddress(ipInt) // Örnek: 192.168.1.180
        } catch (e: Exception) {
            // Wifi kapalıysa ya da hata olursa yedek yöntem
            return getIpFromNetworkInterface() ?: "IP Bulunamadı"
        }
    }

    // Yedek yöntem - Wifi kapalı olsa bile çalışır (Mobil veri vs.)
    private fun getIpFromNetworkInterface(): String? {
        try {
            val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                if (!intf.isUp || intf.isLoopback) continue
                val addresses = intf.inetAddresses.toList()
                for (addr in addresses) {
                    if (addr.isLoopbackAddress || addr.isLinkLocalAddress) continue
                    if (!addr.hostAddress.contains(":")) { // IPv4 istiyoruz
                        return addr.hostAddress
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}