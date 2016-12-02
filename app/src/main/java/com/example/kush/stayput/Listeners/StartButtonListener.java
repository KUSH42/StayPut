package com.example.kush.stayput.listeners;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kush.stayput.MainActivity;
import com.example.kush.stayput.countdown.TimerCountdown;
import com.example.kush.stayput.Consts;

/**
 * Created by Kush on 26.11.2016.
 */

public class StartButtonListener implements View.OnClickListener{

    private TextView tView;
    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;

    public StartButtonListener (Button btnStart, Button btnPause, Button btnResume, Button btnCancel, TextView tView) {
        this.tView = tView;
        this.btnStart = btnStart;
        this.btnPause = btnPause;
        this.btnResume = btnResume;
        this.btnCancel = btnCancel;
    }

    @Override
    public void onClick(View v) {

        MainActivity.setPaused(false);
        MainActivity.setCanceled(false);
        MainActivity.setCountUp(false);
        MainActivity.setTimeRemaining(Consts.WORKTIME_MAX);

        //Disable the start and pause button
        btnStart.setEnabled(false);
        btnResume.setEnabled(false);
        //Enabled the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);

        //Initialize a new CountDownTimer instance
        new TimerCountdown(btnStart, btnPause, btnResume, btnCancel, tView);
    }
}
