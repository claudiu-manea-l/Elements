package com.codez.customviews.titled_list.model;

/**
 * Created by Eptron on 12/1/2015.
 */
public class RowData {
    private String mName;
    private String mValue;

    public RowData(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

}
