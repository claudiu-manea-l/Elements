package com.codez.mainlibrary.custom_views.rebuildable_spinner;

import android.text.TextUtils;

/**
 * Created by eptron on 26/05/2015.
 */
public class SpinnerItem {
    private String mItemName;
    private String mItemCode;
    private int mID;

    public SpinnerItem(String name, int id) {
        mItemName = name;
        mID = id;
        mItemCode = "0";
    }

    public SpinnerItem(String name, String code) {
        if (!TextUtils.isEmpty(code)) {
            try {
                mID = Integer.parseInt(name);
            } catch (NumberFormatException e) {
                String id = name;
                id = id.replaceAll("[^0-9]", "");
                if (id.length() > 0)
                    mID = Integer.parseInt(id);
            }
        }
        mItemCode = code;
        mItemName = name;
    }

    public SpinnerItem() {
        mItemCode = "0";
        mItemName = "";
        mID = -1;
    }

    public String getItemName() {
        return mItemName;
    }

    public String getItemCode() {
        return mItemCode;
    }

    public int getItemId() {
        return mID;
    }

}
