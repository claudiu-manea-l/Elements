package com.codez.mainlibrary.custom_views.other;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.codez.mainlibrary.R;

/**
 * Created by eptron on 7/29/2015.
 */
public class CustomCheckbox extends CheckBox {

    public CustomCheckbox(Context context) {
        super(context);
        useCheckBox();
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomCheckbox);
        try{
            if(a.getBoolean(R.styleable.CustomCheckbox_useRadios,false)){
                useRadios();
            }
            else{
                useCheckBox();
            }
        }finally {
            a.recycle();
        }
    }

    private void useCheckBox(){

        StateListDrawable states = new StateListDrawable();

        Drawable on=getResources().getDrawable(R.drawable.check_on);
        on=on.mutate();

        Drawable off=getResources().getDrawable(R.drawable.check_off);
        off = off.mutate();

        states.addState(new int[]{android.R.attr.state_checked}, on);
        states.addState(new int[]{-android.R.attr.state_checked}, off);
        //states.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN));
        this.setButtonDrawable(states);
    }

    private void useRadios(){

        StateListDrawable states = new StateListDrawable();

        Drawable on=getResources().getDrawable(R.drawable.abc_btn_radio_to_on_mtrl_015);
        on=on.mutate();

        Drawable off=getResources().getDrawable(R.drawable.abc_btn_radio_to_on_mtrl_000);
        off = off.mutate();

        states.addState(new int[]{android.R.attr.state_checked}, on);
        states.addState(new int[]{-android.R.attr.state_checked}, off);
        //states.setColorFilter(new PorterDuffColorFilter(getResources().getColor(R.color.secondary), PorterDuff.Mode.SRC_IN));
        this.setButtonDrawable(states);
    }

}