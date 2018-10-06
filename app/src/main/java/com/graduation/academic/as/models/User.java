package com.graduation.academic.as.models;

import android.content.SharedPreferences;

import com.graduation.academic.as.helpers.ParsingHelper;

import java.io.Serializable;

public class User implements Serializable {

    private static final String PREF_KEY = "user";

    String name;
    String ppURL;

    public void setPpURL(String ppURL) {
        this.ppURL = ppURL;
    }

    public String getPpURL() {
        return ppURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static User restore(SharedPreferences prefs) {
        return ParsingHelper.sGson.fromJson(prefs.getString(PREF_KEY, ""), User.class);
    }

    public void persist(SharedPreferences prefs) {
        prefs.edit().putString(PREF_KEY, ParsingHelper.sGson.toJson(this)).apply();
    }

    public void delete(SharedPreferences prefs) {
        prefs.edit().remove(PREF_KEY).apply();
    }

}
