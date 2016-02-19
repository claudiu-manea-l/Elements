package com.codez.elements.db.improvment;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.codez.elements.db.Component;
import com.codez.elements.db.SQLiteHelper;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * Created by Claudiu on 2/16/2016.
 */
public abstract class SimpleProvider extends ContentProvider {

    protected static final String DIR_URI = "vnd.android.cursor.dir/vnd.eptron.";
    protected static final String ITEM_URI = "vnd.android.cursor.item/vnd.eptron.";

    protected static String[] TABLES;
    protected static String MAIN_URL;
    protected static String PROVIDER_NAME;

    protected SQLiteDatabase mDatabase;

    public static HashMap<String,Component> COMPONENTS;
    private static final UriMatcher uriMatcher;

    @Override
    public boolean onCreate() {
        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return mDatabase != null;
    }

    static {
        COMPONENTS = new HashMap<>();
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        for(int i=0;i<TABLES.length;i++) {
            Uri uri = Uri.parse(MAIN_URL+TABLES[i]);
            COMPONENTS.put(TABLES[i],new Component(TABLES[i],uri,false));
            uriMatcher.addURI(PROVIDER_NAME,TABLES[i],i);
            COMPONENTS.put(TABLES[i],new Component(TABLES[i],uri,true));
            uriMatcher.addURI(PROVIDER_NAME,TABLES[i]+"/#",i);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        //if(COMPONENTS.con)
        return null;
    }

    public abstract HashMap<Integer,Component> defineComponents();

    //public abstract Uri getUri(Uri uri) throws IllegalArgumentException;

    //public abstract String getTable(Uri uri) throws IllegalArgumentException;

}
