package com.codez.mainlibrary.custom_views.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.codez.mainlibrary.R;

/**
 * Created by Eptron on 1/6/2016.
 */
public class ClearableEditText extends EditText implements View.OnFocusChangeListener {
    private boolean firstTouch = true;

    public ClearableEditText(Context context) {
        super(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ClearableEditText);
        boolean shouldClear = a.getBoolean(R.styleable.ClearableEditText_clearOnFocus, true);
        if (shouldClear)
            setOnFocusChangeListener(this);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (firstTouch && v.equals(this)) {
            setText("");
            firstTouch = false;
        }
    }
}
