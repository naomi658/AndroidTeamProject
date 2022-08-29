package com.example.watermelon;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// 업데이트
public class UpdateUser extends AppCompatActivity {
    SQLiteDatabase mdb;
    Cursor mCursor;
    MySQLHelper mydb;
    Button btn_me;
    SimpleCursorAdapter ca;
    Intent i;
    static final int RQCODE_UPDATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_login);

        mydb = new MySQLHelper(this, "login.db", null, 1);
        mdb = mydb.getReadableDatabase();
        i = getIntent();

        btn_me = findViewById(R.id.btn_me);

        btn_me.setOnClickListener(me);
    }

    View.OnClickListener me = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int _id = i.getIntExtra("_id", -1);
            String str_id = i.getStringExtra("id");
            int int_pw = i.getIntExtra("pw", -2);
            String str_name = i.getStringExtra("name");

            mCursor = mdb.rawQuery("SELECT * FROM login WHERE " +
                    "id = '" + str_id + "' AND pw = " + int_pw + " AND name = '"+str_name+"';", null);
            mCursor.moveToNext();

            Intent i = new Intent(UpdateUser.this, ShowMyInfo.class);
            i.putExtra("_id", _id);
            i.putExtra("id", str_id);
            i.putExtra("pw", int_pw);
            i.putExtra("name", str_name);

            startActivityForResult(i, RQCODE_UPDATE);
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            mCursor = mdb.rawQuery("SELECT * FROM login", null);

            mCursor.moveToNext();

            int _id = data.getIntExtra("_id", -1);
            String str_id = data.getStringExtra("id");
            int int_pw = data.getIntExtra("pw", -2);
            String str_name = data.getStringExtra("name");

            if (requestCode == RQCODE_UPDATE) {
                i = getIntent();

                mdb.execSQL("UPDATE login" +
                        " SET id = '" + str_id + "'" + ", pw = " + int_pw + ", " + "name= '" + str_name +
                        "' WHERE _id =" + _id + "; ");

                mCursor.requery();

            }
            mCursor.requery();
        }
    }

}