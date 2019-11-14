import 'dart:typed_data';
import 'dart:ui' as ui;

import 'package:barcode_flutter/barcode_flutter.dart';
import 'package:dprint/dprint.dart';
import 'package:flutter/material.dart';
import 'package:flutter/rendering.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  Dprint printer;
  final List<BeanItem> _beanList = List();
  final List<Map<String, dynamic>> _boundList = List();
  bool _loading = false;
  bool _connected = false;
  GlobalKey globalKey = GlobalKey();

  Future<void> _capturePng() async {
    RenderRepaintBoundary boundary =
    globalKey.currentContext.findRenderObject();
    ui.Image image = await boundary.toImage(pixelRatio: 1.50);
//    var img = copyResize(image, width: 555);
    ByteData byteData = await image.toByteData(format: ui.ImageByteFormat.png);
    Uint8List pngBytes = byteData.buffer.asUint8List();
    print(pngBytes);

    printer.printImage(pngBytes);
  }

  @override
  void initState() {
    super.initState();

    printer = Dprint(updateText);
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  void initPlatformState() {
    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: <Widget>[
            InkWell(
              child: SizedBox(
                  height: 210,
                  width: 280, // old 260
                  child: RepaintBoundary(
                    key: globalKey,
                    child: MemberProductLabel(),
                  )),
              onTap: () async => await _capturePng(),
            ),
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
                      await printer.startScan();
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
            if (!_connected) ...[
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
                  itemBuilder: (context, index) =>
                      _buildItem(_boundList[index]),
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
                        style: TextStyle(
                            fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                    ),
                    if (_loading)
                      Container(
                        margin: const EdgeInsets.all(8.0),
                        alignment: Alignment.center,
                        child: Icon(
                          Icons.bluetooth_searching,
                          size: 32,
                        ),
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
                  itemCount: _beanList.length,
                ),
              ),
            ],
            if (_connected)
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  "Connected to Printer",
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
              ),
            if (_connected)
              RaisedButton(
                onPressed: () => printDemoLabel(),
                child: Text("Print Demo"),
              ),
          ],
        ),
      ),
    );
  }

  void updateText(bean) {
    print("Recieved Bean : $bean");
    final item = BeanItem.fromJson(Map<String, dynamic>.from(bean));
    print("item: $item");
    if (item.status == 0)
      _beanList.add(item);
    else if (item.status == 1)
      _loading = true;
    else if (item.status == 2)
      _loading = false;
    else if (item.status == 3) _connected = true;
    setState(() {});
  }

  getBoundDevices() async {
    final list = await printer.getBoundDevices();
    final l = list.map((item) => Map<String, dynamic>.from(item));
    _boundList.addAll(l);
    setState(() {});
  }

  Widget _buildItem(Map<String, dynamic> item) =>
      ListTile(
        title: Text(item["name"]),
        subtitle: Text(item["address"]),
        leading: Icon(Icons.bluetooth),
        onTap: () => onItemClick(item),
      );

  onItemClick(Map<String, dynamic> item) async {
    print(item);
    print("Paired List: $_boundList");
    final res = await printer
        .connectToDevice({"name": item["name"], "address": item["address"]});
    if(res) setState(() {
      _connected = true;
    });
    print("Connected to device? $res");
  }

  printDemoLabel() async {
    final res = await printer.printLabel({"name": "Random", "price": "12.99"});
    print("Printed Label? $res");
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


class MemberProductLabel extends StatelessWidget {
  final String text;
  final double height;
  final double lineWidth;

  MemberProductLabel({Key key,
    this.text = "(con cart)",
    this.height = 80,
    this.lineWidth = 1.3})
      : super(key: key);

  @override
  Widget build(BuildContext context) {
    return SizedBox(
//      height: 200,
//      width: 260,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        mainAxisSize: MainAxisSize.max,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(4.0),
            child: Text(
              "Product Test 1",
              style: Theme
                  .of(context)
                  .textTheme
                  .title
                  .copyWith(fontSize: 16.0, fontWeight: FontWeight.bold),
            ),
          ),
          Container(
            height: 2.0,
            color: Colors.black,
            margin: EdgeInsets.symmetric(horizontal: 4.0, vertical: 8.0),
          ),
          Row(
            children: <Widget>[
              Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                mainAxisSize: MainAxisSize.max,
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Text(
                      "Siyou Tech",
                      textAlign: TextAlign.center,
                      style:
                      TextStyle(fontWeight: FontWeight.bold, fontSize: 13),
                    ),
                  ),
                  BarCodeImage(
                    padding: EdgeInsets.all(8.0),
                    hasText: true,
                    data: "2010030002880",
                    codeType: BarCodeType.CodeEAN13,
                    barHeight: height,
                    lineWidth: lineWidth,
                    onError: (error) {
                      print('error = $error');
                    },
                  ),
                ],
              ),
              Expanded(
                child: Column(
                  mainAxisSize: MainAxisSize.max,
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  crossAxisAlignment: CrossAxisAlignment.center,
                  children: <Widget>[
                    Text(
                      "\€ 1,99",
                      style: TextStyle(
                          fontWeight: FontWeight.w800, fontSize: 34.0),
                      textAlign: TextAlign.center,
                    ),
                    Text(text),
                    SizedBox(
                      height: 16.0,
                    ),
                    Text(
                      "\€ 1,49",
                      style: TextStyle(
                          fontWeight: FontWeight.w600, fontSize: 28.0),
                      textAlign: TextAlign.center,
                    ),
                  ],
                ),
              )
            ],
          ),
        ],
      ),
    );
  }
}
