package com.xianglei.countdown;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author sunxianglei
 * @date 2018/1/15
 */

public class MyWindowManager {

    /**
     * 小悬浮窗View的实例 
     */
    private static CountDownView countDownView;

    /**
     * 大悬浮窗View的实例 
     */
//    private static FloatWindowBigView bigWindow;

    /**
     * 小悬浮窗View的参数 
     */
    private static LayoutParams countDownViewParams;

    /**
     * 大悬浮窗View的参数 
     */
    private static LayoutParams bigWindowParams;

    /**
     * 用于控制在屏幕上添加或移除悬浮窗 
     */
    private static WindowManager mWindowManager;

    /**
     * 用于获取手机可用内存 
     */
    private static ActivityManager mActivityManager;

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。 
     *
     * @param context
     *            必须为应用程序的Context. 
     */
    public static void createCountDownView(Context context) {
        WindowManager windowManager = getWindowManager(context);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (countDownView == null) {
            countDownView = new CountDownView(context);
            if (countDownViewParams == null) {
                countDownViewParams = new LayoutParams();
                countDownViewParams.type = LayoutParams.TYPE_PHONE;
                countDownViewParams.format = PixelFormat.RGBA_8888;
                countDownViewParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | LayoutParams.FLAG_NOT_FOCUSABLE;
                countDownViewParams.gravity = Gravity.LEFT | Gravity.TOP;
                countDownViewParams.width = CountDownView.viewWidth;
                countDownViewParams.height = CountDownView.viewHeight;
                countDownViewParams.x = screenWidth;
                countDownViewParams.y = screenHeight / 2;
            }
            countDownView.setParams(countDownViewParams);
            windowManager.addView(countDownView, countDownViewParams);
            updateUsedPercent(context);
        }
    }

    /**
     * 将小悬浮窗从屏幕上移除。 
     *
     * @param context
     *            必须为应用程序的Context. 
     */
    public static void removeCountDownView(Context context) {
        if (countDownView != null) {
            WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(countDownView);
            countDownView = null;
        }
    }

    /**
     * 创建一个大悬浮窗。位置为屏幕正中间。 
     *
     * @param context
     *            必须为应用程序的Context. 
     */
//    public static void createBigWindow(Context context) {
//        WindowManager windowManager = getWindowManager(context);
//        int screenWidth = windowManager.getDefaultDisplay().getWidth();
//        int screenHeight = windowManager.getDefaultDisplay().getHeight();
//        if (bigWindow == null) {
//            bigWindow = new FloatWindowBigView(context);
//            if (bigWindowParams == null) {
//                bigWindowParams = new LayoutParams();
//                bigWindowParams.x = screenWidth / 2 - FloatWindowBigView.viewWidth / 2;
//                bigWindowParams.y = screenHeight / 2 - FloatWindowBigView.viewHeight / 2;
//                bigWindowParams.type = LayoutParams.TYPE_PHONE;
//                bigWindowParams.format = PixelFormat.RGBA_8888;
//                bigWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
//                bigWindowParams.width = FloatWindowBigView.viewWidth;
//                bigWindowParams.height = FloatWindowBigView.viewHeight;
//            }
//            windowManager.addView(bigWindow, bigWindowParams);
//        }
//    }

    /**
     * 将大悬浮窗从屏幕上移除。 
     *
     * @param context
     *            必须为应用程序的Context. 
     */
//    public static void removeBigWindow(Context context) {
//        if (bigWindow != null) {
//            WindowManager windowManager = getWindowManager(context);
//            windowManager.removeView(bigWindow);
//            bigWindow = null;
//        }
//    }

    /**
     * 更新小悬浮窗的TextView上的数据
     *
     * @param context
     *            可传入应用程序上下文。 
     */
    public static void updateUsedPercent(Context context) {
        if (countDownView != null) {
            TextView percentView = (TextView) countDownView.findViewById(R.id.percent);
            String endDate = MainActivity.DATE;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            long diff = 0;
            try {
                Date date1 = new Date();
                Date date2 = format.parse(endDate);
                diff = (date2.getTime() - date1.getTime())/(24*3600*1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(diff < 0){
                percentView.setText("已超期");
            }else {
                percentView.setText(MainActivity.TITLE + "\n" + diff + "天");
            }
        }
    }

    /**
     * 是否有悬浮窗(包括小悬浮窗和大悬浮窗)显示在屏幕上。 
     *
     * @return 有悬浮窗显示在桌面上返回true，没有的话返回false。 
     */
    public static boolean isWindowShowing() {
        return countDownView != null/* || bigWindow != null*/;
    }

    /**
     * 如果WindowManager还未创建，则创建一个新的WindowManager返回。否则返回当前已创建的WindowManager。 
     *
     * @param context
     *            必须为应用程序的Context. 
     * @return WindowManager的实例，用于控制在屏幕上添加或移除悬浮窗。 
     */
    private static WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    /**
     * 如果ActivityManager还未创建，则创建一个新的ActivityManager返回。否则返回当前已创建的ActivityManager。 
     *
     * @param context
     *            可传入应用程序上下文。 
     * @return ActivityManager的实例，用于获取手机可用内存。 
     */
    private static ActivityManager getActivityManager(Context context) {
        if (mActivityManager == null) {
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        }
        return mActivityManager;
    }

    /**
     * 计算已使用内存的百分比，并返回。 
     *
     * @param context
     *            可传入应用程序上下文。 
     * @return 已使用内存的百分比，以字符串形式返回。 
     */
    public static String getUsedPercentValue(Context context) {
        String dir = "/proc/meminfo";
        try {
            FileReader fr = new FileReader(dir);
            BufferedReader br = new BufferedReader(fr, 2048);
            String memoryLine = br.readLine();
            String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
            br.close();
            long totalMemorySize = Integer.parseInt(subMemoryLine.replaceAll("\\D+", ""));
            long availableSize = getAvailableMemory(context) / 1024;
            int percent = (int) ((totalMemorySize - availableSize) / (float) totalMemorySize * 100);
            return percent + "%";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "悬浮窗";
    }

    /**
     * 获取当前可用内存，返回数据以字节为单位。 
     *
     * @param context
     *            可传入应用程序上下文。 
     * @return 当前可用内存。 
     */
    private static long getAvailableMemory(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        getActivityManager(context).getMemoryInfo(mi);
        return mi.availMem;
    }

}
