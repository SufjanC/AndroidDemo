package com.sufjanc.demo.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sufjanc.demo.R;

/* TODO: 音乐播放器，使用服务器
         1. 音乐播放器基本功能
         2. 上传自己喜欢的音乐
 */
public class MusicPlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }
}