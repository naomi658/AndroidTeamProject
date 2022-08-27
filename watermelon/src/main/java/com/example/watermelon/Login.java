package com.example.watermelon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class Login extends AppCompatActivity {
    Button btn_signUp;
    static final int RQCODE_INSERT = 1;
    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;

    EditText et_id, et_pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

            mydb = new MySQLHelper(this, "login.db", null, 1);
            mdb = mydb.getWritableDatabase();
            mCursor = mdb.rawQuery("SELECT * FROM login", null);

            btn_signUp = findViewById(R.id.btn_signUp);

            et_id = findViewById(R.id.et_id);
            et_pw = findViewById(R.id.et_pw);

            btn_signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //회원가입 버튼
                    Intent i = new Intent(Login.this, SiginUp.class);
                    startActivityForResult(i, RQCODE_INSERT);
                }
            });

            findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //로그인 버튼
                    Log.i("ming", Integer.toString(mCursor.getPosition()));

                    try {
                        String str_id = et_id.getText().toString();
                        int str_pw = Integer.parseInt(et_pw.getText().toString());

                        mCursor = mdb.rawQuery("SELECT * FROM login WHERE id = '" + str_id + "' AND pw = " + str_pw + ";", null);

                        if (mCursor.moveToNext()) {
                            Log.i("ming", "SUCCESS");
                            Intent success = new Intent(Login.this, FragmentActivity.class);
                            startActivity(success);
                        } else {
                            Log.i("ming", "FAILURE");
                        }
                    } catch (Exception e) {
                        ;
                    }
                }
            });
    }
}