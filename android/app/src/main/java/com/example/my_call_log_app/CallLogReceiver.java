package com.example.my_call_log_app;



import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.CallLog;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;




public class CallLogReceiver extends BroadcastReceiver {
    private static final String TAG = "CallLogReceiver";
    private FlutterEngine flutterEngine;


    public CallLogReceiver(FlutterEngine flutterEnginer){
        this.flutterEngine = flutterEngine;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            // Handle outgoing call
            String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            String type = "Outgoing";
            String date = String.valueOf(System.currentTimeMillis()); // Use current timestamp
            String duration = "0"; // Duration is 0 for outgoing calls in NEW_OUTGOING_CALL broadcast
            sendCallLogToFlutter(context,number, type, date, duration);
          Log.d(TAG, "Outgoing call: " + number);
            //sendCallLogToService(context, number, type, date, duration);
        } else {
            // Handle incoming call or missed call
            String number = intent.getStringExtra(CallLog.Calls.NUMBER);
            String type;
            switch (Integer.parseInt(intent.getStringExtra(CallLog.Calls.TYPE))) {
                case CallLog.Calls.OUTGOING_TYPE:
                    type = "Outgoing";
                    break;
                case CallLog.Calls.INCOMING_TYPE:
                    type = "Incoming";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    type = "Missed";
                    break;
                default:
                    type = "Unknown";
                    break;
            }
                  Log.d(TAG, "Incoming call: " + number);
            String date = intent.getStringExtra(CallLog.Calls.DATE);
            String duration = intent.getStringExtra(CallLog.Calls.DURATION);
            sendCallLogToService(context, number, type, date, duration);
           sendCallLogToFlutter(context,number, type, date, duration);

        }
    }

    private void sendCallLogToService(Context context, String number, String type, String date, String duration) {
        Intent serviceIntent = new Intent(context, CallLogService.class);
        serviceIntent.putExtra("number", number);
        serviceIntent.putExtra("type", type);
        serviceIntent.putExtra("date", date);
        serviceIntent.putExtra("duration", duration);
        context.startService(serviceIntent);
    }


 private void sendCallLogToFlutter(Context context, String number, String type, String date, String duration) {
       MethodChannel methodChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.example.my_call_log_app/call_log");
    Map<String, Object> arguments = new HashMap<>();
    arguments.put("number", number);
    arguments.put("type", type);
    arguments.put("date", date);
    arguments.put("duration", duration);
    methodChannel.invokeMethod("onCallLogAdded", arguments);
}
}