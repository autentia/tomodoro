package com.autentia.custom.formatter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeFormatter {

	final SimpleDateFormat dateFormat = new SimpleDateFormat("H:mm:ss");
	
	public String toString(Date date){
		
		return dateFormat.format(date);
	}
}