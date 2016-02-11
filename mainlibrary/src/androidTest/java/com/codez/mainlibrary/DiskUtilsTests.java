package com.codez.mainlibrary;

import android.content.Context;
import android.test.mock.MockContext;
import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.DiskUtils;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URL;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Shafqat on 2/1/2016.
 */
@SmallTest
public class DiskUtilsTests {

    String FILE_NAME = "data.txt";
    String DUMMY_DATA = "dummy data";

    @Mock
    Context mContext;

    @Mock
    FileOutputStream fileOutputStream;

    @Mock
    FileInputStream fileInputStream;

    @Before
    public void init() {
        mContext = getTargetContext();
        MatcherAssert.assertThat(mContext, CoreMatchers.notNullValue());
        //MockitoAnnotations.initMocks(this);
    }

    /*private static File getFileFromPath(Object obj, String fileName) {
        ClassLoader classLoader = obj.getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return new File(resource.getPath());
    }

    @Test
    public void fileObjectShouldNotBeNull() throws Exception {
        File file = getFileFromPath(this, "res/data.txt");
        assertThat(file, notNullValue());
    }*/

    @Test
    public void WriteStringToDisk_Test() {
        try {
            when(mContext.openFileOutput(anyString(), Context.MODE_PRIVATE)).thenReturn(fileOutputStream);
            DiskUtils.writeStringToDisk(mContext, DUMMY_DATA, FILE_NAME);
            verify(mContext, times(1)).openFileOutput(anyString(), anyInt());
            verify(fileOutputStream, atLeast(1)).write(any(byte[].class));
            fileOutputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void ReadStringFromDisk_Test() {
        try {
            when(mContext.openFileInput(FILE_NAME)).thenReturn(fileInputStream);
            String stringFromDisk = DiskUtils.getStringFromDisk(mContext, FILE_NAME);
            verify(mContext, times(1)).openFileInput(anyString());
            assertThat(stringFromDisk, is(DUMMY_DATA));
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
