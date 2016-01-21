package com.codez.mainlibrary.utilities;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by Claudiu on 2015.03.10..
 * Utility class used for working with Dates (java.util.Date)
 */
public class DateUtils {

    /***********************************************************************************************
     * *********************  General DateFormat set for all dates within the app *******************
     **********************************************************************************************/
    //Format the date should be displayed in within the app
    public static final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
    //Format needed by SQLite to be able to sort properly
    public static final SimpleDateFormat appDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    //Define here the format that is comming from the backend
    public static final SimpleDateFormat backendDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    //Short format of the Date
    public static final SimpleDateFormat shortFormat = new SimpleDateFormat("dd/MM");
    //General TimeFormat for the app
    public static final SimpleDateFormat appTimeFormat = new SimpleDateFormat("kk:mm");
    public static final SimpleDateFormat backendTimeFormat = new SimpleDateFormat("kk:mm:ss");

    /**
     * Changes format of a given date(represented as String) to the new format given
     *
     * @param date       String representation af the date
     * @param fromFormat the format the date is in
     * @param toFormat   the format the date should be in
     * @return the date as String in the newFormat
     */
    public static String changeFormat(String date, SimpleDateFormat fromFormat, SimpleDateFormat toFormat) {
        if (date != null && !date.isEmpty()) {
            try {
                Date temp = fromFormat.parse(date);
                date = toFormat.format(temp);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    /**
     * Converts the date (String representation in display Format) to appDateFormat
     *
     * @param date date in String representation in format diplayFormat
     * @return date in String representation of appDateFormat
     */
    public static String convertDisplayDate(String date) {
        return changeFormat(date, displayFormat, appDateFormat);
    }

    /**
     * Converts the date (String representation in appDateFormat) to displayFormat
     *
     * @param date date in String representation in format appDateFormat
     * @return date in String representation of displayFormat
     */
    public static String formatDateToDisplay(String date) {
        return changeFormat(date, appDateFormat, displayFormat);
    }

    /**
     * Formats the date(String representation) from the backend format to appDateFormat
     *
     * @param date the String represenatation of the date
     * @return the new date as String in the appDateFormat
     */
    public static String formatDate(String date) {
        if (date.length() > 12) {
            return changeFormat(date, backendDateFormat, appDateFormat);
        }
        return date;
    }


    /**
     * Format a String representation of a date to the given DateFormat
     *
     * @param date      the String represenatation of the date
     * @param newFormat the format the date should be in
     * @return the new date as String in the new format
     */
    public static String formatDate(String date, SimpleDateFormat newFormat) {
        if (date.length() > 12) {
            return changeFormat(date, backendDateFormat, newFormat);
        }
        return date;
    }

    /**
     * Formats the time from BackendTimeFormat to the AppTimeFormat
     *
     * @param time the String representation of the time
     * @return the time in the appTimeFormat
     */
    public static String formatTime(String time) {
        if (time.length() > 3 && !time.matches("([0-9]{2}):([0-9]{2})")) {
            return changeFormat(time, backendTimeFormat, appTimeFormat);
        }
        return time;
    }

    /**
     * Builds a String representation of the given date as DAY/MONTH/YEAR (mostly used for displaying
     * the date, since the user is more comfortable with the D/M/Y format)
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return new date in String representation (DAY/MONTH/YEAR)
     */
    public static String getDateStringForDisplay(int year, int month, int day) {
        String monthS = month >= 10 ? month + "" : "0" + month;
        String dayS = day >= 10 ? day + "" : "0" + day;
        return dayS + "/" + monthS + "/" + year;
    }

    /**
     * Builds a String representation of the given date as YEAR-MONTH-DAY (mostly used for SQLite
     * since in this format it is possible for SQLite to sort our dates
     *
     * @param year  the year
     * @param month the month
     * @param day   the day
     * @return new date in String representation (YEAR-MONTH-DAY)
     */
    public static String getDateString(int year, int month, int day) {
        String monthS = month >= 10 ? month + "" : "0" + month;
        String dayS = day >= 10 ? day + "" : "0" + day;
        return year + "-" + monthS + "-" + dayS;
    }

    /**
     * Gets the same date but with time 00:00:00 so there is no conflict
     * when comparing dates since compareTo is done by comparing milliseconds
     *
     * @param date the date to be changed
     * @return date with 00:00:00 time
     */
    public static Date getStartOfDay(Date date) {
        Calendar temp = Calendar.getInstance();
        Calendar returnDate = Calendar.getInstance();
        returnDate.setTimeInMillis(0);
        temp.setTime(date);
        returnDate.set(temp.get(Calendar.YEAR),
                temp.get(Calendar.MONTH),
                temp.get(Calendar.DATE));
        return returnDate.getTime();
    }

    /**
     * Checks if the 2 dates are the same
     *
     * @param date1 date to be checked against
     * @param date2 date to check with
     */
    public static boolean isToday(Date date1, Date date2) {
        date1 = getStartOfDay(date1);
        date2 = getStartOfDay(date2);
        return (date1.compareTo(date2) == 0) ? true : false;
    }

    /**
     * Checks if the given date as a String matches the today's date
     *
     * @param dateText date in String representation
     * @return true/false
     */
    public static boolean isToday(String dateText) {
        try {
            Date date = getDateFromString(dateText);
            return isToday(date, new Date());
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if the given date as a String matches the today's date
     *
     * @param date   date in String representation
     * @param format the format of the Date
     * @return true/false
     */
    public static boolean isToday(String date, SimpleDateFormat format) {
        try {
            Date dateTemp = format.parse(date);
            return isToday(dateTemp, new Date());
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Checks if the date is in the future (>today) (format has to be in appDateFormat)
     *
     * @param dateString date in String representation
     * @return true/false
     */
    public static boolean isFuture(String dateString) {
        Date date = null;
        try {
            date = getDateFromString(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        Date now = new Date();
        now = getStartOfDay(now);
        return date.after(now);
    }

    /**
     * Checks if the date is in the past (<today) (format has to be in appDateFormat)
     *
     * @param dateString date in String representation
     * @return true/false
     */
    public static boolean isPastDate(String dateString) {
        Date date = null;
        try {
            date = getDateFromString(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.AM_PM, Calendar.AM);
        Date temp = cal.getTime();
        return date.before(temp);
    }

    /**
     * Checks if tomorrowDate precedes date by 1 day
     *
     * @param date         date to be checked against
     * @param tomorrowDate date to check with
     */
    public static boolean isTomorrow(Date date, Date tomorrowDate) {
        boolean returnVal = false;
        date = getStartOfDay(date);
        tomorrowDate = getStartOfDay(tomorrowDate);
        if (date.compareTo(tomorrowDate) >= 0) returnVal = false;
        else {
            Calendar tomorrow = Calendar.getInstance();
            tomorrow.setTimeInMillis(0);
            tomorrow.setTime(date);
            tomorrow.add(Calendar.DAY_OF_YEAR, 1);
            if (tomorrowDate.compareTo(tomorrow.getTime()) == 0) returnVal = true;
        }
        return returnVal;
    }

    /**
     * Gets the String of the specified string as yyyy-mm-dd
     *
     * @param date to be converted to string
     * @return the string format of the date
     */
    public static String getDateString(Date date) {
        return appDateFormat.format(date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateForDisplay(Date date) {
        return displayFormat.format(date);
    }

    /**
     * Gets the Date out of a string
     *
     * @param date String to be converted to Date
     * @return The Date of the given strin
     * @throws ParseException If the date is not formated as yyyy-mm-dd
     */
    public static Date getDateFromString(String date) throws ParseException {
        return appDateFormat.parse(date);
    }

    /**
     * Minuses from the current date 1 month and returns it in appDisplayFormat
     *
     * @return Returns a String representation of the date (-1 month) in appDisplayFormat
     */
    public static String getLastMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return formatDateForDisplay(cal.getTime());
    }

    /**
     * @return Today's date in appDisplayFormat
     */
    public static String getTodaysDate() {
        Calendar temp = Calendar.getInstance();
        return formatDateForDisplay(temp.getTime());
    }

    /**
     * Gets the next day from now
     *
     * @return Date object of tomorrow with time 00:00:00
     */
    public static Date getTomorrowDates() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(0);
        tomorrow.setTime(new Date());
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return tomorrow.getTime();
    }

    /**
     * Gets the a random day from now (Span of 1 week)
     *
     * @return Date object of tomorrow with time 00:00:00
     */
    public static Date getAnotherDates() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(0);
        tomorrow.setTime(new Date());
        Random random = new Random();
        int plusDay = random.nextInt(6) + 1;
        tomorrow.add(Calendar.DAY_OF_YEAR, plusDay);
        return tomorrow.getTime();
    }

    /**
     * Gets the String format of tomorrows date
     *
     * @return Tomorrows date in String format yyyy-mm-dd
     */
    public static String getTomorrowDatesString() {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.setTimeInMillis(0);
        tomorrow.setTime(new Date());
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        return getDateString(tomorrow.getTime());
    }

    /**
     * Converts the given time(in milliseconds) to "HH:MM:SS" format
     *
     * @param time given time in milliseconds
     * @return time in String format "HH:MM:SS"
     */
    public static String getTime(long time) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;

        long elapsedHours = time / hoursInMilli;
        time = time % hoursInMilli;
        long elapsedMinutes = time / minutesInMilli;
        time = time % minutesInMilli;

        long elapsedSeconds = time / secondsInMilli;
        String hours = elapsedHours < 10 ? "0" + elapsedHours : elapsedHours + "";
        String minutes = elapsedMinutes < 10 ? "0" + elapsedMinutes : elapsedMinutes + "";
        String seconds = elapsedSeconds < 10 ? "0" + elapsedSeconds : elapsedSeconds + "";
        return hours + ":" + minutes + ":" + seconds;
    }

    /**
     * Returns the week number of the given date
     *
     * @param date String representation of the date in appFormat
     * @return number of the week
     */
    public static int getWeekNumberFromDateString(String date) {
        Date mDate = null;
        try {
            mDate = getDateFromString(date);
        } catch (ParseException e) {
            //e.printStackTrace();
        }
        if (mDate != null) {
            getWeekNumberFromDate(mDate);
        }
        return 0;
    }

    /**
     * Returns the week number of the given date
     *
     * @param date in simple (@Date) Date format
     * @return number of the week
     */
    public static int getWeekNumberFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }


    /**
     * Gets the names of the week days of the Default Locale
     *
     * @param isShort if the names should be in short format (Ex: Mo,Tu) or full length
     * @return Array containing the names of the days (length = 7)
     */
    public static String[] getDaysString(boolean isShort) {
        return getDaysString(isShort, "");
    }

    /**
     * Returns the names of the week days of the given Locale
     *
     * @param isShort   if the names should be in short format (Ex: Mo,Tu) or full length
     * @param localeStr locale String (Ex: "en","us","nl","lv")
     * @return Array containing the names of the days (length = 7)
     */
    public static String[] getDaysString(boolean isShort, String localeStr) {
        Locale locale;
        if (TextUtils.isEmpty(localeStr)) {
            locale = Locale.getDefault();
        } else {
            locale = new Locale(localeStr);
        }
        SimpleDateFormat format;
        String type;
        if (isShort) {
            type = "EEE";
        } else {
            type = "EEEE";
        }
        format = new SimpleDateFormat(type, locale);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String[] temp = new String[7];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = format.format(cal.getTime());
            temp[i] = temp[i].substring(0, 1).toUpperCase() + temp[i].substring(1);
            cal.add(Calendar.DAY_OF_WEEK, 1);
        }
        return temp;
    }
}
