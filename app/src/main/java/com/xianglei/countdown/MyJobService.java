package com.xianglei.countdown;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


/**
 * @author sunxianglei
 * @date 2018/1/21
 */

public class MyJobService extends JobService {

    private int kJobId = 0;
    private static JobScheduler tm;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v("MyJobService", "jobService启动");
        scheduleJob(getJobInfo());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.v("MyJobService", "执行了onStartJob方法");
        boolean isLocalServiceWork = Utils.isServiceWork(this, "com.xianglei.countdown.CountDownService");
        boolean isRemoteServiceWork = Utils.isServiceWork(this, "com.marswin89.marsdaemon.demo.Service2");
        if(!isLocalServiceWork||
                !isRemoteServiceWork){
            this.startService(new Intent(this,CountDownService.class));
//            this.startService(new Intent(this,Service2.class));
//            Toast.makeText(this, "进程启动", Toast.LENGTH_SHORT).show();
            Log.v("onStartJob", "启动CountDownService");
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.v("MyJobService", "执行了onStopJob方法");
        scheduleJob(getJobInfo());
        return true;
    }

    //将任务作业发送到作业调度中去
    public void scheduleJob(JobInfo t) {
        Log.v("MyJobService", "调度job");
        tm = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        tm.schedule(t);
    }

    public static void cancelJobs(){
        if(tm != null) {
            tm.cancelAll();
        }
    }

    public JobInfo getJobInfo(){
        JobInfo.Builder builder = new JobInfo.Builder(kJobId, new ComponentName(this, MyJobService.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED);
        builder.setPersisted(true);
        builder.setRequiresCharging(false);
        builder.setRequiresDeviceIdle(false);
        //间隔1000毫秒
        builder.setPeriodic(1000);
        return builder.build();
    }
}
