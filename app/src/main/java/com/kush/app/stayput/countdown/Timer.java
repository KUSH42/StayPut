package com.kush.app.stayput.countdown;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.Consts;
import com.kush.app.stayput.MainActivity;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Counts down and shit
 * Singleton
 */

public class Timer extends Service {

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;
    private final TextView tView;
    private CountDownTimer timer;
    private long millisInFuture;
    private long millisSurplus;
    private long countDownInterval;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY; //START_NOT_STICKY: OS will recreate service when it is killed
    }

    protected Timer() {
        btnStart = MainActivity.getBtnStart();
        btnPause = MainActivity.getBtnPause();
        btnResume = MainActivity.getBtnResume();
        btnCancel = MainActivity.getBtnCancel();
        tView = MainActivity.getView();
        reset();
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
            public void onTick(long millisUntilFinished) {
                //Do something in every tick
                if (MainActivity.isPaused() || MainActivity.isCanceled()) {
                    //If user requested to pause or cancel the count down timer
                    cancel();
                } else {
                    tView.setText("" + (millisUntilFinished / (60 * 60 * 1000) % 24) + "h " + (millisUntilFinished / (60 * 1000) % 60) + "m " + (millisUntilFinished / 1000 % 60) + "s");
                    //Put count down timer remaining time in a variable
                    MainActivity.setTimeRemaining(millisUntilFinished);
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

