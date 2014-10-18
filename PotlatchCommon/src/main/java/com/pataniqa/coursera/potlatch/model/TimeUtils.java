package com.pataniqa.coursera.potlatch.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Utility methods for dealing with time stamps.
 */
public class TimeUtils {
    
    private static final String DATE_FORMAT = "yyyy-MMM-dd HH:mm:ss";
    private static final String TIME_ZONE = "GMT";
    
    private static SimpleDateFormat getDateFormat() {
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        return dateFormatGmt;
    }
    
    /**
     * Convert a Date to a String.
     * 
     * @param date The Date.
     * @return The String.
     */
    public static final String toString(Date date) {
        return getDateFormat().format(date);
    }
    
    /**
     * Convert a String to a Date.
     * 
     * @param string The String.
     * @return The Date.
     */
    public static final Date toDate(String string) {
        try {
            return getDateFormat().parse(string);
        } catch (ParseException e) {
            return new Date();
        }
    }
    
    /**
     * Convert a date to a time stamp (long).
     *  
     * @param date The date.
     * @return The time stamp.
     */
    public static final long toLong(Date date) {
        return date.getTime();
    }
    
    /**
     * Convert a a time stamp (long) to a date.
     * 
     * @param date The time stamp.
     * @return The date.
     */
    public static final Date toDate(long date) {
        return new Date(date);
    }
    
}
