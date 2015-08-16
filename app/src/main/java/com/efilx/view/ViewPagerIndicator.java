package com.efilx.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.efilx.activity.R;


public class ViewPagerIndicator extends RadioGroup {

    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);

    }

    private int mCount;


    public void setCount(int count) {
        mCount = count;
        removeAllViews();

        for (int i = 0; i < count; i++) {

            RadioButton rb = new RadioButton(getContext());
            rb.setPadding(10,0,10,0);
            rb.setFocusable(false);
            rb.setClickable(false);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {

                Drawable d = getResources().getDrawable(R.drawable.indicator);
                rb.setButtonDrawable(d);

                LinearLayout.LayoutParams params = generateDefaultLayoutParams();
                params.width = d.getIntrinsicWidth();
                params.height = d.getIntrinsicHeight();

                rb.setLayoutParams(params);
            } else {
                rb.setButtonDrawable(R.drawable.indicator);
            }
            addView(rb);
        }
        setCurrentPosition(-1);
    }


    public void setCurrentPosition(int position) {
        if (position >= mCount) {
            position = mCount - 1;
        }
        if (position < 0) {
            position = mCount > 0 ? 0 : -1;
        }

        if (position >= 0 && position < mCount) {

            RadioButton rb = (RadioButton) getChildAt(position);
            rb.setChecked(true);
        }
    }
}