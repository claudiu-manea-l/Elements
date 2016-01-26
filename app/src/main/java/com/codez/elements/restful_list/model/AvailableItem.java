package com.codez.elements.restful_list.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.codez.elements.db.tables.CustAvailableTable;
import com.codez.mainlibrary.restful_list.model.SQLObject;
import com.codez.mainlibrary.utilities.DateUtils;

/**
 * Created by Eptron on 1/25/2016.
 */
public class AvailableItem extends SQLObject {

    public static final int NOT_SET = -1;
    public static final int BOTH_OFF = 0;
    public static final int SET_AM = 1;
    public static final int SET_PM = 2;
    public static final int SET_BOTH = 3;

    protected int PERSONAL_AVAILIBILITY_ID;
    protected int Code;
    protected String Date;
    protected int PersonnelID;

    public AvailableItem() {
    }

    public AvailableItem(Cursor cursor) {
        Code = cursor.getInt(cursor.getColumnIndex(CustAvailableTable.CODE));
        Date = cursor.getString(cursor.getColumnIndex(CustAvailableTable.DATE));
        PersonnelID = cursor.getInt(cursor.getColumnIndex(CustAvailableTable.ID_PERSONNEL));
        PERSONAL_AVAILIBILITY_ID = cursor.getInt(cursor.getColumnIndex(CustAvailableTable.ID_AVAILABLE));
    }

    public String getDate() {
        return Date;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public int getPersonnelID() {
        return PersonnelID;
    }

    public void fixDates() {
        Date = DateUtils.formatDate(Date);
    }

    @Override
    public int getId() {
        return PERSONAL_AVAILIBILITY_ID;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues cvs = new ContentValues();
        cvs.put(CustAvailableTable.ID_AVAILABLE, PERSONAL_AVAILIBILITY_ID);
        cvs.put(CustAvailableTable.DATE, Date);
        cvs.put(CustAvailableTable.ID_PERSONNEL, PersonnelID);
        cvs.put(CustAvailableTable.CODE, Code);
        return cvs;
    }
}
