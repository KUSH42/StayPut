package com.kush.app.stayput.listeners;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.MainActivity;

/**
 * Created by Kush on 02.12.2016.
 */

public class CancelButtonListener implements View.OnClickListener {

    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    private TextView tView;

    public CancelButtonListener (Button btnStart, Button btnPause, Button btnResume, Button btnCancel, TextView tView) {
        this.btnStart = btnStart;
        this.btnPause = btnPause;
        this.btnResume = btnResume;
        this.btnCancel = btnCancel;
        this.tView = tView;
    }

    @Override
    public void onClick (View v){
        //When user request to cancel the CountDownTimer
        MainActivity.setCanceled(true);

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
