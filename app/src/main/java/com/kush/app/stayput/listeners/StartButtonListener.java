package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;

import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.TimerCountdown;
import com.kush.app.stayput.Consts;

/**
 * Created by Kush on 26.11.2016.
 *
 * Listener for the start button on main activity
 */

public class StartButtonListener implements View.OnClickListener{

    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;

    public StartButtonListener (Button btnStart, Button btnPause, Button btnResume, Button btnCancel) {
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
        TimerCountdown.getInstance();
    }
}
