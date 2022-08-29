package com.example.watermelon;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

// 로그인
public class Login extends AppCompatActivity {
    Button btn_signUp, btn_logIn;
    EditText et_id, et_pw;

    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;

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

        et_pw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et_pw.getWindowToken(), 0); // 키보드 내리기
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

    //회원가입 버튼 리스너
    View.OnClickListener lisSignIn = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                Intent i = new Intent(Login.this, SignInUp.class);
                startActivity(i);
            } catch (Exception e){
                ;
            }
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
                    //로그인 정보 담기
                    Log.i("ming", "SUCCESS");
                    int _id = mCursor.getInt(0);
                    String str_id1 = mCursor.getString(1);
                    int int_pw1 = mCursor.getInt(2);
                    String str_name = mCursor.getString(3);
                    Intent success = new Intent(Login.this, FragmentActivity.class);
                    success.putExtra("_id", _id);
                    success.putExtra("id", str_id1);
                    success.putExtra("pw", int_pw1);
                    success.putExtra("name",str_name);
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