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
import android.support.v4.app.NotificationCompat;

import com.kush.app.stayput.Consts;

import net.example.kush.stayput.R;

import static android.text.Html.fromHtml;
import static com.kush.app.stayput.Consts.NOTIFICATION_OVERTIME_STRING;
import static com.kush.app.stayput.Consts.NOTIFICATION_WORKTIME_STRING;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Counts down and shit
 */

public class Timer extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CONTEXT_COLOR_HTML_RED_START = "<font color='red'>";
    private static final String CONTEXT_COLOR_HTML_GREEN_START = "<font color='green'>";
    private static final String CONTEXT_COLOR_HTML_END = "</font>";
    //static flag to indicate if service is running
    public static boolean activeService = false;
    @SuppressWarnings("FieldCanBeLocal")
    private static String NOTIFICATION_MSG;
    //Declare a variable to hold the countUp flag
    private static boolean countUp = false;
    //Declare a variable to hold count down timer's paused status
    private static boolean isPaused = false;
    //Declare a variable to hold count down timer's canceled status
    private static boolean isCanceled = false;
    //Declares a flag to set if CountUp has finished
    private static boolean isFinished = false;
    //Declare a variable to hold CountDownTimer remaining time
    private static long timeRemaining = Consts.WORKTIME_MAX;
    private final IBinder mBinder = new LocalBinder();
    private CountDownTimer timer;
    private long millisInFuture;
    private long millisSurplus;
    private long countDownInterval;

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
        buildNotification();
        reset();
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        activeService = true;
        buildNotification();
        reset();
        return START_NOT_STICKY; //START_NOT_STICKY: OS will not recreate service when it is killed
    }

    private void buildNotification() {
        //runs service in foreground
        Intent notificationIntent = new Intent(this, com.kush.app.stayput.MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long millisUntilFinished = timeRemaining;
        String CONTEXT_COLOR_HTML_START;
        if (countUp) {
            NOTIFICATION_MSG = NOTIFICATION_OVERTIME_STRING;
            CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_GREEN_START;
        } else {
            NOTIFICATION_MSG = NOTIFICATION_WORKTIME_STRING;
            CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_RED_START;
        }
        //noinspection deprecation
        Notification notification = new Notification.Builder(this)
                .setContentTitle(getText(R.string.notification_title))
                .setContentText(fromHtml(NOTIFICATION_MSG + CONTEXT_COLOR_HTML_START + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END))
                .setSmallIcon(R.drawable.ic_stat_name)
                .setOngoing(true)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    //Method for resetting/initializing the Timer
    private void reset() {
        countDownInterval = 500;    //interval has to be <1000 to make sure everything updates correctly
        if (timer != null) {
            timer.cancel();
        }
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
        final NotificationManager mNotificationManager =
                 (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        final Intent notificationIntent = new Intent(getApplicationContext(), com.kush.app.stayput.MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return new CountDownTimer(millisInFuture, countDownInterval) {
            final String CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_RED_START;

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
                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getText(R.string.notification_title))
                            .setContentIntent(pendingIntent)
                            .setContentText(fromHtml(NOTIFICATION_WORKTIME_STRING + CONTEXT_COLOR_HTML_START + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END))
                            .setSmallIcon(R.drawable.ic_stat_name);
                    mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE); //haha he said vibrator!
                v.vibrate(1500);
                //Starts Countup
                timeRemaining = Consts.OVERTIME_MAX;
                millisSurplus = Consts.OVERTIME_MAX;
                countUp = true;
                timer = newUpTimer();
            }
        }.start();
    }

    //Method for initializing an appropriate CountUpTimer
    private CountDownTimer newUpTimer() {
        countUp = true;
        return new CountDownTimer(millisSurplus, countDownInterval) {
            final String CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_GREEN_START;
            final NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            final Intent notificationIntent = new Intent(getApplicationContext(), com.kush.app.stayput.MainActivity.class);
            final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            public void onTick(long millisUntilFinished) {
                long millisGained;
                //Do something in every tick
                if (isPaused || isCanceled) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    millisGained = Consts.OVERTIME_MAX - millisUntilFinished;
                    //Put count down timer remaining time in a variable
                    timeRemaining = millisUntilFinished;
                    //Update notification
                    //noinspection deprecation
                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(getApplicationContext())
                            .setContentTitle(getText(R.string.notification_title))
                            .setContentIntent(pendingIntent)
                            .setContentText(fromHtml(NOTIFICATION_OVERTIME_STRING + CONTEXT_COLOR_HTML_START + (millisGained / (60 * 60 * 1000) % 24) + "h " + (millisGained / (60 * 1000) % 60) + "m " + (millisGained / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END))
                            .setSmallIcon(R.drawable.ic_stat_name);
                    mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1500);
                Timer.isFinished = true;
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

