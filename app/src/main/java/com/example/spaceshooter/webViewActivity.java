package com.example.spaceshooter;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class webViewActivity extends AppCompatActivity {
    WebView myWebView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.internet_view);
        myWebView = (WebView) findViewById(R.id.web_view);
        myWebView.loadUrl("https://github.com/DucNguyen1311/SpaceShooter");
        findViewById(R.id.back_from_webview).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
