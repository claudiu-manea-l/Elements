package com.codez.mainlibrary.custom_views.exp_view;

/**
 * Created by Eptron on 12/22/2015.
 */
public interface IHasData <T> extends IDisableable {
    T getGenericData();
    void loadData(int id);
    boolean checkHasEmptyFields();
}
