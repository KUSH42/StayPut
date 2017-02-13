package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;

import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.Timer;

/**
 * Created by Kush on 26.11.2016.
 * <p>
 * Listener for the pause button on main activity
 */

public class PauseButtonListener implements View.OnClickListener {

    private final MainActivity context;
    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;

    public PauseButtonListener(MainActivity context) {
        this.context = context;
        this.btnStart = context.getBtnStart();
        this.btnPause = context.getBtnPause();
        this.btnResume = context.getBtnResume();
        this.btnCancel = context.getBtnCancel();
    }

    @Override
    public void onClick(View v) {
        //When user request to pause the CountDownTimer
        Timer.setPaused(true);
        //Stop Timer Service
        context.stopService();
        //Enable the resume and cancel button
        btnResume.setEnabled(true);
        btnCancel.setEnabled(true);
        //Disable the start and pause button
        btnStart.setEnabled(false);
        btnPause.setEnabled(false);
    }
}

