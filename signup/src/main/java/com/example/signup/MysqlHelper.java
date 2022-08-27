package com.example.signup;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MysqlHelper extends SQLiteOpenHelper {


    public MysqlHelper(@Nullable Context context, @Nullable String name,
                       @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE login (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "id TEXT NOT NULL, pw INTEGER NOT NULL, name TEXT);");

        sqLiteDatabase.execSQL("INSERT INTO login VALUES (null, 'didgusgn', 1234, '양현후');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
