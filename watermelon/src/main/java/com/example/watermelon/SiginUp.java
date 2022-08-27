package com.example.watermelon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SiginUp extends AppCompatActivity {

    Button btn_setting_ok;
    EditText et_id, et_pw, et_pwOk, et_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);

        et_id = findViewById(R.id.et_id_me);
        et_pw = findViewById(R.id.et_pw_me);
        et_pwOk = findViewById(R.id.et_pwOk_me);
        et_name = findViewById(R.id.et_name_me);

        btn_setting_ok = findViewById(R.id.btn_setting_me);
        btn_setting_ok.setOnClickListener(settingOk);

        Intent i = getIntent();

        String str_id = i.getStringExtra("id");
        int int_pw = i.getIntExtra("pw", -2);
        String str_name = i.getStringExtra("name");
        if (str_id != null && int_pw != 0 && str_name != null &&
                str_id.length() > 0 && str_name.length() > 0 && int_pw > 0) {
            et_id.setText(str_id);
            et_pw.setText(String.valueOf(int_pw));
            et_pwOk.setText(String.valueOf(int_pw));
            et_name.setText(str_name);
        }
    }

    View.OnClickListener settingOk = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i= getIntent();
            String str_id = et_id.getText().toString();
            int str_pw = Integer.parseInt(et_pw.getText().  toString());
            int str_pwOk = Integer.parseInt(et_pwOk.getText().toString());
            String str_name = et_name.getText().toString();

            setResult(RESULT_OK, i);
            finish();
        }
    };

}