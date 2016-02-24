package com.codez.customviews.other;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout;

import com.codez.customviews.R;


/**
 * Created by eptron on 20/05/2015.
 */
public abstract class FragmentWithIndicator extends Fragment implements ViewPager.OnPageChangeListener {

    protected SlideIndicator mSlideIndicator;

    protected void initSlideIndicator(RelativeLayout layout, int indicatorCount) {
        int margin = getResources().getDimensionPixelOffset(R.dimen.medium_big_margin);
        RelativeLayout.LayoutParams slideParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        slideParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        slideParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        slideParams.setMargins(0, 0, margin, margin);

        mSlideIndicator = new SlideIndicator(getActivity(), indicatorCount);
        layout.addView(mSlideIndicator, slideParams);
    }

    @Override
    public void onPageSelected(int position) {
        mSlideIndicator.setActive(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
}
