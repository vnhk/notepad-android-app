package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.data.LoginRepository;
import com.example.myapplication.ui.login.LoginActivity;

public class OnlineActivity extends AppCompatActivity {
    private WebView notepadWebView;
    public static boolean loginPerformed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        notepadWebView = findViewById(R.id.web_view);
        notepadWebView.addJavascriptInterface(new WebInterface(this), "Android");
        notepadWebView.setWebChromeClient(new WebChromeClient());
        notepadWebView.setWebViewClient(new NotepadWebView());

        WebSettings webSettings = notepadWebView.getSettings();
        setWebSettingsOptions(webSettings);
        if (savedInstanceState == null) {
            notepadWebView.loadUrl("https://noter-word-app.herokuapp.com/");
        }
    }

    private void setWebSettingsOptions(WebSettings webSettings) {
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
    }


    public void setToken() {
        if (loginPerformed) {

            LoginRepository instance = LoginRepository.getInstance(null);

            String token = instance.getUser().getToken().split("Bearer")[1].trim();

            notepadWebView.post(() -> {
                notepadWebView.loadUrl("javascript:setToken(\"" + token + "\");");
                notepadWebView.loadUrl("https://noter-word-app.herokuapp.com/");
            });

            loginPerformed = false;
        } else {
            CookieManager.getInstance().removeAllCookie();
            LoginRepository.getInstance(null).logout();
            Intent intent = new Intent(OnlineActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
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
        public void onLoginPageLoaded() {
            setToken();
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