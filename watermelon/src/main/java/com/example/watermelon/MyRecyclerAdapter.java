package com.example.watermelon;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyView> {
    List<Music> musics;
    Context context;

    public MyRecyclerAdapter(List<Music> musics){
        this.musics = musics;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View musicView = inflater.inflate(R.layout.layout_item, parent, false);
        MyView viewHolder = new MyView(musicView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Music music = musics.get(position);

        TextView title = holder.title;
        title.setText(music.getTitle());

        TextView artist = holder.artist;
        artist.setText(music.getArtist());

        ImageView imgID = holder.coverImg;
        imgID.setImageResource(music.getImg_file());
    }

    @Override
    public int getItemCount() {
        return musics.size();
    }
}
