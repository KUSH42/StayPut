package com.kush.app.stayput;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.countdown.Timer;
import com.kush.app.stayput.listeners.CancelButtonListener;
import com.kush.app.stayput.listeners.PauseButtonListener;
import com.kush.app.stayput.listeners.ResumeButtonListener;
import com.kush.app.stayput.listeners.StartButtonListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.content.ContentValues.TAG;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Does things I guess
 */

public class MainActivity extends Activity {

    //GUI references
    private TextView tView;
    private TextView tView2;
    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    //TextField Handler
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        public void run() {
            update();
        }
    };
    //service stuff
    private static Timer mBoundService;
    private boolean mServiceBound = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundService = null;
            mServiceBound = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Timer.LocalBinder myBinder = (Timer.LocalBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }
    };

    //Listeners call this to start the service and therefore the timer
    public void startService() {
        Intent i = new Intent(getApplicationContext(), Timer.class);
        bindService(i, mServiceConnection, Context.BIND_AUTO_CREATE);
        //tell the user when he's done
        String tViewTime = (getDate(System.currentTimeMillis()+Timer.getTimeRemaining(), "hh:mm") + Consts.STR_END_FINISH_TIME);
        tView2.setText(tViewTime);
        //update() Handler
        runnable.run();
    }

    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


    //Listeners call this to stop the service and therefore the timer
    public void stopService() {
        unbindService(mServiceConnection);
        mBoundService.stopSelf();
        mBoundService = null;
        Timer.activeService = false;
        //stop update() Handler
        handler.removeCallbacks(runnable);
    }


    //Method for updating the view and controls
    private void update() {
        if (mServiceBound && !Timer.isCanceled()) {
            long millis;
            String tViewTime;
            if (Timer.isCountUp()) {
                tView.setTextColor(Consts.TIMER_COLOR_GREEN);
                millis = Consts.OVERTIME_MAX - Timer.getTimeRemaining() + 1000; //+1000 to compensate for inaccuracies
                tViewTime = ("" + (millis / (60 * 60 * 1000) % 24) + "h " + (millis / (60 * 1000) % 60) + "m " + (millis / 1000 % 60) + "s");
                tView.setText(tViewTime);
            }
            else {
                tView.setTextColor(Consts.TIMER_COLOR_RED);
                millis = Timer.getTimeRemaining() - 200; //-200 to compensate for inaccuracies
                tViewTime = ("" + (millis / (60 * 60 * 1000) % 24) + "h " + (millis / (60 * 1000) % 60) + "m " + (millis / 1000 % 60) + "s");
                tView.setText(tViewTime);
            }
            if (Timer.hasFinished()) {
                //Disables the pause, resume and start buttons
                btnPause.setEnabled(false);
                btnResume.setEnabled(false);
                btnStart.setEnabled(false);
                //Enable the cancel button
                btnCancel.setEnabled(true);
            }
        }
        handler.postDelayed(runnable,100);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "create");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        //set the main layout of the activity
        setContentView(R.layout.main);

        //Get reference of the XML layout's widgets
        tView = (TextView) findViewById(R.id.tv);
        tView2 = (TextView) findViewById(R.id.tv2);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnCancel = (Button) findViewById(R.id.btn_cancel);

        //Disable buttons
        btnPause.setEnabled(false);
        btnResume.setEnabled(false);
        btnCancel.setEnabled(false);

        //Initialize listeners
        btnStart.setOnClickListener(new StartButtonListener(this));
        btnPause.setOnClickListener(new PauseButtonListener(this));
        btnResume.setOnClickListener(new ResumeButtonListener(this));
        btnCancel.setOnClickListener(new CancelButtonListener(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //only bind service if one is active
        if (Timer.activeService) {
            //Bind to LocalService
            Intent intent = new Intent(this, com.kush.app.stayput.countdown.Timer.class);
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            //initiate gui
            String timeFinished = getDate(System.currentTimeMillis()+Timer.getTimeRemaining(), "hh:mm") + Consts.STR_END_FINISH_TIME;
            tView2.setText(timeFinished);
            btnStart.setEnabled(false);
            btnPause.setEnabled(true);
            btnResume.setEnabled(false);
            btnCancel.setEnabled(true);
            //update() Handler
            runnable.run();
        }
        else tView.setText(Consts.WORKTIME);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
        }
    }

    public TextView getTView() {
        return tView;
    }
    public Button getBtnStart() {
        return btnStart;
    }
    public Button getBtnPause() {
        return btnPause;
    }
    public Button getBtnResume() {
        return btnResume;
    }
    public Button getBtnCancel() {
        return btnCancel;
    }
}