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
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.countdown.Timer;
import com.kush.app.stayput.listeners.CancelButtonListener;
import com.kush.app.stayput.listeners.PauseButtonListener;
import com.kush.app.stayput.listeners.ResumeButtonListener;
import com.kush.app.stayput.listeners.StartButtonListener;

import net.example.kush.stayput.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Does things I guess
 */

public class MainActivity extends Activity {

    //GUI references
    private TextView tView;
    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    //TextField Handler
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            update();
        }
    };
    //service stuff
    private Timer mBoundService;
    private boolean mServiceBound = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
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
        mBoundService.activeService = true;
    }


    //Listeners call this to stop the service and therefore the timer
    public void stopService() {
        mBoundService.stopSelf();
        unbindService(mServiceConnection);
        mBoundService.activeService = false;
    }


    //Method for updating the view and controls
    public void update() {
        if (mServiceBound && !Timer.isCanceled()) {
            long millis;
            if (Timer.isCountUp()) {
                tView.setTextColor(Consts.TIMER_COLOR_GREEN);
                millis = Consts.OVERTIME_MAX - Timer.getTimeRemaining() + 1000; //+1000 to compensate for inaccuracies
                tView.setText(("" + (millis / (60 * 60 * 1000) % 24) + "h " + (millis / (60 * 1000) % 60) + "m " + (millis / 1000 % 60) + "s").toString());
            }
            else {
                millis = Timer.getTimeRemaining() - 200; //-200 to compensate for inaccuracies
                tView.setTextColor(Consts.TIMER_COLOR_RED);
                tView.setText(("" + (millis / (60 * 60 * 1000) % 24) + "h " + (millis / (60 * 1000) % 60) + "m " + (millis / 1000 % 60) + "s").toString());
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
        super.onCreate(savedInstanceState);
        //set the main layout of the activity
        setContentView(R.layout.main);

        //Get reference of the XML layout's widgets
        tView = (TextView) findViewById(R.id.tv);
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
        // Bind to LocalService
        Intent intent = new Intent(this, com.kush.app.stayput.countdown.Timer.class);
        //only bind service if one is active
        if (Timer.activeService) {
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
        //update() Handler
        runnable.run();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
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