package com.autentia.tomodoro.custom.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeFormatter {
	
	public static String FINAL_HOUR = "00:00";

	final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
	
	public String toString(Date date){
		
		return dateFormat.format(date);
	}
}