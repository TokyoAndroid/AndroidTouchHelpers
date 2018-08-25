package com.zw.androidtouchhelpers.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.zw.androidtouchhelpers.R;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ViewDragHelperView extends RelativeLayout {


    private ViewDragHelper mViewDragHelper;

    private View childView1,childView2,childView3;

    public ViewDragHelperView(Context context) {
        this(context,null);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ViewDragHelperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        MyViewDragHelperCallback callback = new MyViewDragHelperCallback(this);
        mViewDragHelper = ViewDragHelper.create(this,1.0f, callback);
        callback.setDragHelper(mViewDragHelper);
        mViewDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT|ViewDragHelper.EDGE_TOP);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if(getChildCount() < 3) {
            throw new RuntimeException("子View不能小于3个！");
        }
        childView1 = getChildAt(0);
        childView2 = getChildAt(1);
        childView3 = getChildAt(2);

        childView1.setId(R.id.child1);
        childView2.setId(R.id.child2);
        childView3.setId(R.id.child3);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mViewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mViewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if(mViewDragHelper.continueSettling(true)){
            invalidate();
        }
        super.computeScroll();
    }

    private static class MyViewDragHelperCallback extends ViewDragHelper.Callback{

        private RelativeLayout mViewGroup;
        private ViewDragHelper mDragHelper;

        private int originLeft,originTop;

        public MyViewDragHelperCallback(RelativeLayout view){
            this.mViewGroup = view;
        }

        public void setDragHelper(ViewDragHelper dragHelper){
            this.mDragHelper = dragHelper;
        }

        /**
         * 根据传入的child确定是否需要捕获此child（对它进行操作）
         * @param child 被触摸的子View
         * @param pointerId 按下手指的id，一般多点触摸时会用来判断是哪根手指触摸
         * @return 返回true表示将要捕获这个child
         */
        @Override
        public boolean tryCaptureView(View child, int pointerId) {

            if(child.getId() == R.id.child3){
                originLeft = child.getLeft();
                originTop = child.getTop();
            }

            return true;
        }

        /**
         * 返回被横向移动的子控件child的上坐标top，和移动距离dy，我们可以根据这些值来返回child的新的top。
         * @param child
         * @param top 拖拽动作后系统建议的距离上侧的距离
         * @param dy Y轴方向拖拽移动的距离
         * @return
         */
        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return top;
        }

        /**
         * 返回被横向移动的子控件child的左坐标left，和移动距离dx，我们可以根据这些值来返回child的新的left。
         * @param child
         * @param left 拖拽动作后系统建议的距离左侧的距离
         * @param dx X轴方向拖拽移动的距离
         * @return
         */
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //不能超过父View左侧
            if(left < mViewGroup.getPaddingLeft()){
                left = mViewGroup.getPaddingLeft();
            }

            //不能超过父View右侧
            if(left + child.getWidth() > mViewGroup.getWidth() - mViewGroup.getPaddingRight()){
                left = mViewGroup.getWidth() - mViewGroup.getPaddingRight() - child.getWidth();
            }

            return left;
        }

        /**
         * 这个用来控制垂直移动的边界范围，单位是像素。
         * @param child
         * @return 返回值大于0时才能捕获子View
         */
        @Override
        public int getViewVerticalDragRange(View child) {
            return 1;
        }

        /**
         * 这个用来控制横向移动的边界范围，单位是像素。
         * @param child
         * @return 返回值大于0时才能捕获子View
         */
        @Override
        public int getViewHorizontalDragRange(View child) {
            return 1;
        }

        /**
         * 当releasedChild被释放的时候回调
         * @param releasedChild
         * @param xvel x轴方向的加速度
         * @param yvel y轴方向的加速度
         */
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            if(releasedChild.getId() == R.id.child3){
                mDragHelper.settleCapturedViewAt(originLeft,originTop);
                mViewGroup.invalidate();
            }
        }


        /**
         * 若ViewDragHelper设置了setEdgeTrackingEnabled()此方法，则调用此方法
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);
            View childView = mViewGroup.findViewById(R.id.child2);
            mDragHelper.captureChildView(childView,pointerId);
        }

        /**
         * mDragState改变时回调
         * STATE_IDLE：所有的View处于静止空闲状态
         * STATE_DRAGGING：某个View正在被用户拖动（用户正在与设备交互）
         * STATE_SETTLING：某个View正在安置状态中（用户并没有交互操作），就是自动滚动的过程中
         * @param state
         */
        @Override
        public void onViewDragStateChanged(int state) {
            super.onViewDragStateChanged(state);

        }

        /**
         * 当捕获的子View的位置发生改变时回调
         * @param changedView
         * @param left
         * @param top
         * @param dx
         * @param dy
         */
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
        }

        /**
         * 当子View被捕获时回调
         * @param capturedChild
         * @param activePointerId
         */
        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            super.onViewCaptured(capturedChild, activePointerId);
        }

        /**
         * 当触摸到边界时回调。
         * @param edgeFlags
         * @param pointerId
         */
        @Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            super.onEdgeTouched(edgeFlags, pointerId);
        }

        /**
         *
         * @param edgeFlags
         * @return true的时候会锁住当前的边界，false则unLock。
         */
        @Override
        public boolean onEdgeLock(int edgeFlags) {
            return super.onEdgeLock(edgeFlags);
        }

        /**
         * 改变同一个坐标（x,y）去寻找captureView位置的方法。(具体作用暂时未知)
         * @param index
         * @return
         */
        @Override
        public int getOrderedChildIndex(int index) {
            return super.getOrderedChildIndex(index);
        }
    }
}
