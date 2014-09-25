package com.blitz.app.utilities.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mrkcsc on 9/25/14. Copyright 2014 Blitz Studios
 */
public class DateUtils {

    /**
     * Fetch date with GMT time zone.
     *
     * @return Date object in GMT time zone.
     */
    @SuppressWarnings("unused")
    public static Date getDateInGMT() {

        // GMT date format.
        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        // Set a GMT time zone.
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Local time zone format.
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");

        try {

            // Parse current date into the GMT format.
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));

        } catch (ParseException ignored) { }

        return null;
    }

    /**
     * Fetch date with GMT time zone in milliseconds.
     *
     * @return Milliseconds.
     */
    @SuppressWarnings("unused")
    public static Long getDateInGMTMilliseconds() {

        return getDateInGMT().getTime();
    }

    /**
     * Given a GMT date, get time since that
     * date in seconds.
     *
     * @param date GMT date.
     *
     * @return Seconds elapsed.
     */
    @SuppressWarnings("unused")
    public static int getTimeSinceDateInGMTAsSeconds(Date date) {

        // Fetch seconds elapsed.
        return (int) (getTimeSinceDateInGMTAsMilliseconds(date) / 1000);
    }

    /**
     * Given a GMT date, get time since that date
     * in milliseconds.
     *
     * @param date GMT date.
     *
     * @return Milliseconds elapsed.
     */
    @SuppressWarnings("unused")
    public static long getTimeSinceDateInGMTAsMilliseconds(Date date) {

        // Error condition.
        if (date == null) {

            return 0;
        }

        // Fetch milliseconds elapsed.
        return Math.abs(getDateInGMTMilliseconds() - date.getTime());
    }
}