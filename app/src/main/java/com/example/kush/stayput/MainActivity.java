package com.example.kush.stayput;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.example.kush.stayput.listeners.CancelButtonListener;
import com.example.kush.stayput.listeners.PauseButtonListener;
import com.example.kush.stayput.listeners.ResumeButtonListener;
import com.example.kush.stayput.listeners.StartButtonListener;

import net.example.kush.stayput.R;

public class MainActivity extends Activity {

    //Declare a variable to hold count down timer's paused status
    private static boolean isPaused = false;
    //Declare a variable to hold count down timer's canceled status
    private static boolean isCanceled = false;
    //Declare a variable to hold CountDownTimer remaining time
    private static long timeRemaining = (7*60*60*1000)+(42*60*1000);

    public static boolean isPaused() {
        return isPaused;
    }

    public static boolean isCanceled() {
        return isCanceled;
    }

    public static long getTimeRemaining() {
        return timeRemaining;
    }

    public static void setPaused(boolean paused) {
        isPaused = paused;
    }

    public static void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public static void setTimeRemaining(long newTimeRemaining) {
        timeRemaining = newTimeRemaining;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the main layout of the activity
        setContentView(R.layout.main);

        //Get reference of the XML layout's widgets
        final TextView tView = (TextView) findViewById(R.id.tv);
        final Button btnStart = (Button) findViewById(R.id.btn_start);
        final Button btnPause = (Button) findViewById(R.id.btn_pause);
        final Button btnResume = (Button) findViewById(R.id.btn_resume);
        final Button btnCancel = (Button) findViewById(R.id.btn_cancel);

        //Disable buttons
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnCancel.setEnabled(false);

        //Initialize listeners
        btnStart.setOnClickListener(new StartButtonListener(btnStart, btnPause, btnResume, btnCancel, tView));
        btnPause.setOnClickListener(new PauseButtonListener(btnStart, btnPause, btnResume, btnCancel));
        btnResume.setOnClickListener(new ResumeButtonListener(btnStart, btnPause, btnResume, btnCancel, tView));
        btnCancel.setOnClickListener(new CancelButtonListener(btnStart, btnPause, btnResume, btnCancel, tView));
    }
}