package com.example.itubeapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp.R;
import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.handlers.ServicesHandler;
import com.example.itubeapp.helpers.YouTubeApiHelper;
import com.example.itubeapp.models.Url;
import com.example.itubeapp.models.User;
import com.example.itubeapp.persistent.UrlRepository;
import com.example.itubeapp.services.authenticate.session.SessionService;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {
    EditText urlEditText;
    Button playButton;
    Button addToPlaylistButton;
    Button myPlaylistButton;


    private void initViews() {
        urlEditText = findViewById(R.id.home_url_input);
        playButton = findViewById(R.id.home_play_btn);
        addToPlaylistButton = findViewById(R.id.home_to_playlist_btn);
        myPlaylistButton = findViewById(R.id.home_my_playlist_btn);

    }

    private void initListeners() {
        playButton.setOnClickListener(v -> {
            String url = urlEditText.getText().toString();
            String videoId = YouTubeApiHelper.getYouTubeVideoId(url);

            try {
                if (!YouTubeApiHelper.isYouTubeVideoPlayable(url)) {
                    urlEditText.setError("Invalid YouTube URL");
                    return;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
            intent.putExtra("video_id", videoId);
            startActivity(intent);
        });

        addToPlaylistButton.setOnClickListener(v -> {
            // UrlRepository CREATE

            ServicesHandler servicesHandler = ServicesHandler.getInstance();
            SessionService sessionService = (SessionService) servicesHandler.getService(SessionService.class.getName());
            User user = sessionService.getSession();

            RepositoriesHandler repositoriesHandler = RepositoriesHandler.getInstance();
            UrlRepository urlRepository = (UrlRepository) repositoriesHandler.getRepository(UrlRepository.class.getName());
            urlRepository.create(new Url(urlEditText.getText().toString(), user.getId()));

            Toast.makeText(HomeActivity.this, "Added to playlist", Toast.LENGTH_SHORT).show();
        });

        myPlaylistButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, PlayListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initViews();
        initListeners();


    }
}
