package com.pj.app.youragewidget.ui;

import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
import com.pj.app.youragewidget.Prefs;
import com.pj.app.youragewidget.R;
import com.pj.app.youragewidget.WidgetProvider;
import com.pj.lib.widgets.DateTimeButton;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Toast;

public abstract class WidgetConfigureActivity extends Activity implements OnClickListener{

	public static final int YEAR = 1;
	public static final int MONTH = 2;
	public static final int WEEK = 3;
	public static final int DAY = 4;
	public static final int HOUR = 5;
	
	DateTimeButton btnDateOfBirth, btnTimeOfBirth;
	CheckBox chYear, chMonth, chWeek, chDay, chHour;
	
	Button btnOk;
	
	private int mAppWidgetId;
	
	ArrayList<Integer> mSelectedCheckboxes = new ArrayList<Integer>();

    long startTime;
	
	SharedPreferences mPreferences;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main_configuration);
		
		mPreferences = Prefs.get(this);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(
		            AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		setResult(Activity.RESULT_CANCELED);
		
		long birth = mPreferences.getLong("birth", -1);
		
		btnDateOfBirth = (DateTimeButton) findViewById(R.id.btn_date_of_birth);
		btnTimeOfBirth = (DateTimeButton) findViewById(R.id.btn_time_of_birth);
		btnTimeOfBirth.setType(DateTimeButton.TYPE_TIME);
        startTime = System.currentTimeMillis();
		
		if(birth != -1) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(birth);
			btnDateOfBirth.setDateTime(calendar);
			btnTimeOfBirth.setDateTime(calendar);
		}
		
		btnOk = (Button) findViewById(R.id.ok);
		btnOk.setOnClickListener(this);
		
		chYear = (CheckBox) findViewById(R.id.ch_years);
		chMonth = (CheckBox) findViewById(R.id.ch_months);
		chWeek = (CheckBox) findViewById(R.id.ch_weeks);
		chDay = (CheckBox) findViewById(R.id.ch_days);
		chHour = (CheckBox) findViewById(R.id.ch_hours);
		
		
		
	    
	}
	
	public void checkboxClicked(View v) {
		if (((CheckBox) v).isChecked() && mSelectedCheckboxes.size() >= getWidgetWidth()) {
			((CheckBox) v).setChecked(false);
	        Toast.makeText(this, String.format(getString(R.string.txt_too_many_selected), getWidgetWidth()), Toast.LENGTH_SHORT).show();
	    } else {
			mSelectedCheckboxes.clear();
			
		    if(chYear.isChecked()) mSelectedCheckboxes.add(YEAR);
		    if(chMonth.isChecked()) mSelectedCheckboxes.add(MONTH);
		    if(chWeek.isChecked()) mSelectedCheckboxes.add(WEEK);
		    if(chDay.isChecked()) mSelectedCheckboxes.add(DAY);
		    if(chHour.isChecked()) mSelectedCheckboxes.add(HOUR);
	    }
	}

	
	@Override
	public void onClick(View v) {
		if(v == btnOk) {

            long buttonTime = btnDateOfBirth.getDateTime().getTimeInMillis();

			if(startTime - 2000  <buttonTime) {
                Toast.makeText(this, getText(R.string.txt_invalid_birth_date), Toast.LENGTH_LONG).show();
                return;

            }

            if(mSelectedCheckboxes.size() == 0) {
                Toast.makeText(this, getText(R.string.txt_invalid_checkboxes), Toast.LENGTH_LONG).show();
                return;
            }
			
			SharedPreferences.Editor editor = mPreferences.edit();
			
			Calendar date = btnDateOfBirth.getDateTime();
			Calendar time = btnTimeOfBirth.getDateTime();
			date.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
			date.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
			
			editor.putLong("birth", date.getTimeInMillis());
			editor.putLong("birth_" + mAppWidgetId, date.getTimeInMillis());
			editor.putInt("format_size_" + mAppWidgetId, mSelectedCheckboxes.size());
            editor.putInt("widget_size_" + mAppWidgetId, getWidgetWidth());
			int i = 0;
			for(Integer format : mSelectedCheckboxes) {
				editor.putInt("format_" + i + "_" + mAppWidgetId, format);
				i++;
			}

            editor.commit();

			//sets default colors
			Prefs.setDefaultValues(this);
			

			
			Intent intent;
			intent = new Intent(this, getProviderClass());
			
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,new int[] {mAppWidgetId});
			sendBroadcast(intent);
			
//			AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
//			RemoteViews views = new RemoteViews(this.getPackageName(),
//					R.layout.main_widget);
//			appWidgetManager.updateAppWidget(mAppWidgetId, views);
			
			Intent resultValue = new Intent();
			resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
			setResult(RESULT_OK, resultValue);
			finish();
		}
	}

    protected abstract Class getProviderClass();

    protected abstract  int getWidgetWidth();


}
