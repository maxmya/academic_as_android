package com.graduation.academic.as;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDexApplication;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.graduation.academic.as.handlers.ExceptionHandler;

public class App extends MultiDexApplication {

    private static final String TAG = App.class.getSimpleName();

    // for singleton class and functionaries
    private static App mInstance;
    private static SharedPreferences sPrefs;

    //  keys
    public static final String PREF_KEY_LOGIN = "key_login";
    public static final Integer READ_EXST = 0x4;
    public static final Integer WRITE_EXST = 0x3;
    public static final Integer PICKFILE_RESULT_CODE = 200;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sPrefs = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static <T> T openActivityWithFadeAnim(final Class<T> wantedActivity, Context currentContext, boolean finish) {
        Intent intent = new Intent(currentContext, wantedActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        currentContext.startActivity(intent);
        ((Activity) currentContext).overridePendingTransition(R.anim.fadout, R.anim.faidin);
        if (finish)
            ((Activity) currentContext).finish();
        return null;
    }

    public static void saveLoginToPrefs() {
        sPrefs.edit().putBoolean(PREF_KEY_LOGIN, true).apply();
    }

    public static void deleteLoginFromPrefs() {
        sPrefs.edit().putBoolean(PREF_KEY_LOGIN, false).apply();
    }

    public static boolean isLoggedIn() {
        return sPrefs.getBoolean(PREF_KEY_LOGIN, false);
    }

    public static void askForPermission(String permission, Integer requestCode, Context mContext) {
        if (ContextCompat.checkSelfPermission(mContext, permission) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions((Activity) mContext, new String[]{permission}, requestCode);
            }
        }
    }
}
