package com.example.sd_io;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQCODE_PERMISSION_WRITE_EXTERNAL = 1;
    EditText et_input;
    TextView tv_output;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission();

        tv_output = findViewById(R.id.tv_data);
        et_input = findViewById(R.id.et_input);
    }

    // write
    public void onClickSave(View view){
        FileOutputStream fos = null;
        String str = et_input.getText().toString();

        try {
            File sdPath = getExternalFilesDir(null);
            fos = new FileOutputStream(sdPath.getAbsolutePath() + "/dataFile.txt");
            fos.write(str.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        setVisiblityViews(View.INVISIBLE, View.INVISIBLE);
        Toast.makeText(this, "SAVED", Toast.LENGTH_SHORT).show();
    }
    //read
    public void onClickRead(View view){
        FileInputStream fis = null;
        int len = 0;
        int result = 0;

        try {
            File sdPath = getExternalFilesDir(null);
            fis = new FileInputStream(sdPath.getAbsolutePath() + "/dataFile.txt");
            len = fis.available();

            if(len > 0){
                byte buf[] = new byte[len];
                while(result > -1){
                    result = fis.read(buf);
                }
                fis.close();
                tv_output.setText(new String(buf));
            }

            setVisiblityViews(View.VISIBLE, View.INVISIBLE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void onClickInput(View view){
        setVisiblityViews(View.INVISIBLE, View.VISIBLE);
    }

    void setVisiblityViews(int tv_state, int et_state){
        tv_output.setVisibility(tv_state);
        et_input.setVisibility(et_state);
    }

    //사용권한을 확인하고 필요 시 요청
    void checkPermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 권한 승인을 거절한 이력을 확인한다.
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // 사용자가 권한 승인에 거절을 누른 경우
                Toast.makeText(this, "권한이 없습니다", Toast.LENGTH_SHORT).show();

            } else {
                // 최초 접속 시 권한이 없는 경우 사용권한을 요청함
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQCODE_PERMISSION_WRITE_EXTERNAL);
            }
        } else {
            //사용권한을 승인 받았으므로 파일읽기 가능
            Toast.makeText(this, "SD 카드 쓰기 가능", Toast.LENGTH_SHORT).show();
//            readExternalMusicFiles();
        }
    }

    //요청한 사용권한에 관한 결과를 전달받음
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQCODE_PERMISSION_WRITE_EXTERNAL) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SD 카드 쓰기 가능", Toast.LENGTH_SHORT).show();
            }
        }
    }
}