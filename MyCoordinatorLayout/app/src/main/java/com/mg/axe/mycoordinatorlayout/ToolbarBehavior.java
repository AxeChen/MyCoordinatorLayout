package com.mg.axe.mycoordinatorlayout;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.mg.axe.mycoordinatorlayout.base.MyBehavior;

/**
 * Created by Axe on 2017/9/19.
 */

public class ToolbarBehavior extends MyBehavior {

    private int maxHeight = 400;

    public ToolbarBehavior(Context context, AttributeSet set) {
        super(context, set);
    }

    @Override
    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (scrollView.getScrollY() <= maxHeight) {
            ViewCompat.setAlpha(target, scrollView.getScrollY() / maxHeight*1.0f);
        } else if (scrollView.getScrollY() == 0) {
            ViewCompat.setAlpha(target, 0);
        }
    }
}
