package com.mg.axe.mycoordinatorlayout.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.NestedScrollingParent;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.mg.axe.mycoordinatorlayout.R;

import java.lang.reflect.Constructor;

import static android.content.ContentValues.TAG;

/**
 * Created by Axe on 2017/9/17.
 */

public class MyCoordinatorLayout extends RelativeLayout implements NestedScrollingParent, ViewTreeObserver.OnGlobalLayoutListener {

    public MyCoordinatorLayout(Context context) {
        this(context, null);
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCoordinatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 布局加载结束
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    /**
     * 会将属性封装成AttributeSet
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * 布局发生变化时的事件
     */
    @Override
    public void onGlobalLayout() {
        for (int i = 0; i > getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.getBehavior() != null) {
                layoutParams.getBehavior().onLayoutFinish(this, child);
            }
        }
    }

    public static class LayoutParams extends RelativeLayout.LayoutParams {

        private MyBehavior behavior;

        public MyBehavior getBehavior() {
            return behavior;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            final TypedArray a = c.obtainStyledAttributes(attrs,
                    R.styleable.MyCoordinatorLayout);
            behavior = parseBehavior(c, attrs, a.getString(
                    R.styleable.MyCoordinatorLayout_layout_behavior));
            Log.i(TAG, "LayoutParams:   名字   " + a.getString(
                    R.styleable.MyCoordinatorLayout_layout_behavior));
            a.recycle();
        }

        private MyBehavior parseBehavior(Context context, AttributeSet attrs, String name) {

            if (TextUtils.isEmpty(name)) {
                return null;
            }
            try {
                final Class clazz = Class.forName(name, true,
                        context.getClassLoader());
                Constructor c = clazz.getConstructor(new Class[]{Context.class, AttributeSet.class});
                c.setAccessible(true);
                return (MyBehavior) c.newInstance(context, attrs);
            } catch (Exception e) {
                throw new RuntimeException("Could not inflate Behavior subclass " + name, e);
            }
        }
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                onTouchMove(event);
                break;
        }

        return super.onTouchEvent(event);
    }

    // 将滑动事件通过behavior传递
    private void onTouchMove(MotionEvent event) {

        float moveX = event.getRawX();
        float moveY = event.getRawY();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.getBehavior() != null) {
                layoutParams.getBehavior().onTouchMove(this, child, event, moveX, moveY, lastX, lastY);
            }
        }

        lastY = moveY;
        lastX = moveX;
    }

    // 滚动控件（包含滚动控件）  把滚动产生的事件全部转到behavior中去

    /**
     * 该方法决定是否能接收到子控件View的滚动事件
     * 返回true，可以接收，返回false，不能接收
     *
     * @param child
     * @param target
     * @param nestedScrollAxes
     * @return
     */
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return true;
    }

    /**
     * 停止滚动
     *
     * @param child
     */
    @Override
    public void onStopNestedScroll(View child) {
//        super.onStopNestedScroll(child);
    }

    /**
     * 接收滚动
     * @param child
     * @param target
     * @param axes
     */
    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
//        super.onNestedScrollAccepted(child, target, axes);
    }



    /**
     * 惯性滑动
     * @param target
     * @param velocityX
     * @param velocityY
     * @param consumed
     * @return
     */
    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
//        return super.onNestedFling(target, velocityX, velocityY, consumed);
        return false;
    }

    /**
     * 准备惯性滑动
     * @param target
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
//        return super.onNestedPreFling(target, velocityX, velocityY);
        return false;
    }

    /**
     *
     * @param target
     * @param dxConsumed
     * @param dyConsumed
     * @param dxUnconsumed
     * @param dyUnconsumed
     */
    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
            if (layoutParams.getBehavior() != null) {
                layoutParams.getBehavior().onNestedScroll(target, child, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
            }
        }

    }

    /**
     * 准备滑动
     * @param target
     * @param dx
     * @param dy
     * @param consumed
     */
    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
//        super.onNestedPreScroll(target, dx, dy, consumed);
    }



}
