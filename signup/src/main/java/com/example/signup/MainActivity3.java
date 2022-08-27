package com.example.signup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity3 extends AppCompatActivity {

    EditText et_id, et_pw, et_pw_sign, et_name;
    SQLiteDatabase mdb;
    Cursor mCursor;
    MysqlHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout3);

        mydb = new MysqlHelper(this, "mydb.db", null, 1);
        mdb = mydb.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM login", null);
        Intent i = getIntent();

        et_id = findViewById(R.id.et_id);
        et_pw = findViewById(R.id.et_pw);
        et_pw_sign = findViewById(R.id.et_pwok);
        et_name = findViewById(R.id.et_name);

        findViewById(R.id.btn_success).setOnClickListener(success);
    }

        View.OnClickListener success = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String str_id = et_id.getText().toString();

            int str_pw = Integer.valueOf(et_pw.getText().toString());
            int str_pw_sign = Integer.valueOf(et_pw_sign.getText().toString());
            String str_name = et_name.getText().toString();

            //pw 같은지 확인
            if (str_pw == str_pw_sign) {
                mdb.execSQL("INSERT INTO login VALUES (null, '" + str_id + "', "
                        + str_pw + ", '" + str_name + "');");

                finish();
            }
        }
    };

}