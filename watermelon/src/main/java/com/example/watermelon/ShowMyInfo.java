package com.example.watermelon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import com.example.watermelon.FragmentActivity;
import com.example.watermelon.MySQLHelper;
import com.example.watermelon.R;

public class ShowMyInfo extends AppCompatActivity {
    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;

    EditText et_new_pw, et_new_pw_check;
    Button btn_setting_me;
    static final int RQCODE_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_info);

        mydb = new MySQLHelper(ShowMyInfo.this, "login.db", null, 1);
        mdb = mydb.getReadableDatabase();
        mCursor = mdb.rawQuery("SELECT * FROM login", null);

        Intent intent = getIntent();

        btn_setting_me = findViewById(R.id.btn_setting_me);
        et_new_pw = findViewById(R.id.et_pw_me);
        et_new_pw_check = findViewById(R.id.et_pwOk_me);

        btn_setting_me.setOnClickListener(me);
    }

    View.OnClickListener me = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCursor.moveToFirst();
            int _id = mCursor.getInt(0);
            String str_id = mCursor.getString(1);

            new MySQLHelper(ShowMyInfo.this, "login.db", null, 1).getLogInTableData();
            Log.i("log", MySQLHelper.logList.get(0).getUserName());


            int int_pw = Integer.parseInt(et_new_pw.getText().toString());
            int int_pw_check = Integer.parseInt(et_new_pw_check.getText().toString());
            String str_name = mCursor.getString(3);

            Intent i = new Intent(ShowMyInfo.this.getApplicationContext(), FragmentActivity.class);
            i.putExtra("_id", _id);
//            i.putExtra("id", str_id);

            if (int_pw == int_pw_check) {
                i.putExtra("pw", Integer.toString(int_pw));
            }

            i.putExtra("name", str_name);
            startActivityForResult(i, RQCODE_UPDATE);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {

            if (requestCode == RQCODE_UPDATE) {

                int _id = data.getIntExtra("_id", -1);
                String str_id = data.getStringExtra("id");
                int int_pw = data.getIntExtra("pw", -2);
                String str_name = data.getStringExtra("name");

                mdb.execSQL("UPDATE login SET id = '" + str_id + "', pw = '" + int_pw + "', " +
                        "name= '" + str_name + "' WHERE _id =" + _id + "; ");
            }
        }
        mCursor.requery();
    }
}