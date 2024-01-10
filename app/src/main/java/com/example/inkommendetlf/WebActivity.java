package com.example.inkommendetlf;


import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        String incomingNumber = getIntent().getStringExtra("incoming_number");

        WebView myWebView = findViewById(R.id.webview);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.i("mylog", "pageFinished");
                // Side lastet, javascript interaksjon under
                String js = "var searchBox = document.getElementById('search-box');" +
                        "var nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                        "nativeInputValueSetter.call(searchBox, '" + '+' + incomingNumber + "');" +
                        "var inputEvent = new Event('input', { bubbles: true });" +
                        "searchBox.dispatchEvent(inputEvent);" +
                        "var keydownEvent = new KeyboardEvent('keydown', { bubbles: true });" +
                        "searchBox.dispatchEvent(keydownEvent);" +
                        "var enterEvent = new KeyboardEvent('keydown', { key: 'Enter', bubbles: true });" +
                        "searchBox.dispatchEvent(enterEvent);" +
                        "console.log('JavaScript executed successfully');";
                view.loadUrl("javascript:" + js);

            }
        });

        myWebView.loadUrl("https://www.gulesider.no/");
        Log.i("mylog", "found page");
    }
}



