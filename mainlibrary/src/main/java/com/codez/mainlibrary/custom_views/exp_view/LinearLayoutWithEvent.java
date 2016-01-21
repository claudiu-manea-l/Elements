package com.codez.mainlibrary.custom_views.exp_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 12/23/2015.
 */
public class LinearLayoutWithEvent extends LinearLayout {

    public LinearLayoutWithEvent(Context context) {
        super(context);
    }

    public LinearLayoutWithEvent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        registerEventBus();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterEventBus();
    }

    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
