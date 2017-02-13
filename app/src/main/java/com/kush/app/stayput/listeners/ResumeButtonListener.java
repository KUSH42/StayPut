package com.kush.app.stayput.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.Timer;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Listener for the resume button on main activity
 */

public class ResumeButtonListener implements View.OnClickListener {

    private final MainActivity context;
    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;

    public ResumeButtonListener(MainActivity context) {
        this.context = context;
        this.btnStart = context.getBtnStart();
        this.btnPause = context.getBtnPause();
        this.btnResume = context.getBtnResume();
        this.btnCancel = context.getBtnCancel();
    }

    @Override
    public void onClick(View v) {
        //Disable the start and resume button
        btnStart.setEnabled(false);
        btnResume.setEnabled(false);
        //Enable the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);
        //Specify the current state is not paused and canceled.
        Timer.setPaused(false);
        Timer.setCanceled(false);
        //Start Timer Service
        Intent i = new Intent(context, Timer.class);
        context.startService(i);

        //Set a Click Listener for cancel/stop button
        btnCancel.setOnClickListener(new CancelButtonListener(context));
    }
}
