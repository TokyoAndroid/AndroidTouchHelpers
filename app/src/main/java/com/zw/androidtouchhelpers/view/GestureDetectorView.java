package com.zw.androidtouchhelpers.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/8/24.
 */

public class GestureDetectorView extends LinearLayout {

    private GestureDetector mGestureDetector;

    public GestureDetectorView(Context context) {
        this(context, null);
    }

    public GestureDetectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureDetectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mGestureDetector = new GestureDetector(context, new MyGestureDetectorListener(this));
        //设置双击事件监听
        mGestureDetector.setOnDoubleTapListener(new MyGestureDetectorDoubleTapListener());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }


    private static class MyGestureDetectorListener implements GestureDetector.OnGestureListener{

        private GestureDetectorView mView;

        public MyGestureDetectorListener(GestureDetectorView view){
            this.mView = view;
        }

        /**
         * 用户按下屏幕的时候回调
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            Log.e("ll", "MyGestureDetectorListener : onDown");
            return true;
        }

        /**
         * 用户按下屏幕100ms后，如果还没有松手或者移动就会回调
         * @param motionEvent
         */
        @Override
        public void onShowPress(MotionEvent motionEvent) {
            Log.e("ll", "MyGestureDetectorListener : onShowPress");
        }

        /**
         * 单纯的点击再抬手时调用。用户手指松开（UP事件）的时候如果没有执行onScroll()和onLongPress()这两个回调的话，就会回调
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Log.e("ll", "MyGestureDetectorListener : onSingleTapUp");
            return false;
        }

        /**
         * 屏幕拖动事件，如果按下的时间过长，调用了onLongPress，再拖动屏幕不会触发onScroll。
         * 拖动屏幕会多次触发
         * @param motionEvent 开始拖动的第一次按下down操作,也就是第一个ACTION_DOWN
         * @param motionEvent1 触发当前onScroll方法的ACTION_MOVE
         * @param distanceX 当前的x坐标与最后一次触发scroll方法的x坐标的差值。
         * @param distanceY 当前的y坐标与最后一次触发scroll方法的y坐标的差值。
         * @return
         */
        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float distanceX, float distanceY) {
            Log.e("ll", "MyGestureDetectorListener : onScroll : " + distanceY);
            mView.scrollBy(0, (int) distanceY);
            return false;
        }

        /**
         * 用户长按后（好像不同手机的时间不同，源码里默认是100ms+500ms）触发，触发之后不会触发其他回调，直至松开（UP事件）。
         * @param motionEvent
         */
        @Override
        public void onLongPress(MotionEvent motionEvent) {
            Log.e("ll", "MyGestureDetectorListener : onLongPress");
        }

        /**
         * 按下屏幕，在屏幕上快速滑动后松开，由一个down,多个move,一个up触发
         * @param motionEvent 开始快速滑动的第一次按下down操作,也就是第一个ACTION_DOWN
         * @param motionEvent1 触发当前onFling方法的move操作,也就是最后一个ACTION_MOVE
         * @param velocityX X轴上的移动速度，像素/秒
         * @param velocityY Y轴上的移动速度，像素/秒
         * @return
         */
        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float velocityX, float velocityY) {
            Log.e("ll", "MyGestureDetectorListener : onFling");
            return false;
        }
    }

    private static class MyGestureDetectorDoubleTapListener implements GestureDetector.OnDoubleTapListener{

        /**
         * 单击事件。用来判定该次点击是单纯的SingleTap而不是DoubleTap，如果连续点击两次就是DoubleTap手势，
         * 如果只点击一次，系统等待一段时间后没有收到第二次点击则判定该次点击为SingleTap而不是DoubleTap，然后触发SingleTapConfirmed事件。
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            Log.e("ll","MyGestureDetectorDoubleTapListener : onSingleTapConfirmed");
            return false;
        }

        /**
         * 双击触发
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            Log.e("ll","MyGestureDetectorDoubleTapListener : onDoubleTap");
            return false;
        }

        /**
         * 双击间隔中发生的动作。指触发onDoubleTap以后，在双击之间发生的其它动作，包含down、up和move事件
         * @param motionEvent
         * @return
         */
        @Override
        public boolean onDoubleTapEvent(MotionEvent motionEvent) {
            Log.e("ll","MyGestureDetectorDoubleTapListener : onDoubleTapEvent");
            return false;
        }
    }


    /**
     * 需要最低API为23
     */
/*    private static class MyGestureDetectorContextClickListener implements GestureDetector.OnContextClickListener{

        *//**
         * 当鼠标/触摸板，右键点击时候的回调。
         * @param motionEvent
         * @return
         *//*
        @Override
        public boolean onContextClick(MotionEvent motionEvent) {
            return false;
        }
    }*/

}