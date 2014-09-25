package com.pataniqa.coursera.potlatch.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {
    
    private static final String DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";
    private static final String TIME_ZONE = "GMT";
    
    public static final String toString(Date date) {
        // TODO fixme
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return dateFormatGmt.format(date);
    }
    
    public static final Date toDate(String string) {
        // TODO fixme
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT);
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        try {
            return dateFormatGmt.parse(string);
        } catch (ParseException e) {
            // TODO fix me 
            return new Date();
        }
    }
    
    public static final long toLong(Date date) {
        return date.getTime();
    }
    
    public static final Date toDate(long date) {
        return new Date(date);
    }
    
}
