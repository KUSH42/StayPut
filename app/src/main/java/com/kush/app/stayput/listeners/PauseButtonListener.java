package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;

import com.kush.app.stayput.MainActivity;

/**
 * Created by Kush on 26.11.2016.
 *
 * Listener for the pause button on main activity
 */

public class PauseButtonListener implements View.OnClickListener {

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;

    public PauseButtonListener (Button btnStart, Button btnPause, Button btnResume, Button btnCancel) {
        this.btnStart = btnStart;
        this.btnPause = btnPause;
        this.btnResume = btnResume;
        this.btnCancel = btnCancel;
    }

    @Override
    public void onClick (View v){
        //When user request to pause the CountDownTimer
       MainActivity.setPaused(true);

        //Enable the resume and cancel button
        btnResume.setEnabled(true);
        btnCancel.setEnabled(true);
        //Disable the start and pause button
        btnStart.setEnabled(false);
        btnPause.setEnabled(false);
    }
}

