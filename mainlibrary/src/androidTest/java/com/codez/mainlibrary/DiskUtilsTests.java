package com.codez.mainlibrary;

import android.test.suitebuilder.annotation.SmallTest;

import android.content.Context;

import com.codez.mainlibrary.utilities.DiskUtils;

import static org.hamcrest.Matchers.is;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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

    String FILE_NAME = "data.text";
    String DUMMY_DATA = "dummy data";

    @Mock
    Context context;

    @Mock
    FileOutputStream fileOutputStream;

    @Mock
    FileInputStream fileInputStream;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void WriteStringToDisk_Test() {
        try {
            when(context.openFileOutput(anyString(), anyInt())).thenReturn(fileOutputStream);
            DiskUtils.writeStringToDisk(context, DUMMY_DATA, FILE_NAME);
            verify(context, times(1)).openFileOutput(anyString(), anyInt());
            verify(fileOutputStream, atLeast(1)).write(any(byte[].class));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void ReadStringFromDisk_Test() {
        try {
            when(context.openFileInput(FILE_NAME)).thenReturn(fileInputStream);
            String stringFromDisk = DiskUtils.getStringFromDisk(context, FILE_NAME);
            verify(context, times(1)).openFileInput(anyString());
            assertThat(stringFromDisk, is(DUMMY_DATA));

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
