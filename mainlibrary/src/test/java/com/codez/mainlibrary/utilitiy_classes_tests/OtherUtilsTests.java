package com.codez.mainlibrary.utilitiy_classes_tests;

import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.OtherUtils;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Eptron on 1/25/2016.
 */
@SmallTest
public class OtherUtilsTests {

    @Test
    public void isCorrectString_null() {
        String testString = OtherUtils.correctString("");
        assertThat(testString, isEmptyOrNullString());
    }

    @Test
    public void isCorrectString_success() {
        String testString = OtherUtils.correctString("Lorem ipsum dolor sit amet");
        assertThat(testString, is("Lorem,ipsum,dolor,sit,amet"));
    }

    @Test
    public void getExtensionNormal_success() {
        String extension = OtherUtils.getExtensionNormal("http://www.cokestudio.com.pk/season8/media/downloads/audio/04%20-%20Rockstar%20-%20S08E02.mp3");
        assertThat(extension, is("mp3"));
    }

    @Test
    public void getExtensionNormal_null() {
        String extension = OtherUtils.getExtensionNormal("");
        assertThat(extension, isEmptyOrNullString());
    }

    @Test
    public void checkStatus_null() {
        String testString = OtherUtils.checkSet("");
        assertThat(testString, is("Not Set"));
    }

    @Test
    public void checkStatus() {
        String testString = OtherUtils.checkSet("Test String");
        assertThat(testString, is("Test String"));
    }

    @Test
    public void intCheck_null() {
        int number = OtherUtils.checkIntNull(null);
        assertThat(number, is(0));
    }

    @Test
    public void intCheck() {
        int number = OtherUtils.checkIntNull(25);
        assertThat(number, is(25));
    }

    @Test
    public void emptyStringCheck_null() {
        String testString = OtherUtils.checkNull(null);
        assertThat(testString, is("N/A"));
    }

    @Test
    public void emptyStringCheck() {
        String testString = OtherUtils.checkNull("Test String");
        assertThat(testString, is("Test String"));
    }

    @Test
    public void isURL_returnTrue() {
        boolean isURL = OtherUtils.isURL("http://www.cokestudio.com.pk/season8/media/downloads/audio/04%20-%20Rockstar%20-%20S08E02.mp3");
        assertTrue(isURL);
    }

    @Test
    public void isURL_returnFalse() {
        boolean isURL = OtherUtils.isURL("cokestudio");
        assertFalse(isURL);
    }

    @Test
    public void ChangeValue_false() {
        String value = OtherUtils.changeValue("false");
        assertThat(value, is("0"));
    }

    @Test
    public void ChangeValue_true() {
        String value = OtherUtils.changeValue("true");
        assertThat(value, is("1"));
    }

    @Test
    public void getBoolean_zero() {
        boolean value = OtherUtils.getBoolean("0");
        assertFalse(value);
    }

    @Test
    public void getBoolean_one() {
        boolean value = OtherUtils.getBoolean("1");
        assertTrue(value);
    }

}
