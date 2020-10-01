package com.nubiform.photo.common;

import java.io.PrintWriter;
import java.io.StringWriter;

public class CommonUtils {
	
	public static String getPrintStackTrace(Exception e) {
		StringWriter errors = new StringWriter();
		e.printStackTrace(new PrintWriter(errors));
		
		return errors.toString();
	}
	
}