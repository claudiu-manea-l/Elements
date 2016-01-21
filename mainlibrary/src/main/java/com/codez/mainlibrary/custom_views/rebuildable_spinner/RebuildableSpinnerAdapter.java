package com.codez.mainlibrary.custom_views.rebuildable_spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.codez.mainlibrary.R;

import java.util.List;

/**
 * Created by eptron on 26/05/2015.
 */
public class RebuildableSpinnerAdapter extends BaseAdapter {

    private List<SpinnerItem> mItems;
    private LayoutInflater mInflater;
    private ViewHolder mHolder;

    public RebuildableSpinnerAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public RebuildableSpinnerAdapter(Context context, List<SpinnerItem> items) {
        this(context);
        mItems = items;
    }

    public void setItems(List<SpinnerItem> items) {
        mItems = items;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mItems.get(position).getItemId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.c_item_simple_spinner, null);
            mHolder = new ViewHolder();
            mHolder.textView = (TextView) convertView.findViewById(R.id.spinner_item);
            convertView.setTag(mHolder);
        } else {
            mHolder = (ViewHolder) convertView.getTag();
        }
        mHolder.textView.setText(mItems.get(position).getItemName());
        return convertView;
    }

    class ViewHolder {
        TextView textView;
    }
}
