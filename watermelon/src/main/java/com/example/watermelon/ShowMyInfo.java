package com.example.watermelon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ShowMyInfo extends AppCompatActivity {
    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;

    EditText et_id, et_new_pw, et_new_pw_check, et_name;
    Button btn_setting_me;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_info);

        mydb = new MySQLHelper(ShowMyInfo.this, "login.db", null, 1);
        mdb = mydb.getWritableDatabase();

        et_id = findViewById(R.id.et_id_me);
        et_new_pw = findViewById(R.id.et_pw_me);
        et_new_pw_check = findViewById(R.id.et_pwOk_me);
        et_name = findViewById(R.id.et_name_me);

        btn_setting_me = findViewById(R.id.btn_setting_me);

        btn_setting_me.setOnClickListener(settingOk);

        i = getIntent();
        //로그인했던정보 -> 수정된 정보도 받아와야 하므로 커서를 _id(변하지않는값)로 설정
        int _id = i.getIntExtra("_id", -1);
        mCursor = mdb.rawQuery("SELECT * FROM login WHERE _id='" + _id + "';", null);
        mCursor.moveToNext();

        //후 에딧텍스트에 설정해야하는 값은 커서의 id값 0123;
        String str_id = mCursor.getString(1);
        int int_pw = mCursor.getInt(2);
        String str_name = mCursor.getString(3);

        et_id.setText(str_id);
        et_new_pw.setText(String.valueOf(int_pw));
        et_name.setText(str_name);
    }

    View.OnClickListener settingOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String str_id = et_id.getText().toString();
            String str_name = et_name.getText().toString();
            int int_pw = Integer.parseInt(et_new_pw.getText().toString());
            int int_pwOk = Integer.parseInt(et_new_pw_check.getText().toString());

            if (int_pw == int_pwOk) {
                Log.i("update", str_name);
                Log.i("update", str_id);
                Log.i("update", Integer.toString(int_pw));

                i.putExtra("id", str_id);
                i.putExtra("pw", int_pw);
                i.putExtra("name", str_name);
                setResult(RESULT_OK, i);
                finish();

            } else {
                Toast.makeText(ShowMyInfo.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };
}