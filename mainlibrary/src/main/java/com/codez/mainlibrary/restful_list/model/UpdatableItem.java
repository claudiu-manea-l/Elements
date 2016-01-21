package com.codez.mainlibrary.restful_list.model;

/**
 * Created by Eptron on 10/15/2015.
 */
public class UpdatableItem {
    public static final String TIMESTAMP = "timestamp";
    public static final String MAX_STAMP = "MAX("+TIMESTAMP+")";

    public static final String[] TIMESTAMP_PROJ = { MAX_STAMP };
}
