package com.codez.elements.db.improvment;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.codez.elements.db.Component;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

import java.util.HashMap;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class DemoProvider extends SimpleProvider{
    public static final String PROVIDER_NAME = "com.codez.elements";
    public static final String MAIN_URL = "content://" + PROVIDER_NAME + "/";

    static {
        TABLES = new String[]{
                CStaffTable.TABLE, CustAvailableTable.TABLE
        };
    }

    @Override
    public HashMap<Integer, Component> defineComponents() {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
