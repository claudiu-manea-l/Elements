package com.codez.customviews.titled_list.model;

import android.content.Context;

import com.codez.customviews.titled_list.ListWithTitle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eptron on 12/1/2015.
 */
public class DataModel<T> {
    public String mTitle;
    public List<T> mRowData;
    public String mEmptyString;

    public DataModel(String title) {
        mTitle = title;
        mEmptyString = ListWithTitle.EMPTY_STRING;
        mRowData = new ArrayList<>();
    }

    public DataModel(String title, List<T> data) {
        this(title);
        mRowData = data;
    }

    public String getEmptyString() {
        return mEmptyString;
    }

    public void setRowData(List<T> rowData) {
        if (!mRowData.isEmpty())
            mRowData.clear();
        mRowData.addAll(rowData);
    }

    public void setEmptyString(String emptyString) {
        mEmptyString = emptyString;
    }

    public String getTitle() {
        return mTitle;
    }

    public List<T> getRowData() {
        return mRowData;
    }

    public void addRowData(T rowData) {
        if (mRowData == null) mRowData = new ArrayList<>();
        mRowData.add(rowData);
    }

    public static List<RowData> buildRowData(Context context, int resId, String[] data) {
        String[] titles = context.getResources().getStringArray(resId);
        return buildRowData(titles, data);
    }

    public static List<RowData> buildRowData(String[] titles, String[] data) {
        List<RowData> rowsData = new ArrayList<>();
        int max;
        if (titles.length > data.length)
            max = data.length;
        else
            max = titles.length;
        if (max > 0) {
            for (int i = 0; i < max; i++)
                rowsData.add(new RowData(titles[i], data[i]));
        }
        return rowsData;
    }
}
