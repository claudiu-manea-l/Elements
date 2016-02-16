package com.codez.elements.db;

import android.net.Uri;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class Component {
    private String mTableName;
    public final Uri URI;
    private boolean isItem;

    public Component(String tableName, Uri uri, boolean isItem){
        mTableName = tableName;
        URI = uri;
        this.isItem = isItem;
    }

    public String getTableName() {
        return mTableName;
    }

    public Uri getUri() {
        return URI;
    }

    public boolean isItem() {
        return isItem;
    }
}
