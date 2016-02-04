package com.codez.mainlibrary.custom_views.dialogs;

import android.os.Bundle;
import android.view.View;

import com.codez.mainlibrary.custom_views.dialogs.time_picker.MyTimePicker;

/**
 * Used to help in dismissing the Dialog onOrientationChange
 * Created by Claudiu on 14/04/2015.
 */
public class SingleOrientationDialog extends DialogTemplate {

    protected int oldOrientation;
    protected int currOrientation;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("orientation", oldOrientation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null)
            oldOrientation = savedInstanceState.getInt("orientation");
        else oldOrientation = getResources().getConfiguration().orientation;
        currOrientation = getResources().getConfiguration().orientation;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currOrientation != oldOrientation) dismiss();
    }
}