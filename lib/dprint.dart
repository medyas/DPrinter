import 'dart:async';

import 'package:flutter/services.dart';

class Dprint {
  final MethodChannel _channel =
  const MethodChannel('com.siyoumarket.dprint');
  final _stream =
  const EventChannel('com.siyoumarket.dprint/stream');

  StreamSubscription _beanList = null;


  Future<void> init() async {
    await _channel.invokeMethod('initBluetooth');
  }

  Future<bool> get isAvailable async {
    return await _channel.invokeMethod('isAvailable');
  }

  Future<bool> get isOn async {
    return await _channel.invokeMethod('isOn');
  }

  Future<void> startScan(Function updateData) async {
    if (_beanList == null)
      _beanList = _stream.receiveBroadcastStream([""]).listen(updateData);
    await _channel.invokeMethod('startScan');
  }

  Future<void> getBoundDevices() async {
    await _channel.invokeMethod('getBoundDevices');
  }

  Future<void> destroy() async {
    _beanList = null;
    await _channel.invokeMethod('destroy');
  }
}
