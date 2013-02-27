package com.pj.app.youragewidget.ui;

import android.content.ComponentName;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.pj.app.youragewidget.*;

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
import android.widget.CompoundButton.OnCheckedChangeListener;

public class DetailsSettingsFragment extends Fragment implements
		OnCheckedChangeListener, OnClickListener {

	boolean isTransparent = false;

	DetailsActivity activity;

	View flTxtColor, flNumberColor, flStartColor, flMiddleColor, flEndColor,
			flBackgroundColor, flBorderColor;

    
    ViewAnimator mBackgroundAnimator, mBorderAnimator;

	Button btnTxtColor, btnNumberColor, btnStartColor, btnMiddleColor,
			btnEndColor, btnBackgroundColor, btnBorderColor;

	CheckBox chMakeBackgroundTransparent, chIsGradient, chWithBorder,
			chWithCorner;

	Spinner spOrientation;

	SharedPreferences mPrefs;

    
	int mNumberColor, mTextColor, mStartColor, mMiddleColor, mEndColor,
			mBackgroundColor, mBorderColor;

	int mAppWidgetId;

    final Handler mHandler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.main_details_config, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

        activity = (DetailsActivity) getActivity();

        Intent intent = activity.getIntent();
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);


		chMakeBackgroundTransparent = (CheckBox) getView().findViewById(
				R.id.ch_make_background_transparent);
		chMakeBackgroundTransparent.setOnCheckedChangeListener(this);

		chIsGradient = (CheckBox) getView().findViewById(R.id.ch_is_gradient);
		chIsGradient.setOnCheckedChangeListener(this);

		chWithBorder = (CheckBox) getView().findViewById(R.id.ch_with_border);
		chWithBorder.setOnCheckedChangeListener(this);

		chWithCorner = (CheckBox) getView().findViewById(R.id.ch_with_corner);
		chWithCorner.setOnCheckedChangeListener(this);

		flTxtColor = getView().findViewById(R.id.fl_txt_color);
		btnTxtColor = (Button) getView().findViewById(R.id.btn_txt_color);
		btnTxtColor.setOnClickListener(this);

		flNumberColor = getView().findViewById(R.id.fl_number_color);
		btnNumberColor = (Button) getView().findViewById(R.id.btn_number_color);
		btnNumberColor.setOnClickListener(this);

		flStartColor = getView().findViewById(R.id.fl_start_color);
		btnStartColor = (Button) getView().findViewById(R.id.btn_start_color);
		btnStartColor.setOnClickListener(this);

		flMiddleColor = getView().findViewById(R.id.fl_middle_color);
		btnMiddleColor = (Button) getView().findViewById(R.id.btn_middle_color);
		btnMiddleColor.setOnClickListener(this);

		flEndColor = getView().findViewById(R.id.fl_end_color);
		btnEndColor = (Button) getView().findViewById(R.id.btn_end_color);
		btnEndColor.setOnClickListener(this);

		flBackgroundColor = getView().findViewById(R.id.fl_background_color);
		btnBackgroundColor = (Button) getView().findViewById(
				R.id.btn_background_color);
		btnBackgroundColor.setOnClickListener(this);

		flBorderColor = getView().findViewById(R.id.fl_border_color);
		btnBorderColor = (Button) getView().findViewById(R.id.btn_border_color);
		btnBorderColor.setOnClickListener(this);

        
        mBackgroundAnimator = (ViewAnimator) getView().findViewById(R.id.background_animator);
        mBackgroundAnimator.setMeasureAllChildren(false);
        mBorderAnimator = (ViewAnimator) getView().findViewById(R.id.border_animator);
		mBorderAnimator.setMeasureAllChildren(false);

		spOrientation = (Spinner) getView().findViewById(R.id.sp_orientation);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity,
				android.R.layout.simple_spinner_item, getResources()
						.getStringArray(R.array.orientations));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spOrientation.setAdapter(adapter);

		mPrefs = Prefs.get(activity);



		setColorFromPrefs();

		setFlBackgrounds();

        boolean test = mPrefs.getBoolean(Prefs.WITH_BORDER + mAppWidgetId, mPrefs.getBoolean(Prefs.WITH_BORDER, false));

        chIsGradient.setChecked(mPrefs.getBoolean(Prefs.IS_GRADIENT + mAppWidgetId, mPrefs.getBoolean(Prefs.IS_GRADIENT, false)));
        chWithBorder.setChecked(test);



        showHideBorderContainer();
        showHideGradientContainer();

	}

	private void setColorFromPrefs() {
		mNumberColor = Prefs.getInt(mPrefs, Prefs.NUMBER_COLOR, mAppWidgetId);
		mTextColor = Prefs.getInt(mPrefs, Prefs.TEXT_COLOR, mAppWidgetId);
		mBackgroundColor = Prefs.getInt(mPrefs, Prefs.BACKGROUND_COLOR, mAppWidgetId);
		mBorderColor = Prefs.getInt(mPrefs, Prefs.BORDER_COLOR, mAppWidgetId);
		mEndColor = Prefs.getInt(mPrefs, Prefs.END_COLOR, mAppWidgetId);
		mMiddleColor = Prefs.getInt(mPrefs, Prefs.MIDDLE_COLOR, mAppWidgetId);
		mStartColor = Prefs.getInt(mPrefs, Prefs.START_COLOR, mAppWidgetId);

	}

	private void setFlBackgrounds() {
		flTxtColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mTextColor));
		flNumberColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mNumberColor));
		flBackgroundColor.setBackgroundDrawable(Tools
				.getColorBackgroundDrawable(this.getActivity(),
						mBackgroundColor));
		flBorderColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mBorderColor));
		flEndColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mEndColor));
		flMiddleColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mMiddleColor));
		flStartColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(
				this.getActivity(), mStartColor));
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView == chMakeBackgroundTransparent) {
			if (isTransparent) {
				isTransparent = false;
			} else {
				isTransparent = true;
			}
			makeBackgroundTransparent();
		} else if (buttonView == chIsGradient) {
			Prefs.setValue(mPrefs, Prefs.IS_GRADIENT + mAppWidgetId, chIsGradient.isChecked());
			showHideGradientContainer();
		} else if (buttonView == chWithBorder) {
			Prefs.setValue(mPrefs, Prefs.WITH_BORDER + mAppWidgetId, chWithBorder.isChecked());
			showHideBorderContainer();
		} else if (buttonView == chWithCorner) {
			Prefs.setValue(mPrefs, Prefs.WITH_CORNER + mAppWidgetId, chWithCorner.isChecked());
		}
	}

	private void showHideGradientContainer() {
        if(chIsGradient.isChecked()) {
            mBackgroundAnimator.setDisplayedChild(1);
        } else {
            mBackgroundAnimator.setDisplayedChild(0);
        }
        mBackgroundAnimator.showNext();

        setWidgetColors();
	}

	private void showHideBorderContainer() {
        if(chWithBorder.isChecked()) {
            mBorderAnimator.setDisplayedChild(1);
        } else {
            mBorderAnimator.setDisplayedChild(0);
        }
		mBorderAnimator.showNext();

        setWidgetColors();
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
		} else if (v == btnNumberColor) {
			color = mNumberColor;
			listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					ColorPickerDialog spDialog = (ColorPickerDialog) dialog;
					setNumberColor(spDialog.getColor());
				}
			};
		} else if( v == btnBackgroundColor) {
            color = mBackgroundColor;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ColorPickerDialog spDialog = (ColorPickerDialog) dialogInterface;
                    setBackgroundColor(spDialog.getColor());
                }
            };
        } else if(v == btnBorderColor) {
            color = mBorderColor;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ColorPickerDialog spDialog = (ColorPickerDialog) dialogInterface;
                    setBorderColor(spDialog.getColor());
                }
            };
        } else if(v == btnEndColor) {
            color = mEndColor;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ColorPickerDialog spDialog = (ColorPickerDialog) dialogInterface;
                    setEndColor(spDialog.getColor());
                }
            };
        } else if(v == btnMiddleColor) {
            color = mMiddleColor;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ColorPickerDialog spDialog = (ColorPickerDialog) dialogInterface;
                    setMiddleColor(spDialog.getColor());
                }
            };
        } else if(v == btnStartColor) {
            color = mStartColor;
            listener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ColorPickerDialog spDialog = (ColorPickerDialog) dialogInterface;
                    setStartColor(spDialog.getColor());
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

    private void setStartColor(int color) {
        mStartColor = color;
        flStartColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity,mStartColor));
        
        setWidgetColors();
    }

    private void setMiddleColor(int color) {
        mMiddleColor = color;
        flMiddleColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mMiddleColor));
        
        setWidgetColors();
    }

    private void setEndColor(int color) {
        mEndColor = color;
        flEndColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mEndColor));

        setWidgetColors();
    }

    private void setBorderColor(int color) {
        mBorderColor = color;
        flBorderColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mBorderColor));

        setWidgetColors();
    }

    private void setBackgroundColor(int color) {
        mBackgroundColor = color;
        flBackgroundColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mBackgroundColor));

        setWidgetColors();
    }

	protected void setNumberColor(int color) {
		mNumberColor = color;
		flNumberColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mNumberColor));

		setWidgetColors();
	}

	protected void setTextColor(int color) {
		mTextColor = color;
		flTxtColor.setBackgroundDrawable(Tools.getColorBackgroundDrawable(activity, mTextColor));

		setWidgetColors();
	}

	private void savePrefs() {
		SharedPreferences.Editor editor = mPrefs.edit();
		// sets default colors
		editor.putInt(Prefs.NUMBER_COLOR+ mAppWidgetId, mNumberColor);
		editor.putInt(Prefs.TEXT_COLOR+ mAppWidgetId, mTextColor);
        editor.putBoolean(Prefs.IS_GRADIENT+ mAppWidgetId, chIsGradient.isChecked());
        editor.putBoolean(Prefs.WITH_BORDER+ mAppWidgetId, chWithBorder.isChecked());
        editor.putBoolean(Prefs.WITH_CORNER+ mAppWidgetId, chWithCorner.isChecked());
        editor.putInt(Prefs.BACKGROUND_COLOR+ mAppWidgetId, mBackgroundColor);
        editor.putInt(Prefs.BORDER_COLOR+ mAppWidgetId, mBorderColor);
        editor.putInt(Prefs.END_COLOR+ mAppWidgetId, mEndColor);
        editor.putInt(Prefs.MIDDLE_COLOR+ mAppWidgetId, mMiddleColor);
        editor.putInt(Prefs.START_COLOR+ mAppWidgetId, mStartColor);
        editor.putInt(Prefs.GRADIENT_TYPE+ mAppWidgetId, spOrientation.getSelectedItemPosition());
		editor.commit();


        boolean test = chWithBorder.isChecked();
	}

	private void setWidgetColors() {
		savePrefs();

		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(activity);
//		RemoteViews views = new RemoteViews(activity.getPackageName(),
//				R.layout.main_widget);
//		WidgetProvider.setWidgetColors(activity, views, mPrefs, mAppWidgetId, mPrefs.getInt("widget_size_" + mAppWidgetId,1));
//		appWidgetManager.updateAppWidget(mAppWidgetId, views);
        //in your activity


        Intent intent = new Intent(WidgetProvider.ACTION_UPDATE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[] {mAppWidgetId});
        activity.sendBroadcast(intent);
//        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, LargeWidgetProvider.class));
//        if (appWidgetIds.length > 0) {
//            new LargeWidgetProvider().onUpdate(activity, appWidgetManager, appWidgetIds);
//        }
//        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, MediumWidgetProvider.class));
//        if (appWidgetIds.length > 0) {
//            new MediumWidgetProvider().onUpdate(activity, appWidgetManager, appWidgetIds);
//        }
//
//        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, MinimalWidgetProvider.class));
//        if (appWidgetIds.length > 0) {
//            new MinimalWidgetProvider().onUpdate(activity, appWidgetManager, appWidgetIds);
//        }
//
//        appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(activity, SmallWidgetProvider.class));
//        if (appWidgetIds.length > 0) {
//            new SmallWidgetProvider().onUpdate(activity, appWidgetManager, appWidgetIds);
//        }
	}

    

}
