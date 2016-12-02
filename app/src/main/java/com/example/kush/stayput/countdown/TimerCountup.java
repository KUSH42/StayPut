package com.example.kush.stayput.countdown;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.example.kush.stayput.Consts;
import com.example.kush.stayput.MainActivity;

/**
 * Created by Kush on 02.12.2016.
 */

public class TimerCountup {

    public TimerCountup (final Button btnStart, final Button btnPause, final Button btnResume, final Button btnCancel, final TextView tView) {

        long millisSurplus = MainActivity.getTimeRemaining();
        long countDownInterval = 1000;

        tView.setTextColor(Consts.TIMER_GREEN);

        new CountDownTimer(millisSurplus, countDownInterval) {
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
                tView.setText("Done");
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
