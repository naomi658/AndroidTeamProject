package com.example.watermelon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.watermelon.databinding.LayoutFragmentBinding;

// 메인화면(플레이리스트)
public class FragmentActivity extends AppCompatActivity {
    private static final int REQCODE_PERMISSION_WRITE_EXTERNAL = 1;
    private LayoutFragmentBinding fbinding;

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
        checkPermission();
        intent = getIntent();

        MySQLHelper helper = new MySQLHelper(FragmentActivity.this, "music.db", null, 1);
//        // 뮤직파일 어댑터에 세팅
        musicAdapter = new MyRecyclerAdapter(helper.mList);
        fbinding.recyclerPlaylist.setAdapter(musicAdapter);
        fbinding.recyclerPlaylist.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // play 이미지 클릭시
        fbinding.imgPlay.setOnClickListener(lisImg);
        // menu 이미지 클릭시
        fbinding.playListImageView.setOnClickListener(lisList);
    }

    // playListImageView Click Listener
    View.OnClickListener lisList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (fbinding.playerViewGroup.getVisibility() == View.VISIBLE) {
                setVisibilities(VISIBLE, GONE, VISIBLE);
            } else {
                setVisibilities(GONE, VISIBLE, VISIBLE);
                int pos = intent.getIntExtra("itemPos", -1);
                Music musicListPos = MySQLHelper.mList.get(pos);
                int imgRes = musicListPos.getImg_file();

                fbinding.playListImageView.setImageResource(imgRes);
            }
        }
    };

    // Play Button Click Listener
    View.OnClickListener lisImg = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            play_flag = !play_flag;

            if (play_flag) {
                fbinding.imgPlay.setImageResource(R.drawable.pause);
            } else {
                fbinding.imgPlay.setImageResource(R.drawable.play);
            }
        }
    };

    // ListView Item Click Listener
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            setVisibilities(GONE, VISIBLE, VISIBLE);
            intent.putExtra("itemPos", position);
        }
    };

    // Main List 화면과 재생화면의 visible을 컨트롤하는 메소드
    void setVisibilities(int list, int playerView) {
        fbinding.listGroup.setVisibility(list);
        fbinding.playerViewGroup.setVisibility(playerView);
    }

    void setVisibilities(int list, int playerView, int bottom) {
        fbinding.listGroup.setVisibility(list);
        fbinding.playerViewGroup.setVisibility(playerView);
        fbinding.bottomBarGroup.setVisibility(bottom);
    }

    // 사용권한을 확인하고 필요 시 요청
    // 권한 승인한 경우에만 MySQLHelper 접근가능
    void checkPermission() {
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
    }

    // 요청한 사용결과에 대한 결과 처리
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQCODE_PERMISSION_WRITE_EXTERNAL) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                new MySQLHelper(FragmentActivity.this, "music.db", null, 1).readExternalMusicFiles();

//                musicAdapter = new MyRecyclerAdapter(MySQLHelper.mList);
//                fbinding.recyclerPlaylist.setAdapter(musicAdapter);
//                fbinding.recyclerPlaylist.setLayoutManager(new LinearLayoutManager(this));
            }
        }
    }
}
