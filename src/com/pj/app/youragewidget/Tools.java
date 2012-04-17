package com.pj.app.youragewidget;

import java.util.Date;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.graphics.drawable.ShapeDrawable;
import android.util.Log;

public class Tools {
	
	
	//e.g. GradientDrawable.Orientation.TOP_BOTTOM
	public static GradientDrawable getShapeDrawable(int[] colors, Orientation orientation, int borderColor, int borderWidth, float cornerRadius) {
		GradientDrawable shape = new GradientDrawable(orientation, colors);
		shape.setStroke(borderWidth, borderColor);
		shape.setCornerRadius(cornerRadius);
		return shape;
	}
	
	public static long getYears(long newer, long older) {
		Date newerDate = new Date(newer);
		Date olderDate = new Date(older);
		
		int newerYears = newerDate.getYear();
		int olderYears = olderDate.getYear();
		
		return newerYears - olderYears;
	}

	public static Drawable getColorBackgroundDrawable(Context context, int mTextColor) {
		return Tools.getShapeDrawable(new int[] {mTextColor, mTextColor, mTextColor}, 
				GradientDrawable.Orientation.BOTTOM_TOP, 
				context.getResources().getColor(R.color.holo_blue_light), 
				2, 
				1);
	}
	
//	public static long getDays(long newer, long older) {
//		
//	}
//	public static long getHours(long newer, long older) {
//		
//	}
//	
//	public static long getWeeks(long newer, long older) {
//		
//	}
//	
//	public static long getMonths(long newer, long older) {
//		
//	}
}
