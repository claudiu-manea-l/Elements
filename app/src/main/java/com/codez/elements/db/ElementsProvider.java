package com.codez.elements.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class ElementsProvider extends ContentProvider{
    public static final String PROVIDER_NAME = "com.codez.elements";
    public static final String MAIN_URL = "content://" + PROVIDER_NAME + "/";
    protected static final String DIR_URI = "vnd.android.cursor.dir/vnd.eptron.";
    protected static final String ITEM_URI = "vnd.android.cursor.item/vnd.eptron.";

    protected SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return mDatabase != null;
    }

    private static final String CSTAFF_URL = MAIN_URL + CStaffTable.TABLE;
    public static final Uri CSTAFF_URI= Uri.parse(CSTAFF_URL);

    private static final String CUST_AVAILABILITY_URL = MAIN_URL + CustAvailableTable.TABLE;
    public static final Uri CUST_AVAILABILITY_URI = Uri.parse(CUST_AVAILABILITY_URL);

    private static final int CSTAFF = 0;
    private static final int AVAILABILITY = 1;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME,CStaffTable.TABLE,CSTAFF);
        uriMatcher.addURI(PROVIDER_NAME,CStaffTable.TABLE,AVAILABILITY);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor returnCursor;
        String TABLE = getTable(uri);
        returnCursor = mDatabase.query(TABLE, projection, selection, selectionArgs,
                null, null, sortOrder);
        returnCursor.setNotificationUri(getContext().getContentResolver(),
                uri);
        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case AVAILABILITY:
                return DIR_URI + CustAvailableTable.TABLE;
            case CSTAFF:
                return DIR_URI + CStaffTable.TABLE;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        Uri _uri, URI = getUri(uri);
        String TABLE = getTable(uri);
        rowID = mDatabase.insert(TABLE, "", values);
        _uri = ContentUris.withAppendedId(URI, rowID);
        Log.i("CustomerProvider ", TABLE + "_Id = " + rowID);
        if (rowID > 0) {
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i("CustomerProvider", "Delete Called...");
        int rowsUpdated;
        String id;
        String TABLE = "";
        try {
            TABLE = getTable(uri);
            id = uri.getLastPathSegment();
            selectionArgs = new String[]{id};
            selection = BaseColumns._ID + " = ?";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        rowsUpdated = mDatabase.delete(TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i("CustomerProvider", "Update Called...");
        int rowsUpdated;
        String id;
        String TABLE = "";
        try {
            TABLE = getTable(uri);
            id = uri.getLastPathSegment();
            selectionArgs = new String[]{id};
            selection = BaseColumns._ID + " = ?";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        rowsUpdated = mDatabase.update(TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }


    private String getTable(Uri uri) throws IllegalArgumentException {
        String TABLE = "";
        switch (uriMatcher.match(uri)) {
            case CSTAFF:
                TABLE = CStaffTable.TABLE;
                break;
            case AVAILABILITY:
                TABLE = CustAvailableTable.TABLE;
                break;
            default:
                throw new IllegalArgumentException("Query --Invalid URI:" + uri);
        }
        return TABLE;
    }

    public Uri getUri(Uri uri) throws IllegalArgumentException {
        Uri URI = null;
        switch (uriMatcher.match(uri)) {
            case CSTAFF:
                URI = CSTAFF_URI;
                break;
            case AVAILABILITY:
                URI = CUST_AVAILABILITY_URI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return URI;
    }
}
