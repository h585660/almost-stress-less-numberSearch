package com.example.inkommendetlf;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1;

    public static final String CHANNEL_ID = "com.example.inkommendetlf.CALL_NOTIFICATION_CHANNEL";
    public static final int NOTIFICATION_ID = 1;




    public final BroadcastReceiver callReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);



            if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                // Intent for the "Yes" button
                Intent yesIntent = new Intent(context, YourYesActionReceiver.class);
                yesIntent.putExtra("incoming_number", incomingNumber);

                int yesFlags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    yesFlags |= PendingIntent.FLAG_IMMUTABLE;
                }
                PendingIntent yesPendingIntent = PendingIntent.getBroadcast(context, 0, yesIntent, yesFlags);

                Intent noIntent = new Intent(context, YourNoActionReceiver.class);

                int noFlags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    noFlags |= PendingIntent.FLAG_IMMUTABLE;
                }
                PendingIntent noPendingIntent = PendingIntent.getBroadcast(context, 1, noIntent, noFlags);


                // Build the notification
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.sym_call_incoming)
                        .setContentTitle("Inkommende anrop")
                        .setContentText("Sjekk nummer?")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .addAction(android.R.drawable.ic_menu_view, "Ja", yesPendingIntent)
                        .addAction(android.R.drawable.ic_menu_view, "Nei", noPendingIntent);

                // Get an instance of NotificationManagerCompat and call notify on it
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                notificationManager.notify(NOTIFICATION_ID, builder.build());
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        }

        // Create Notification Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Call Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Register BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver, filter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_PHONE_STATE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, continue with registering BroadcastReceiver
                registerCallReceiver();
            } else {
                // Permission denied
                // Check if 'Never ask again' was selected
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)) {
                    Toast.makeText(this, "Permission denied. The app needs this permission to function properly.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permission denied. Please enable it in the app settings.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void registerCallReceiver() {
        // Register BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE");
        registerReceiver(callReceiver, filter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(callReceiver);
    }
}



