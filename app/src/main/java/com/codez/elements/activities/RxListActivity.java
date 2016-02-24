package com.codez.elements.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.codez.elements.controllers.DataController;
import com.codez.elements.db.tables.CStaffTable;
import com.codez.elements.temp.DataProvider;


/**
 * Created by Claudiu on 2/16/2016.
 */
public class RxListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private StaffAdapter mAdapter;
    private View mProgressLayout;
    private View mListLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.codez.fulllists.R.layout.f_main_list);
        ListView view = (ListView) findViewById(com.codez.fulllists.R.id.list_view);
        mAdapter = new StaffAdapter();
        view.setAdapter(mAdapter);
        mProgressLayout = findViewById(com.codez.fulllists.R.id.progress_layout);
        mListLayout = findViewById(com.codez.fulllists.R.id.list_layout);
        getSupportLoaderManager().initLoader(0, null, this);
        DataController.getController().getCustomerStaff();


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                DataProvider.STAFF_CONTENT_URI,
                CStaffTable.FULL_PROJECTION,
                null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        setListVisibility(true);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
        setListVisibility(false);
    }

    protected void setListVisibility(boolean isVisible) {
        mListLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mProgressLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    class StaffAdapter extends CursorAdapter {

        public StaffAdapter() {
            super(RxListActivity.this, null, false);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return getLayoutInflater().inflate(com.codez.customviews.R.layout.c_item_simple_spinner, null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textView = (TextView) view.findViewById(com.codez.customviews.R.id.spinner_item);
            String name = cursor.getString(cursor.getColumnIndex(CStaffTable.NAME));
            textView.setText(name);
        }
    }
}
