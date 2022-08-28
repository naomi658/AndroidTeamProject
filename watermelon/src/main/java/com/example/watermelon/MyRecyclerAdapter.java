package com.example.watermelon;

import android.content.Context;
import android.graphics.Bitmap;
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

    // Click Event 구현하기 위한 인터페이스
    public interface OnItemClickListener {
        void onItemClicked(int position, String title, String artist, Bitmap imgRes);
    }

    // OnItemClickListener 참조변수 선언
    private OnItemClickListener itemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }

    public MyRecyclerAdapter(List<Music> musics) {
        this.musics = musics;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View musicView = inflater.inflate(R.layout.layout_item, parent, false);
        MyView viewHolder = new MyView(musicView);

        musicView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "";
                String artist = "";
                Bitmap imgRes = null;

                int position = viewHolder.getAdapterPosition();

                if (position != RecyclerView.NO_POSITION) {
                    title = viewHolder.getTitleTextView();
                    artist = viewHolder.getArtistTextView();
                    imgRes = viewHolder.getCoverImgView();
                }
                itemClickListener.onItemClicked(position, title, artist, imgRes);
            }
        });

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
