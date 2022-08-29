package com.example.watermelon;

import static com.example.watermelon.MySQLHelper.mp3List;
import static com.example.watermelon.ShowMyInfo.RQCODE_UPDATE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private static MyView mv;

    MediaPlayer mp;
    final int VISIBLE = View.VISIBLE;
    final int INVISIBLE = View.INVISIBLE;
    final int GONE = View.GONE;

    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;
    Intent i;
    // play/pause 클릭 상태
    // true: pause, false: play
    boolean play_flag = false;

    MyRecyclerAdapter musicAdapter;
    // image Resource ID
    Bitmap imgResID;
    private long backKeyPressedTime = 0;
    //첫 번째 뒤로가기 버튼을 누를때 표시
    private Toast toast;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fbinding = DataBindingUtil.setContentView(this, R.layout.layout_fragment);

        // Water Melon 한 글자만 빨간색으로 변경
        SpannableStringBuilder ssb = new SpannableStringBuilder(fbinding.tvAppName.getText().toString());
        ssb.setSpan(new ForegroundColorSpan(Color.RED), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        fbinding.tvAppName.setText(ssb);
        mydb = new MySQLHelper(this, "login.db", null, 1);
        mdb = mydb.getReadableDatabase();
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
        // mypage 이미지(버튼) 클릭시
        findViewById(R.id.btn_myInfo).setOnClickListener(info);


        //로그인 후 로그인 정보 받기
        i = getIntent();
        //새롭게 추가 이미지뷰
        mv = new MyView(fbinding.playListImageView);

    }

    // recyclerPlaylist Click Listener
    MyRecyclerAdapter.OnItemClickListener recyclerListener = new MyRecyclerAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(int position, String title, String artist, Bitmap imgRes) {
            setVisibilities(GONE, VISIBLE, VISIBLE);
            fbinding.tvTitle.setText(title);
            fbinding.tvArtist.setText(artist);
            fbinding.imgCover.setImageBitmap(imgRes);
            fbinding.playListImageView.setImageResource(R.drawable.menu);

            //ImageView Color Setting
//            fbinding.playListImageView.setColorFilter(Color.parseColor("#FF3700B3"));
            imgResID = imgRes;

            Log.i("pos", position + "\t" + imgRes);
        }
    };

    // playListImageView Click Listener
    View.OnClickListener lisList = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (fbinding.playerViewGroup.getVisibility() == VISIBLE) {
                setVisibilities(VISIBLE, GONE, VISIBLE);
                //새롭게 추가
                fbinding.playListImageView.setImageBitmap(imgResID);
            } else {
                setVisibilities(GONE, VISIBLE, VISIBLE);
                //새롭게 추가
                fbinding.playListImageView.setImageResource(R.drawable.menu);
            }
        }
    };

    @Override
    public void onBackPressed() {
        if (fbinding.playerViewGroup.getVisibility() == VISIBLE) {
            setVisibilities(VISIBLE, GONE, VISIBLE);
            fbinding.playListImageView.setImageBitmap(imgResID);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
            // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
            // 현재 표시된 Toast 취소
            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
                toast.cancel();
            }
        }
    }


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

            if (requestCode == REQ_PLAY_SONG) {

            }
        } catch (Exception e) {
            ;
        }
    }

    //마이페이지 버튼 클릭
    View.OnClickListener info = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int _id = i.getIntExtra("_id", -1);
            String str_id = i.getStringExtra("id");
            int int_pw = i.getIntExtra("pw", -2);
            String str_name = i.getStringExtra("name");

            mCursor = mdb.rawQuery("SELECT * FROM login WHERE " +
                    "id = '" + str_id + "' AND pw = " + int_pw + " AND name = '" + str_name + "';", null);
            mCursor.moveToNext();

            Intent i = new Intent(FragmentActivity.this, ShowMyInfo.class);
            i.putExtra("_id", _id);
            i.putExtra("id", str_id);
            i.putExtra("pw", int_pw);
            i.putExtra("name", str_name);

            startActivityForResult(i, RQCODE_UPDATE);
        }
    };

    // onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            mCursor = mdb.rawQuery("SELECT * FROM login", null);

            mCursor.moveToNext();

            int _id = data.getIntExtra("_id", -1);
            String str_id = data.getStringExtra("id");
            int int_pw = data.getIntExtra("pw", -2);
            String str_name = data.getStringExtra("name");

            if (requestCode == RQCODE_UPDATE) {
                i = getIntent();
                Log.i("update", str_name);
                Log.i("update", str_id);
                Log.i("update", Integer.toString(int_pw));

                mdb.execSQL("UPDATE login" +
                        " SET id = '" + str_id + "'" +
                        ", pw = " + int_pw + ", "
                        + "name= '" + str_name +
                        "' WHERE _id =" + _id + "; ");

                mCursor.requery();

            }
            mCursor.requery();
        }
    }
}