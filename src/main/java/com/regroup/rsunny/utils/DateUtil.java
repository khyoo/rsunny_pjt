package com.regroup.rsunny.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.groovy.parser.antlr4.util.StringUtils;

public class DateUtil {
	
	public static String getToday() {
		return getToday("yyyyMMdd");
 	}
	
	public static String getYm() {
		return getToday("yyyyMM");
 	}
	
	public static String getToday(String format) {
		if(StringUtils.isEmpty(format)) format = "yyyyMMdd";
		return LocalDate.now().format(DateTimeFormatter.ofPattern(format));
 	}

}
