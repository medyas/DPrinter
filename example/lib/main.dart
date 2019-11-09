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
  final List<BeanItem> _beanList = List();
  final List<Map<String, dynamic>> _boundList = List();
  bool _loading = false;

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
            Row(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Expanded(
                  child: RaisedButton(
                    onPressed: () async => await printer.init(),
                    child: Text("Init Bluetooth"),
                  ),
                ),
                Expanded(
                  child: RaisedButton(
                    onPressed: () async {
                      await printer.startScan(updateText);
                    },
                    child: Text("Start Scan"),
                  ),
                ),
                Expanded(
                  child: RaisedButton(
                    onPressed: () => getBoundDevices(),
                    child: Text("Bound Devices"),
                  ),
                ),
                Expanded(
                  child: RaisedButton(
                    onPressed: () async => await printer.destroy(),
                    child: Text("Destroy"),
                  ),
                ),
              ],
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Text(
                "Paired Devices",
                style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: ListView.builder(
                shrinkWrap: true,
                itemBuilder: (context, index) => _buildItem(_boundList[index]),
                itemCount: _boundList.length,
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(8.0),
              child: Row(
                children: <Widget>[
                  Expanded(
                    child: Text(
                      "Available Devices",
                      style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                    ),
                  ),
                  if(_loading)
                    Container(
                      margin: const EdgeInsets.all(8.0),
                      alignment: Alignment.center,
                      child: CircularProgressIndicator(),
                    ),
                ],
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: ListView.builder(
                shrinkWrap: true,
                itemBuilder: (context, index) =>
                    _buildItem(_beanList[index].toJson()),
                itemCount: _boundList.length,
              ),
            ),
          ],
        ),
      ),
    );
  }

  void updateText(bean) {
    print("Recieved Bean : $bean");
    final item = BeanItem.fromJson(bean);
    if (item.status == 0)
      _beanList.add(item);
    else if (item.status == 1)
      _loading = true;
    else if (item.status == 2) _loading = false;
    setState(() {});
  }

  getBoundDevices() async {
    final List<Map<String, dynamic>> list = await printer.getBoundDevices();
    _boundList.addAll(list);
  }

  Widget _buildItem(Map<String, dynamic> item) => ListTile(
        title: Text(item["name"]),
        subtitle: Text(item["address"]),
        leading: Icon(Icons.bluetooth),
        onTap: () => onItemClick(item),
      );

  onItemClick(Map<String, dynamic> item) {
    print(item);

    printer.connectToDevice({"name": item["name"], "address": item["address"]});
  }
}

class BeanItem {
  String name;
  String address;
  int status;

  BeanItem(this.name, this.address, this.status);

  BeanItem.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    address = json['address'];
    status = json['status'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['name'] = this.name;
    data['address'] = this.address;
    data['status'] = this.status;
    return data;
  }
}
