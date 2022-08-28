package com.example.watermelon;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class PlaySongActivity extends AppCompatActivity {
    private String title;
    private String artist;
    private String coverImg;
    private String url;

    private String[] mp3List = MySQLHelper.mp3List;
    private MediaPlayer player = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity.setVisibilities(View.GONE, View.VISIBLE, View.VISIBLE);
    }
}