package com.codez.customviews.rebuildable_spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import com.codez.customviews.R;

/**
 * Created by eptron on 26/06/2015.
 */
public class SimpleSpinnerAdapter extends BaseAdapter {

    private List<String> mTitles;
    private LayoutInflater mInflater;
    private boolean hasDates = false;

    public SimpleSpinnerAdapter(Context context, List<String> items) {
        mInflater = LayoutInflater.from(context);
        mTitles = items;
    }

    public void enableDates() {
        hasDates = true;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public Object getItem(int position) {
        return mTitles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.c_item_simple_spinner, null);
        TextView textView = (TextView) view.findViewById(R.id.spinner_item);
        view.setTag(mTitles.get(position));
        String temp = mTitles.get(position).substring(0, 2);
        boolean isDate = false;
        try {
            int x = Integer.parseInt(temp);
            isDate = true;
        } catch (Exception e) {
          //  e.printStackTrace();
        }
         textView.setText(mTitles.get(position));

        return view;
    }
}
