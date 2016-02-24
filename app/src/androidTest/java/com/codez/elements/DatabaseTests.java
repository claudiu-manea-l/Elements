package com.codez.elements;

import android.database.Cursor;
import android.support.test.espresso.core.deps.guava.util.concurrent.ExecutionError;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.codez.elements.db.ElementsProvider;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.temp.StaffModel;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by Claudiu on 2/23/2016.
 */
public class DatabaseTests extends ProviderTestCase2<ElementsProvider>{

    private MockContentResolver resolve;

    public DatabaseTests(){
        super(ElementsProvider.class,ElementsProvider.PROVIDER_NAME);
    }


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        resolve = getMockContentResolver();
    }

    public void testInsertedData(){
        Log.i("DatabaseTEsts","Started");
        StaffModel dummy = StaffModel.getDummyStaff();
        resolve.insert(ElementsProvider.CSTAFF_URI,dummy.toContentValues());
        Cursor cursor = resolve.query(ElementsProvider.CSTAFF_URI,
                CStaffTable.FULL_PROJECTION,null,null,null);
        StaffModel dbData = null;
        if(cursor!=null && cursor.moveToFirst()){
            dbData = new StaffModel(cursor);
        } else {
            throw new ExecutionError("Cursor null or empty",null);
        }
        cursor.close();
        assertThat(dummy.getPersonnelID(),is(dbData.getPersonnelID()));
        assertThat(dummy.getCity(),is(dbData.getCity()));
        assertThat(dummy.getEmail(),is(dbData.getEmail()));
        assertThat(dummy.getFax(),is(dbData.getFax()));
        assertThat(dummy.getName(),is(dbData.getName()));
        assertThat(dummy.getNameFirst(),is(dbData.getNameFirst()));
    }

}
