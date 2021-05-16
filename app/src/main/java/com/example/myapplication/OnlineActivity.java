package com.example.myapplication;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.NonNull;

public class OnlineActivity extends NotepadNavigableActivity {
    private WebView notepadWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initLayout();
        notepadWebView = findViewById(R.id.web_view);

        if (savedInstanceState == null) {
            notepadWebView.loadUrl("https://noter-word-app.herokuapp.com/");
        }

        notepadWebView.setWebViewClient(new NotepadWebView());
        notepadWebView.setWebChromeClient(new WebChromeClient());

        WebSettings webSettings = notepadWebView.getSettings();
        setWebSettingsOptions(webSettings);
    }

    private void setWebSettingsOptions(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
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

    protected void initLayout() {
        setContentView(R.layout.activity_online);
    }

}