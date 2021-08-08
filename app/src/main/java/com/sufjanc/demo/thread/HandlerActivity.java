package com.sufjanc.demo.thread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sufjanc.demo.R;

public class HandlerActivity extends AppCompatActivity {

    String TAG = "HandlerActivity";
    private static final int send1 = 0x11;
    private static final int send2 = 0x12;
    private static final int SEND_DELAY = 0x13;
    private static final int SEND_T = 0x14;
    private Button btnSend1;
    private Button btnSend2;
    private Button btnPost;
    private Button btnThread;
    private Button btnFoo;
    private Button btnBaz;
    private TextView tvHandler;

    private final Handler handler = new Handler(Looper.myLooper()){
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            if(msg.what == send1){
                tvHandler.setText("send1: " + bundle.getString("str"));
            }else if(msg.what == send2){
                tvHandler.setText("send2: " + bundle.getString("str"));
            }else if(msg.what == SEND_DELAY){
                tvHandler.setText("delay: " + bundle.getString("str"));
            }
        }
    };

    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == SEND_T) {
                Log.d(TAG, "HandlerThread!");
                Bundle bundle = msg.getData();
                int num = bundle.getInt("num");
                num++;
                int finalNum = num;
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        tvHandler.setText("num from ui thread and increment in another thread: " + finalNum);
                    }
                });
            }
            return true;
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyIntentService.ACTION_FOO)){
                tvHandler.setText("Foo: " + intent.getStringExtra("str"));
            }else if(action.equals(MyIntentService.ACTION_BAZ)){
                tvHandler.setText("Baz: " + intent.getStringExtra("str"));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        btnSend1 = findViewById(R.id.btn_handler_send1);
        btnSend2 = findViewById(R.id.btn_handler_send2);
        tvHandler = findViewById(R.id.tv_handler);
        btnPost = findViewById(R.id.btn_handler_post);
        btnThread = findViewById(R.id.btn_handler_thread);
        btnFoo = findViewById(R.id.btn_handler_intent_foo);
        btnBaz = findViewById(R.id.btn_handler_intent_baz);
        HandlerThread handlerThread = new HandlerThread("Test");
        handlerThread.start();
        Handler handlerT = new Handler(handlerThread.getLooper(), callback);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyIntentService.ACTION_FOO);
        intentFilter.addAction(MyIntentService.ACTION_BAZ);
        registerReceiver(receiver, intentFilter);

        btnSend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg1 = Message.obtain();
                msg1.what = send1;
                Bundle bundle1 = new Bundle();
                bundle1.putString("str", "this is the message from send1.");
                msg1.setData(bundle1);
                handler.sendMessage(msg1);

                Message msg2 = Message.obtain();
                msg2.what = SEND_DELAY;
                Bundle bundle2 = new Bundle();
                bundle2.putString("str", "delay 5s message from send1.");
                msg2.setData(bundle2);
                handler.sendMessageDelayed(msg2, 5000);
            }
        });

        btnSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                msg.what = send2;
                Bundle bundle = new Bundle();
                bundle.putString("str", "this is the message from send2.");
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String text = "string from another thread";
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                tvHandler.setText(text);
                            }
                        });
                    }
                }).start();
            }
        });

        btnThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putInt("num", 1);
                msg.what = SEND_T;
                msg.setData(bundle);
                handlerT.sendMessage(msg);
            }
        });

        btnFoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyIntentService.startActionFoo(HandlerActivity.this, "foo_param1", "foo_param2");
            }
        });

        btnBaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyIntentService.startActionBaz(HandlerActivity.this, "baz_param1", "baz_param2");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}