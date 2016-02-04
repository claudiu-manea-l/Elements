package com.codez.mainlibrary.custom_views.exp_view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import de.greenrobot.event.EventBus;

/**
 * @LinearLayout with EventBus already registered.
 * Use this class when needing to create a customView with LinearLayout and
 * need the EventBus present
 * Created by Claudiu on 12/23/2015.
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

    /**
     * Registers to the EventBus
     */
    private void registerEventBus() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }

    /**
     * Unregisters from the EventBus
     */
    private void unregisterEventBus() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
