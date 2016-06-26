package com.example.xia.weixin;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by xia on 2016/6/23.
 */
public class settingActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
//        addPreferencesFromResource(R.layout.activity_about);
    }
}
