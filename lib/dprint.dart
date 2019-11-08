import 'dart:async';

import 'package:flutter/services.dart';

class Dprint {
  static const MethodChannel _channel =
      const MethodChannel('com.siyoumarket.dprint');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
