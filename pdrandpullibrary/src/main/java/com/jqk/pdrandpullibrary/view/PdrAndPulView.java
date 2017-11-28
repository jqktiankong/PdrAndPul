package com.jqk.pdrandpullibrary.view;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jqk.pdrandpullibrary.R;
import com.jqk.pdrandpullibrary.pullview.Pullable;

/**
 * Created by YASCN on 2017/5/13.
 */

public class PdrAndPulView extends RelativeLayout {
    private static final String TAG = "pdrandpul";

    private static final int REFRESH_PULLING = 0;
    private static final int REFRESH_READY = 1;
    private static final int REFRESH_LOADING = 2;
    private static final int REFRESH_OVER = 3;
    private static final int REFRESH_COMPLETE = 4;

    private static final int LOADMORE_PULLING = 5;
    private static final int LOADMORE_READY = 6;
    private static final int LOADMORE_LOADING = 7;
    private static final int LOADMORE_OVER = 8;
    private static final int LOADMORE_COMPLETE = 9;

    private static final int NOMORE = 10;

    private static final int CLOSE_DOWN = 1;
    private static final int CLOSE_UP = 2;

    private static final int closeAnimDuration = 250;
    private static final int arrowAnimDuration = 100;
    private static final int completePauseTime = 500;

    private float scrollY = 0;
    private boolean isToolbarAnimOver = true;

    private View refreshView;
    private View loadmoreView;
    private View contentView;

    private ImageView ivRefreshArrow, ivRefreshLoading, ivRefreshRight, ivRefreshFail;
    private TextView tvRefreshMessage;

    private ImageView ivLoadmoreArrow, ivLoadmoreLoading, ivLoadmoreRight, ivLoadmoreFail;
    private TextView tvLoadmoreMessage;

    private ValueAnimator closeAnim;
    private ObjectAnimator arrowAnim;
    private ValueAnimator toolbarAnim;

    private ObjectAnimator rippleAnim;

    // 1 箭头向上，0 箭头向下
    private int arrowType = 0;

    private int upArrowType = 1;

    private int llRefreshVeiwHeight;

    private int llLoadmoreViewHeight;

    private float pullDownY = 0;
    private float pullUpY = 0;

    private int mActivePointerId = 0;

    private float mLastMotionY = 0;

    private int refreshType = REFRESH_PULLING;

    private int loadmoreType = LOADMORE_PULLING;

    private boolean init = true;

    private float radio = 3;

    private boolean canPullDown, canPullUp;

    private boolean refreshSuccess, loadmoreSuccess;

    private boolean downEvent = true;

    private boolean setCanPulldown = true, setCanPullup = true;

    private boolean nomore = false;

    private float density;

    private OnRefreshFinishListener onRefreshFinishListener;

    public void setOnRefreshFinishListener(OnRefreshFinishListener onRefreshFinishListener) {
        this.onRefreshFinishListener = onRefreshFinishListener;
    }

    public interface OnRefreshFinishListener {
        void onReflesh();

        void onLoadmore();
    }

    public PdrAndPulView(Context context) {
        super(context);
    }

    public PdrAndPulView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PdrAndPulView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_LOADING:
                    refreshType = REFRESH_LOADING;
                    if (onRefreshFinishListener != null) {
                        onRefreshFinishListener.onReflesh();
                    }
                    closeAnim(false, pullDownY, llRefreshVeiwHeight, CLOSE_DOWN);
                    break;
                case REFRESH_OVER:

