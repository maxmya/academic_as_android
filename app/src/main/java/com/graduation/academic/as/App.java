package com.graduation.academic.as;

import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, TAG + "initialized", Toast.LENGTH_SHORT).show();
    }
}
