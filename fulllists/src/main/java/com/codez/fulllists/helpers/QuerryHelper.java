package com.codez.fulllists.helpers;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eptron on 11/20/2015.
 */
public class QuerryHelper {
    private String mSelection = "";
    private List<String> mSelectionArgs = new ArrayList<>();

    public void addToSelection(String selection) {
        if (TextUtils.isEmpty(mSelection))
            mSelection = selection;
        else mSelection = mSelection + " AND " + selection;
    }

    public void addSelectionArgs(String args) {
        mSelectionArgs.add(args);
    }

    public String getSelection() {
        return mSelection;
    }

    public String[] getSelectionArgs() {
        String[] selectionArgs = new String[mSelectionArgs.size()];
        for (int i = 0; i < mSelectionArgs.size(); i++)
            selectionArgs[i] = mSelectionArgs.get(i);
        return selectionArgs;
    }

    public static String buildOrSelection(String idColumn, int count) {
        String selection = idColumn + " LIKE ? ";
        String fullSelection = selection;
        String OR = " OR ";
        for (int i = 1; i < count; i++) {
            if (i == count - 1)
                fullSelection = fullSelection + selection;
            fullSelection = fullSelection + OR + selection;
        }
        return fullSelection;
    }

    public static String buildSelectionString(String idColumn, int[] ids) {
        String selection = "";
        if (ids.length > 0) {
            selection = idColumn + " IN (";
            selection = selection + ids[0];
            if (ids.length == 1)
                selection = selection + ")";
            for (int i = 1; i < ids.length; i++) {
                if (i == ids.length - 1)
                    selection = selection + "," + ids[i] + ")";
                else selection = selection + "," + ids[i];
            }
        }
        return selection;
    }

}
