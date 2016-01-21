package com.codez.mainlibrary.custom_views.titled_list;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.custom_views.TextViewPlus;
import com.codez.mainlibrary.custom_views.titled_list.model.DataModel;
import com.codez.mainlibrary.custom_views.titled_list.model.RowData;

/**
 * Created by Eptron on 12/1/2015.
 */
public class ListWithTitle<T> extends LinearLayout {
    public static String EMPTY_STRING;

    private TextViewPlus mTitleView;
    private TextViewPlus mEmptyView;
    private LinearLayout mRowHolder;
    //private View mHeaderView;

    private DataModel<T> mDataModel;
    private int mCustomViewResId = 0;
    private OnFillDataListener<T> mCallback;

    public ListWithTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        EMPTY_STRING = context.getString(R.string.empty_item);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTitleView = (TextViewPlus) findViewById(R.id.desc_title);
        mRowHolder = (LinearLayout) findViewById(R.id.row_holder);
        mEmptyView = (TextViewPlus) findViewById(R.id.empty_view);
    }

    public void setDataModel(DataModel<T> dataModel) {
        mDataModel = dataModel;
        mTitleView.setText(dataModel.getTitle());
        if (mDataModel.getRowData().isEmpty()) {
            enableEmptyView();
        } else {
            for (int i = 0; i < mDataModel.getRowData().size(); i++) {
                addRowView(mDataModel.getRowData().get(i),i);
            }
        }
        if (mRowHolder.getChildCount() == 1)
            enableEmptyView();
    }

    public void setCustomDataModel(int resId, OnFillDataListener callback, DataModel<T> data) {
        mCustomViewResId = resId;
        mCallback = callback;
        setDataModel(data);
    }

    private void addRowView(T rowData,int position) {
        View temp = null;
        if (rowData instanceof RowData) {
            temp = View.inflate(getContext(), R.layout.c_titled_list_row, null);
            ((TextViewPlus) temp.findViewById(R.id.desc_name)).setText(((RowData) rowData).getName());
            ((TextViewPlus) temp.findViewById(R.id.desc_value)).setText(((RowData) rowData).getValue());
        } else {
            if (mCustomViewResId != 0 && mCallback != null) {
                temp = View.inflate(getContext(), mCustomViewResId, null);
                mCallback.setDataToView(temp, rowData,position);
            }
        }
        if (temp != null) {
            mRowHolder.addView(temp);
        }
    }

    private void enableEmptyView() {
        mEmptyView.setText(mDataModel.getEmptyString());
        mEmptyView.setVisibility(VISIBLE);
    }
}
