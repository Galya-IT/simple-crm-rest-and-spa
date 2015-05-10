package com.company.crm.utils;

public class StringUtils {
	
    public static boolean isNullOrEmpty(String value) {
    	boolean isNullOrEmpty = false;
    	if (value == null || value.isEmpty()) {
    		isNullOrEmpty = true; 
    	}
    	return isNullOrEmpty;
    }
}
