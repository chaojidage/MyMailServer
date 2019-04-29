package com.mail.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtil {
	
	public static String formatSeconds(long seconds) {
		String timeStr = seconds + " ��";
		if (seconds > 60) {
			long second = seconds % 60;
			long min = seconds / 60;
			timeStr = min + " �� " + second + " ��";
			if (min > 60) {
				min = (seconds / 60) % 60;
				long hour = (seconds / 60) / 60;
				timeStr = hour + " Сʱ " + min + " �� " + second + " ��";
				if (hour > 24) {
					hour = ((seconds / 60) / 60) % 24;
					long day = (((seconds / 60) / 60) / 24);
					timeStr = day + " �� " + hour + " Сʱ " + min + " �� " + second + " ��";
				}
			}
		}
		return timeStr;
	}
	
	public static String getNormalTime() {    
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss") ;    
        String time = format.format(new Date()) ;    
        return time;    
    }   
}
