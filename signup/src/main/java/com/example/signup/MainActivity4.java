package com.example.signup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

public class MainActivity4 extends AppCompatActivity {
    SQLiteDatabase mdb;
    Cursor mCursor;
    MysqlHelper mydb;
    static final int RQCODE_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mydb = new MysqlHelper(this, "mydb.db", null, 1);
        mdb = mydb.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM login", null);
        Intent intent=getIntent();


        findViewById(R.id.btn_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int _id = mCursor.getInt(0);
                String str_id = mCursor.getString(1);
                int int_pw = mCursor.getInt(2);
                int int_pwOk = mCursor.getInt(2);
                String str_name=mCursor.getString(3);
                Intent i = new Intent(MainActivity4.this,MainActivity2.class);
                i.putExtra("_id", _id);
                i.putExtra("id", str_id);
                i.putExtra("pw",int_pw);
                i.putExtra("pw",int_pwOk);
                i.putExtra("name",str_name);

                startActivityForResult(i,RQCODE_UPDATE);
            }
        });
    }

}