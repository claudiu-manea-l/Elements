package com.codez.mainlibrary.custom_views.exp_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.custom_views.TextViewPlus;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 12/16/2015.
 */
public class ExpandableView<T> extends LinearLayout implements View.OnClickListener {

    boolean isExpanded = true;
    public int mResId = -1;
    private boolean isViewInflated = false;

    //private View mView;
    private IHasData<T> mIntCalls;
    private FrameLayout mContainerView;
    private TextViewPlus mTitle;
    private ImageView mArrow;

    public ExpandableView(Context context, int resId) {
        super(context);
        mResId = resId;
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableView(Context context) {
        super(context);
    }

    private void setCustomTitle(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableView);
        String customTitle = a.getString(R.styleable.ExpandableView_customTitle);
        if (!TextUtils.isEmpty(customTitle)) {
            setTitle(customTitle);
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        registerEventBus();
        mTitle = (TextViewPlus) findViewById(R.id.container_title);
        mContainerView = (FrameLayout) findViewById(R.id.container_content);
        mArrow = (ImageView) findViewById(R.id.container_arrow);
        findViewById(R.id.container_title_frame).setOnClickListener(this);
        if (mResId != -1 && !isViewInflated) {
            inflateView();
        }
        //initState();
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

    public void disableView() {
        if (mIntCalls != null)
            mIntCalls.disableView();
        else {
            Log.i("ExpandableView", "View inflated in the container does not implement IDisableable, " +
                    "therefore it cannot be disabled");
        }
    }

    public void setTitleAndViewResource(String title, int resId) {
        setTitle(title);
        mResId = resId;
        if (mContainerView != null && !isViewInflated) {
            inflateView();
        }
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    private void inflateView() {
        View mView = inflate(getContext(), mResId, null);
        if (mView instanceof IHasData) {
            mIntCalls = (IHasData<T>) mView;
        }
        mContainerView.addView(mView);
        isViewInflated = true;
    }

    public void setInflatedView(View view) {
        //mView = view;
        if (view instanceof IHasData) {
            mIntCalls = (IHasData<T>) view;
        }
        mContainerView.addView(view);
        isViewInflated = true;
    }

    public T getObjectData() {
        if (mIntCalls != null) {
            return mIntCalls.getGenericData();
        }
        Log.i("ExpandableView", "View inflated in the container does not implement IHasData, " +
                "therefore it cannot fetch data");
        return null;
    }

    public void onEventAsync(LoadDataEvent event) {
        if (mIntCalls != null) {
            Log.i("ExpandableView", "LoadingData");
            mIntCalls.loadData(event.ID);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_title_frame) {
            switchState();
        }
    }

    private void initState() {
        if (isExpanded) {
            openContainer();
        } else {
            closeContainer();
        }
    }

    private void switchState() {
        if (isExpanded) {
            closeContainer();
        } else {
            openContainer();
        }
    }

    private void openContainer() {
        isExpanded = true;
        if (mContainerView.getVisibility() != VISIBLE) {
            mContainerView.setVisibility(VISIBLE);
            mArrow.setImageResource(R.drawable.arrow_down);
        }

    }

    private void closeContainer() {
        isExpanded = false;
        if (mContainerView.getVisibility() != GONE) {
            mArrow.setImageResource(R.drawable.arrow_up);
            mContainerView.setVisibility(GONE);
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("stateToSave", this.isExpanded ? 1 : 0);
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            this.isExpanded = bundle.getInt("stateToSave") > 0;
            initState();
            state = bundle.getParcelable("instanceState");
        }
        super.onRestoreInstanceState(state);
    }

    public static class LoadDataEvent {
        private int ID;

        public LoadDataEvent(int id) {
            ID = id;
        }
    }
}
    /*
    @Override
    protected Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        ViewSavedState ss = new ViewSavedState(superState);
        //end
        ss.isExpanded = this.isExpanded;
        return ss;

    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        if (!(state instanceof ViewSavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        ViewSavedState ss = (ViewSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        this.isExpanded = ss.isExpanded;
    }

    static class ViewSavedState extends BaseSavedState {
        boolean isExpanded;

        ViewSavedState(Parcelable superState) {
            super(superState);
        }

        private ViewSavedState(Parcel in) {
            super(in);
            this.isExpanded = in.readInt() > 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            int stateToSave = (isExpanded) ? 1 : 0;
            out.writeInt(stateToSave);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<ViewSavedState> CREATOR =
                new Parcelable.Creator<ViewSavedState>() {
                    public ViewSavedState createFromParcel(Parcel in) {
                        return new ViewSavedState(in);
                    }

                    public ViewSavedState[] newArray(int size) {
                        return new ViewSavedState[size];
                    }
                };
    }
*/

