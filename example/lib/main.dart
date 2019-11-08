import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:dprint/dprint.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  final printer = Dprint();
  String text = "Start";

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            RaisedButton(
              onPressed: () async => await printer.init(),
              child: Text("Init Bluetooth"),
            ),
            RaisedButton(
              onPressed: () async {
                await printer.startScan(updateText);
              },
              child: Text("Start Scan"),
            ),
            RaisedButton(
              onPressed: () async => await printer.getBoundDevices(),
              child: Text("Bound Devices"),
            ),
            RaisedButton(
              onPressed: () async => await printer.destroy(),
              child: Text("Destroy"),
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(text),
            )
          ],
        ),
      ),
    );
  }

  void updateText(bean) {
    print("Recieved Bean : $bean");
    setState(() {
      text = bean.toString();
    });
  }
}
