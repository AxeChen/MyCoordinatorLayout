package com.mg.axe.mycoordinatorlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mg.axe.mycoordinatorlayout.base.MyBehavior;
import com.mg.axe.mycoordinatorlayout.base.MyCoordinatorLayout;

/**
 * Created by Axe on 2017/9/19.
 */

public class ImageViewBehavior extends MyBehavior {

    private int maxHeight = 400;
    private int originHeight ;
    public ImageViewBehavior(Context context, AttributeSet set) {
        super(context, set);
    }

    /**
     * 绘制完成的方法
     * @param parent
     * @param child
     */
    @Override
    public void onLayoutFinish(View parent, View child) {
        super.onLayoutFinish(parent, child);
        if(originHeight==0){
            originHeight = child.getHeight();
        }
    }

    @Override
    public void onNestedScroll(View scrollView, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if(scrollView.getScrollY()>0){
            ViewGroup.LayoutParams params = target.getLayoutParams();
            params.height = params.height-Math.abs(dyConsumed);
            if(params.height<originHeight){
                params.height = originHeight;
            }
            target.setLayoutParams(params);
        }else if (scrollView.getScrollY()==0){
            // ImageView 处于最大
            ViewGroup.LayoutParams params = target.getLayoutParams();
            params.height = params.height+Math.abs(dyUnconsumed);
            if(params.height>= maxHeight){
                params.height =maxHeight;
            }
            target.setLayoutParams(params);
        }
    }
}
