package com.example.watermelon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

// DB의 music 테이블 내용 getter, setter
public class Music {
    public int id, img_file, playtime;
    public String title, artist, filename;

    // set function
    public void setId(int id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setImg_file(int img_file) { this.img_file = img_file; }
    public void setPlaytime(int playtime) { this.playtime = playtime; }
    public void setFilename(String filename) { this.filename = filename; }

    // get function
    public int getID(){ return this.id; }
    public String getTitle(){ return this.title; }
    public String getArtist(){ return this.artist; }
    public int getImg_file(){ return this.img_file; }
    public int getPlaytime(){ return this.playtime; }
    public String getFilename(){ return this.filename; }
}
