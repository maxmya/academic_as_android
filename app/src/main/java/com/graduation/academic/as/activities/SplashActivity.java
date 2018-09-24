package com.graduation.academic.as.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.graduation.academic.as.R;
import com.graduation.academic.as.api.Api;
import com.graduation.academic.as.api.ApiHelper;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // this is how to create instance of api interface
        Api api = ApiHelper.getClient().create(Api.class);

    }

}
