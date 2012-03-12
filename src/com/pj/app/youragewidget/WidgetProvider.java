package com.pj.app.youragewidget;

import java.util.ArrayList;
import java.util.Date;

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

public class WidgetProvider extends AppWidgetProvider{
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        SharedPreferences preferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, DetailsActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.main_widget);
//            views.setOnClickPendingIntent(R.id.your_age, pendingIntent);
            
            long dateBirth = preferences.getLong("birth_" + appWidgetId, 0);
            int formatSize = preferences.getInt("format_size_" + appWidgetId, 1);
            
            ArrayList<Integer> formats = new ArrayList<Integer>();
            for(int k = 0; k < formatSize; k++) {
            	formats.add(preferences.getInt("format_" + k + "_" + appWidgetId, -1));
            }
            
            hideSections(context,views);
            
            
            setSections(context,views, formats, dateBirth);
            
            
            
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
		
		for(Integer format : formats) {
			views.setInt(getSectionIdentifier(context, i), "setVisibility", View.VISIBLE);
			int yourAge = getYourAge(period, format);
			views.setTextViewText(getFormatIdentifier(context, i), String.valueOf(yourAge));
			
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
			
        	i++;
        }
	}

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
	
	private int getSectionIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/section_" + sectionNo, null, context.getPackageName());
	}
	
	private int getTypeIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/type_" + sectionNo, null, context.getPackageName());
	}
	
	private int getFormatIdentifier(Context context, int sectionNo) {
		return context.getResources().getIdentifier("id/format_" + sectionNo, null, context.getPackageName());
	}
}
