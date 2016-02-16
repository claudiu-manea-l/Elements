package com.codez.elements.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.temp.DataProvider;
import com.codez.mainlibrary.restful_list.lists.UpdatableSQList;

/**
 * Created by Eptron on 1/25/2016.
 */
public class MainListFragment extends UpdatableSQList {

    @Override
    protected Uri getSQLUri() {
        return DataProvider.STAFF_CONTENT_URI;
    }

    @Override
    protected String getContentAuthority() {
        return DataProvider.PROVIDER_NAME;
    }

    @Override
    protected String getEmptyString() {
        return "No items found";
    }

    @Override
    protected CursorAdapter onInitAdapter() {
        return new SimpleCursorLoader();
    }

    @Override
    protected boolean fetchData() {
        return super.fetchData();
    }

    @Override
    protected boolean fetchData(long timeStamp) {
        return super.fetchData(timeStamp);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                DataProvider.STAFF_CONTENT_URI,
                CStaffTable.FULL_PROJECTION,
                null, null, null);
    }

    class SimpleCursorLoader extends CursorAdapter {

        public SimpleCursorLoader() {
            super(getActivity(), null, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

        }
    }
}
