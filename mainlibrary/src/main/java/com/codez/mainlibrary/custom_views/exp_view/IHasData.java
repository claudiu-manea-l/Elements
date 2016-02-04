package com.codez.mainlibrary.custom_views.exp_view;

/**
 * Created by Eptron on 12/22/2015.
 */
public interface IHasData <T> extends IDisableable {
    /**
     * @return the data persisting in this view
     */
    T getGenericData();

    /**
     * Put here the logic for loading the Data needed for this View
     * @param id Can be used to pass an id to help identify which Data to load
     *           Commonly used for interconnected Database tables
     */
    void loadData(int id);

    /**
     * Set the logic for checking if there are empty fields (In case the view
     * cannot save data unless all fields are filled)
     * @return
     */
    boolean checkHasEmptyFields();
}
