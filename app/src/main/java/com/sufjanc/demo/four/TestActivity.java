package com.sufjanc.demo.four;

import android.os.Bundle;
import android.util.Log;

import com.sufjanc.demo.R;
import com.sufjanc.demo.four.TestBaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends TestBaseActivity {

    String TAG = "TestActivity";
    private List<String> mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mSecond = 3;
        mList = new ArrayList<String>();
        mList.add("sub");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "Override");
        super.onResume();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    protected List<String> getMenuList() {
//        return mList;
//    }
}