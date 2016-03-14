package com.codez.customviews.exp_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.Bindable;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.databinding.tool.DataBinder;
import android.databinding.tool.DataBindingBuilder;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.codez.customviews.BR;
import com.codez.customviews.R;
import com.codez.customviews.TextViewPlus;

import de.greenrobot.event.EventBus;

//TODO Add default animation for closing/opening the view as well as support for custom animation

/**
 * Class which supports opening and collapsing, it supports custom views that can be extended
 * The views added to this view can return Data as well which can be caught via the
 * IHasData interface
 * Created by Claudiu on 12/16/2015.
 */
public class ExpandableView<T> extends FrameLayout implements View.OnClickListener {

    boolean isExpanded = true;
    public int mResId = -1;
    private boolean isViewInflated = false;
    private IHasData<T> mIntCalls;
    private FrameLayout mContainerView;
    private TextViewPlus mTitle;
    private ImageView mArrow;

    private String mTitleString;

    public ExpandableView(Context context, int resId) {
        super(context);
        setUp();
        mResId = resId;
    }

    public ExpandableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUp();
        setCustomTitle(context, attrs);
    }

    public ExpandableView(Context context) {
        super(context);
        setUp();
    }

    public ExpandableView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setUp();
        setCustomTitle(context, attrs);
    }

    /**
     * Attribute for xml to set the title of the view
     *
     * @param context
     * @param attrs
     */
    private void setCustomTitle(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableView);
        String customTitle = a.getString(R.styleable.ExpandableView_customTitle);
        if (!TextUtils.isEmpty(customTitle)) {
            setTitle(customTitle);
        }
        a.recycle();
    }

    private void setUp() {
        inflate(getContext(), R.layout.c_extendable_container, this);
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
        if (!TextUtils.isEmpty(mTitleString)) {
            mTitle.setText(mTitleString);
        }
        initState();
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

    /**
     * Disable the child views of this layout, In order for this to function
     * The ChildrenViews need to implement IDisableable interface
     */
    public void disableView() {
        if (mIntCalls != null)
            mIntCalls.disableView();
        else {
            Log.i("ExpandableView", "View inflated in the container does not implement IDisableable, " +
                    "therefore it cannot be disabled");
        }
    }

    public void setViewResId(int resId) {
        mResId = resId;
        if (mContainerView != null && !isViewInflated) {
            inflateView();
        }
    }

    /**
     * Assign the ID of a xml layout view which will be used for the main container
     *
     * @param title the title to be set to the view
     * @param resId the resource identifier via R.java to be used to inflate the
     *              main container view
     */
    public void setTitleAndViewResource(String title, int resId) {
        setTitle(title);
        setViewResId(resId);
    }

    /**
     * Sets the title of this view
     *
     * @param title the title to be set
     */
    public void setTitle(String title) {
        mTitleString = title;
        if (mTitle != null)
            mTitle.setText(title);
    }

    /**
     * Inflates the view provided via the resource id provided via
     * setTitleAndViewResource
     */
    private void inflateView() {
        View mView = inflate(getContext(), mResId, null);
        if (mView instanceof IHasData) {
            mIntCalls = (IHasData<T>) mView;
        }
        mContainerView.addView(mView);
        isViewInflated = true;
    }

    /**
     * Sets an already inflated @View to the mainContainer
     *
     * @param view the view to be set
     */
    public void setInflatedView(View view) {
        //mView = view;
        if (view instanceof IHasData) {
            mIntCalls = (IHasData<T>) view;
        }
        mContainerView.addView(view);
        isViewInflated = true;
    }

    /**
     * Returns the data persisting in the mainContainer (in order for this to work
     * the main @View class inflated in the mainContainer has to implement @IHasData
     *
     * @return the data present in the mainContainer
     */
    public T getObjectData() {
        if (mIntCalls != null) {
            return mIntCalls.getGenericData();
        }
        Log.i("ExpandableView", "View inflated in the container does not implement IHasData, " +
                "therefore it cannot fetch data");
        return null;
    }

    /**
     * Notifies the mainContaier View to load it's data.
     * In order for this to work the View inflated in the mainContainer has to
     * implement the IHasData interface
     *
     * @param event
     */
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

    /**
     * Initialises the State (if this is a recreation)
     */
    private void initState() {
        if (isExpanded) {
            openContainer();
        } else {
            closeContainer();
        }
    }

    /**
     * Switch the states between Opened and Closed
     */
    private void switchState() {
        if (isExpanded) {
            closeContainer();
        } else {
            openContainer();
        }
    }

    public void setMyState(boolean isOpened) {
        if (isOpened) {
            closeContainer();
        } else {
            openContainer();
        }
    }

    /**
     * Opens the mainContainer
     */
    private void openContainer() {
        isExpanded = true;
        if (mContainerView.getVisibility() != VISIBLE) {
            mContainerView.setVisibility(VISIBLE);
            mArrow.setImageResource(R.drawable.arrow_down);
        }

    }

    /**
     * Closes the mainContainer
     */
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

    /**
     * Event Object sent to this View from the parent(Activity/Fragment/Other Custom View
     * to instantiate loadData of the mainContainer (in order for this to work
     * the main @View class inflated in the mainContainer has to implement @IHasData
     */
    public static class LoadDataEvent {
        private int ID;

        public LoadDataEvent() {
        }

        public LoadDataEvent(int id) {
            ID = id;
        }
    }
}

