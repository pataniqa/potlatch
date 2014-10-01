package com.pataniqa.coursera.potlatch.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TimeUtils {
    
    private static final String DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";
    private static final String TIME_ZONE = "GMT";
    
    private static SimpleDateFormat getDateFormat() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return dateFormatGmt;
    }
    
    public static final String toString(Date date) {
        return getDateFormat().format(date);
    }
    
    public static final Date toDate(String string) {
        try {
            return getDateFormat().parse(string);
        } catch (ParseException e) {
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
