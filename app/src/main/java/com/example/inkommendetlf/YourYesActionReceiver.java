package com.example.inkommendetlf;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class YourYesActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the incoming number from the intent
        String incomingNumber = intent.getStringExtra("incoming_number");

        // Create InputData with the incoming number
        Data inputData = new Data.Builder()
                .putString("incomingNumber", incomingNumber)
                .build();

        // Create a OneTimeWorkRequest for the WebSearchWorker
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(WebSearchWorker.class)
                .setInputData(inputData)
                .build();

        // Enqueue the WorkRequest
        WorkManager.getInstance(context).enqueue(workRequest);
    }
}
