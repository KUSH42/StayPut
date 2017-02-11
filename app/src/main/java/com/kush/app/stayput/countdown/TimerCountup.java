package com.kush.app.stayput.countdown;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.Consts;
import com.kush.app.stayput.MainActivity;

/**
 * Created by Kush on 02.12.2016.
 *
 * Counts up and shit
 * Singleton
 *
 */

public class TimerCountup {

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;
    private final TextView tView;
    private CountDownTimer timer;
    private long millisSurplus;
    private long countDownInterval;

    private static TimerCountup instance;

    public static TimerCountup getInstance() {
        if (instance == null) {
            instance = new TimerCountup();
            return instance;
        }
        else {
            return instance;
        }
    }

    private TimerCountup () {
        btnStart = MainActivity.getBtnStart();
        btnPause = MainActivity.getBtnPause();
        btnResume = MainActivity.getBtnResume();
        btnCancel = MainActivity.getBtnCancel();
        tView = MainActivity.getView();
        reset();
    }

    //Method for resetting/initializing the Countup
    public void reset() {
        millisSurplus = MainActivity.getTimeRemaining();
        countDownInterval = 1000;
        tView.setTextColor(Consts.TIMER_COLOR_GREEN);
        if (timer != null) { timer.cancel();}
        timer = newTimer();
    }

    //Method for initializing an appropriate CountDownTimer
    private CountDownTimer newTimer() {
        return         new CountDownTimer(millisSurplus, countDownInterval) {
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
                //Disable the pause, resume and cancel button
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnCancel.setEnabled(false);
                //Enable the start button
                btnStart.setEnabled(true);
            }
        }.start();
    }
}
