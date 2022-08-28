package com.example.watermelon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.watermelon.databinding.LayoutFragmentBinding;

// 메인화면(플레이리스트)
public class FragmentActivity extends AppCompatActivity {
    private static final int REQCODE_PERMISSION_WRITE_EXTERNAL = 1;
    private static final int REQ_PLAY_SONG = 2;
    static LayoutFragmentBinding fbinding;

    final int VISIBLE = View.VISIBLE;
    final int INVISIBLE = View.INVISIBLE;
    final int GONE = View.GONE;

    // play/pause 클릭 상태
    // true: pause, false: play
    boolean play_flag = false;

    MyRecyclerAdapter musicAdapter;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbinding = DataBindingUtil.setContentView(this, R.layout.layout_fragment);

        // Water Melon 한 글자만 빨간색으로 변경
        SpannableStringBuilder ssb = new SpannableStringBuilder(fbinding.tvAppName.getText().toString());
        ssb.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fbinding.tvAppName.setText(ssb);

        checkPermission();

        // 뮤직파일 어댑터에 세팅
        musicAdapter = new MyRecyclerAdapter(MySQLHelper.mList);
        fbinding.recyclerPlaylist.setAdapter(musicAdapter);
        fbinding.recyclerPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // 아이템 클릭 리스너
        musicAdapter.setOnItemClickListener(recyclerListener);
        // menu 이미지 클릭시
        fbinding.playListImageView.setOnClickListener(lisList);
        // play 이미지 클릭시
        fbinding.imgPlay.setOnClickListener(lisImg);
    }

    // recyclerPlaylist Click Listener
    MyRecyclerAdapter.OnItemClickListener recyclerListener = new MyRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(int position, String title, String artist, Bitmap imgRes) {
            setVisibilities(GONE, VISIBLE, VISIBLE);
            fbinding.tvTitle.setText(title);
            fbinding.tvArtist.setText(artist);
            fbinding.imgCover.setImageBitmap(imgRes);
            Log.i("pos", position + "\t" + imgRes);
        }
    };
//    View.OnContextClickListener recyclerListener = new View.OnContextClickListener() {
//        @Override
//        public boolean onContextClick(View v) {
//
//            setVisibilities(GONE, VISIBLE, VISIBLE);
//            int pos = fbinding.recyclerPlaylist.getChildAdapterPosition(v);
//            Log.i("pos", pos+"");
//
//            intent = new Intent(FragmentActivity.this, PlaySongActivity.class);
//            startActivityForResult(intent, REQ_PLAY_SONG);
//
//            return false;
//        }
//    };
    // playListImageView Click Listener
    View.OnClickListener lisList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (fbinding.playerViewGroup.getVisibility() == VISIBLE) {
                setVisibilities(VISIBLE, GONE, VISIBLE);
            } else {
                setVisibilities(GONE, VISIBLE, VISIBLE);
            }
        }
    };

    // imgPlay Click Listener
    View.OnClickListener lisImg = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                play_flag = !play_flag;

                if (play_flag) {
                    fbinding.imgPlay.setImageResource(R.drawable.pause);
                } else {
                    fbinding.imgPlay.setImageResource(R.drawable.play);
                }
            } catch (Exception e) {
                ;
            }
        }
    };

    // Main List 화면과 재생 중 화면의 visible을 컨트롤하는 메소드
    void setVisibilities(int list, int playerView) {
        fbinding.listGroup.setVisibility(list);
        fbinding.playerViewGroup.setVisibility(playerView);
    }

    // Main List 화면, 재생 중 화면, 하단바
    static public void setVisibilities(int list, int playerView, int bottom) {
        fbinding.listGroup.setVisibility(list);
        fbinding.playerViewGroup.setVisibility(playerView);
        fbinding.bottomBarGroup.setVisibility(bottom);
    }

    // 사용권한을 확인하고 필요 시 요청
    // 권한 승인한 경우에만 MySQLHelper 접근가능
    void checkPermission() {
        try {
            if (ContextCompat.checkSelfPermission(FragmentActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // 권한 승인을 거절한 이력을 확인
                if (ActivityCompat.shouldShowRequestPermissionRationale(FragmentActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // 사용자가 권한 승인에 거절을 누른 경우
                    Toast.makeText(this, "No Permissions", Toast.LENGTH_SHORT).show();

                } else {
                    // 최초 접속 시 권한이 없는 경우 사용권한을 요청함
                    ActivityCompat.requestPermissions(FragmentActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQCODE_PERMISSION_WRITE_EXTERNAL);
                }
            } else {
                new MySQLHelper(FragmentActivity.this, "music.db", null, 1).readExternalMusicFiles();
                for (Music item : MySQLHelper.mList) {
                    Log.i("musicListMain", item.getArtist());
                }
            }
        } catch (Exception e) {
            ;
        }
    }

    // 요청한 사용결과에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == REQCODE_PERMISSION_WRITE_EXTERNAL) {
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    new MySQLHelper(FragmentActivity.this, "music.db", null, 1).readExternalMusicFiles();
                }
            }

            if(requestCode == REQ_PLAY_SONG){

            }
        } catch (Exception e) {
            ;
        }
    }
}