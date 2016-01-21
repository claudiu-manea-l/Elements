package com.codez.mainlibrary.custom_views.titled_list;

import android.view.View;

/**
 * Created by Eptron on 12/1/2015.
 */
public interface OnFillDataListener<T> {
    void setDataToView(View view, T dataItem, int position);
}
