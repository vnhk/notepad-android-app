package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class OnlineActivity extends AppCompatActivity {
    private WebView notepadWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        notepadWebView = findViewById(R.id.web_view);
        notepadWebView.addJavascriptInterface(new WebInterface(this), "Android");

        if (savedInstanceState == null) {
            notepadWebView.loadUrl("https://noter-word-app.herokuapp.com/");
            addListenerOnTheme();
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

    private void addListenerOnTheme() {
        notepadWebView.loadUrl("javascript:alert(1);");
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

    class WebInterface {
        OnlineActivity mContext;
        View panel;
        ImageView back;

        public WebInterface(OnlineActivity c) {
            this.mContext = c;
            panel = mContext.findViewById(R.id.layout_panel);
            back = mContext.findViewById(R.id.back);
        }

        @JavascriptInterface
        public void showAlert() {
            Toast.makeText(mContext, "This is being called from the interface", Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void setGreenPanel() {
            panel.setBackgroundColor(Color.parseColor("#004000"));
            back.setColorFilter(Color.parseColor("#FFFFFF"));
        }

        @JavascriptInterface
        public void setDarkPanel() {
            panel.setBackgroundColor(Color.parseColor("#000000"));
            back.setColorFilter(Color.parseColor("#FFFFFF"));
        }

        @JavascriptInterface
        public void setBluePanel() {
            panel.setBackgroundColor(Color.parseColor("#5161ce"));
            back.setColorFilter(Color.parseColor("#FFFFFF"));
        }
    }
}