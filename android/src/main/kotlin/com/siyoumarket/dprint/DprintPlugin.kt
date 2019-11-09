package com.siyoumarket.dprint

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.siyoumarket.dprint.BluetoothPlugin.Companion.REQUEST_ENABLE_BT
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.EventChannel


class DprintPlugin(val activity: Activity, val channel: MethodChannel, registrar: Registrar) : MethodCallHandler, PluginRegistry.ActivityResultListener {

    private lateinit var mResult: Result
    private lateinit var mBluetoothPlugin: BluetoothPlugin

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "com.siyoumarket.dprint")
            val plugin = DprintPlugin(registrar.activity(), channel, registrar)
            channel.setMethodCallHandler(plugin)

            registrar.addActivityResultListener(plugin)
        }
    }

    init {
        this.channel.setMethodCallHandler(this)
        this.mBluetoothPlugin = BluetoothPlugin()

        EventChannel(registrar.messenger(), "com.siyoumarket.dprint/stream").setStreamHandler(
                object : EventChannel.StreamHandler {
                    override fun onListen(args: Any, events: EventChannel.EventSink) {
                        mBluetoothPlugin.setEventChannel(events)
                        Log.w("MainDprint", "adding listener")
                    }

                    override fun onCancel(args: Any) {
                        Log.w("MainDprint", "cancelling listener")
                    }
                }
        )
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (this.mBluetoothPlugin.isAdapterNull() && "isAvailable" != call.method) {
            result.error("bluetooth_unavailable", "the device does not have bluetooth", null)
            return
        }

        when (call.method) {
            "initBluetooth" -> init()
            "isAvailable" -> result.success(!this.mBluetoothPlugin.isAdapterNull())
            "isOn" -> result.success(this.mBluetoothPlugin.isEnabled())
            "startScan" -> mBluetoothPlugin.startScan(activity)
            "getBoundDevices" -> getBoundDevices()
            "connectToDevice" -> connectToDevice(call, result)
            "destroy" -> destroy()
            else -> result.notImplemented()
        }
    }

    private fun connectToDevice(call: MethodCall, result: Result) {
        val device = call.arguments as Map<*, *>
        Log.d("MainDPrint", "Connecting to Device: ${device["name"]} - ${device["address"]}")
    }

    private fun init() {
        mBluetoothPlugin.startBluetooth(activity)

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.activity.registerReceiver(mReceiver, filter)

//        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            return true
        }
        return false
    }

    private fun destroy() {
        this.activity.unregisterReceiver(mReceiver)
        this.mBluetoothPlugin.destroy()
    }


    private fun getBoundDevices(): List<Map<String, String>>? {
        val devices =  mBluetoothPlugin.getBoundDevices()?.toList()
        return devices?.map<BluetoothDevice?, Map<String, String>> { item ->
            mapOf<String, String>(Pair("name", item!!.name), Pair("address", item.address))
        }
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("MainReciever", intent.action)
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    Log.d("MainDPrint", "Device: ${device.name} - ${device.address}")
                    mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 0), Pair("name", device.name), Pair("address", device.address)))

                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 1)))
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 2)))
            }
        }
    }


}


