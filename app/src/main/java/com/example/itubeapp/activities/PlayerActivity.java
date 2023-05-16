package com.example.itubeapp.activities;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp.R;
import com.example.itubeapp.helpers.YouTubeApiHelper;

public class PlayerActivity extends AppCompatActivity {
    WebView webView;

    private void initViews() {
        webView = findViewById(R.id.player_webview);
    }

    private void initListeners() {

    }

    private void playVideo(String videoId) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, YouTubeApiHelper.getYouTubePlayerHtml(videoId), "text/html", "utf-8", null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        initListeners();

        String url = getIntent().getStringExtra("video_id");
        getIntent().removeExtra("video_id");

        playVideo(url);
    }
}