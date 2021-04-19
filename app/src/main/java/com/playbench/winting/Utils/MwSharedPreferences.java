package com.playbench.winting.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MwSharedPreferences {

    private SharedPreferences mPref;

    public MwSharedPreferences (Context mContext){
        mPref = mContext.getSharedPreferences("winting", Activity.MODE_PRIVATE);
    }

    public Boolean setValue(String pK, int pValue){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putInt(pK, pValue);
        editor.apply();
        return true;
    }

    public Boolean setValue(String pK, String pValue){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(pK, pValue);
        editor.apply();
        return true;
    }

    public Boolean setValue(String pK, boolean pValue){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(pK, pValue);
        editor.apply();
        return true;
    }

    public int getIntValue(String pK){
        return mPref.getInt(pK, -1);
    }

    public boolean getBooleanValue(String pK){
        return mPref.getBoolean(pK, false);
    }

    public String getStringValue(String pK){
        return mPref.getString(pK, "");
    }

    public Boolean remove(String pK){
        SharedPreferences.Editor editor = mPref.edit();
        editor.remove(pK);
        editor.apply();
        return true;
    }

    public Boolean clear(){
        SharedPreferences.Editor editor = mPref.edit();
        editor.clear();
        return true;
    }
}
