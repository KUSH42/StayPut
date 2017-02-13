package com.kush.app.stayput.countdown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.Consts;
import com.kush.app.stayput.MainActivity;

import net.example.kush.stayput.R;

import static android.text.Html.fromHtml;
import static com.kush.app.stayput.Consts.NOTIFICATION_OVERTIME_STRING;
import static com.kush.app.stayput.Consts.NOTIFICATION_WORKTIME_STRING;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Counts down and shit
 * Singleton
 */

public class Timer extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CONTEXT_COLOR_HTML_RED_START = "<font color='red'>";
    private static final String CONTEXT_COLOR_HTML_GREEN_START = "<font color='green'>";
    private static final String CONTEXT_COLOR_HTML_END = "</font>";
    @SuppressWarnings("FieldCanBeLocal")
    private static String NOTIFICATION_MSG;
    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;
    private final TextView tView;
    private CountDownTimer timer;
    private long millisInFuture;
    private long millisSurplus;
    private long countDownInterval;

    public Timer() {
        btnStart = MainActivity.getBtnStart();
        btnPause = MainActivity.getBtnPause();
        btnResume = MainActivity.getBtnResume();
        btnCancel = MainActivity.getBtnCancel();
        tView = MainActivity.getView();
        reset();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //runs service in foreground
        Intent notificationIntent = new Intent(this, Timer.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        long millisUntilFinished = MainActivity.getTimeRemaining();
        String CONTEXT_COLOR_HTML_START;
        if (MainActivity.isCountUp()) {
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
                .setContentIntent(pendingIntent)
                .build();

        startForeground(NOTIFICATION_ID, notification);

        return START_NOT_STICKY; //START_NOT_STICKY: OS will not recreate service when it is killed
    }

    //Method for resetting/initializing the Timer
    private void reset() {

        countDownInterval = 100;    //interval has to be <1000 to make sure everything updates correctly
        if (timer != null) {
            timer.cancel();
        }
        if (MainActivity.isCountUp()) {
            millisSurplus = MainActivity.getTimeRemaining();
            tView.setTextColor(Consts.TIMER_COLOR_GREEN);
            timer = newUpTimer();
        } else {
            millisInFuture = MainActivity.getTimeRemaining();
            tView.setTextColor(Consts.TIMER_COLOR_RED);
            timer = newDownTimer();
        }
    }

    //Method for initializing an appropriate CountDownTimer
    private CountDownTimer newDownTimer() {
        return new CountDownTimer(millisInFuture, countDownInterval) {
            final String CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_RED_START;

            public void onTick(long millisUntilFinished) {
                //Do something in every tick
                if (MainActivity.isPaused() || MainActivity.isCanceled()) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    tView.setText("" + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s");
                    //Put count down timer remaining time in a variable
                    MainActivity.setTimeRemaining(millisUntilFinished);
                    //Update notification
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //noinspection deprecation
                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(MainActivity.getContext())
                            .setContentTitle(getText(R.string.notification_title))
                            .setContentText(fromHtml(NOTIFICATION_WORKTIME_STRING + CONTEXT_COLOR_HTML_START + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END))
                            .setSmallIcon(R.drawable.ic_stat_name);

                    mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) MainActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE); //haha he said vibrator!
                v.vibrate(1500);
                //Starts Countup
                MainActivity.setTimeRemaining(Consts.OVERTIME_MAX);
                millisSurplus = Consts.OVERTIME_MAX;
                MainActivity.setCountUp(true);
                tView.setTextColor(Consts.TIMER_COLOR_GREEN);
                timer = newUpTimer();
            }
        }.start();
    }

    //Method for initializing an appropriate CountUpTimer
    private CountDownTimer newUpTimer() {
        return new CountDownTimer(millisSurplus, countDownInterval) {
            final String CONTEXT_COLOR_HTML_START = CONTEXT_COLOR_HTML_GREEN_START;

            public void onTick(long millisUntilFinished) {
                long millisGained;
                //Do something in every tick
                if (MainActivity.isPaused() || MainActivity.isCanceled()) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    millisGained = Consts.OVERTIME_MAX - millisUntilFinished;
                    tView.setText("" + (millisGained / (60 * 60 * 1000) % 24) + "h " + (millisGained / (60 * 1000) % 60) + "m " + (millisGained / 1000 % 60) + "s");
                    //Put count down timer remaining time in a variable
                    MainActivity.setTimeRemaining(millisUntilFinished);
                    //Update notification
                    NotificationManager mNotificationManager =
                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    //noinspection deprecation
                    NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(MainActivity.getContext())
                            .setContentTitle(getText(R.string.notification_title))
                            .setContentText(fromHtml(NOTIFICATION_OVERTIME_STRING + CONTEXT_COLOR_HTML_START + (millisGained / (60 * 60 * 1000) % 24) + "h " + (millisGained / (60 * 1000) % 60) + "m " + (millisGained / 1000 % 60) + "s" + CONTEXT_COLOR_HTML_END))
                            .setSmallIcon(R.drawable.ic_stat_name);
                    mNotificationManager.notify(NOTIFICATION_ID, mNotifyBuilder.build());
                }
            }

            public void onFinish() {
                //Do something when count down finished
                Vibrator v = (Vibrator) MainActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(1500);
                //Disables the pause, resume and start buttons
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnStart.setEnabled(false);
                //Enable the cancel button
                btnCancel.setEnabled(true);
            }
        }.start();
    }
}

