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
 */

public class TimerCountup {

    public TimerCountup (final Button btnStart, final Button btnPause, final Button btnResume, final Button btnCancel, final TextView tView) {

        long millisSurplus = MainActivity.getTimeRemaining();
        long countDownInterval = 1000;

        tView.setTextColor(Consts.TIMER_COLOR_GREEN);

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
                Vibrator v = (Vibrator) MainActivity.getContext().getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(3000);
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