                    break;
                case REFRESH_COMPLETE:
                    closeAnim(true, pullDownY, 0, CLOSE_DOWN);
                    break;
                case LOADMORE_LOADING:
                    loadmoreType = LOADMORE_LOADING;
                    if (onRefreshFinishListener != null) {
                        onRefreshFinishListener.onLoadmore();
                    }
                    closeAnim(false, pullUpY, -llLoadmoreViewHeight, CLOSE_UP);
                    break;
                case LOADMORE_COMPLETE:
                    closeAnim(true, pullUpY, 0, CLOSE_UP);
                    break;
            }
        }
    };


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void init() {

        refreshView = getChildAt(0);
        contentView = getChildAt(1);
        loadmoreView = getChildAt(2);

        ivRefreshArrow = (ImageView) refreshView.findViewById(R.id.refreshArrow);
        tvRefreshMessage = (TextView) refreshView.findViewById(R.id.refreshMessage);
        ivRefreshLoading = (ImageView) refreshView.findViewById(R.id.refreshLoading);
        ivRefreshRight = (ImageView) refreshView.findViewById(R.id.refreshRight);
        ivRefreshFail = (ImageView) refreshView.findViewById(R.id.refreshFail);

        ivLoadmoreArrow = (ImageView) loadmoreView.findViewById(R.id.loadmoreArrow);
        tvLoadmoreMessage = (TextView) loadmoreView.findViewById(R.id.loadmoreMessage);
        ivLoadmoreLoading = (ImageView) loadmoreView.findViewById(R.id.loadmoreLoading);
        ivLoadmoreRight = (ImageView) loadmoreView.findViewById(R.id.loadmoreRight);
        ivLoadmoreFail = (ImageView) loadmoreView.findViewById(R.id.loadmoreFail);

        AnimatorSet setHeader = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.anim.refresh_loading);
        AnimatorSet setFooter = (AnimatorSet) AnimatorInflater.loadAnimator(getContext(),
                R.anim.refresh_loading);
        LinearInterpolator lir = new LinearInterpolator();
        setHeader.setInterpolator(lir);
        setHeader.setTarget(ivRefreshLoading);
        setHeader.start();

        setFooter.setInterpolator(lir);
        setFooter.setTarget(ivLoadmoreLoading);
        setFooter.start();

        ivRefreshArrow.setImageResource(R.drawable.ic_pull_down);
        tvRefreshMessage.setText("下拉刷新");
        arrowBottomAnim();

        ivLoadmoreArrow.setImageResource(R.drawable.ic_pull_up);
        tvLoadmoreMessage.setText("上拉刷新");
        upArrowTopAnim();

        llRefreshVeiwHeight = ((ViewGroup) refreshView).getChildAt(0).getMeasuredHeight();
        llLoadmoreViewHeight = ((ViewGroup) loadmoreView).getChildAt(0).getMeasuredHeight();

        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();

        density = dm.density;
    }

    public void setRefresh() {
        switch (refreshType) {
            case REFRESH_PULLING:
                ivRefreshLoading.setVisibility(View.INVISIBLE);
                ivRefreshArrow.setVisibility(View.VISIBLE);
                ivRefreshRight.setVisibility(View.INVISIBLE);
                ivRefreshFail.setVisibility(View.INVISIBLE);
                tvRefreshMessage.setText("下拉刷新");
                arrowBottomAnim();
                break;
            case REFRESH_READY:
                ivRefreshLoading.setVisibility(View.INVISIBLE);
                ivRefreshArrow.setVisibility(View.VISIBLE);
                ivRefreshRight.setVisibility(View.INVISIBLE);
                ivRefreshFail.setVisibility(View.INVISIBLE);
                tvRefreshMessage.setText("释放立即刷新");
                arrowTopAnim();
                break;
            case REFRESH_LOADING:
                ivRefreshLoading.setVisibility(View.VISIBLE);
                ivRefreshArrow.setVisibility(View.INVISIBLE);
                ivRefreshRight.setVisibility(View.INVISIBLE);
                ivRefreshFail.setVisibility(View.INVISIBLE);
                tvRefreshMessage.setText("正在刷新");
                break;
            case REFRESH_OVER:
                ivRefreshLoading.setVisibility(View.INVISIBLE);
                ivRefreshArrow.setVisibility(View.INVISIBLE);

                if (refreshSuccess) {
                    ivRefreshRight.setVisibility(View.VISIBLE);
                    ivRefreshFail.setVisibility(View.INVISIBLE);
                    tvRefreshMessage.setText("刷新完毕");
                } else {
                    ivRefreshRight.setVisibility(View.INVISIBLE);
                    ivRefreshFail.setVisibility(View.VISIBLE);
                    tvRefreshMessage.setText("刷新失败");
                }
                break;
        }
    }

    public void setLoadmore() {

//        Log.d(TAG, "loadmoreType = " + loadmoreType);

        switch (loadmoreType) {
            case LOADMORE_PULLING:
                ivLoadmoreLoading.setVisibility(View.INVISIBLE);
                ivLoadmoreArrow.setVisibility(View.VISIBLE);
                ivLoadmoreRight.setVisibility(View.INVISIBLE);
                ivLoadmoreFail.setVisibility(View.INVISIBLE);
                tvLoadmoreMessage.setText("上拉加载更多");
                upArrowTopAnim();
                break;
            case LOADMORE_READY:
                ivLoadmoreLoading.setVisibility(View.INVISIBLE);
                ivLoadmoreArrow.setVisibility(View.VISIBLE);
                ivLoadmoreRight.setVisibility(View.INVISIBLE);
                ivLoadmoreFail.setVisibility(View.INVISIBLE);
                tvLoadmoreMessage.setText("释放立即加载");
                upArrowBottomAnim();
                break;
            case LOADMORE_LOADING:
                ivLoadmoreLoading.setVisibility(View.VISIBLE);
                ivLoadmoreArrow.setVisibility(View.INVISIBLE);
                ivLoadmoreRight.setVisibility(View.INVISIBLE);
                ivLoadmoreFail.setVisibility(View.INVISIBLE);
                tvLoadmoreMessage.setText("正在加载");
                break;
            case LOADMORE_OVER:
                ivLoadmoreLoading.setVisibility(View.INVISIBLE);
                ivLoadmoreArrow.setVisibility(View.INVISIBLE);
                if (loadmoreSuccess) {
                    ivLoadmoreRight.setVisibility(View.VISIBLE);
                    ivLoadmoreFail.setVisibility(View.INVISIBLE);
                    tvLoadmoreMessage.setText("加载完毕");
                } else {
                    ivLoadmoreRight.setVisibility(View.INVISIBLE);
                    ivLoadmoreFail.setVisibility(View.VISIBLE);
                    tvLoadmoreMessage.setText("加载失败");
                }
                break;
            case NOMORE:
                ivLoadmoreLoading.setVisibility(View.INVISIBLE);
                ivLoadmoreArrow.setVisibility(View.INVISIBLE);
                tvLoadmoreMessage.setText("已经到底了！");
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (init) {
            init();
            init = false;
        }

        setRefresh();
        setLoadmore();

        refreshView.layout(0,
                (int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
                refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
        contentView.layout(0, (int) (pullDownY + pullUpY),
                contentView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
                        + contentView.getMeasuredHeight());
        loadmoreView.layout(0,
                (int) (pullDownY + pullUpY) + contentView.getMeasuredHeight(),
                loadmoreView.getMeasuredWidth(),
                (int) (pullDownY + pullUpY) + contentView.getMeasuredHeight()
                        + loadmoreView.getMeasuredHeight());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getActionIndex();
                mLastMotionY = ev.getY(mActivePointerId);

                canPullDown = true;
                canPullUp = true;

                break;
            case MotionEvent.ACTION_MOVE:

                scrollY = ev.getY(mActivePointerId) - mLastMotionY;

                if ((pullDownY > 0 || ((Pullable) contentView).canPullDown() && canPullDown && loadmoreType != LOADMORE_LOADING) && setCanPulldown) {
                    // 下拉操作
                    pullDownY = pullDownY + (ev.getY(mActivePointerId) - mLastMotionY) / radio;

                    if (pullDownY < 0) {
                        pullDownY = 0;
                        canPullDown = false;
                        canPullUp = true;

                    } else {
                        canPullDown = true;
                        canPullUp = false;

                    }

                    if (pullDownY < llRefreshVeiwHeight) {
                        if (refreshType != REFRESH_LOADING && refreshType != REFRESH_OVER) {
                            refreshType = REFRESH_PULLING;
                        }
                    }

                    if (pullDownY >= llRefreshVeiwHeight) {
                        if (refreshType != REFRESH_LOADING && refreshType != REFRESH_OVER) {
                            refreshType = REFRESH_READY;
                        }
                    }

                    if (Math.abs(pullDownY) > 0) {
                        downEvent = true;
                    }

                } else if ((pullUpY < 0 || ((Pullable) contentView).canPullUp() && canPullUp && refreshType != REFRESH_LOADING) && setCanPullup) {
                    // 上拉操作
                    pullUpY = pullUpY + (ev.getY(mActivePointerId) - mLastMotionY) / radio;

                    if (pullUpY > 0) {
                        pullUpY = 0;
                        canPullDown = true;
                        canPullUp = false;
                    } else {
                        canPullDown = false;
                        canPullUp = true;
                    }

                    if (nomore) {
                        loadmoreType = NOMORE;
                    } else {
                        if (Math.abs(pullUpY) < llLoadmoreViewHeight) {
                            if (loadmoreType != LOADMORE_LOADING && loadmoreType != LOADMORE_OVER) {
                                loadmoreType = LOADMORE_PULLING;
                            }
                        }

                        if (Math.abs(pullUpY) >= llLoadmoreViewHeight) {
                            if (loadmoreType != LOADMORE_LOADING && loadmoreType != LOADMORE_OVER) {
                                loadmoreType = LOADMORE_READY;
                            }
                        }
                    }

                    if (Math.abs(pullUpY) > 0) {
                        downEvent = true;
                    }
                } else {
                    canPullDown = true;
                    canPullUp = true;

                    if (downEvent) {
                        downEvent = false;
//                        sendDownEvent(ev);
                    }

                }

                if ((pullDownY + Math.abs(pullUpY)) > 8) {
                    // 防止下拉过程中误触发长按事件和点击事件
                    sendCancelEvent(ev);
                }

                mLastMotionY = ev.getY(mActivePointerId);
                radio = (float) (3 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
                        * Math.abs(pullDownY)));
                requestLayout();
                break;
            case MotionEvent.ACTION_UP:

                if (canPullUp && canPullDown) {
                    if (refreshType == REFRESH_LOADING) {
                        if (pullDownY > 0) {
                            closeAnim(false, pullDownY, llRefreshVeiwHeight, CLOSE_DOWN);
                        }
                    }

                    if (loadmoreType == LOADMORE_LOADING) {
                        if (pullUpY < 0) {
                            closeAnim(false, pullUpY, -llLoadmoreViewHeight, CLOSE_UP);
                        }
                    }
                } else if (canPullDown) {
                    if (refreshType == REFRESH_READY) {
                        handler.sendEmptyMessage(REFRESH_LOADING);
                    }

                    if (refreshType == REFRESH_PULLING) {
                        closeAnim(false, pullDownY, 0, CLOSE_DOWN);
                    }

                    if (refreshType == REFRESH_LOADING) {
                        closeAnim(false, pullDownY, llRefreshVeiwHeight, CLOSE_DOWN);
                    }

                    if (refreshType == REFRESH_OVER) {
                        closeAnim(true, pullDownY, 0, CLOSE_DOWN);
                        handler.removeMessages(REFRESH_COMPLETE);
                    }
                } else if (canPullUp) {
                    if (loadmoreType == LOADMORE_READY) {
                        handler.sendEmptyMessage(LOADMORE_LOADING);
                    }

                    if (loadmoreType == LOADMORE_PULLING) {
                        closeAnim(false, pullUpY, 0, CLOSE_UP);
                    }

                    if (loadmoreType == LOADMORE_LOADING) {
                        closeAnim(false, pullUpY, -llLoadmoreViewHeight, CLOSE_UP);
                    }

                    if (loadmoreType == LOADMORE_OVER) {
                        closeAnim(true, pullUpY, 0, CLOSE_UP);
                        handler.removeMessages(LOADMORE_COMPLETE);
                    }

                    if (loadmoreType == NOMORE) {
                        closeAnim(true, pullUpY, 0, CLOSE_UP);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean sendCancelEvent(MotionEvent event) {
        MotionEvent last = event;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        return super.dispatchTouchEvent(e);
    }

    private boolean sendDownEvent(MotionEvent event) {
        final MotionEvent last = event;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime(), MotionEvent.ACTION_DOWN, last.getX(), last.getY(), last.getMetaState());
        return super.dispatchTouchEvent(e);
    }

    public void refreshComplete(boolean success) {
        refreshSuccess = success;
        refreshType = REFRESH_OVER;
        setRefresh();
        handler.sendEmptyMessageDelayed(REFRESH_COMPLETE, completePauseTime);
    }

    public void loadmoreComplete(boolean success) {
        loadmoreSuccess = success;
        loadmoreType = LOADMORE_OVER;
        setLoadmore();
        handler.sendEmptyMessageDelayed(LOADMORE_COMPLETE, completePauseTime);
    }

    public void closeAnim(final boolean finish, final float disY, float endY, final int closeType) {
        closeAnim = ObjectAnimator.ofFloat(null, "y", disY, endY);
        closeAnim.setDuration(closeAnimDuration);

        closeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (closeType == CLOSE_DOWN) {
                    pullDownY = (float) animation.getAnimatedValue();
                } else if (closeType == CLOSE_UP) {
                    pullUpY = (float) animation.getAnimatedValue();
                }

                requestLayout();
            }
        });

        closeAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (finish) {
                    if (closeType == CLOSE_DOWN) {
                        refreshType = REFRESH_PULLING;
                        setRefresh();
                    } else if (closeType == CLOSE_UP) {
                        loadmoreType = LOADMORE_PULLING;
                        setLoadmore();
                    }

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        closeAnim.setInterpolator(new DecelerateInterpolator());
        closeAnim.start();
    }

    public void arrowTopAnim() {
        if (arrowType == 1) {
            arrowAnim = ObjectAnimator.ofFloat(ivRefreshArrow, "rotation", 0, 180);
            arrowAnim.setDuration(arrowAnimDuration);
            arrowAnim.start();
            arrowType = 0;
        }
    }

    public void arrowBottomAnim() {
        if (arrowType == 0) {
            arrowAnim = ObjectAnimator.ofFloat(ivRefreshArrow, "rotation", 180, 360);
            arrowAnim.setDuration(arrowAnimDuration);
            arrowAnim.start();
            arrowType = 1;
        }
    }

    public void upArrowBottomAnim() {
        if (upArrowType == 1) {
            arrowAnim = ObjectAnimator.ofFloat(ivLoadmoreArrow, "rotation", 0, 180);
            arrowAnim.setDuration(arrowAnimDuration);
            arrowAnim.start();
            upArrowType = 0;
        }
    }

    public void upArrowTopAnim() {
        if (upArrowType == 0) {
            arrowAnim = ObjectAnimator.ofFloat(ivLoadmoreArrow, "rotation", 180, 360);
            arrowAnim.setDuration(arrowAnimDuration);
            arrowAnim.start();
            upArrowType = 1;
        }
    }

    public void setCanPulldown(boolean setCanPulldown) {
        this.setCanPulldown = setCanPulldown;
    }

    public void setCanPullup(boolean setCanPullup) {
        this.setCanPullup = setCanPullup;
        loadmoreType = LOADMORE_PULLING;
    }

    public void setNomore(boolean nomore) {
        this.nomore = nomore;
    }

    public boolean isLoading() {
        if (refreshType == REFRESH_LOADING || loadmoreType == LOADMORE_LOADING) {
            return true;
        } else {
            return false;
        }
    }
}
