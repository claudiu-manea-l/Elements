package com.codez.elements.db.tables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by eptron on 2015.03.13..
 */
public class CustAvailableTable {
    public static final String TABLE = "cust_availability";

    public static final String ID_AVAILABLE = BaseColumns._ID;
    public static final String ID_PERSONNEL = "id_pers";
    public static final String DATE = "date";
    public static final String CODE = "code";

    public static final String[] CUST_AVA_PROJECTION = {ID_AVAILABLE, ID_PERSONNEL,
            DATE, CODE};


    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE
            + " (" + ID_AVAILABLE + " INTEGER PRIMARY KEY NOT NULL, "
            + ID_PERSONNEL + " INTEGER, " + DATE + " TEXT, " + CODE + " INTEGER "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(database);
    }
}
