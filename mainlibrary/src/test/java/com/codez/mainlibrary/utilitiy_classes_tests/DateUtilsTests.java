package com.codez.mainlibrary.utilitiy_classes_tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.DateUtils;

import org.junit.Before;
import org.junit.Test;

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
    public void formatTime_success() {
        String testTime = DateUtils.formatTime(mTestTime_BackendFormat);
        assertThat(testTime, is(mTestTime_AppTimeFormat));
    }

    @Test
    public void formatTime_null() {
        String testTime = DateUtils.formatTime("");
        assertThat(testTime, isEmptyOrNullString());
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
        assertThat(isTomorrow, is(true));
    }

    @Test
    public void isTomorrow_returnFalse() {
        boolean isTomorrow = DateUtils.isTomorrow(mCurDate, mCurDate);
        assertThat(isTomorrow, is(false));
    }

    @Test
    public void getLastMonthDate_success() {
        String lastMonth = "26/12/2015";
        String returnedMonth = DateUtils.getLastMonthDate();
        assertThat(lastMonth, is(returnedMonth));
    }

    public void getDaysString_englishShort() {
        String[] weekDays = new String[]{
                "Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"
        };
        String[] returnedWeekDays = DateUtils.getDaysString(true);
        assertThat(weekDays, is(returnedWeekDays));
    }


}
