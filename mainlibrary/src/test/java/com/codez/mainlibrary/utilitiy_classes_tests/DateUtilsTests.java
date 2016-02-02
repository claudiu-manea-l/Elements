package com.codez.mainlibrary.utilitiy_classes_tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.DateUtils;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.*;

/**
 * Created by Eptron on 1/21/2016.
 */
@SmallTest
public class DateUtilsTests {

    public Date mCurDate;
    public String mTestDate_AppDateFormat;
    public String mTestDate_DisplayFormat;
    public String mTestDate_BackendDateFormat;
    public String mTestTime_AppTimeFormat;
    public String mTestTime_BackendFormat;

    @Before
    public void initDummyDates() {
        mCurDate = new Date();
        mTestDate_AppDateFormat = DateUtils.appDateFormat.format(mCurDate);
        mTestDate_DisplayFormat = DateUtils.displayFormat.format(mCurDate);
        mTestDate_BackendDateFormat = DateUtils.backendDateFormat.format(mCurDate);
        mTestTime_AppTimeFormat = DateUtils.appTimeFormat.format(mCurDate);
        mTestTime_BackendFormat = DateUtils.backendTimeFormat.format(mCurDate);
    }

    @Test
    public void changeFormat_nulls() {
        String date = DateUtils.changeFormat(null, null, null);
        assertThat(date, isEmptyOrNullString());
    }

    @Test
    public void changeFormat_success() {
        String formattedDate = DateUtils.changeFormat(
                mTestDate_AppDateFormat, DateUtils.appDateFormat, DateUtils.displayFormat);
        assertThat(formattedDate, is(mTestDate_DisplayFormat));
    }

    @Test
    public void changeFormat_fail() {
        String formattedDate = DateUtils.changeFormat(
                "", DateUtils.appDateFormat, DateUtils.backendDateFormat
        );
        assertThat(formattedDate, is(""));
    }

    @Test
    public void convertDisplayDate_null() {
        String testDate = DateUtils.convertDisplayDate(null);
        assertThat(testDate, isEmptyOrNullString());
    }

    @Test
    public void convertDisplayDate_success() {
        String testDate = DateUtils.convertDisplayDate(mTestDate_DisplayFormat);
        assertThat(testDate, is(mTestDate_AppDateFormat));
    }

    @Test
    public void formatDateToDisplay_null() {
        String testDate = DateUtils.formatDateToDisplay(null);
        assertThat(testDate, isEmptyOrNullString());
    }

    @Test
    public void formatDateToDisplay_success() {
        String testDate = DateUtils.formatDateToDisplay(mTestDate_AppDateFormat);
        assertThat(testDate, is(mTestDate_DisplayFormat));
    }

    @Test
    public void formatDateWithDefaultFormat_null() {
        String formattedDate = DateUtils.formatDate("");
        assertThat(formattedDate, isEmptyOrNullString());
    }

    @Test
    public void formatDateWithDefaultFormat_success() {
        String formattedDate = DateUtils.formatDate(mTestDate_BackendDateFormat);
        assertThat(formattedDate, is(mTestDate_AppDateFormat));
    }

    @Test
    public void formatDateWithGivenFormat_success() {
        String formattedDate = DateUtils.formatDate(mTestDate_BackendDateFormat, DateUtils.displayFormat);
        assertThat(formattedDate, is(mTestDate_DisplayFormat));
    }

    @Test
    public void formatTime_null() {
        String testTime = DateUtils.formatTime("");
        assertThat(testTime, isEmptyOrNullString());
    }

    @Test
    public void formatTime_success() {
        String testTime = DateUtils.formatTime(mTestTime_BackendFormat);
        assertThat(testTime, is(mTestTime_AppTimeFormat));
    }

    @Test
    public void getDateStringToDisplay_success() {
        String date = DateUtils.getDateStringForDisplay(2016, 10, 21);
        assertThat(date, is("21/10/2016"));
    }

    @Test
    public void getDateStringToDisplay_onlyZeroes() {
        String date = DateUtils.getDateStringForDisplay(0, 0, 0);
        assertThat(date, is("00/00/0"));
    }

    @Test
    public void getDateStringToDisplay_smallMonthAndYear() {
        String date = DateUtils.getDateStringForDisplay(2016, 1, 1);
        assertThat(date, is("01/01/2016"));
    }

    @Test
    public void getStartOfDay_success() {
        Date date = DateUtils.getStartOfDay(new Date());
        DateUtils.appTimeFormat.setTimeZone(new SimpleTimeZone(1, ""));
        String time = DateUtils.appTimeFormat.format(date);
        assertThat(time, is("24:00"));
    }

    @Test
    public void isToday_testStringAndFormat() {
        boolean isToday = DateUtils.isToday(mTestDate_AppDateFormat, DateUtils.appDateFormat);
        assertThat(isToday, is(true));
    }

    @Test
    public void isToday_testStringAndFormat_different() {
        boolean isToday = DateUtils.isToday(mTestDate_AppDateFormat, DateUtils.backendDateFormat);
        assertThat(isToday, is(false));
    }

