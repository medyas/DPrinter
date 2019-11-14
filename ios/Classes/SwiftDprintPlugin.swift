import Flutter
import UIKit

public class SwiftDprintPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "com.siyoumarket.dprint", binaryMessenger: registrar.messenger())
    let instance = SwiftDprintPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    if ([@"setLogLevel" isEqualToString:call.method]) {
        result(nil);
      } else if ([@"state" isEqualToString:call.method]) {
        result(nil);
      } else if([@"isAvailable" isEqualToString:call.method]) {

      } else {
        result(FlutterMethodNotImplemented);
      }
  }
}
