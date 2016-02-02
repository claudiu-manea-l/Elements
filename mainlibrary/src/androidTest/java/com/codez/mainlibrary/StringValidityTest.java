package com.codez.mainlibrary;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Shafqat on 2/1/2016.
 */

/**
 * A simple integration test which checks the validity of a string loaded from resources.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class StringValidityTest {

    private Context mContext;

    @Before
    public void initTargetContext() {
        // Obtain the target context from InstrumentationRegistry
        mContext = getTargetContext();
        assertThat(mContext, notNullValue());
    }

    @Test
    public void verifyResourceString() {
        assertThat(mContext.getString(R.string.app_name),
                is(equalTo("Elements Demo")));
    }

}


