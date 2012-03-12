package com.pj.app.youragewidget;

import java.util.Date;

import android.util.Log;

public class Tools {
	public static long getYears(long newer, long older) {
		Date newerDate = new Date(newer);
		Date olderDate = new Date(older);
		
		int newerYears = newerDate.getYear();
		int olderYears = olderDate.getYear();
		
		return newerYears - olderYears;
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
