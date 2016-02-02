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
    public void verifyResourceString_App_Name() {
        assertThat(mContext.getString(R.string.app_name),
                is(equalTo("MainLibrary")));
    }

    @Test
    public void verifyResourceString_Empty_List() {
        assertThat(mContext.getString(R.string.empty_list),
                is(equalTo("No items present in the list!")));
    }

    @Test
    public void verifyResourceString_Empty_Item() {
        assertThat(mContext.getString(R.string.empty_item),
                is(equalTo("No item")));
    }

    @Test
    public void verifyResourceString_No_Gps_Error() {
        assertThat(mContext.getString(R.string.no_gps_error),
                is(equalTo("The GPS has not been started or is still trying to initialize")));
    }

    @Test
    public void verifyResourceString_No_Internet_Error() {
        assertThat(mContext.getString(R.string.no_internet_error),
                is(equalTo("There is no Internet connection present")));
    }

    @Test
    public void verifyResourceString_Unknown_Error() {
        assertThat(mContext.getString(R.string.unknown_error),
                is(equalTo("Unknown error occurred")));
    }

    @Test
    public void verifyResourceString_Map_Waypoint_Error() {
        assertThat(mContext.getString(R.string.map_waypoint_error),
                is(equalTo("One or more waypoints not found.")));
    }

    @Test
    public void verifyResourceString_Error_Message() {
        assertThat(mContext.getString(R.string.error_message),
                is(equalTo("Unknown error occurred")));
    }

    @Test
    public void verifyResourceString_Other_Error() {
        assertThat(mContext.getString(R.string.other_error),
                is(equalTo("Other error occurred")));
    }

    @Test
    public void verifyResourceString_Error_Retrieving_Data() {
        assertThat(mContext.getString(R.string.error_retrieving_data),
                is(equalTo("Error retrieving data")));
    }

    @Test
    public void verifyResourceString_Device_Not_Supported() {
        assertThat(mContext.getString(R.string.device_not_supported),
                is(equalTo("This device is not supported.")));
    }

    @Test
    public void verifyResourceString_Gms_Not_Installed() {
        assertThat(mContext.getString(R.string.gms_not_installed),
                is(equalTo("Google play Services not installed, Maps functionality disabled.")));
    }

    @Test
    public void verifyResourceString_Error_Server() {
        assertThat(mContext.getString(R.string.error_server),
                is(equalTo("Error occurred on Back-End")));
    }

    @Test
    public void verifyResourceString_Wifi_Connection() {
        assertThat(mContext.getString(R.string.wifi_connection),
                is(equalTo("Connected to WiFi")));
    }
}


