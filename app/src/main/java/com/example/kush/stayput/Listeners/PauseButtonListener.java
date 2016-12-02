package com.example.kush.stayput.listeners;

import android.view.View;
import android.widget.Button;

import com.example.kush.stayput.MainActivity;

/**
 * Created by Kush on 26.11.2016.
 */

public class PauseButtonListener implements View.OnClickListener {

    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;

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

