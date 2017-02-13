package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;

import com.kush.app.stayput.Consts;
import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.Timer;

/**
 * Created by Kush on 26.11.2016.
 * <p>
 * Listener for the start button on main activity
 */

public class StartButtonListener implements View.OnClickListener {

    private final MainActivity context;
    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;

    public StartButtonListener(MainActivity context) {
        this.context = context;
        this.btnStart = context.getBtnStart();
        this.btnPause = context.getBtnPause();
        this.btnResume = context.getBtnResume();
        this.btnCancel = context.getBtnCancel();
    }

    @Override
    public void onClick(View v) {

        Timer.setPaused(false);
        Timer.setCanceled(false);
        Timer.setCountUp(false);
        Timer.setTimeRemaining(Consts.WORKTIME_MAX);

        //Disable the start and pause button
        btnStart.setEnabled(false);
        btnResume.setEnabled(false);
        //Enabled the pause and cancel button
        btnPause.setEnabled(true);
        btnCancel.setEnabled(true);
        //Initialize a new CountDownTimer instance
        context.startService();
    }
}
