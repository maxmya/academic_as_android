package com.graduation.academic.as.activities;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.graduation.academic.as.App;
import com.graduation.academic.as.R;
import com.graduation.academic.as.api.Api;
import com.graduation.academic.as.api.ApiHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //api = ApiHelper.getClient().create(Api.class);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                App.openActivityWithoutAnimation(GroupsListActivity.class, SplashActivity.this);
            }
        }, 5000);

    }


}
