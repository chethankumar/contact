package com.chethan.utils;

import com.chethan.contact.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
	
	private static final String FIRST_RUN = "isFirstRun";
	private static final String THEME_NAME = "themeName";
	
	public static final String BLUE = "blue";
	public static final String AQUA = "aqua";
	public static final String BLUE_GRAY = "bluegray";
	public static final String GREEN = "green";
	public static final String PINK = "pink";
	public static final String SEPIA = "sepia";
	public static final String TEAL = "teal";
	public static final String WHITE = "white";
	private static final String COLOR_DARK_GRAY = "#515151";
	private static final String COLOR_WHITE = "#FFFFFF";
	private static final String COLOR_LIGHT_GRAY = "#D6D6D6";
	private static final String COLOR_GRAY = "#7A7A7A";
	
	private static SharedPreferences prefs;

	public static void initPreferences(Activity activity) {
		prefs = activity.getSharedPreferences(
			      "com.chethan.contact", Context.MODE_PRIVATE);
	}
	
	public static boolean isFirstRun() {
		if(prefs.getBoolean(FIRST_RUN, true)){
			prefs.edit().putBoolean(FIRST_RUN, false).commit();
			return true;
		}
		return false;
	}
	
	public static void setTheme(String name) {
		prefs.edit().putString(THEME_NAME, name).commit();
	}
	
	public static String getTheme() {
		return prefs.getString(THEME_NAME, BLUE);
	}
	
	public static int convertThemeNameToInt() {
		String themeName = prefs.getString(THEME_NAME, BLUE);
		if (themeName.equalsIgnoreCase(AQUA)) {
			return 2;
		}
		else if (themeName.equalsIgnoreCase(BLUE_GRAY)) {
			return 3;
		}
		else if (themeName.equalsIgnoreCase(GREEN)) {
			return 4;
		}
		else if (themeName.equalsIgnoreCase(PINK)) {
			return 5;
		}
		else if (themeName.equalsIgnoreCase(SEPIA)) {
			return 6;
		}
		else if (themeName.equalsIgnoreCase(TEAL)) {
			return 7;
		}
		else if (themeName.equalsIgnoreCase(WHITE)) {
			return 8;
		}else{
			return 1;//default to BLUE
		}
	}
	
	public static int getScreenBackground() {
		switch (convertThemeNameToInt()) {
		case 1:return R.drawable.winkle; 
		case 2:return R.drawable.aqua;
		case 3:return R.drawable.bluegray;
		case 4:return R.drawable.green_grain;
		case 5:return R.drawable.pink;
		case 6:return R.drawable.sepia;
		case 7:return R.drawable.teal;
		case 8:return R.drawable.white;
		default:
			return R.drawable.winkle;
		}
	}
	
	public static String getDialerFontColor() {
		if (prefs.getString(THEME_NAME, BLUE).equalsIgnoreCase(WHITE)) {
			return COLOR_DARK_GRAY;
		}
		return COLOR_WHITE;
	}
	
	public static String getDialerCharacterFontColor() {
		if (prefs.getString(THEME_NAME, BLUE).equalsIgnoreCase(WHITE)) {
			return COLOR_GRAY;
		}
		return COLOR_LIGHT_GRAY;
	}
}
