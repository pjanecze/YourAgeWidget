package com.pj.app.youragewidget;

import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Months;
import org.joda.time.Period;
import org.joda.time.Years;

import com.pj.app.youragewidget.ui.DetailsActivity;
import com.pj.app.youragewidget.ui.WidgetConfigureActivity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.pj.lib.utils.MetricsTools;

public abstract class WidgetProvider extends AppWidgetProvider{

    private static int request = 0;
    public static final String ACTION_UPDATE = "com.pj.app.youragewidget.update";


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if(intent.getAction().equals(ACTION_UPDATE)){
            Bundle extras = intent.getExtras();
            if (extras != null) {
                int[] appWidgetIds = extras.getIntArray(AppWidgetManager.EXTRA_APPWIDGET_IDS);
                if (appWidgetIds != null && appWidgetIds.length > 0) {
                    this.onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
                }
            }
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        SharedPreferences preferences = Prefs.get(context);
        
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {


            int appWidgetId = appWidgetIds[i];
            Log.i("test", "WIDGET ID = " + appWidgetId);

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, request++, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);
            views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
            
            long dateBirth = preferences.getLong("birth_" + appWidgetId, 0);
            int formatSize = preferences.getInt("format_size_" + appWidgetId, 1);


            ArrayList<Integer> formats = new ArrayList<Integer>();
            for(int k = 0; k < formatSize; k++) {
            	formats.add(preferences.getInt("format_" + k + "_" + appWidgetId, -1));
            }
            
            hideSections(context,views);
            
            
            setSections(context,views, formats, dateBirth);
            
            setWidgetColors(context, views, preferences, appWidgetId, preferences.getInt("widget_size_" + appWidgetId,1));
            
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

	private void setSections(Context context, RemoteViews views, ArrayList<Integer> formats,
			long birthMilis) {
		int i = 0;
		
		
		DateTime now = new DateTime();
		DateTime dateBirth = new DateTime(birthMilis);
		Period period = new Period(dateBirth, now);		
		
		boolean wasYear = false;
		boolean wasMonth = false;
		boolean wasWeeks = false;
		boolean wasDays = false;
		
		int widgetSize = getWidgetSize();
		
		int bigCharsInCell = context.getResources().getInteger(R.integer.big_chars_in_cell);
		int mediumCharsInCell = context.getResources().getInteger(R.integer.medium_chars_in_cell);
		int smallCharsInCell = context.getResources().getInteger(R.integer.small_chars_in_cell);
		
		float bigSize = context.getResources().getInteger(R.integer.biggest_num);
		float mediumSize = context.getResources().getInteger(R.integer.medium_num);
		float smallSize = context.getResources().getInteger(R.integer.small_num);
		
		if(widgetSize > formats.size()) {
			if(widgetSize == formats.size() + 1) {
				bigCharsInCell += 2;
				mediumCharsInCell += 2;
				smallCharsInCell += 4;
			} else if(widgetSize == formats.size() + 2) {
				bigCharsInCell += 7;
				mediumCharsInCell += 4;
				smallCharsInCell += 8;
			} else {
				bigCharsInCell += 9;
				mediumCharsInCell += 16;
				smallCharsInCell += 32;
			}
		}
		
		for(Integer format : formats) {
            Log.i("test", "WIDGET SECTION: " + getSectionIdentifier(context, i));
			views.setInt(getSectionIdentifier(context, i), "setVisibility", View.VISIBLE);
			int yourAge = getYourAge(period, format);
			
			
			
			if(format == WidgetConfigureActivity.YEAR) {
				if(yourAge <= 1) {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_year));
				} else {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_years));
				}
				wasYear = true;
			} else if(format == WidgetConfigureActivity.MONTH) {
				if(!wasYear)
					yourAge += 12 * period.getYears();
				if(yourAge <= 1) {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_month));
				} else {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_months));
				}
				wasMonth = true;
			} else if(format == WidgetConfigureActivity.WEEK){
				
				if(!wasMonth && !wasYear) {
					
					yourAge += 52.177457 * period.getYears();  
					yourAge += 4.34812142 * period.getMonths();
				} else if(!wasMonth) {
					yourAge += 4.34812142 * period.getMonths(); 
				} 
				
				if(yourAge <= 1) {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_week));
				} else {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_weeks));
				}
				wasWeeks = true;
			} else if(format == WidgetConfigureActivity.DAY){
				if(!wasYear && !wasMonth && !wasWeeks) {
					Days days = Days.daysBetween(dateBirth, now);
					yourAge = days.getDays();
				} else if(!wasMonth && !wasWeeks){
					yourAge += 30.436849 * period.getMonths();
					yourAge += 7 * period.getWeeks();
				} else if(!wasWeeks) {
					yourAge += 7 * period.getWeeks();
				}
				if(yourAge <= 1) {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_day));
				} else {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_days));
				}
				wasDays = true;
			} else if(format == WidgetConfigureActivity.HOUR) {
				if(!wasYear && !wasMonth && !wasWeeks && !wasDays) {
					Hours hours = Hours.hoursBetween(dateBirth, now);
					yourAge = hours.getHours();
				} else if(!wasMonth && !wasWeeks && !wasDays) {
					yourAge += 30.436849 * 24 * period.getMonths();
					yourAge += 7 * 24 * period.getWeeks();
					yourAge += 24 * period.getDays();
				} else if(!wasWeeks && !wasDays) {
					yourAge += 7 * 24 * period.getWeeks();
					yourAge += 24 * period.getDays();
				} else if(!wasDays) {
					yourAge += 24 * period.getDays();
				}
				
				if(yourAge <= 1) {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_hour));
				} else {
					views.setTextViewText(getTypeIdentifier(context, i), context.getString(R.string.txt_hours));
				}
			}  
			
			String yourAgeStr = String.valueOf(yourAge);
			
			if(yourAgeStr.length() <= bigCharsInCell) {
				views.setFloat(getFormatIdentifier(context, i), "setTextSize", MetricsTools.spToPixels(context, bigSize));
				Log.i("test", "textSize1: " + MetricsTools.spToPixels(context, bigSize) + " " + yourAgeStr);
			} else if(yourAgeStr.length() <= mediumCharsInCell) {
				views.setFloat(getFormatIdentifier(context, i), "setTextSize", MetricsTools.spToPixels(context, mediumSize));
				Log.i("test", "textSize2: " + MetricsTools.spToPixels(context, mediumSize) + " " + yourAgeStr);
			} else if(yourAgeStr.length() <= smallCharsInCell) {
				views.setFloat(getFormatIdentifier(context, i), "setTextSize", MetricsTools.spToPixels(context, smallSize));
				Log.i("test", "textSize3: " + MetricsTools.spToPixels(context, smallSize) + " " + yourAgeStr);
			} else {
				views.setFloat(getFormatIdentifier(context, i), "setTextSize", MetricsTools.spToPixels(context, smallSize));
				Log.i("test", "textSize4: " + MetricsTools.spToPixels(context, smallSize) + " " + yourAgeStr);
			}
			
			
			
			views.setTextViewText(getFormatIdentifier(context, i), yourAgeStr);

            Log.i("test", "WIDGET AGE = " + yourAgeStr);

        	i++;
        }
	}

	abstract int getWidgetSize();

	private void hideSections(Context context, RemoteViews views) {
		for (int i = 0; i < 4; i++) {
			views.setInt(getSectionIdentifier(context, i), "setVisibility", View.GONE);
		}

	}
	
	private int getYourAge(Period period, Integer format) {
		
		if(format == WidgetConfigureActivity.HOUR) {
			return period.getHours();
		} else if(format == WidgetConfigureActivity.YEAR) {
			return period.getYears();
		} else if(format == WidgetConfigureActivity.MONTH) {
			return period.getMonths();
		} else if(format == WidgetConfigureActivity.WEEK){
			return period.getWeeks();
		} else if(format == WidgetConfigureActivity.DAY) {
			return period.getDays();
		}
		return 0;
		
	}
	
	public static int getSectionIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/section_" + sectionNo, null, context.getPackageName());
	}
	
	public static int getTypeIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/type_" + sectionNo, null, context.getPackageName());
	}
	
	public static int getFormatIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/format_" + sectionNo, null, context.getPackageName());
	}

	public static void setWidgetColors(Context context,RemoteViews views,
			SharedPreferences mPrefs, int appWidgetId, int widgetSize) {
        int numberColor = Prefs.getInt(mPrefs,Prefs.NUMBER_COLOR, appWidgetId);
		int textColor = Prefs.getInt(mPrefs,Prefs.TEXT_COLOR, appWidgetId);
		for (int i = 0; i < 4; i++) {
			views.setTextColor(getFormatIdentifier(context, i), numberColor);
			views.setTextColor(getTypeIdentifier(context, i), textColor);
		}

        boolean withBorder = mPrefs.getBoolean(Prefs.WITH_BORDER+ appWidgetId, false);
        boolean withCorner = mPrefs.getBoolean(Prefs.WITH_CORNER+ appWidgetId, false);

        boolean isGradient = mPrefs.getBoolean(Prefs.IS_GRADIENT+ appWidgetId, false);

        int startColor;
        int middleColor;
        int endColor;

        int borderColor = Prefs.getInt(mPrefs, Prefs.BORDER_COLOR, appWidgetId);

        GradientDrawable.Orientation orientation = GradientDrawable.Orientation.TOP_BOTTOM;
        if(isGradient) {
            startColor = Prefs.getInt(mPrefs, Prefs.START_COLOR, appWidgetId);
            middleColor = Prefs.getInt(mPrefs, Prefs.MIDDLE_COLOR, appWidgetId);
            endColor = Prefs.getInt(mPrefs, Prefs.END_COLOR, appWidgetId);
            int gradientType = Prefs.getInt(mPrefs, Prefs.GRADIENT_TYPE, appWidgetId);

            switch (gradientType) {
                case 0:
                    orientation = GradientDrawable.Orientation.TOP_BOTTOM;
                    break;
                case 1:
                    orientation = GradientDrawable.Orientation.BOTTOM_TOP;
                    break;
                case 2:
                    orientation = GradientDrawable.Orientation.LEFT_RIGHT;
                    break;
                case 3:
                    orientation = GradientDrawable.Orientation.RIGHT_LEFT;
                    break;
            }
        } else {
            int color = Prefs.getInt(mPrefs, Prefs.BACKGROUND_COLOR, appWidgetId);
            startColor = color;
            middleColor = color;
            endColor = color;
        }


        int borderWidth = 0;
        if(withBorder) {
            borderWidth = 4;
        }

        int cornerRadius = 0;
        if(withCorner) {
            cornerRadius = 4;
        }

        Drawable drawable = Tools.getShapeDrawable(new int[]{startColor, middleColor, endColor},
                orientation,
                borderColor,
                borderWidth,
                cornerRadius);


        Bitmap bmp;

        switch (widgetSize) {
            case 1:
                bmp = Bitmap.createBitmap(context.getResources().getInteger(R.integer.bitmap_width_1),
                        context.getResources().getInteger(R.integer.bitmap_height_1), Bitmap.Config.ARGB_8888);
                break;
            case 2:
                bmp = Bitmap.createBitmap(context.getResources().getInteger(R.integer.bitmap_width_2),
                        context.getResources().getInteger(R.integer.bitmap_height_2), Bitmap.Config.ARGB_8888);
                break;
            case 3:
                bmp = Bitmap.createBitmap(context.getResources().getInteger(R.integer.bitmap_width_3),
                        context.getResources().getInteger(R.integer.bitmap_height_3), Bitmap.Config.ARGB_8888);
                break;
            case 4:
                bmp = Bitmap.createBitmap(context.getResources().getInteger(R.integer.bitmap_width_4),
                        context.getResources().getInteger(R.integer.bitmap_height_4), Bitmap.Config.ARGB_8888);
                break;
            default:
                bmp = null;
        }

        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        views.setImageViewBitmap(R.id.background, bmp);
		
	}
    
}
