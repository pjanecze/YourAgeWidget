package com.pj.app.youragewidget;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
	public static final String NUMBER_COLOR = "number_color";
	public static final String TEXT_COLOR = "text_color";
	public static final String START_COLOR = "start_color";
	public static final String MIDDLE_COLOR = "middle_color";
	public static final String END_COLOR = "end_color";
	public static final String BACKGROUND_COLOR = "background_color";
	public static final String BORDER_COLOR = "border_color";
	public static final String IS_GRADIENT = "is_gradient";
	public static final String WITH_BORDER = "with_border";
	public static final String WITH_CORNER = "with_corner";
    public static final String GRADIENT_TYPE = "gradient_type";

    public static final SharedPreferences get(Context context) {
		return context.getSharedPreferences("PREFS", Context.MODE_PRIVATE);
	}
	
	public static final void setDefaultValues(Context context) {
		SharedPreferences prefs = Prefs.get(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(NUMBER_COLOR + "default", context.getResources().getColor(R.color.default_number));//holo_blue_light
		editor.putInt(TEXT_COLOR + "default", context.getResources().getColor(R.color.default_text));//holo_orange_light
		editor.putInt(START_COLOR + "default", context.getResources().getColor(R.color.default_start));
		editor.putInt(MIDDLE_COLOR + "default", context.getResources().getColor(R.color.default_middle));
		editor.putInt(END_COLOR + "default", context.getResources().getColor(R.color.default_end));
		editor.putInt(BACKGROUND_COLOR + "default", context.getResources().getColor(R.color.default_background));
		editor.putInt(BORDER_COLOR + "default", context.getResources().getColor(R.color.default_border));
		editor.commit();
	}

	public static void setValue(SharedPreferences prefs, String key,
			Object value) {
		SharedPreferences.Editor editor = prefs.edit();
		if(value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if(value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if(value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if(value instanceof String) {
			editor.putString(key, (String) value);
		} else {
			throw new RuntimeException("not supported type");
		}
		editor.commit();
	}

    public static int getInt(SharedPreferences mPrefs, String key, int appWidgetId) {
        return mPrefs.getInt(key + appWidgetId, mPrefs.getInt(key + "default", appWidgetId));
    }
}
