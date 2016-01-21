package com.codez.mainlibrary.restful_list.model;

import android.content.ContentValues;

/**
 * Created by eptron on 01/06/2015.
 */
public abstract class SQLObject {
    private int isDeleted = 0;

    public abstract ContentValues toContentValues();

    public int getId() {
        return -1;
    }

    public boolean isDeleted() {
        return isDeleted > 0;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = (isDeleted) ? 1 : 0;
    }

}
