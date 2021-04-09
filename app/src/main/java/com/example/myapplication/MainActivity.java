package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {
    private WebView notepadWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notepadWebView = findViewById(R.id.webview);

        if (savedInstanceState == null) {
            notepadWebView.loadUrl("https://noter-word-app.herokuapp.com/");
        }

        notepadWebView.setWebViewClient(new NotepadWebView());
        notepadWebView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = notepadWebView.getSettings();
        setWebSettingsOptions(webSettings);
    }

    @Override
    public void onBackPressed() {
        if (notepadWebView.canGoBack()) {
            notepadWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        notepadWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        notepadWebView.restoreState(savedInstanceState);
    }

    private void setWebSettingsOptions(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }
}