package com.sufjanc.demo;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.sufjanc.demo.app.CameraActivity;
import com.sufjanc.demo.four.ServiceActivity;
import com.sufjanc.demo.four.TestActivity;
import com.sufjanc.demo.four.TestBaseActivity;
import com.sufjanc.demo.thread.HandlerActivity;

/*
    TODO： 1. 修改成Tab分类显示
           2. 使用Fragment
           3. 添加布局练习
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private Button btnRecyclerView;
    private Button btnResumeTest;
    private Button btnResumeBase;
    private Button btnHandler;
    private Button btnCamera;
    private Button btnService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        btnResumeTest = findViewById(R.id.btn_resume_test);
        btnResumeBase = findViewById(R.id.btn_resume_base);
        btnHandler = findViewById(R.id.btn_handler);
        btnCamera = findViewById(R.id.btn_camera);
        btnService = findViewById(R.id.btn_service);

        btnResumeTest.setOnClickListener(this);
        btnResumeBase.setOnClickListener(this);
        btnHandler.setOnClickListener(this);
        btnHandler.setOnClickListener(this);
        btnCamera.setOnClickListener(this);
        btnService.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.btn_resume_test:
                intent = new Intent(MainActivity.this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_resume_base:
                intent = new Intent(MainActivity.this, TestBaseActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_handler:
                intent = new Intent(MainActivity.this, HandlerActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_camera:
                intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_service:
                intent = new Intent(MainActivity.this, ServiceActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_toast:
                Toast.makeText(MainActivity.this, "Click Toast.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_alert_dialog:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("AlertDialog test:")
                        .setMessage("Now is testing AlertDialog, click the button to continue")
                        .setNegativeButton("cancel", null)
                        .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Click confirm.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}