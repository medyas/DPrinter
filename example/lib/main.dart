import 'package:dprint/dprint.dart';
import 'package:flutter/material.dart';

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
            if(!_connected) ...[
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
                        style:
                        TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                      ),
                    ),
                    if (_loading)
                      Container(
                        margin: const EdgeInsets.all(8.0),
                        alignment: Alignment.center,
                        child: Icon(Icons.bluetooth_searching, size: 32,),
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
            if(_connected)
              Padding(
                padding: const EdgeInsets.all(8.0),
                child: Text(
                  "Connected to Printer",
                  style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                ),
              ),
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
    else if (item.status == 2) _loading = false;
    else if (item.status == 3) _connected = true;
    setState(() {});
  }

  getBoundDevices() async {
    final list = await printer.getBoundDevices();
    final l = list.map((item) => Map<String, dynamic>.from(item));
    _boundList.addAll(l);
    setState(() {});
  }

  Widget _buildItem(Map<String, dynamic> item) => ListTile(
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
