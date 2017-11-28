package com.jqk.pdrandpullibrary.pullview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/4/24.
 */

public class PullableRecyclerView extends RecyclerView implements Pullable {
    private static final String TAG = "pdrandpul";

    private boolean canPullDown, canPullUp;

    public PullableRecyclerView(Context context) {
        super(context);
    }

    public PullableRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PullableRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    //    RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部

    @Override
    public boolean canPullDown() {

        if (canScrollVertically(-1)) {
            canPullDown = false;
        } else {
            canPullDown = true;
        }

        return canPullDown;
    }

    //    RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部

    @Override
    public boolean canPullUp() {

        if (!canScrollVertically(1)) {
            if (getLinearTotalHeight(this) <= getHeight()) {
                canPullUp = false;
            } else {
                canPullUp = true;
            }

        } else {
            canPullUp = false;
        }

        return canPullUp;
    }

    public static int getItemHeight(RecyclerView recyclerView) {
        int itemHeight = 0;
        View child = null;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstPos = layoutManager.findFirstCompletelyVisibleItemPosition();
        int lastPos = layoutManager.findLastCompletelyVisibleItemPosition();
        child = layoutManager.findViewByPosition(lastPos);
        if (child != null) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            itemHeight = child.getHeight() + params.topMargin + params.bottomMargin;
        }
        return itemHeight;
    }

    public static int getLinearScrollY(RecyclerView recyclerView) {
        int scrollY = 0;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int headerCildHeight = 0;
        int firstPos = layoutManager.findFirstVisibleItemPosition();
        View child = layoutManager.findViewByPosition(firstPos);
        int itemHeight = getItemHeight(recyclerView);
        if (child != null) {
            int firstItemBottom = layoutManager.getDecoratedBottom(child);
            scrollY = headerCildHeight + itemHeight * firstPos - firstItemBottom;
            if (scrollY < 0) {
                scrollY = 0;
            }
        }
        return scrollY;
    }

    public static int getLinearTotalHeight(RecyclerView recyclerView) {
        int totalHeight = 0;
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View child = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
        int headerCildHeight = 0;
        if (child != null) {
            int itemHeight = getItemHeight(recyclerView);
            int childCount = layoutManager.getItemCount();
            totalHeight = headerCildHeight + (childCount - 1) * itemHeight;
        }
        return totalHeight;
    }

    public static boolean isLinearBottom(RecyclerView recyclerView) {
        boolean isBottom = true;
        int scrollY = getLinearScrollY(recyclerView);
        int totalHeight = getLinearTotalHeight(recyclerView);
        int height = recyclerView.getHeight();
        //    Log.e("height","scrollY  " + scrollY + "  totalHeight  " +  totalHeight + "  recyclerHeight  " + height);
        if (scrollY + height < totalHeight) {
            isBottom = false;
        }
        return isBottom;
    }
}
