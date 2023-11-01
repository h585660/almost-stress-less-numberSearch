package com.example.inkommendetlf;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import javax.xml.transform.Result;

public class WebSearchWorker extends Worker {

    public WebSearchWorker(
            @NonNull Context appContext,
            @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
    }

    @Override
    public Result doWork() {
        // Get the input data (incoming number)
        String incomingNumber = getInputData().getString("incomingNumber");

        // Use JSoup to perform the web scraping
        try {
            // Connect to the website
            Document doc = Jsoup.connect("https://www.gulesider.no/person?q=" + incomingNumber).get();

            // Extract the name or any other information you need
            String name = doc.select("YOUR_CSS_SELECTOR").first().text();

            // If you need to send this information back to your app, you could use some form of local storage, broadcasting, etc.
            // For example, you could use a local broadcast, save to SharedPreferences, or any other mechanism your app already uses.

            // Indicate success and return
            return Result.success();

        } catch (Exception e) {
            // Handle the exception appropriately
            e.printStackTrace();
            return Result.failure();
        }
    }

}

