package com.codez.elements.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.codez.customviews.exp_view.ExpandableView;
import com.codez.elements.R;
import com.codez.mainlibrary.BaseActivity;

/**
 * Created by Claudiu on 1/25/2016.
 * Sample class demonstrating the use of ExpandableView
 */
public class ExpandableViewActivity extends BaseActivity {

    private ExpandableView mExpView1;
    private ExpandableView mExpView2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_activity);
        initLayoutComponents();
    }

    private void initLayoutComponents() {
        mExpView1 = (ExpandableView) findViewById(R.id.expView1);
        mExpView2 = (ExpandableView) findViewById(R.id.expView2);

        mExpView1.setViewResId(R.layout.rx_activity);
        mExpView2.setViewResId(R.layout.some_layout);
    }
}
