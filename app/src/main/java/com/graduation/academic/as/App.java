package com.graduation.academic.as;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDexApplication;
import android.widget.Toast;

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();

    // for singleton class
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Toast.makeText(this, TAG + " Initialized", Toast.LENGTH_SHORT).show();
    }

    public static App getInstance() {
        return mInstance;
    }

    public static <T> T openActivityWithoutAnimation(final Class<T> wantedActivity, Context currentContext) {
        Intent intent = new Intent(currentContext, wantedActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        currentContext.startActivity(intent);
        ((Activity) currentContext).finish();
        return null;
    }

}
