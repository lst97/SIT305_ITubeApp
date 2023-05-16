package com.example.itubeapp.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp.R;
import com.example.itubeapp.handlers.RepositoriesHandler;
import com.example.itubeapp.models.Url;
import com.example.itubeapp.persistent.UrlRepository;

import java.util.List;

public class UrlRecyclerViewAdapter extends RecyclerView.Adapter<UrlRecyclerViewAdapter.ViewHolder> {

    Context context;
    List<Url> urls;
    UrlRepository urlRepository;

    public UrlRecyclerViewAdapter(Context context, List<Url> urls) {
        this.context = context;
        this.urls = urls;
        urlRepository = (UrlRepository) RepositoriesHandler.getInstance().getRepository(UrlRepository.class.getName());
    }

    @NonNull
    @Override
    public UrlRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_url, parent, false);
        return new UrlRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UrlRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.urlView.setText(urls.get(position).getUrl());
        holder.urlView.setOnClickListener(v -> {

            // copy url to clipboard
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", urls.get(position).getUrl());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView urlView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            urlView = itemView.findViewById(R.id.recycler_url_view);
        }
    }
}
