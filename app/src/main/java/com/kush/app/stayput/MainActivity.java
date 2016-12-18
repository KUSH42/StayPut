package com.kush.app.stayput;

import android.os.Bundle;
import android.app.Activity;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.listeners.CancelButtonListener;
import com.kush.app.stayput.listeners.PauseButtonListener;
import com.kush.app.stayput.listeners.ResumeButtonListener;
import com.kush.app.stayput.listeners.StartButtonListener;

import net.example.kush.stayput.R;

/**
 * @value #countUp If true, is counting up. Default on app creation is false.
 */

public class MainActivity extends Activity {

    //Declare a variable to hold the countUp flag
    private static boolean countUp = false;
    //Declare a variable to hold count down timer's paused status
    private static boolean isPaused = false;
    //Declare a variable to hold count down timer's canceled status
    private static boolean isCanceled = false;
    //Declare a variable to hold CountDownTimer remaining time
    private static long timeRemaining = Consts.WORKTIME_MAX;
    //Reference for main context
    private static MainActivity context;

    public static MainActivity getContext() {
        return context;
    }

    public static void setContext(MainActivity context) {
        MainActivity.context = context;
    }

    public static boolean isCountUp() {
        return countUp;
    }

    public static void setCountUp(boolean countUp) {
        MainActivity.countUp = countUp;
    }

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
        //save context to static variable
        MainActivity.setContext(this);
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