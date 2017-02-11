package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.TimerCountdown;
import com.kush.app.stayput.countdown.TimerCountup;

/**
 * Created by Kush on 02.12.2016.
 *
 * Listener for the resume button on main activity
 */

public class ResumeButtonListener implements View.OnClickListener {

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;
    private final TextView tView;

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
        if (MainActivity.isCountUp()) {
            TimerCountup.getInstance().reset();
        }
        else {
           TimerCountdown.getInstance().reset();
        }

        //Set a Click Listener for cancel/stop button
        btnCancel.setOnClickListener(new CancelButtonListener(btnStart, btnPause, btnResume, btnCancel, tView));
    }
}
