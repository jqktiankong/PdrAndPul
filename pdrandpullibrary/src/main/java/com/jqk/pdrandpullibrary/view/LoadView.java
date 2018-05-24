package com.jqk.pdrandpullibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingParent;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * Created by Administrator on 2018/5/24 0024.
 */

public class LoadView extends FrameLayout implements NestedScrollingParent{

    private RelativeLayout backgroundView;

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void initView() {
        backgroundView = new RelativeLayout(getContext());
        ViewGroup.LayoutParams lp = backgroundView.getLayoutParams();
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        backgroundView.setLayoutParams(lp);

        backgroundView.addView();
    }
}
