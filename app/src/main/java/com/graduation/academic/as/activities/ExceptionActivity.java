package com.graduation.academic.as.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.graduation.academic.as.R;

public class ExceptionActivity extends AppCompatActivity {

    public static final String EXCEPTION_TAG = "exception_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_exception);
    }
}
