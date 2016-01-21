package com.codez.mainlibrary.utilities;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Eptron on 1/21/2016.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class DateUtilsTest {

    @Test
    public void dateUtils_isToday_returnsTrue(){
        boolean isToday = DateUtils.isToday(new Date(),new Date());
        assertThat(isToday,is(true));
    }
}
