package com.example.itubeapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp.R;
import com.example.itubeapp.adapters.UrlRecyclerViewAdapter;
import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.models.Url;
import com.example.itubeapp.persistent.UrlRepository;

import java.util.List;

public class PlayListActivity extends AppCompatActivity {
    List<Url> urls;
    RecyclerView recyclerView;

    private void setupUrlRecycler() {
        UrlRepository urlRepository = (UrlRepository) RepositoriesHandler.getInstance().getRepository(UrlRepository.class.getName());
        urls = urlRepository.readAll();

        UrlRecyclerViewAdapter adapter = new UrlRecyclerViewAdapter(this, urls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initViews() {
        recyclerView = findViewById(R.id.playlist_recycler_view);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        initViews();
        setupUrlRecycler();

    }
}
