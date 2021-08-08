package com.sufjanc.demo.four;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.sufjanc.demo.R;

public class TestBaseActivity extends AppCompatActivity {

    String TAG = "TestBaseActivity";
    protected int mSecond = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_base);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String out = String.format("mSecond=%d", mSecond);
        if(mSecond == 0)
            Toast.makeText(TestBaseActivity.this, out + " Base value", Toast.LENGTH_SHORT).show();
        else if(mSecond == 3)
            Toast.makeText(TestBaseActivity.this, out + " This value", Toast.LENGTH_SHORT).show();
//        List<String> list = getMenuList();//获取子类的list，动态绑定
//        Log.d(TAG, list.toString());
    }

//    protected abstract List<String> getMenuList();//子类实现
}