package com.company.crm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static final SimpleDateFormat dateFormatBulgarian = new SimpleDateFormat("dd.MM.yyyy");
    private static final SimpleDateFormat dateFormatWestern = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String convertWesternToBulgarianDateFormat(String date) throws ParseException {
        Date dateParsed = dateFormatBulgarian.parse(date);
        String dateInWesternFormat = dateFormatWestern.format(dateParsed);
        
        return dateInWesternFormat;
    }

    public static String convertDateToBulgarianFormat(Date date) throws ParseException {
        String dateInBulgarianFormat = dateFormatBulgarian.format(date);
        
        return dateInBulgarianFormat;
    }

    public static String convertDateToCustomFormat(Date date, String datePattern) throws ParseException {
    	SimpleDateFormat dateFormatCustom = new SimpleDateFormat(datePattern);
        String dateInCustomFormat = dateFormatCustom.format(date);
        
        return dateInCustomFormat;
    }

    public static Date convertStringToDate(String dateAsString) throws ParseException {
        SimpleDateFormat dateFormat = dateFormatBulgarian;
        
        String dateFormatWesternPattern = "\\d{4}-\\d{2}-\\d{2}";
        
        if (dateAsString.matches(dateFormatWesternPattern)) {
            dateFormat = dateFormatWestern;
        }
        
        Date date = dateFormat.parse(dateAsString);
        
        return date;
    }
    
}
