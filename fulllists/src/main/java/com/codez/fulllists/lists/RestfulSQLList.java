package com.codez.fulllists.lists;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.codez.fulllists.R;
import com.codez.fulllists.SyncLogic;
import com.codez.fulllists.helpers.SyncHelper;
import com.codez.fulllists.model.SQLObject;
import com.codez.mainlibrary.restfulkit.model.FailedRequest;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by eptron on 27/06/2015.
 */
public abstract class RestfulSQLList extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemClickListener {

    private View mLayoutView;
    protected SyncHelper mSyncHelper;
    protected AbsListView mAbsView;
    protected View mListLayout;
    protected View mProgressLayout;
    protected CursorAdapter mAdapter;

    protected int mDataCount;
    protected boolean isPulling = false;

    public static final int LOADER = 1;

    protected abstract String getEmptyString();

    protected abstract CursorAdapter onInitAdapter();

    protected Uri getSQLUri() {
        return null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    protected boolean isListView() {
        return true;
    }

    protected boolean fetchData() {
        return false;
    }

    protected void onInitFragment(View view) {
    }

    protected void onInitFragment(LayoutInflater inflater, View view) {
    }

    protected void onEmptyAdapter() {
    }

    protected void onNormalAdapter() {
    }

    protected SyncLogic mCallback;
    protected boolean hasCustomSyncLogic = false;

    protected void setCustomSyncLogic(SyncLogic callback) {
        hasCustomSyncLogic = true;
        mCallback = callback;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(onInitAdapter());
        getLoaderManager().initLoader(LOADER, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        registerEventBus();
        if (isListView()) {
            view = inflater.inflate(R.layout.f_main_list, container, false);
            mAbsView = (ListView) view.findViewById(R.id.list_view);
        } else {
            view = inflater.inflate(R.layout.f_main_grid, container, false);
            mAbsView = (GridView) view.findViewById(R.id.grid_view);
        }
        mLayoutView = view.findViewById(R.id.main_layout);
        mListLayout = view.findViewById(R.id.list_layout);
        mProgressLayout = view.findViewById(R.id.progress_layout);
        TextView emptyView = (TextView) view.findViewById(R.id.empty_view);
        emptyView.setText(getEmptyString());
        setListVisibility(false);

        mAbsView.setEmptyView(emptyView);
        //mAbsView.setAdapter(mAdapter);
        mAbsView.setOnItemClickListener(this);

        onInitFragment(view);
        onInitFragment(inflater, view);
        if (getSQLUri() != null)
            initSyncHelper();
        checkData();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAdapter != null && mAbsView != null) {
            mAbsView.setAdapter(mAdapter);
        }
    }

    protected void initSyncHelper() {
        SyncHelper.Builder builder = new SyncHelper.Builder()
                .create(getActivity())
                .setUri(getSQLUri());
        if (hasCustomSyncLogic)
            builder.setCustomLogic(mCallback);
        mSyncHelper = builder.build();
    }

    protected void checkData() {
        if (getSQLUri() != null) {
            Cursor cur = getActivity().getContentResolver().query(
                    getSQLUri(),
                    new String[]{BaseColumns._ID},
                    null, null, null);
            mDataCount = cur.getCount();
            cur.close();
            if (mDataCount == 0) {
                setListVisibility(false);
                isPulling = fetchData();
            }
        }
    }

    protected void insertIntoTable(List<? extends SQLObject> list) {
        mSyncHelper.insertOrUpdateData(list, false, getSQLUri());
        EventBus.getDefault().post(new FinishedInsertion());
    }

    public void onEventMainThread(FinishedInsertion event) {
        setListVisibility(true);
    }

    public void onEventMainThread(FailedRequest event) {
        setListVisibility(true);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.changeCursor(data);
        if (!isPulling) {
            setListVisibility(true);
        }
        if (mAdapter.isEmpty()) {
            onEmptyAdapter();
        } else {
            onNormalAdapter();
        }
    }

    public void setListAdapter(CursorAdapter adapter) {
        mAdapter = adapter;
        if (mAbsView != null) {
            mAbsView.setAdapter(adapter);
        }
    }

    public void setMainLayoutPadding(int padding) {
        mLayoutView.setPadding(padding, padding, padding, padding);
    }

    public void setListViewPadding(int padding) {
        mAbsView.setPadding(padding, padding, padding, padding);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.changeCursor(null);
        setListVisibility(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        registerEventBus();
    }

    @Override
    public void onPause() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);
    }

    protected void setListVisibility(boolean isVisible) {
        //mListLayout.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mProgressLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    protected void setBackgroundColor(int color) {
        mLayoutView.setBackgroundColor(color);
    }

    public class FinishedInsertion {
        private boolean isUpdate = false;

        public void setUpdate() {
            isUpdate = true;
        }

        public boolean isUpdate() {
            return isUpdate;
        }
    }
}
