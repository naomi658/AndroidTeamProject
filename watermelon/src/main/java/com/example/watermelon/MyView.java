package com.example.watermelon;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    public String getTitleTextView(){
        return this.title.getText().toString();
    }
    public String getArtistTextView(){
        return this.artist.getText().toString();
    }
    public Bitmap getCoverImgView(){
//        return this.coverImg.getDrawable();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) this.coverImg.getDrawable();

        return bitmapDrawable.getBitmap();
    }
}
