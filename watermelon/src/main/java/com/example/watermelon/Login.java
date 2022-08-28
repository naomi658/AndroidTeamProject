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

// 로그인
public class Login extends AppCompatActivity {
    Button btn_signUp, btn_logIn;
    EditText et_id, et_pw;

    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;

    static final int RQCODE_INSERT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);

        mydb = new MySQLHelper(this, "login.db", null, 1);
        mdb = mydb.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM login", null);

        btn_signUp = findViewById(R.id.btn_sign_in_up);
        btn_logIn = findViewById(R.id.btn_log_in);

        et_id = findViewById(R.id.log_et_id);
        et_pw = findViewById(R.id.log_et_pw);

        btn_signUp.setOnClickListener(lisSignIn);
        btn_logIn.setOnClickListener(lisLogIn);

        Log.i("ming", "LogIn Activity is Running");
    }

    //회원가입 버튼 리스너
    View.OnClickListener lisSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(Login.this, SignInUp.class);
//                    startActivityForResult(i, RQCODE_INSERT);
            startActivity(i);
        }
    };
    // 로그인 버튼 리스너
    View.OnClickListener lisLogIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
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
    };
}