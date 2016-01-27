package com.codez.mainlibrary.utilitiy_classes_tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.DateUtils;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

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
        String formatedDate = DateUtils.changeFormat(
                mTestDate_AppDateFormat, DateUtils.appDateFormat, DateUtils.displayFormat);
        assertThat(formatedDate, is(mTestDate_DisplayFormat));
    }

    @Test
    public void changeFormat_fail() {
        String formatedDate = DateUtils.changeFormat(
                "", DateUtils.appDateFormat, DateUtils.backendDateFormat
        );
        assertThat(formatedDate, is(""));
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
        String formatedDate = DateUtils.formatDate("");
        assertThat(formatedDate, isEmptyOrNullString());
    }

    @Test
    public void formatDateWithDefaultFormat_sucess() {
        String formatedDate = DateUtils.formatDate(mTestDate_BackendDateFormat);
        assertThat(formatedDate, is(mTestDate_AppDateFormat));
    }

    @Test
    public void formatDateWithGivenFormat_null() {
        String formatedDate = DateUtils.formatDate("", null);
        assertThat(formatedDate, isEmptyOrNullString());
    }

    @Test
    public void formatDateWithGivenFormat_sucess() {
        String formatedDate = DateUtils.formatDate(mTestDate_BackendDateFormat, DateUtils.displayFormat);
        assertThat(formatedDate, is(mTestDate_DisplayFormat));
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
        String time = DateUtils.appTimeFormat.format(date);
        assertThat(time, is("02:00"));
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
        assertThat(isTomorrow, is(false));
    }

    @Test
    public void isTomorrow_returnFalse() {
        boolean isTomorrow = DateUtils.isTomorrow(mCurDate, mCurDate);
        assertThat(isTomorrow, is(true));
    }

    @Test
    public void isFuture_returnTrue() {
        boolean isFuture = DateUtils.isFuture(mTestDate_AppDateFormat);
        assertTrue(isFuture);
    }

    @Test
    public void isFuture_returnFalse() {
        boolean isFuture = DateUtils.isFuture(mTestDate_AppDateFormat);
        assertFalse(isFuture);
    }

    @Test
    public void isPastDate_returnTrue() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurDate);
        cal.add(Calendar.DATE, -1);
        boolean isPastDate = DateUtils.isPastDate(DateUtils.appDateFormat.format(cal.getTime()));
        assertTrue(isPastDate);
    }

    @Test
    public void isPastDate_returnFalse() {
        boolean isPastDate = DateUtils.isPastDate(mTestDate_AppDateFormat);
        assertFalse(isPastDate);
    }

    @Test
    public void getLastMonthDate_success() {
        String lastMonth = "22/12/2015";
        String returnedMonth = DateUtils.getLastMonthDate();
        assertThat(lastMonth, is(returnedMonth));
    }

    @Test
    public void getTodayDate_success() {
        String todayDate = DateUtils.getTodaysDate();
        assertThat(todayDate, is(mTestDate_AppDateFormat));
    }

    @Test
    public void getTomorrowsDate_success() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(mCurDate);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tomorrowsDate = DateUtils.getTomorrowDates();
        assertThat(tomorrowsDate, is(cal.getTime()));
    }

    @Test
    public void getDaysString_englishShort() {
        String[] weekDays = new String[]{
                "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"
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
        Calendar cal = Calendar.getInstance();
        String timeInMillis = DateUtils.getTime(cal.getTimeInMillis());
        String hours = cal.HOUR_OF_DAY < 10 ? "0" + cal.HOUR_OF_DAY : cal.HOUR_OF_DAY + "";
        String minutes = cal.MINUTE < 10 ? "0" + cal.MINUTE : cal.MINUTE + "";
        String seconds = cal.SECOND < 10 ? "0" + cal.SECOND : cal.SECOND + "";
        String timeString = hours + ":" + minutes + ":" + seconds;
        assertThat(timeInMillis, is(timeString));
    }

    @Test
    public void getWeekNumberFromDateString_null() {
        int week = DateUtils.getWeekNumberFromDateString(null);
        assertNull(week);
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
