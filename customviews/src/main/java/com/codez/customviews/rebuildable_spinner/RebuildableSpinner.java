package com.codez.customviews.rebuildable_spinner;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eptron on 12/23/2015.
 */
public class RebuildableSpinner extends Spinner {

    private static final int TIMEOUT = 500;

    protected List<SpinnerItem> mItems;
    protected RebuildableSpinnerAdapter mAdapter;
    protected Class<? extends RebuildableSpinnerAdapter> mCustomAdapterClass;

    private OnRebuildListener mOnRebuildListener;
    protected String mSelectedItemCode;
    protected String mSelectedItemName;
    protected boolean isDisabled = false;
    protected boolean isLoading = false;

    protected boolean mHasFirstItem = false;
    protected SpinnerItem mFirstItem;

    protected boolean mHasEmptyView = false;
    protected SpinnerItem mEmptyItem;

    private int prevId = -1;
    // public abstract int getSpinnerType();
    protected SpinnerLoader mLoader;
    protected Handler mHandler = new Handler();
    protected Runnable mRestartWorker = new Runnable() {
        @Override
        public void run() {
            if (mLoader.getStatus() == AsyncTask.Status.FINISHED) {
                mLoader = null;
            }
            startWorker(prevId);
        }
    };

    public RebuildableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        mItems = new ArrayList<>();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        startWorker();
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setFirstItemView(String text) {
        mHasFirstItem = true;
        mFirstItem = new SpinnerItem(text, "");
    }

    public void setEmptyView(String text) {
        mHasEmptyView = true;
        mEmptyItem = new SpinnerItem(text, "");
    }

    public void setOnRebuildListener(OnRebuildListener listener) {
        mOnRebuildListener = listener;
    }

    public void startWorker() {
        startWorker(-1);
    }

    public void startWorker(int id) {
        if (mOnRebuildListener != null) {
            prevId = id;
            if (mLoader == null) {
                mLoader = new SpinnerLoader();
                mLoader.execute(id);
            } else {
                if (mLoader.getStatus() == AsyncTask.Status.FINISHED) {
                    mLoader = null;
                    startWorker(id);
                } else {
                    mHandler.postDelayed(mRestartWorker, TIMEOUT);
                }
            }
        }
    }

    private void initSpinner(List<SpinnerItem> items) {
        if (mAdapter == null) {
            addItems(items);
            initNewAdapter();
            initSelection();
        } else {
            mItems.clear();
            addItems(items);
            initSelection();
        }
        mAdapter.notifyDataSetChanged();
        //isDisabled comming from parrent
        if (!isDisabled) setDisable(false);
        if (mOnRebuildListener != null)
            mOnRebuildListener.onDoneLoading();
    }

    private void addItems(List<SpinnerItem> items) {
        if (mItems == null)
            mItems = new ArrayList<>();
        if (items.isEmpty()) {
            initEmptyView();
        } else {
            mItems.addAll(items);
            initFirstItemView();
        }
        if (mAdapter != null)
            mAdapter.notifyDataSetChanged();
    }

    public void setCustomAdapter(Class<? extends RebuildableSpinnerAdapter> adapter) {
        mCustomAdapterClass = adapter;
    }

    private void initNewAdapter() {
        if (mCustomAdapterClass != null) {
            try {
                mAdapter = mCustomAdapterClass
                        .getDeclaredConstructor(Context.class)
                        .newInstance(getContext());
                mAdapter.setItems(mItems);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mAdapter = new RebuildableSpinnerAdapter(getContext(), mItems);
        }
        setAdapter(mAdapter);
    }

    private void initEmptyView() {
        if (mHasEmptyView && mEmptyItem != null) {
            mItems.add(mEmptyItem);
        }
    }

    private void initFirstItemView() {
        if (mHasFirstItem && mFirstItem != null)
            mItems.add(0, mFirstItem);
    }

    private void initSelection() {
        for (SpinnerItem item : mItems)
            if (item.getItemCode().equals(mSelectedItemCode))
                setSelection(mItems.indexOf(item));
    }

    public void setSpinnerByCode(String code) {
        mSelectedItemCode = code;
        selectItemAtCode(code);
    }

    public void setSpinnerByName(String name) {
        mSelectedItemName = name;
        selectItemAtName(name);
    }

    private void selectItemAtCode(String code) {
        if (mItems != null && !mItems.isEmpty() && mAdapter != null) {
            for (SpinnerItem item : mItems) {
                if (item.getItemCode().equals(code)) {
                    setSelection(mItems.indexOf(item));
                }
            }
        }
    }

    private void selectItemAtName(String name) {
        if (mItems != null && !mItems.isEmpty() && mAdapter != null) {
            for (SpinnerItem item : mItems) {
                if (item.getItemName().equals(name)) {
                    setSelection(mItems.indexOf(item));
                }
            }
        }
    }

    public SpinnerItem getItemAt(int position) {
        return mItems.get(position);
    }

    public SpinnerItem getSelectedItem() {
        if (mItems == null || mItems.size() == 0)
            return new SpinnerItem();
        if (mHasEmptyView && getSelectedItemPosition() < 0)
            return new SpinnerItem();
        return mItems.get(getSelectedItemPosition());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mLoader != null && mLoader.getStatus().equals(AsyncTask.Status.RUNNING))
            mLoader.cancel(true);
    }

    private void setDisable(boolean isEnabled) {
        if (isEnabled() == isEnabled) {
            setClickable(!isEnabled);
            setEnabled(!isEnabled);
            setFocusable(!isEnabled);
        }
    }

    public void setDisable() {
        isDisabled = true;
        setClickable(false);
        setEnabled(false);
        setFocusable(false);
    }

    protected class SpinnerLoader extends AsyncTask<Integer, Void, List<SpinnerItem>> {

        @Override
        protected void onPreExecute() {
            isLoading = true;
        }

        @Override
        protected List<SpinnerItem> doInBackground(Integer... params) {
            int param = params[0];
            if (mHasFirstItem && param == -1) {
                return new ArrayList<>();
            }
            if (mOnRebuildListener != null)
                return mOnRebuildListener.onRebuild(params[0]);
            else return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<SpinnerItem> items) {
            if (!isCancelled()) initSpinner(items);
            isLoading = false;
        }
    }

    public interface OnRebuildListener {
        List<SpinnerItem> onRebuild(int position);

        void onDoneLoading();
        //  List<SpinnerItem> onRebuild(long id);
    }
}
