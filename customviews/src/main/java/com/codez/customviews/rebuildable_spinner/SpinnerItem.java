package com.codez.customviews.rebuildable_spinner;

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
        mID = parseCode(code);
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

    private int parseCode(String code) {
        int id = -1;
        if (!TextUtils.isEmpty(code)) {
            try {
                id = Integer.parseInt(code);
            } catch (NumberFormatException e) {
                String codeString = code;
                codeString = codeString.replaceAll("[^0-9]", "");
                if (codeString.length() > 0)
                    id = Integer.parseInt(codeString);
            }
        }
        return id;
    }

}
