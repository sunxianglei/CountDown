package com.xianglei.countdown;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static String TITLE = " 倒计时";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText eventET = (EditText)findViewById(R.id.et_event);
        Button startBtn = (Button) findViewById(R.id.btn_start);
        Button stopBtn = (Button) findViewById(R.id.btn_stop);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                boolean isExist = Utils.isServiceWork(MainActivity.this, "com.xianglei.countdown.CountDownService");
                if(isExist){
                    Toast.makeText(MainActivity.this, "倒计时服务已存在！",Toast.LENGTH_SHORT).show();
                }else {
                    String title = eventET.getText().toString();
                    if (!TextUtils.isEmpty(title)) {
                        TITLE = title;
                    }
                    Intent intent = new Intent(MainActivity.this, MyJobService.class);
                    startService(intent);
                    finish();
                }
            }
        });
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MyJobService.cancelJobs();
                Intent intent = new Intent(MainActivity.this, MyJobService.class);
                Intent intent1 = new Intent(MainActivity.this, CountDownService.class);
                stopService(intent);
                stopService(intent1);
                finish();
            }
        });

    }
}
