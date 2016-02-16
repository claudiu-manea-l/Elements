package com.codez.elements.db.improvment;

import android.net.Uri;

import com.codez.elements.db.Component;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

import java.util.HashMap;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class DemoProvider {
    public static final String PROVIDER_NAME = "com.codez.elements";
    public static final String MAIN_URL = "content://" + PROVIDER_NAME + "/";

    public static final int CSTAFF = 0;

    private static final String[] TABLES = new String[]{
            CStaffTable.TABLE, CustAvailableTable.TABLE
    };
    public static HashMap<String,Component> COMPONENTS;


    static {
        COMPONENTS = new HashMap<>();
        for(int i=0;i<TABLES.length;i++) {
            Uri uri = Uri.parse(MAIN_URL+TABLES[i]);
            COMPONENTS.put(TABLES[i],new Component(TABLES[i],uri,false));
            COMPONENTS.put(TABLES[i],new Component(TABLES[i],uri,true));
        }
        Uri uri =DemoProvider.COMPONENTS.get(DemoProvider.CSTAFF).URI;
    }

    static {

    }
}
