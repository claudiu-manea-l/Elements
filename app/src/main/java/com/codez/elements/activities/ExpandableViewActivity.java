package com.codez.elements.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codez.elements.R;
import com.codez.mainlibrary.BaseActivity;

/**
 * Created by Claudiu on 1/25/2016.
 * Sample class demonstrating the use of ExpandableView
 */
public class ExpandableViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity);
    }
}
