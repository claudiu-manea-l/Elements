package com.codez.elements.temp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codez.elements.db.SQLiteHelper;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.db.tables.CustAvailableTable;

/**
 * Created by Eptron on 1/25/2016.
 */
public class DataProvider extends ContentProvider {

    protected static final String DIR_URI = "vnd.android.cursor.dir/vnd.eptron.";
    protected static final String ITEM_URI = "vnd.android.cursor.item/vnd.eptron.";

    protected SQLiteDatabase mDatabase;

    @Override
    public boolean onCreate() {
        SQLiteHelper dbHelper = new SQLiteHelper(getContext());
        mDatabase = dbHelper.getWritableDatabase();
        return (mDatabase == null) ? false : true;
    }

    public static final String PROVIDER_NAME = "com.codez.elements.demo";
    public static final String MAIN_URL = "content://" + PROVIDER_NAME + "/";

    //Customer Staff Uri definition
    private static final String STAFF_URL = MAIN_URL + CStaffTable.TABLE;
    public static final Uri STAFF_CONTENT_URI = Uri.parse(STAFF_URL);

    //Customer Staff Availability Uri definition
    private static final String AVA_URL = MAIN_URL + CustAvailableTable.TABLE;
    public static final Uri AVA_CONTENT_URI = Uri.parse(AVA_URL);

    private static final int STAFF = 1;
    private static final int STAFF_ITEM = 2;
    private static final int AVAILABILITY = 3;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, CustAvailableTable.TABLE, AVAILABILITY);

        uriMatcher.addURI(PROVIDER_NAME, CStaffTable.TABLE, STAFF);
        uriMatcher.addURI(PROVIDER_NAME, CStaffTable.TABLE + "/#", STAFF_ITEM);

    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case AVAILABILITY:
                return DIR_URI + CustAvailableTable.TABLE;

            case STAFF:
                return DIR_URI + CStaffTable.TABLE;
            case STAFF_ITEM:
                return ITEM_URI + CStaffTable.TABLE;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
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

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID;
        Uri _uri, URI = getUri(uri);
        String TABLE = getTable(uri);
        rowID = mDatabase.insert(TABLE, "", values);
        _uri = ContentUris.withAppendedId(URI, rowID);
        Log.i("DataProvider ", TABLE + "_Id = " + rowID);
        if (rowID > 0) {
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.i("DataProvider", "Delete Called...");
        int count;
        String id;
        String TABLE = "";
        boolean notFound;
        try {
            TABLE = getTable(uri);
            notFound = false;
        } catch (IllegalArgumentException e) {
            notFound = true;
            id = uri.getLastPathSegment();
            selectionArgs = new String[]{id};
        }
        if (notFound) {
            switch (uriMatcher.match(uri)) {
                case STAFF_ITEM:
                    TABLE = CStaffTable.TABLE;
                    selection = CStaffTable.ID_STAFF + " = ?";
                    break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        }
        count = mDatabase.delete(TABLE, selection, selectionArgs);
        Log.i("DataProvider", TABLE + " Deleted..." + count);
        if (count > 0) getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.i("DataProvider", "Update Called...");
        int rowsUpdated;
        String id;
        String TABLE = "";
        boolean notFound;
        try {
            TABLE = getTable(uri);
            notFound = false;
        } catch (IllegalArgumentException e) {
            notFound = true;
            id = uri.getLastPathSegment();
            selectionArgs = new String[]{id};
        }
        if (notFound) {
            switch (uriMatcher.match(uri)) {
                case STAFF_ITEM:
                    TABLE = CStaffTable.TABLE;
                    selection = CStaffTable.ID_STAFF + " = ?";
                    break;
                default:
                    throw new UnsupportedOperationException("Not yet implemented");
            }
        }
        rowsUpdated = mDatabase.update(TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private String getTable(Uri uri) throws IllegalArgumentException {
        String TABLE = "";
        switch (uriMatcher.match(uri)) {
            case STAFF:
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
            case STAFF:
                URI = STAFF_CONTENT_URI;
                break;
            case AVAILABILITY:
                URI = AVA_CONTENT_URI;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI:" + uri);
        }
        return URI;
    }

}
