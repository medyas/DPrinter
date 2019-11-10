package com.siyoumarket.dprint

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import io.flutter.plugin.common.EventChannel


class BluetoothPlugin {
    private val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private var mEvents: EventChannel.EventSink? = null

    fun setEventChannel(event: EventChannel.EventSink) {
        this.mEvents = event
    }

    fun isAdapterNull() = this.mBluetoothAdapter == null
    fun isEnabled() = this.mBluetoothAdapter?.isEnabled


    fun startBluetooth(activity: Activity) {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    fun startScan(activity: Activity) {
        if (this.isEnabled()!!) {
            this.mBluetoothAdapter?.startDiscovery()
        } else this.startBluetooth(activity)
    }

    fun getBoundDevices() = this.mBluetoothAdapter?.bondedDevices?.toList()

    fun sendMsg(data: Any) {
        if (mEvents == null) Log.d("MainDPrint", "MEvent is null !!!!!!!!!!!!!")
        mEvents?.success(data)
    }

    fun destroy() {
        mBluetoothAdapter?.cancelDiscovery()
        mEvents?.endOfStream()
    }

    companion object {
        const val REQUEST_ENABLE_BT: Int = 1524
    }
}