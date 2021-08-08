package com.sufjanc.demo.four;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

public class TestService extends Service {
    
    private static final String TAG = "TestService";
    private SimpleBinder binder = null;

    public TestService() {
        Log.d(TAG, "TestService: ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        binder = new SimpleBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: ");
        if(binder != null)
            return binder;
        return null;
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG, "onRebind: ");
    }
    
    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        return super.onUnbind(intent);
    }

    static class SimpleBinder extends Binder{

        public void doTask(){
            Log.d(TAG, "doTask: ");
        }
        public void updateUi(TextView textView){
            textView.setText("Update UI in Service");
        }
    }

}