package com.abdev.codestervpn.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class Constant {
    private static final String PREF_NAME = "snow-intro-slider";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    public static final String PRIMIUM_STATE = "primium_state";//boolean

    public static final String COUNTRY_DATA = "Country_data";
    public static final String BUNDLE = "Bundle";
    public static final String SELECTED_COUNTRY = "selected_country";

    public static final String INAPPSKUUNIT ="inappskuunit";
    public static final String  PURCHASETIME="purchasetime";
    public static final String INAPPKEY ="inappkey";
    public static final String MONTHLYKEY ="monthkey";
    public static final String SIXMONTHKEY ="sixmonthkey";
    public static final String YEARKEY ="yearkey";


    public Constant(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        editor.commit();
    }
}