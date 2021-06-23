package com.example.myapplication;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

public class NoteDetailsActivity extends AppCompatActivity {
    TextView header;
    TextView secondary;
    TextView tags;
    TextView persistInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        Intent intent = getIntent();
        Note item = (Note) intent.getSerializableExtra("note");

        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());

        header = findViewById(R.id.headerNote);
        secondary = findViewById(R.id.secondaryNote);
        tags = findViewById(R.id.tags);
        persistInfo = findViewById(R.id.persistInfo);

        header.setText(item.getHeader());
        secondary.setText(item.getSecondary());
        tags.setText(item.getTags());
        persistInfo.setText(item.getPersistInfoDisplay());

        prepareWebView(item);
    }

    private void prepareWebView(Note item) {
        String head = getHeader();
        String html =
                "<html>" + head + "<body>" + item.getContent() + "</body></html>";
        String encodedHtml = Base64.encodeToString(html.getBytes(),
                Base64.NO_PADDING);

        WebView myWebView = findViewById(R.id.web_view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        } else {
            myWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        myWebView.setWebViewClient(new NotepadWebView());
        myWebView.setWebChromeClient(new WebChromeClient());
        myWebView.loadData(encodedHtml, "text/html", "base64");
    }

    @NotNull
    private String getHeader() {
        return "<head><style>img{max-width: 100%; width:auto; height: auto;}" +
                "body{width:80%,max-width:80%,margin:10%;}" +
                "</style></head>";
    }
}