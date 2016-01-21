package com.codez.mainlibrary.custom_views.other;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.RelativeLayout;

import com.codez.mainlibrary.R;

/*
 * This class is useful for using inside of ListView that needs to have checkable items.
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private CheckBox _checkbox;

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // find checked text view
        _checkbox = (CheckBox) findViewById(R.id.check_box);
    }

    @Override
    public boolean isChecked() {
        return _checkbox != null ? _checkbox.isChecked() : false;
    }

    @Override
    public void setChecked(boolean checked) {
        if (_checkbox != null) {
            _checkbox.setChecked(checked);
        }
    }

    @Override
    public void toggle() {
        if (_checkbox != null) {
            _checkbox.toggle();
        }
    }
}
