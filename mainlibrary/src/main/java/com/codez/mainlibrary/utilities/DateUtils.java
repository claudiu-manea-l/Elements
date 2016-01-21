package com.codez.mainlibrary.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by eptron on 2015.03.10..
 * Utility class used for working with Dates (java.util.Date)
 */
public class DateUtils {

    //General DateFormat set for all dates within the app
    public static final SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat backendFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    public static final SimpleDateFormat shortFormat = new SimpleDateFormat("dd/MM");

    //General TimeFormat for the app
    public static final SimpleDateFormat timeFormat = new SimpleDateFormat("kk:mm");
    public static final SimpleDateFormat backendTimeFormat = new SimpleDateFormat("kk:mm:ss");

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

    public static String formatTime(String time) {
        if (time != null && !time.isEmpty())
            if (time.length() > 3 && !time.matches("([0-9]{2}):([0-9]{2})")) {
                try {
                    Date temp = backendTimeFormat.parse(time);
                    time = timeFormat.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        return time;
    }

    public static String getDateStringForDisplay(int year, int month, int day) {
        String monthS = month >= 10 ? month + "" : "0" + month;
        String dayS = day >= 10 ? day + "" : "0" + day;
        return dayS + "/" + monthS + "/" + year;
    }

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

    public static boolean isToday(String dateText) {
        try {
            Date date = getDateFromString(dateText);
            return isToday(date, new Date());
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isToday(String date, SimpleDateFormat format) {
        try {
            Date dateTemp = format.parse(date);
            return isToday(dateTemp, new Date());
        } catch (ParseException e) {
            return false;
        }
    }

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
        return dateFormat.format(date);
    }

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
        return dateFormat.parse(date);
    }

    public static String getLastMonthDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return formatDateForDisplay(cal.getTime());
    }

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

    public static int getWeekNumberFromDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }

    public static String[] getDaysString(boolean isShort) {
        SimpleDateFormat format;
        String type;
        if (isShort) {
            type = "EEE";
        } else {
            type = "EEEE";
        }
        format = new SimpleDateFormat(type, Locale.getDefault());
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

    public static String convertDisplayDate(String date) {
        if (date != null && !date.isEmpty())
            try {
                Date temp = displayFormat.parse(date);
                date = dateFormat.format(temp);
            } catch (ParseException e) {
                // e.printStackTrace();
            }
        return date;
    }

    public static String formatDateToDisplay(String date) {
        if (date != null && !date.isEmpty())
            try {
                Date temp = dateFormat.parse(date);
                date = displayFormat.format(temp);
            } catch (ParseException e) {
                // e.printStackTrace();
            }
        return date;
    }

    public static String formatDate(String date) {
        if (date != null && !date.isEmpty())
            if (date.length() > 12) {
                try {
                    Date temp = backendFormat.parse(date);
                    date = dateFormat.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        return date;
    }

    public static String formatDate(String date, SimpleDateFormat newFormat) {
        if (date != null && !date.isEmpty())
            if (date.length() > 12) {
                try {
                    Date temp = backendFormat.parse(date);
                    date = newFormat.format(temp);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        return date;
    }
}
