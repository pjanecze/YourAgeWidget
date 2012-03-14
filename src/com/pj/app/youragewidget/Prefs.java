package com.pj.app.youragewidget;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	public static final SharedPreferences get(Context context) {
		return context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
	}
}
