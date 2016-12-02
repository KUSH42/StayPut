package com.example.kush.stayput.listeners;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.kush.stayput.MainActivity;
import com.example.kush.stayput.countdown.TimerCountdown;

/**
 * Created by Kush on 02.12.2016.
 */

public class ResumeButtonListener implements View.OnClickListener {

    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    private TextView tView;

    public ResumeButtonListener (Button btnStart, Button btnPause, Button btnResume, Button btnCancel, TextView tView) {
        this.btnStart = btnStart;
        this.btnPause = btnPause;
        this.btnResume = btnResume;
        this.btnCancel = btnCancel;
        this.tView = tView;
    }

    @Override
    public void onClick (View v){
        //Disable the start and resume button
        btnStart.setEnabled(false);
        btnResume.setEnabled(false);
        //Enable the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);
        //Specify the current state is not paused and canceled.
        MainActivity.setPaused(false);
        MainActivity.setCanceled(false);
        //Initialize a new CountDownTimer instance
        new TimerCountdown(btnStart, btnPause, btnResume, btnCancel, tView);

        //Set a Click Listener for cancel/stop button
        btnCancel.setOnClickListener(new CancelButtonListener(btnStart, btnPause, btnResume, btnCancel, tView));
    }
}
