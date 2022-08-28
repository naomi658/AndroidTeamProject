package com.example.watermelon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watermelon.databinding.LayoutFragmentBinding;

public class MyView extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView artist;
    public ImageView coverImg;

    public MyView(@NonNull View itemView) {
        super(itemView);

        this.title = itemView.findViewById(R.id.tv_item_title);
        this.artist = itemView.findViewById(R.id.tv_item_artist);
        this.coverImg = itemView.findViewById(R.id.img_item_cover);
    }
}
