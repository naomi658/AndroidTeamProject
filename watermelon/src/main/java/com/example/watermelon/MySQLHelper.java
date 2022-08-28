package com.example.watermelon;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MySQLHelper extends SQLiteOpenHelper {
    SQLiteDatabase mdb;
    Cursor cursor;
    Music music;

    private static String DB_PATH = "";
    private static String TABLE_NAME = "music.db";
    private static String mPath = DB_PATH + TABLE_NAME;
    private final Context mContext;

    String[] mp3List; // mp3 파일 저장용
    String[] musicInfoArr = {
            "'에잇(Eight)', 'IU', " + R.drawable.eight + ", " + 222 + ", 'eight.mp3');",
            "'forever1', 'Girls Generation', " + R.drawable.forever_1 + ", " + 214 + ", 'forever1.mp3');",
            "'Love Dive', 'Ive', " + R.drawable.lovedive + ", " + 179 + ", 'lovedive.mp3');",
            "'Off My Face', 'Justin Bieber', " + R.drawable.offmyface + ", " + 157 + ", 'offmyface.mp3');",
            "'That That', 'Psy', " + R.drawable.thatthat + ", " + 217 + ", 'thatthat.mp3');",
            "'Wellerman', 'Nathan Evans', " + R.drawable.wellerman + ", " + 222 + ", 'wellerman.mp3');"
    };

    public MySQLHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE music (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, artist TEXT NOT NULL, img_file int NOT NULL, playtime int NOT NULL, filename TEXT);");
        db.execSQL("CREATE TABLE login (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id TEXT NOT NULL, pw INTEGER NOT NULL, name TEXT);");

        for (int i = 0; i < musicInfoArr.length; i++) {
            db.execSQL("INSERT INTO music VALUES (null, " + musicInfoArr[i]);
        }

        db.execSQL("INSERT INTO login VALUES (null, 'a', 1, '양현후');");
        db.execSQL("INSERT INTO login VALUES (null, 'b', 2, '이민영');");

        Log.i("ming", "DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // SD 카드에 파일 저장
    void readExternalMusicFiles() {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        String sdPath = path.getAbsolutePath();

        // mp3 파일 필터링
        // 파일 확장자 .mp3 인 것만 필터링
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".mp3");
            }
        };

        // 외부 저장소의 기본 음악폴더에서 mp3 파일만을 배열에 저장
        mp3List = path.list(filter);

        for (int i = 0; i < mp3List.length; i++) {
            Log.i("ming", mp3List[i]);
        }

        for (int i = 0; i < musicInfoArr.length; i++) {
            Log.i("ming", musicInfoArr[i].toString());
        }
        getTableData();
    }

    // music 테이블 정보 read
    public List getTableData() {
        List mList = new ArrayList(); // 테이블 정보를 저장할 List

        mdb = getReadableDatabase();
        cursor = mdb.rawQuery("SELECT * FROM music", null);

        try {
            // 테이블 끝까지 읽기
            if (cursor != null) {
                // 다음 Row로 이동
                while (cursor.moveToNext()) {
                    music = new Music();

                    music.setId(cursor.getInt(0));
                    music.setTitle(cursor.getString(1));
                    music.setArtist(cursor.getString(2));
                    music.setImg_file(cursor.getInt(3));
                    music.setPlaytime(cursor.getInt(4));
                    music.setFilename(cursor.getString(5));

                    mList.add(music);
                }
                for (int i = 0; i < mList.size(); i++) {
                    Log.i("musicList", mList.get(i).toString());
                }
            }
        } catch (Exception e) {
            ;
        }
        return mList;
    }

}
