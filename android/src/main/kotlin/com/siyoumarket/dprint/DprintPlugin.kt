package com.siyoumarket.dprint

import android.app.Activity
import android.app.Dialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.flutter.plugin.common.EventChannel


class DprintPlugin(val activity: Activity, val channel: MethodChannel, registrar: Registrar) : MethodCallHandler, PluginRegistry.ActivityResultListener {

    private val mBluetoothAdapter: BluetoothAdapter?
    private lateinit var mResult: Result
    private lateinit var mEvents: EventChannel.EventSink

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "com.siyoumarket.dprint")
            val plugin = DprintPlugin(registrar.activity(), channel, registrar)
            channel.setMethodCallHandler(plugin)

            registrar.addActivityResultListener(plugin)
        }

        const val REQUEST_ENABLE_BT: Int = 1524
    }

    init {
        this.channel.setMethodCallHandler(this)
        this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        EventChannel(registrar.messenger(), "com.siyoumarket.dprint/stream").setStreamHandler(
                object : EventChannel.StreamHandler {
                    override fun onListen(args: Any, events: EventChannel.EventSink) {
                        mEvents = events
                        Log.w("MainDprint", "adding listener")
                    }

                    override fun onCancel(args: Any) {
                        Log.w("MainDprint", "cancelling listener")
                    }
                }
        )
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        if (this.mBluetoothAdapter == null && "isAvailable" != call.method) {
            result.error("bluetooth_unavailable", "the device does not have bluetooth", null)
            return
        }

        when (call.method) {
            "initBluetooth" -> init()
            "isAvailable" -> result.success(mBluetoothAdapter != null)
            "isOn" -> result.success(mBluetoothAdapter?.isEnabled)
            "startScan" -> startScan()
            "getBoundDevices" -> getBoundDevices()
            "destroy" -> destroy()
            else -> result.notImplemented()
        }
    }

    private fun init() {
        startBluetooth()

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        this.activity.registerReceiver(mReceiver, filter)

//        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    private fun startBluetooth() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            return true
        }
        return false
    }

    private fun startScan() {
        if (this.mBluetoothAdapter?.isEnabled!!) {
            mBluetoothAdapter.startDiscovery()
        } else startBluetooth()
    }

    private fun destroy() {
        this.activity.unregisterReceiver(mReceiver)
        this.mEvents.endOfStream()
    }


    private fun getBoundDevices() {
        val pairedDevices: Set<BluetoothDevice>? = mBluetoothAdapter?.bondedDevices
        pairedDevices?.forEach { device ->
            Log.d("MainDPrint", "Bound Device: ${device.name} - ${device.address}")
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
                    mEvents.success(mapOf<String, String>(Pair("name", device.name), Pair("address", device.address)))

                }
//                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> mDialog.show()
//                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> mDialog.hide()
            }
        }
    }


}


