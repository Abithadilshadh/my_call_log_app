import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  // Create a MethodChannel instance
  static const platform = MethodChannel('com.example.my_call_log_app/call_log');

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Call Log App',
      home: HomePage(),
    );
  }
}

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> {
  String logMessage = '';

  @override
  void initState() {
    super.initState();
    // Set up method channel handler
    MyApp.platform.setMethodCallHandler((call) async {
      if (call.method == 'onCallLogAdded') {
        // Handle call log added event from native
        String number = call.arguments['number'];
        String type = call.arguments['type'];
        String date = call.arguments['date'];
        String duration = call.arguments['duration'];

        setState(() {
          logMessage = 'Received call log: $number, $type, $date, $duration';
        });

        // Example: Send data to API or perform other actions
        sendCallLogToApi(number, type, date, duration);
      }
    });
  }

  void sendCallLogToApi(String number, String type, String date, String duration) {
    // Implement your API call logic here
    print('Sending call log to API: $number, $type, $date, $duration');
    // Replace with actual API integration code
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Call Log App'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Text(logMessage),
            ElevatedButton(
              onPressed: () {
                // Example: Send mock call log data to Android
                MyApp.platform.invokeMethod('sendCallLogToFlutter', {
                  'number': '1234567890',
                  'type': 'Outgoing',
                  'date': '2024-07-16',
                  'duration': '300',
                });
              },
              child: Text('Send Call Log to Flutter'),
            ),
          ],
        ),
      ),
    );
  }
}