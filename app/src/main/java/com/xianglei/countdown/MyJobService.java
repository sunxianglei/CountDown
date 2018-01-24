package com.xianglei.countdown;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;


/**
 * @author sunxianglei
 * @date 2018/1/21
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MyJobService extends JobService {

    private int kJobId = 0;
    private Handler mJobHandler = new Handler( new Handler.Callback() {

        @Override
        public boolean handleMessage( Message msg ) {
            boolean isServiceWork = Utils.isServiceWork(MyJobService.this, "com.xianglei.countdown.CountDownService");
            if(!isServiceWork){
                MyJobService.this.startService(new Intent(MyJobService.this,CountDownService.class));
                Log.v("MyJobService", "启动CountDownService");
            }
            jobFinished( (JobParameters) msg.obj, true );
            return true;
        }

    } );

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.v("MyJobService", "执行了onStartJob方法");
        Message m = Message.obtain();
        m.obj = params;
        mJobHandler.sendMessage(m);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.v("MyJobService", "执行了onStopJob方法");
        boolean isServiceWork = Utils.isServiceWork(MyJobService.this, "com.xianglei.countdown.CountDownService");
        if(isServiceWork){
            MyJobService.this.stopService(new Intent(MyJobService.this,CountDownService.class));
        }
        mJobHandler.removeCallbacksAndMessages(1);
        return false;
    }

}
