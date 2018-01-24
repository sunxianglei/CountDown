package com.xianglei.countdown;

import android.app.DatePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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

    private JobScheduler jobScheduler;
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
                    jobSchedule();
                    finish();
                }
                break;
            case R.id.btn_stop:
                TITLE = " 倒计时";
                DATE = "2018-02-20";
                cancelJob();
                Process.killProcess(Process.myPid());
                break;
            default:
                break;
        }
    }

    public void jobSchedule(){
        Log.v("MyJobService", "调度job");
        jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(this, MyJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPersisted(true);
//        builder.setRequiresCharging(false);
//        builder.setRequiresDeviceIdle(false);
        //间隔1000毫秒
        builder.setPeriodic(60 * 1000);
        jobScheduler.schedule(builder.build());
    }

    private void cancelJob(){
        jobScheduler.cancel(1);
    }
}
