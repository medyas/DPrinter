package com.siyoumarket.dprint

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.widget.Toast
import com.siyoumarket.dprint.BluetoothPlugin.Companion.REQUEST_ENABLE_BT
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar


class DprintPlugin(private val activity: Activity, private val channel: MethodChannel, registrar: Registrar) : MethodCallHandler, PluginRegistry.ActivityResultListener {

    private var mBluetoothPlugin = BluetoothPlugin()
    private var mPrinterPlugin = PrinterPlugin()


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
            "isConnected" -> result.success(mPrinterPlugin.isConnected())
            "startScan" -> mBluetoothPlugin.startScan(activity)
            "getBoundDevices" -> getBoundDevices(result)
            "connectToDevice" -> connectToDevice(call, result)
            "printLabel" -> printLabel(call, result)
            "printImage" -> printImage(call, result)
            "destroy" -> destroy()
            else -> result.notImplemented()
        }
    }


    private fun init() {
        mBluetoothPlugin.startBluetooth(activity)

        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED)
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED)
        this.activity.registerReceiver(mReceiver, filter)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_OK) {
            return true
        }
        return false
    }

    private fun getBoundDevices(result: Result) {
        val devices = mBluetoothPlugin.getBoundDevices()
        val list = devices?.map<BluetoothDevice?, Map<String, String>> { item ->
            mapOf<String, String>(Pair("name", item!!.name), Pair("address", item.address))
        }
        result.success(list)
    }

    private fun connectToDevice(call: MethodCall, result: Result) {
        val device = call.arguments as Map<*, *>

        activity.showToast("Connecting to ${device["name"]}...")

        try {
            val res = mPrinterPlugin.connectToPrinter(device, activity)
            if (res) activity.showToast("Connected to ${device["name"]}")
            else activity.showToast("Failed to connect !")

            result.success(res)
        } catch (e: Exception) {
            e.printStackTrace()

            activity.showToast("Failed to connect !")
            result.success(false)
        }
    }

    private fun printLabel(call: MethodCall, result: Result) {
        if (this.mPrinterPlugin.isConnected()) {
            val label = call.arguments as Map<*, *>

            try {
                mPrinterPlugin.printDemo()
                activity.showToast("Printing Label...")
                result.success(true)
                return
            } catch (e: Exception) {
                activity.showToast("Error: ${e.message}")
            }
        }

        activity.showToast("Please connect to printer !")
        result.success(false)
        return
    }

    private fun printImage(call: MethodCall, result: Result) {
        val image = call.arguments as ByteArray
        val res = mPrinterPlugin.printImage(image)
        result.success(res)
    }

    private fun destroy() {
        this.activity.unregisterReceiver(mReceiver)
        this.mBluetoothPlugin.destroy()
        this.mPrinterPlugin.destroy()
    }

    // Create a BroadcastReceiver for ACTION_FOUND.
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("MainReciever", intent.action)
            // Discovery has found a device. Get the BluetoothDevice
            // object and its info from the Intent.

            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 0), Pair("name", device.name), Pair("address", device.address)))

                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 1)))
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 2)))

                BluetoothDevice.ACTION_ACL_CONNECTED -> {
                    if(mPrinterPlugin.isConnected())
                        mBluetoothPlugin.sendMsg(mapOf<String, Any>(Pair("status", 3)))
                }
//                BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED ->
//                BluetoothDevice.ACTION_ACL_DISCONNECTED -> connected = false
            }
        }
    }

    private fun Activity.showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

}


