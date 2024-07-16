import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';

class CallLogService {
  static const platform = MethodChannel('com.example.my_call_log_app/calllog');

  Future<void> fetchAndSendCallLogs() async {
    try {
      final List<dynamic> callLogs = await platform.invokeMethod('getCallLogs');
      // Process call logs and send them to your API
      for (var callLog in callLogs) {
        await sendCallLogToApi(callLog);
      }
    } on PlatformException catch (e) {
      print("Failed to get call logs: '${e.message}'.");
    }
  }

  Future<void> sendCallLogToApi(dynamic callLog) async {
    final response = await http.post(
      Uri.parse('https://your.api/endpoint'),
      headers: <String, String>{
        'Content-Type': 'application/json; charset=UTF-8',
      },
      body: jsonEncode(callLog),
    );
    if (response.statusCode == 200) {
      print("Call log sent successfully");
    } else {
      print("Failed to send call log");
    }
  }
}
