package com.pj.app.youragewidget.ui;

import com.pj.app.youragewidget.Prefs;
import com.pj.app.youragewidget.R;
import com.pj.app.youragewidget.WidgetProvider;

import afzkl.development.mColorPicker.ColorPickerDialog;
import afzkl.development.mColorPicker.views.ColorPickerView;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RemoteViews;

public class DetailsSettingsFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener {

	boolean isTransparent = false;

	DetailsActivity activity;

	View flTxtColor,  flNumberColor;

	Button btnTxtColor,  btnNumberColor;

	CheckBox chMakeBackgroundTransparent;

	SharedPreferences mPrefs;

	int mNumberColor, mTextColor;
	
	int mAppWidgetId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_details_config, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = (DetailsActivity) getActivity();

		chMakeBackgroundTransparent = (CheckBox) getView().findViewById(
				R.id.ch_make_background_transparent);
		chMakeBackgroundTransparent.setOnCheckedChangeListener(this);

		flTxtColor = getView().findViewById(R.id.fl_txt_color);
		btnTxtColor = (Button) getView().findViewById(R.id.btn_txt_color);
		btnTxtColor.setOnClickListener(this);

		flNumberColor = getView().findViewById(R.id.fl_number_color);
		btnNumberColor = (Button) getView().findViewById(R.id.btn_number_color);
		btnNumberColor.setOnClickListener(this);

		mPrefs = Prefs.get(activity);

		setColorFromPrefs();

		setFlBackgrounds();
		
		Intent intent = activity.getIntent();
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	private void setColorFromPrefs() {
		mNumberColor = mPrefs.getInt("number_color",
				getResources().getColor(R.color.holo_blue_light));
		mTextColor = mPrefs.getInt("text_color",
				getResources().getColor(R.color.holo_orange_light));
	}

	private void setFlBackgrounds() {
		flTxtColor.setBackgroundColor(mTextColor);
		flNumberColor.setBackgroundColor(mNumberColor);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isTransparent) {
			isTransparent = false;
		} else {
			isTransparent = true;
		}
		makeBackgroundTransparent();
	}

	private void makeBackgroundTransparent() {
		activity.makeBackgroundTransparent(isTransparent);
	}

	@Override
	public void onClick(View v) {

		DialogInterface.OnClickListener listener = null;
		int color = -1;
		if (v == btnTxtColor) {
			color = mTextColor;
			listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					ColorPickerDialog spDialog = (ColorPickerDialog) dialog;
					setTextColor(spDialog.getColor());
				}
			};
		}  else if (v == btnNumberColor) {
			color = mNumberColor;
			listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					ColorPickerDialog spDialog = (ColorPickerDialog) dialog;
					setNumberColor(spDialog.getColor());
				}
			};
		} 

		ColorPickerDialog dialog = new ColorPickerDialog(activity, color);
		dialog.setAlphaSliderVisible(true);
		dialog.setButton(AlertDialog.BUTTON_POSITIVE,
				getString(R.string.txt_set), listener);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE,
				getString(R.string.txt_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		dialog.show();
	}

	protected void setNumberColor(int color) {
		mNumberColor = color;
		flNumberColor.setBackgroundColor(mNumberColor);
		
		setWidgetColors();
	}

	protected void setTextColor(int color) {
		mTextColor = color;
		flTxtColor.setBackgroundColor(mTextColor);
		
		setWidgetColors();
	}

	private void savePrefs() {
		SharedPreferences.Editor editor = mPrefs.edit();
		//sets default colors
		editor.putInt("number_color", mNumberColor);
		editor.putInt("text_color", mTextColor);
		editor.commit();
		
	}
	
	private void setWidgetColors() {
		savePrefs();
		
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
		RemoteViews views = new RemoteViews(activity.getPackageName(),
				R.layout.main_widget);
		WidgetProvider.setWidgetColors(activity, views, mPrefs);
		appWidgetManager.updateAppWidget(mAppWidgetId, views);
	}

	
	
}
