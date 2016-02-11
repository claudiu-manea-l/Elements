package com.codez.mainlibrary;

import android.content.Context;
import android.net.ConnectivityManager;
import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.connect_manager.ConnectManager;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Shafqat on 2/11/2016.
 */
@SmallTest
public class ConnectManagerTest {

    @Mock
    Context mContext;

    @Before
    public void init() {
        mContext = getTargetContext();
        //MockitoAnnotations.initMocks(this);
        MatcherAssert.assertThat(mContext, notNullValue());
    }

    @Test
    public void hasInternet_ReturnsTrue() {
        ConnectManager singleton = ConnectManager.getManager();
        boolean value = singleton.hasInternet(mContext);
        assertThat(value, is(isAvailable()));
        assertTrue(value);
    }

    public Boolean isAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null) {
            //getActiveNetworkInfo() will return null if there is no active network
            return connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
        } else {
            return false;
        }
    }
}
