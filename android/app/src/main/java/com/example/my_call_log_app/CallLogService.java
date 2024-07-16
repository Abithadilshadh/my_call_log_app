package com.example.my_call_log_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class CallLogService extends Service {
    private static final String TAG = "CallLogService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service onStartCommand: Service is started.");

        // Process incoming call log data from Flutter
        String number = intent.getStringExtra("number");
        String type = intent.getStringExtra("type");
        String date = intent.getStringExtra("date");
        String duration = intent.getStringExtra("duration");

        // Send call log data to API
        sendCallLogToAPI(number, type, date, duration);

        return START_STICKY;
    }

    private void sendCallLogToAPI(String number, String type, String date, String duration) {
        new Thread(() -> {
            try {
                URL url = new URL("http://192.168.2.162/Leads/CallLogFecth");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                // Create JSON data
                String jsonInputString = String.format(
                        "{\"number\": \"%s\", \"type\": \"%s\", \"date\": \"%s\", \"duration\": \"%s\"}",
                        number, type, date, duration);

                // Send JSON as payload
                try (OutputStream os = conn.getOutputStream()) {
                    byte[] input = jsonInputString.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Check response code (optional)
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    Log.d(TAG, "Call log sent successfully.");
                } else {
                    Log.e(TAG, "Failed to send call log. Response code: " + responseCode);
                }

            } catch (Exception e) {
                Log.e(TAG, "Error sending call log to API: " + e.getMessage());
            }
        }).start();
    }
}