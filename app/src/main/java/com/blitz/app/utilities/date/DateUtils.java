package com.blitz.app.utilities.date;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by mrkcsc on 9/25/14. Copyright 2014 Blitz Studios
 */
public class DateUtils {

    // region Public Methods
    // ============================================================================================================

    /**
     * Fetch date with GMT time zone.
     *
     * @return Date object in GMT time zone.
     */
    @SuppressWarnings("unused")
    public static Date getDateInGMT() {

        // GMT date format.
        SimpleDateFormat dateFormatGmt = getSimpleDateFormat();

        // Set a GMT time zone.
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        // Local time zone format.
        SimpleDateFormat dateFormatLocal = getSimpleDateFormat();

        try {

            // Parse current date into the GMT format.
            return dateFormatLocal.parse(dateFormatGmt.format(new Date()));

        } catch (ParseException ignored) { }

        return null;
    }

    /**
     * Given a date json string, convert
     * into associated GMT object.
     *
     * @param date Date json.
     *
     * @return Date object.
     */
    @SuppressWarnings("unused")
    public static Date getDateInGMT(String date) {

        // Create a special type of date format.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {

            return simpleDateFormat.parse(date);

        } catch (ParseException ignored) { }

        return null;
    }

    /**
     * Create a new date with specified offset in milliseconds.
     *
     * @param targetDate Target date.
     * @param offset Offset in milliseconds.
     *
     * @return New date with offset applied.
     */
    @SuppressWarnings("unused")
    public static Date getDateWithOffsetInMilliseconds(Date targetDate, Long offset) {

        if (targetDate == null) {

            return null;
        }

        return new Date(targetDate.getTime() + offset);
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

    /**
     * Fetch date object as json readable string.
     *
     * @param date GMT date.
     *
     * @return Json string.
     */
    @SuppressWarnings("unused")
    public static String getDateAsString(Date date) {

        // Format accepted by Blitz database.
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

        return formatter.format(date);
    }

    // endregion

    // region Private Methods
    // ============================================================================================================

    /**
     * Get a standard simple date format.
     *
     * @return Date format.
     */
    private static SimpleDateFormat getSimpleDateFormat() {

        return new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss.SSS");
    }

    // endregion
}