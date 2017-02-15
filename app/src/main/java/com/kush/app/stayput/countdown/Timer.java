package com.kush.app.stayput.countdown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;

import com.kush.app.stayput.R;

import static android.text.Html.fromHtml;
import static com.kush.app.stayput.Consts.CONTEXT_COLOR_HTML_END;
import static com.kush.app.stayput.Consts.CONTEXT_COLOR_HTML_GREEN_START;
import static com.kush.app.stayput.Consts.CONTEXT_COLOR_HTML_RED_START;
import static com.kush.app.stayput.Consts.NOTIFICATION_OVERTIME_STRING;
import static com.kush.app.stayput.Consts.NOTIFICATION_WORKTIME_STRING;
import static com.kush.app.stayput.Consts.OVERTIME_MAX;
import static com.kush.app.stayput.Consts.WORKTIME_MAX;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Counts down and shit
 */

public class Timer extends Service {

    //Service notification ID
    private static final int NOTIFICATION_ID = 1;
    //Constant that defines for how long the phone will vibrate when the timers finish
    private static final int VIBRATE_DURATION = 1500;
    //Static flag to indicate if service is running
    public static boolean activeService = false;
    //Declare a variable to hold the countUp flag
    private static boolean countUp = false;
    //Declare a variable to hold count down timer's paused status
    private static boolean isPaused = false;
    //Declare a variable to hold count down timer's canceled status
    private static boolean isCanceled = false;
    //Declares a flag to set if CountUp has finished
    private static boolean isFinished = false;
    //Declare a variable to hold CountDownTimer remaining time
    private static long timeRemaining = WORKTIME_MAX;
    //Interval for the CountdownTimers has to be <1000 to make sure everything updates correctly
    private static final long countDownInterval = 500;
    private long millisInFuture;
    private long millisSurplus;
    private final IBinder mBinder = new LocalBinder();
    private static String contextColorHtmlStart;
    private static NotificationManager mNotificationManager;
    private static Intent notificationIntent;
    private static PendingIntent pendingIntent;
    private static Notification.Builder statusBarNBuilder;
    private static Notification notification;
    private CountDownTimer timer;

    public Timer() {
    }

    public static void setCountUp(boolean countUp) {
        Timer.countUp = countUp;
    }
    public static boolean isCountUp() {
        return countUp;
    }
    public static void setPaused(boolean paused) {
        isPaused = paused;
    }
    public static void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }
    public static void setTimeRemaining(long newTimeRemaining) {
        timeRemaining = newTimeRemaining;
    }
    public static long getTimeRemaining() {return timeRemaining;}
    public static void setIsFinished(boolean val) {isFinished = val;}
    public static boolean hasFinished() {return isFinished;}
    public static boolean isCanceled() {return isCanceled;}


    @Override
    public IBinder onBind(Intent arg0) {
        activeService = true;
        init();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        activeService = true;
        init();
        return START_NOT_STICKY; //START_NOT_STICKY: OS will not recreate service when it is killed
    }

    private void buildNotification() {
        //runs service in foreground
        long millisUntilFinished = timeRemaining;
        String notificationMsg;
        if (countUp) {
            notificationMsg = NOTIFICATION_OVERTIME_STRING;
            contextColorHtmlStart = CONTEXT_COLOR_HTML_GREEN_START;
        } else {
            notificationMsg = NOTIFICATION_WORKTIME_STRING;
            contextColorHtmlStart = CONTEXT_COLOR_HTML_RED_START;
        }
        //noinspection deprecation
        mNotificationManager.cancelAll();
        statusBarNBuilder.setContentText(fromHtml(notificationMsg + contextColorHtmlStart + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END));
        notification = statusBarNBuilder.build();
        startForeground(NOTIFICATION_ID, notification);
    }

    //Method for initializing the service
    private void init() {
        if (timer != null) {
            timer.cancel();
        }
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        notificationIntent = new Intent(getApplicationContext(), com.kush.app.stayput.MainActivity.class);
        pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (statusBarNBuilder == null) {
            statusBarNBuilder = new Notification.Builder(this);
            statusBarNBuilder.setContentTitle(getText(R.string.notification_title))
                    .setSmallIcon(R.drawable.ic_stat_name)
                    .setOngoing(true)
                    .setContentIntent(pendingIntent);
        }
        buildNotification();
        if (countUp) {
            millisSurplus = timeRemaining;
            timer = newUpTimer();
        } else {
            millisInFuture = timeRemaining;
            timer = newDownTimer();
        }
    }

    //Method for initializing an appropriate CountDownTimer
    private CountDownTimer newDownTimer() {
        mNotificationManager.cancelAll();
        contextColorHtmlStart = CONTEXT_COLOR_HTML_RED_START;
        return new CountDownTimer(millisInFuture, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                //Do something in every tick
                if (isPaused || isCanceled) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Update notification
                    //noinspection deprecation
                    statusBarNBuilder.setContentText(fromHtml(NOTIFICATION_WORKTIME_STRING + contextColorHtmlStart + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END));
                    notification = statusBarNBuilder.build();
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE); //haha he said vibrator!
                v.vibrate(VIBRATE_DURATION);
                //Starts Countup
                timeRemaining = OVERTIME_MAX;
                millisSurplus = OVERTIME_MAX;
                countUp = true;
                timer = newUpTimer();
            }
        }.start();
    }

    //Method for initializing an appropriate CountUpTimer
    private CountDownTimer newUpTimer() {
        mNotificationManager.cancelAll();
        countUp = true;
        contextColorHtmlStart = CONTEXT_COLOR_HTML_GREEN_START;
        return new CountDownTimer(millisSurplus, countDownInterval) {
            public void onTick(long millisUntilFinished) {
                long millisGained;
                //Do something in every tick
                if (isPaused || isCanceled) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    millisGained = OVERTIME_MAX - millisUntilFinished;
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Update notification
                    //noinspection deprecation
                    statusBarNBuilder.setContentText(fromHtml(NOTIFICATION_OVERTIME_STRING + contextColorHtmlStart + (millisGained / (60 * 60 * 1000) % 24) + "h " + (millisGained / (60 * 1000) % 60) + "m " + (millisGained / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END));
                    notification = statusBarNBuilder.build();
                    mNotificationManager.notify(NOTIFICATION_ID, notification);
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VIBRATE_DURATION);
                Timer.isFinished = true;
                timeRemaining = 0;
            }
        }.start();
    }

    //Class for client binder
    public class LocalBinder extends Binder {
        public Timer getService() {
            return Timer.this;
        }
    }
}

