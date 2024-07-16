import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Call Log App',
      home: HomePage(),
    );
  }
}

class HomePage extends StatelessWidget {
  static const platform = MethodChannel('com.example.my_call_log_app/call_log');

  void sendCallLogToApi(String number, String type, String date, String duration) async {
    try {
      await platform.invokeMethod('sendCallLogToAPI', {
        'number': number,
        'type': type,
        'date': date,
        'duration': duration,
      });
      print('Call log sent successfully from Flutter.');
    } on PlatformException catch (e) {
      print('Error sending call log from Flutter: ${e.message}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Call Log App'),
      ),
      body: Center(
        child: ElevatedButton(
          onPressed: () {
            // Replace with actual call log data
            sendCallLogToApi('1234567890', 'Outgoing', '2024-07-15', '300');
          },
          child: Text('Send Call Log to API'),
        ),
      ),
    );
  }
}