package com.example.inkommendetlf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class YourYesActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //ja knapp, starter søk
        String incomingNumber = intent.getStringExtra("incoming_number");

        Intent webIntent = new Intent(context, WebActivity.class);
        webIntent.putExtra("incoming_number", incomingNumber);
        webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(webIntent);
    }
}

