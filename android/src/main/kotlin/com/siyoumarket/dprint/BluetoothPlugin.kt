package com.siyoumarket.dprint

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import io.flutter.plugin.common.EventChannel
import android.bluetooth.BluetoothHeadset
import java.util.*


class BluetoothPlugin {
    private val mBluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private lateinit var mEvents: EventChannel.EventSink

    fun setEventChannel(event: EventChannel.EventSink) {
        this.mEvents = event
    }

    fun isAdapterNull() = this.mBluetoothAdapter == null
    fun isEnabled() = this.mBluetoothAdapter?.isEnabled
    fun isBluetoothConnected(): Boolean {
        //TODO: Recheck this method
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.GATT) == BluetoothHeadset.STATE_CONNECTED)
    }

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
        mEvents.success(data)
    }

    fun destroy() {
        mBluetoothAdapter?.cancelDiscovery()
        mEvents.endOfStream()
    }

    companion object {
        const val REQUEST_ENABLE_BT: Int = 1524

    }
}