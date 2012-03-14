package com.pj.app.youragewidget.ui;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Period;

import com.pj.app.youragewidget.Prefs;
import com.pj.app.youragewidget.R;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailsInfoFragment extends Fragment{

	TextView tvYears, tvTypeYears, tvMonths, tvTypeMonths, tvWeeks, tvTypeWeeks,
		tvDays, tvTypeDays, tvHours, tvTypeHours;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_details_info, container, false);
	}

	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		tvYears = (TextView) getView().findViewById(R.id.tv_years);
		tvTypeYears = (TextView) getView().findViewById(R.id.tv_type_years);
		tvMonths = (TextView) getView().findViewById(R.id.tv_months);
		tvTypeMonths = (TextView) getView().findViewById(R.id.tv_type_months);
		tvWeeks = (TextView) getView().findViewById(R.id.tv_weeks);
		tvTypeWeeks = (TextView) getView().findViewById(R.id.tv_type_weeks);
		tvDays = (TextView) getView().findViewById(R.id.tv_days);
		tvTypeDays = (TextView) getView().findViewById(R.id.tv_type_days);
		tvHours= (TextView) getView().findViewById(R.id.tv_hours);
		tvTypeHours = (TextView) getView().findViewById(R.id.tv_type_hours);
		
		setViews();
	}
	
	private void setViews() {
		Intent intent = getActivity().getIntent();
		int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID);
		
		SharedPreferences preferences = Prefs.get(getActivity());
		
		long dateBirth = preferences.getLong("birth_" + mAppWidgetId, 0);
		
		DateTime dateBirthTime = new DateTime(dateBirth);
		DateTime now = new DateTime();
		
		Period period = new Period(dateBirthTime, now);
		
		int years = period.getYears();
		if(years <= 1) {
			tvTypeYears.setText(R.string.txt_year);
		} else {
			tvTypeYears.setText(R.string.txt_years);
		}
		tvYears.setText(String.valueOf(years));
		
		int months = period.getMonths();
		if(months <= 1) {
			tvTypeMonths.setText(R.string.txt_month);
		} else {
			tvTypeMonths.setText(R.string.txt_months);
		}
		tvMonths.setText(String.valueOf(months));
		
		int weeks = period.getWeeks();
		if(weeks <= 1) {
			tvTypeWeeks.setText(R.string.txt_week);
		} else {
			tvTypeWeeks.setText(R.string.txt_weeks);
		}
		tvWeeks.setText(String.valueOf(weeks));
		
		int days = period.getDays();
		if(years <= 1) {
			tvTypeDays.setText(R.string.txt_day);
		} else {
			tvTypeDays.setText(R.string.txt_days);
		}
		tvDays.setText(String.valueOf(days));
		
		int hours = period.getHours();
		if(hours <= 1) {
			tvTypeHours.setText(R.string.txt_hour);
		} else {
			tvTypeHours.setText(R.string.txt_hours);
		}
		tvHours.setText(String.valueOf(hours));
		
	}
	
	
}
