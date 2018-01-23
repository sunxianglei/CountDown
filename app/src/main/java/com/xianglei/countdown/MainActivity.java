package com.xianglei.countdown;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static String TITLE = " 倒计时";
    public static String DATE = "2018-02-20";
    private EditText eventET;
    private TextView timeTV;
    private Button startBtn;
    private Button stopBtn;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventET = (EditText)findViewById(R.id.et_event);
        timeTV = (TextView) findViewById(R.id.tv_time);
        startBtn = (Button) findViewById(R.id.btn_start);
        stopBtn = (Button) findViewById(R.id.btn_stop);
        timeTV.setOnClickListener(this);
        startBtn.setOnClickListener(this);
        stopBtn.setOnClickListener(this);

        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day =cal.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_time:
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = year + "-" + (++month) + "-" + dayOfMonth;
                        timeTV.setText(date);
                        DATE = date;
                    }
                };
                DatePickerDialog dialog = new DatePickerDialog(MainActivity.this, listener, year, month, day);
                dialog.show();
                break;
            case R.id.btn_start:
                boolean isExist = Utils.isServiceWork(MainActivity.this, "com.xianglei.countdown.CountDownService");
                if(isExist){
                    Toast.makeText(MainActivity.this, "倒计时服务已存在！",Toast.LENGTH_SHORT).show();
                }else {
                    String title = eventET.getText().toString();
                    if (!TextUtils.isEmpty(title)) {
                        TITLE = title;
                    }else{
                        TITLE = "倒计时";
                    }
                    Intent intent = new Intent(MainActivity.this, MyJobService.class);
                    startService(intent);
                    finish();
                }
                break;
            case R.id.btn_stop:
//                MyJobService.cancelJobs();
                Intent intent = new Intent(MainActivity.this, MyJobService.class);
                Intent intent1 = new Intent(MainActivity.this, CountDownService.class);
                stopService(intent);
                stopService(intent1);
                finish();
                TITLE = " 倒计时";
                DATE = "2018-02-20";
                break;
            default:
                break;
        }
    }
}
