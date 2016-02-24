package com.codez.elements.db.tables;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.codez.fulllists.model.UpdatableItem;

/**
 * Created by Eptron on 11/12/2015.
 */
public class CStaffTable extends UpdatableItem {

    public static final String TABLE = "customer_staff";

    public static final String ID_STAFF = BaseColumns._ID;
    public static final String NAME = "name";
    public static final String NAME_FIRST = "name_first";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String CITY = "city";
    public static final String ZIP_CODE = "zip_code";
    public static final String STREET = "street";
    public static final String STREET_NUMBER = "str_nr";
    public static final String STREET_BOX = "str_box";
    public static final String TELEPHONE = "telephone";
    public static final String FAX = "fax";
    public static final String LANGUAGE_NL = "lang_nl";
    public static final String LANGUAGE_FR = "lang_fr";
    public static final String LANGUAGE_DE = "lang_de";
    public static final String LANGUAGE_EN = "lang_en";
    public static final String PICTURE_URL = "picture_url";

    public static final String[] FULL_PROJECTION = {
            ID_STAFF, NAME, NAME_FIRST, MOBILE, EMAIL, CITY, ZIP_CODE,
            STREET, STREET_NUMBER, STREET_NUMBER, STREET_BOX, TELEPHONE,
            FAX, LANGUAGE_NL, LANGUAGE_FR, LANGUAGE_DE, LANGUAGE_EN,
            PICTURE_URL, TIMESTAMP};

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE
            + " (" + ID_STAFF + " INTEGER PRIMARY KEY NOT NULL, "
            + NAME + " TEXT, " + NAME_FIRST + " TEXT, "
            + MOBILE + " TEXT, " + EMAIL + " TEXT, "
            + CITY + " TEXT, " + ZIP_CODE + " TEXT, "
            + STREET + " TEXT, " + STREET_NUMBER + " TEXT, "
            + STREET_BOX + " TEXT, " + TELEPHONE + " TEXT, "
            + FAX + " TEXT, " + LANGUAGE_NL + " TEXT, "
            + LANGUAGE_DE + " TEXT, " + LANGUAGE_EN + " TEXT, "
            + LANGUAGE_FR + " TEXT, " + PICTURE_URL + " TEXT, "
            + TIMESTAMP + " LONG " + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(database);
    }

}
