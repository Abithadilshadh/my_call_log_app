package com.example.my_call_log_app;

import android.os.Bundle;
import android.content.Intent;
import io.flutter.embedding.android.FlutterActivity;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "com.example.my_call_log_app/call_log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL)
            .setMethodCallHandler((call, result) -> {
                switch (call.method) {
                    case "sendCallLogToAPI":
                        String number = call.argument("number");
                        String type = call.argument("type");
                        String date = call.argument("date");
                        String duration = call.argument("duration");
                        sendCallLogToService(number, type, date, duration, result);
                        break;
                    case "sendCallLogToFlutter":
                        String flutterNumber = call.argument("number");
                        String flutterType = call.argument("type");
                        String flutterDate = call.argument("date");
                        String flutterDuration = call.argument("duration");
                        sendCallLogToFlutter(flutterNumber, flutterType, flutterDate, flutterDuration);
                        result.success(null); // Respond to Flutter
                        break;
                    default:
                        result.notImplemented();
                        break;
                }
            });
    }

    private void sendCallLogToFlutter(String number, String type, String date, String duration) {
    // Forward call log data to Flutter
    MethodChannel methodChannel = new MethodChannel(getFlutterEngine().getDartExecutor().getBinaryMessenger(), CHANNEL);
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("number", number);
    arguments.put("type", type);
    arguments.put("date", date);
    arguments.put("duration", duration);
    methodChannel.invokeMethod("onCallLogAdded", arguments);
}

    private void sendCallLogToService(String number, String type, String date, String duration, MethodChannel.Result result) {
        // Forward call log data to CallLogService
        Intent serviceIntent = new Intent(this, CallLogService.class);
        serviceIntent.putExtra("number", number);
        serviceIntent.putExtra("type", type);
        serviceIntent.putExtra("date", date);
        serviceIntent.putExtra("duration", duration);
        startService(serviceIntent);

        // Optional: Provide a result back to Flutter indicating success
        result.success("Call log data sent to service");
    }
}