package com.example.watermelon;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.IOException;

public class PlayMusicActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener {
    private String title;
    private String artist;
    private String coverImg;
    private String url;
    int pos; // 재생 멈춘 시점
    SeekBar sb; // 음악 재생위치를 나타내는 시크바
    boolean isPlaying = false; // 재생중인지 확인할 변수

    static MediaPlayer player = null;

    class MyThread extends Thread {
        @Override
        public void run() { // 쓰레드가 시작되면 콜백되는 메서드
            // seekBar 막대기 조금씩 움직이기 (노래 끝날 때까지 반복)
            while (isPlaying) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sb.setProgress(player.getCurrentPosition());
            }
        }
    }

    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC); // 파일명

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            player.setDataSource(String.valueOf(Uri.fromFile(path)));
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mp != null) {
            mp.start();
        }
    }
}