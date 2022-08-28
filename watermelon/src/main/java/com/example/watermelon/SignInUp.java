package com.example.watermelon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

// 회원가입
public class SignInUp extends AppCompatActivity {
    Button btn_setting_ok;
    EditText et_id, et_pw, et_pwOk, et_name;

    MySQLHelper mydb;
    SQLiteDatabase mdb;
    Cursor mCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);

        et_id = findViewById(R.id.sign_et_id);
        et_pw = findViewById(R.id.sign_et_pw);
        et_pwOk = findViewById(R.id.sign_et_pw_ok);
        et_name = findViewById(R.id.sign_et_name);
        btn_setting_ok = findViewById(R.id.btn_success);

        mydb = new MySQLHelper(this, "login.db", null, 1);
        mdb = mydb.getWritableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM login", null);

        btn_setting_ok.setOnClickListener(success);
        et_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_name.getWindowToken(), 0); // 키보드 내리기
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    View.OnClickListener success = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            try {
                String str_id = et_id.getText().toString();
                int str_pw = Integer.valueOf(et_pw.getText().toString());
                int str_pw_sign = Integer.valueOf(et_pwOk.getText().toString());
                String str_name = et_name.getText().toString();

                //pw 같은지 확인
                if (str_pw == str_pw_sign) {
                    mdb.execSQL("INSERT INTO login VALUES (null, '" + str_id + "', "
                            + str_pw + ", '" + str_name + "');");

                    finish();
                } else {
                    Toast.makeText(SignInUp.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                ;
            }
        }
    };
}