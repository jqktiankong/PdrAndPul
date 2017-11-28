package com.jqk.pdrandpullibrary.pullview;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by YASCN on 2017/5/11.
 */

public class PullableNestedScrollView extends NestedScrollView implements Pullable {
    public PullableNestedScrollView(Context context) {
        super(context);
    }

    public PullableNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp() {
        if (getScrollY() == 0) {
            return false;
        } else {
            if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
                return true;
            else
                return false;
        }
    }
}
