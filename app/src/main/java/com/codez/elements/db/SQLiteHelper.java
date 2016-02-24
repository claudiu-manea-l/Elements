package com.codez.elements.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

/**
 * Created by Eptron on 1/25/2016.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "sample_db";

    public SQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        CStaffTable.onCreate(db);
        CustAvailableTable.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CStaffTable.onUpgrade(db, oldVersion, newVersion);
        CustAvailableTable.onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        CStaffTable.onUpgrade(db, oldVersion, newVersion);
        CustAvailableTable.onUpgrade(db, oldVersion, newVersion);
    }
}
