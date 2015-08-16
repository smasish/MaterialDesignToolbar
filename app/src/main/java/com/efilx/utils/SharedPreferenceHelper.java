package com.efilx.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceHelper {
	// App Preferences
	private static final String PREFS_FILE_NAME = "AppPreferences";
    private static String ACCOUNT_DATA =  "account_data";

	public static String getString(final Context context) {
		return context.getSharedPreferences(
				SharedPreferenceHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE)
				.getString(ACCOUNT_DATA, null);
	}
	
	public static void setString(final Context context, String vlue) {
		final SharedPreferences prefs = context.getSharedPreferences(
				SharedPreferenceHelper.PREFS_FILE_NAME, Context.MODE_PRIVATE);
		final Editor editor = prefs.edit();

		editor.putString(ACCOUNT_DATA, vlue);
		editor.commit();
	}

	public static Boolean getBoolean(Context context, String s) {
        return Boolean.valueOf(context.getSharedPreferences("AppPreferences", 0).getBoolean(s, true));
    }

    public static void setBoolean(Context context, String s, Boolean boolean1){
        Editor editor = context.getSharedPreferences("AppPreferences", 0).edit();
        editor.putBoolean(s, boolean1.booleanValue());
        editor.commit();
    }
	
}
