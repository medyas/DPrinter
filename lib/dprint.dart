import 'dart:async';

import 'package:flutter/services.dart';

class Dprint {
  final MethodChannel _channel =
  const MethodChannel('com.siyoumarket.dprint');
  final _stream =
  const EventChannel('com.siyoumarket.dprint/stream');

  StreamSubscription _beanList;

  Dprint(Function updateData) {
    _beanList = _stream.receiveBroadcastStream([""]).listen(updateData);
  }


  Future<void> init() async {
    await _channel.invokeMethod('initBluetooth');
  }

  Future<bool> get isAvailable async {
    return await _channel.invokeMethod('isAvailable');
  }

  Future<bool> get isOn async {
    return await _channel.invokeMethod('isOn');
  }

  Future<bool> get isConnected async {
    return await _channel.invokeMethod('isConnected');
  }

  Future<void> startScan() async {
    await _channel.invokeMethod('startScan');
  }

  Future<List<dynamic>> getBoundDevices() async {
    return await _channel.invokeMethod('getBoundDevices');
  }

  Future<bool> connectToDevice(Map<String, dynamic> device) async {
    return await _channel.invokeMethod('connectToDevice', device);
  }

  Future<bool> printLabel(Map<String, dynamic> label) async {
    return await _channel.invokeMethod('printLabel', label);
  }

  Future<void> destroy() async {
    _beanList.cancel();
    await _channel.invokeMethod('destroy');
  }
}