    @Test
    public void isToday_2dates_returnTrue() {
        boolean isToday = DateUtils.isToday(new Date(), new Date());
        assertThat(isToday, is(true));
    }

    @Test
    public void isToday_2dates_returnFalse() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        boolean isToday = DateUtils.isToday(new Date(), cal.getTime());
        boolean isToday2 = DateUtils.isToday(new Date(), new Date());
        //Acceptable to assert 2 times but not really a good practice
        assertThat(isToday, is(false));
        assertThat(isToday2, is(true));
    }

    @Test
    public void isToday_withString_returnTrue() {
        boolean isToday = DateUtils.isToday(mTestDate_AppDateFormat);
        assertThat(isToday, is(true));
    }

    @Test
    public void isToday_withString_returnTrue2() {
        boolean isToday = DateUtils.isToday(mTestDate_AppDateFormat);
        assertThat(isToday, is(true));
    }

    @Test
    public void isToday_withString_returnFalse() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrowDate = DateUtils.appDateFormat.format(cal.getTime());
        boolean isToday = DateUtils.isToday(tomorrowDate);
        assertThat(isToday, is(false));
    }

    @Test
    public void isTomorrow_returnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        boolean isTomorrow = DateUtils.isTomorrow(mCurDate, cal.getTime());
        assertThat(isTomorrow, is(true));
    }

    @Test
    public void isTomorrow_returnFalse() {
        boolean isTomorrow = DateUtils.isTomorrow(mCurDate, mCurDate);
        assertThat(isTomorrow, is(false));
    }

    @Test
    public void isFuture_returnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrowDate = DateUtils.appDateFormat.format(cal.getTime());
        boolean isFuture = DateUtils.isFuture(tomorrowDate);
        assertThat(isFuture, is(true));
    }

    @Test
    public void isFuture_returnFalse() {
        boolean isFuture = DateUtils.isFuture(mTestDate_AppDateFormat);
        assertThat(isFuture, is(false));
    }

    @Test
    public void isPastDate_returnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurDate);
        cal.add(Calendar.DATE, -1);
        boolean isPastDate = DateUtils.isPastDate(DateUtils.appDateFormat.format(cal.getTime()));
        assertThat(isPastDate, is(true));
    }

    @Test
    public void isPastDate_returnFalse() {
        boolean isPastDate = DateUtils.isPastDate(mTestDate_AppDateFormat);
        assertThat(isPastDate, is(false));
    }

    public void getLastMonthDate_success() {
        String lastMonth = "02/01/2015";
        String returnedMonth = DateUtils.getLastMonthDate();
        assertThat(lastMonth, is(returnedMonth));
    }

    @Test
    public void getTodayDate_success() {
        String todayDate = DateUtils.getTodaysDate();
        assertThat(todayDate, is(mTestDate_DisplayFormat));
    }

    @Test
    public void getTomorrowsDate_success() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurDate);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrowsDate = DateUtils.getTomorrowDates();
        assertThat(tomorrowsDate.getTime(), is(cal.getTime().getTime()));
    }

    @Test
    public void getDaysString_englishShort() {
        String[] weekDays = new String[]{
                "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"
        };
        String[] returnedWeekDays = DateUtils.getDaysString(true);
        assertThat(weekDays, is(returnedWeekDays));
    }

    @Test
    public void getDateString_success() {
        Calendar cal = Calendar.getInstance();
        String date = DateUtils.getDateString(cal.YEAR, cal.MONTH, cal.DAY_OF_MONTH);
        String dateString = cal.YEAR + "-" + (cal.MONTH >= 10 ? cal.MONTH + "" : "0" + cal.MONTH)
                + "-" + (cal.DAY_OF_MONTH >= 10 ? cal.DAY_OF_MONTH + "" : "0" + cal.DAY_OF_MONTH);
        assertThat(date, is(dateString));
    }

    @Test
    public void getTomorrowDatesString_success() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        String tomorrow = DateUtils.getTomorrowDatesString();
        assertThat(tomorrow, is(DateUtils.getDateString(cal.getTime())));
    }

    @Test
    public void getTimeFromMilliseconds_success() {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        int hours = 10;
        int minutes = 15;
        int seconds = 12;
        String timeString = hours + ":" + minutes + ":" + seconds + "";
        long timeInMillis = secondsInMilli * seconds + minutes * minutesInMilli + hours * hoursInMilli;
        assertThat(DateUtils.getTime(timeInMillis), is(timeString));
    }

    @Test
    public void getWeekNumberFromDateString_null() {
        int week = DateUtils.getWeekNumberFromDateString("");
        assertThat(week, is(0));
    }

    @Test
    public void getWeekNumberFromDateString_success() {
        Calendar cal = Calendar.getInstance();
        String dateString = cal.YEAR + "-" + (cal.MONTH >= 10 ? cal.MONTH + "" : "0" + cal.MONTH)
                + "-" + (cal.DAY_OF_MONTH >= 10 ? cal.DAY_OF_MONTH + "" : "0" + cal.DAY_OF_MONTH);
        int week = DateUtils.getWeekNumberFromDateString(dateString);
        assertThat(week, is(cal.get(Calendar.WEEK_OF_YEAR)));
    }
}
