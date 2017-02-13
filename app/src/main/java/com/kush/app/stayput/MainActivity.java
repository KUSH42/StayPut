package com.kush.app.stayput;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Button;
import android.widget.TextView;

import com.kush.app.stayput.countdown.Timer;
import com.kush.app.stayput.listeners.CancelButtonListener;
import com.kush.app.stayput.listeners.PauseButtonListener;
import com.kush.app.stayput.listeners.ResumeButtonListener;
import com.kush.app.stayput.listeners.StartButtonListener;

import net.example.kush.stayput.R;

/**
 * Created by Kush on 02.12.2016.
 * <p>
 * Does things I guess
 */

public class MainActivity extends Activity {

    //GUI references
    public static MainActivity context;     //fixme
    private TextView tView;
    private Button btnStart;
    private Button btnPause;
    private Button btnResume;
    private Button btnCancel;
    //service stuff
    @SuppressWarnings("FieldCanBeLocal")
    private boolean mServiceBound = false;
    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBound = true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
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
        context = this;
        tView = (TextView) findViewById(R.id.tv);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        // Bind to LocalService
        Intent intent = new Intent(this, com.kush.app.stayput.countdown.Timer.class);
        //only bind service if one is active
        if (Timer.activeService) {
            bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
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