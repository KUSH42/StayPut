package com.kush.app.stayput.countdown;

import android.content.Context;
import android.os.Vibrator;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.kush.app.stayput.Consts;
import com.kush.app.stayput.MainActivity;

/**
 * Created by Kush on 02.12.2016.
 *
 * Counts down and shit
 * Singleton
 */

public class TimerCountdown {

    private final TextView tView;
    private CountDownTimer timer;
    private long millisInFuture;
    private long countDownInterval;


    private static TimerCountdown instance;

    public static TimerCountdown getInstance() {
        if (instance == null) {
            instance = new TimerCountdown();
            return instance;
        } else {
            return instance;
        }
    }

    private TimerCountdown() {
        tView = MainActivity.getView();
        reset();
    }

    //Method for resetting/initializing the Countdown
    public void reset() {
        millisInFuture = MainActivity.getTimeRemaining();
        countDownInterval = 1000;
        tView.setTextColor(Consts.TIMER_COLOR_RED);
        if (timer != null) { timer.cancel();}
        timer = newTimer();
    }

    //Method for initializing an appropriate CountDownTimer
    private CountDownTimer newTimer() {
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
                MainActivity.setCountUp(true);
                Vibrator v = (Vibrator) MainActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(3000);
                MainActivity.setTimeRemaining(Consts.OVERTIME_MAX);
                TimerCountup.getInstance();
            }
        }.start();
    }
}

