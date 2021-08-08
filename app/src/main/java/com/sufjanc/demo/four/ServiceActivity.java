package com.sufjanc.demo.four;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sufjanc.demo.R;

public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "ServiceActivity";
    private Button btnStart, btnStop, btnBind, btnUnbind;
    private TextView tvService;
    private TestService.SimpleBinder binder = null;
    private boolean isServiceConnected = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected: " + name.toString());
            isServiceConnected = true;
            binder = (TestService.SimpleBinder) service;
            binder.doTask();
            binder.updateUi(tvService);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected: " + name.toString());
            isServiceConnected = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        btnStart = findViewById(R.id.btn_service_start);
        btnStop = findViewById(R.id.btn_service_stop);
        btnBind = findViewById(R.id.btn_service_bind);
        btnUnbind = findViewById(R.id.btn_service_unbind);
        tvService = findViewById(R.id.tv_service);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TestService.class);
                startService(intent);
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ServiceActivity.this, TestService.class);
                stopService(intent);
            }
        });
        btnBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bindIntent = new Intent(ServiceActivity.this, TestService.class);
                bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
            }
        });
        btnUnbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isServiceConnected) {
                    isServiceConnected = false;
                    unbindService(serviceConnection);
                }
            }
        });
    }
}