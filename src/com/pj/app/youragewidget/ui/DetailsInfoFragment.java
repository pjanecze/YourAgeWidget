package com.pj.app.youragewidget.ui;

import java.util.Calendar;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.MutablePeriod;
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
		tvDays, tvTypeDays, tvHours, tvTypeHours, tvMonthsBd, 
		tvTypeMonthsBd, tvWeeksBd, tvTypeWeeksBd,
		tvDaysBd, tvTypeDaysBd, tvHoursBd, tvTypeHoursBd,
		tvYearsOm, tvTypeYearsOm,
		tvDaysOm, tvTypeDaysOm;
	
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
		
		tvMonthsBd = (TextView) getView().findViewById(R.id.tv_months_bd);
		tvTypeMonthsBd = (TextView) getView().findViewById(R.id.tv_type_months_bd);
		tvWeeksBd = (TextView) getView().findViewById(R.id.tv_weeks_bd);
		tvTypeWeeksBd = (TextView) getView().findViewById(R.id.tv_type_weeks_bd);
		tvDaysBd = (TextView) getView().findViewById(R.id.tv_days_bd);
		tvTypeDaysBd = (TextView) getView().findViewById(R.id.tv_type_days_bd);
		tvHoursBd = (TextView) getView().findViewById(R.id.tv_hours_bd);
		tvTypeHoursBd = (TextView) getView().findViewById(R.id.tv_type_hours_bd);
		
		tvYearsOm = (TextView) getView().findViewById(R.id.tv_years_om);
		tvTypeYearsOm = (TextView) getView().findViewById(R.id.tv_type_years_om);
		tvDaysOm = (TextView) getView().findViewById(R.id.tv_days_om);
		tvTypeDaysOm = (TextView) getView().findViewById(R.id.tv_type_days_om);
		
		Intent intent = getActivity().getIntent();
		int mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, 
				AppWidgetManager.INVALID_APPWIDGET_ID);
		
		SharedPreferences preferences = Prefs.get(getActivity());
		
		long dateBirth = preferences.getLong("birth_" + mAppWidgetId, 0);
		
		setLiveViews(dateBirth);
		
		setBdViews(dateBirth);
		
		setOmViews(dateBirth);
	}
	
	
	private void setLiveViews(long dateBirth) {
		
		
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
	
	
	private void setBdViews(long dateBirth) {
		
		DateTime dateBirthTime = new DateTime(dateBirth);
		
		DateTime now = new DateTime();
		
		Period period = new Period(dateBirthTime, now);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(dateBirth);
		calendar.add(Calendar.YEAR, period.getYears());
		
		if(System.currentTimeMillis() > calendar.getTimeInMillis()) {
			calendar.add(Calendar.YEAR, 1);
		}
		
		dateBirthTime = new DateTime(calendar.getTimeInMillis());
		
		period = new Period(now, dateBirthTime);
		
		int years = period.getYears();
		
		int months = period.getMonths();
		if(months <= 1) {
			tvTypeMonthsBd.setText(R.string.txt_month);
		} else {
			tvTypeMonthsBd.setText(R.string.txt_months);
		}
		tvMonthsBd.setText(String.valueOf(months + 12*years));
		
		int weeks = period.getWeeks();
		if(weeks <= 1) {
			tvTypeWeeksBd.setText(R.string.txt_week);
		} else {
			tvTypeWeeksBd.setText(R.string.txt_weeks);
		}
		tvWeeksBd.setText(String.valueOf(weeks));
		
		int days = period.getDays();
		if(years <= 1) {
			tvTypeDaysBd.setText(R.string.txt_day);
		} else {
			tvTypeDaysBd.setText(R.string.txt_days);
		}
		tvDaysBd.setText(String.valueOf(days));
		
		int hours = period.getHours();
		if(hours <= 1) {
			tvTypeHoursBd.setText(R.string.txt_hour);
		} else {
			tvTypeHoursBd.setText(R.string.txt_hours);
		}
		tvHoursBd.setText(String.valueOf(hours));
		
	}
	//122 years and 164 days
	private void setOmViews(long dateBirth) {
		DateTime dateBirthTime = new DateTime(dateBirth);
		DateTime now = new DateTime();
		
		Period birth = new Period(dateBirthTime, now);
		
		DateTime oldestBirth = new DateTime(1875, 1, 21, 0, 0);
		DateTime oldestDeath = new DateTime(1997, 7, 4, 0, 0);
		Period oldestPeriod = new Period(oldestBirth, oldestDeath);
		
		int years = oldestPeriod.getYears() - birth.getYears();
		int months = oldestPeriod.getMonths() - birth.getMonths();
		if(months <0) {
			years--;
			months = 12 - months;
		}
		int weeks = oldestPeriod.getWeeks() - birth.getWeeks();
		if(weeks <0) {
			months --;
			weeks = 4 - weeks;
			if(months <0) {
				years--;
				months = 12 - months;
			}
		}
		int days = (int) (oldestPeriod.getDays() - birth.getDays() + months * 30.436849 + weeks * 4.34812142);
		if(days <0) {
			years--;
			days = 366 - days;
		} else if(days >365) {
			years++;
			days -= 366;
		}
		
		if(years <= 1) {
			tvTypeYearsOm.setText(R.string.txt_year);
		} else {
			tvTypeYearsOm.setText(R.string.txt_years);
		}
		tvYearsOm.setText(String.valueOf(years));
		
		
		if(years <= 1) {
			tvTypeDaysOm.setText(R.string.txt_day);
		} else {
			tvTypeDaysOm.setText(R.string.txt_days);
		}
		tvDaysOm.setText(String.valueOf(days));
		
		
		
	}
	
}
