package com.jqk.pdrandpullibrary.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by YASCN on 2017/5/11.
 */

public class PullableFrameLayout extends FrameLayout implements Pullable {
    private static final String TAG = "pdrandpul";
    private boolean canSlideDown = false;
    private boolean canSlideUp = false;

    private int mActivePointerId = 0;

    private float mLastMotionY = 0;

    private boolean isMove = true;

    public PullableFrameLayout(Context context) {
        super(context);
    }

    public PullableFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {

        return true;
    }

    @Override
    public boolean canPullUp() {

        return false;
    }

}
