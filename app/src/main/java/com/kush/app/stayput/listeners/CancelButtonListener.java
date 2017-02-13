package com.kush.app.stayput.listeners;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.MainActivity;
import com.kush.app.stayput.countdown.Timer;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Listener for the cancel button on main activity
 */

public class CancelButtonListener implements View.OnClickListener {

    private final MainActivity context;
    private final Button btnStart;
    private final Button btnPause;
    private final Button btnResume;
    private final Button btnCancel;
    private final TextView tView;

    public CancelButtonListener(MainActivity context) {
        this.context = context;
        this.btnStart = context.getBtnStart();
        this.btnPause = context.getBtnPause();
        this.btnResume = context.getBtnResume();
        this.btnCancel = context.getBtnCancel();
        this.tView = context.getTView();
    }

    @Override
    public void onClick(View v) {
        //When user request to cancel the CountDownTimer
        Timer.setCanceled(true);

        //Stop Timer Service
        Intent i = new Intent(context, Timer.class);
        context.stopService(i);

        //Disable the cancel, pause and resume button
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnCancel.setEnabled(false);
        //Enable the start button
        btnStart.setEnabled(true);

        //Notify the user that CountDownTimer is canceled/stopped
        tView.setText("Zeit gestoppt");
    }
}
