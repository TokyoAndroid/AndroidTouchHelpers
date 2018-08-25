package com.zw.androidtouchhelpers.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zw.androidtouchhelpers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2018/8/25.
 */

public class ItemTouchHelperAdapter extends RecyclerView.Adapter<ItemTouchHelperAdapter.ItemTouchHelperViewHolder> {


    private Context mContext;
    private List<String> mDatas;

    public ItemTouchHelperAdapter(Context context){
        this.mContext = context;

        mDatas = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            mDatas.add("第" + i + "个100天");
        }
    }

    @Override
    public ItemTouchHelperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_touch_helper_item, parent,false);
        ItemTouchHelperViewHolder viewHolder = new ItemTouchHelperViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ItemTouchHelperViewHolder holder, int position) {
        TextView tvItem = holder.itemView.findViewById(R.id.tv_item);
        tvItem.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public static class ItemTouchHelperViewHolder extends RecyclerView.ViewHolder{

        public ItemTouchHelperViewHolder(View itemView) {
            super(itemView);
        }
    }

    public static class MyItemTouchHelperCallback extends ItemTouchHelper.Callback{

        //RecyclerView的Adapter
        private ItemTouchHelperAdapter mAdapter;

        public MyItemTouchHelperCallback(ItemTouchHelperAdapter adapter){
            this.mAdapter = adapter;
        }

        /**
         * 允许哪个方向的拖拽和滑动，一般配合makeMovementFlags(int,int)去使用
         * @param recyclerView
         * @param viewHolder
         * @return
         */
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            //允许上下拖拽，和从右向左滑动
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.LEFT;
            //计算拖拽和滑动的方向
            return makeMovementFlags(dragFlags,swipeFlags);
        }

        /**
         * 拖拽一个item到新位置时会调用此方法，一般配合Adapter的notifyItemMoved()方法来交换两个ViewHolder的位置
         * @param recyclerView
         * @param viewHolder
         * @param target
         * @return true表示已经到达移动目的地
         */
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //交换item位置
            Collections.swap(mAdapter.mDatas,viewHolder.getAdapterPosition(),target.getAdapterPosition());
            mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
            return true;
        }

        /**
         * 当滑动到一定程度时，松手会继续滑动，然后调用此方法，反之item会回到原位，不调用此方法
         * @param viewHolder
         * @param direction 滑动的方向
         */
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Log.e("zw", "direction : " + direction);
            //删除此item
            mAdapter.mDatas.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
        }

        /**
         * 是否长按时才使拖拽生效
         * @return 默认返回true
         */
        @Override
        public boolean isLongPressDragEnabled() {
            return super.isLongPressDragEnabled();
        }

        /**
         * 是否可以进行滑动时的删除动作，也就是是否能调用onSwiped()方法
         * @return 默认返回true
         */
        @Override
        public boolean isItemViewSwipeEnabled() {
            return super.isItemViewSwipeEnabled();
        }

        /**
         * 静止状态变为拖拽或者滑动的时候会回调
         * @param viewHolder
         * @param actionState 当前的状态
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 当用户操作完毕某个item并且其动画也结束后会调用该方法，一般我们在该方法内恢复ItemView的初始状态，防止由于复用而产生的显示错乱问题。
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            //复位
            viewHolder.itemView.setScrollX(0);
            TextView alphaView = viewHolder.itemView.findViewById(R.id.tv_item1);
            alphaView.setTextColor(Color.parseColor("#00FFFFFF"));
        }

        /**
         * 实现我们自定义的交互规则或者自定义的动画效果
         * @param c
         * @param recyclerView
         * @param viewHolder
         * @param dX
         * @param dY
         * @param actionState
         * @param isCurrentlyActive
         */
        @Override
        public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            //仅仅针对Item的滑动事件
            if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                TextView alphaView = viewHolder.itemView.findViewById(R.id.tv_item1);
                int viewWidth = alphaView.getWidth();
                if(Math.abs(dX) < viewWidth){

                    //滚动
                    viewHolder.itemView.scrollTo((int) -dX,0);

                } else if(Math.abs(dX) < recyclerView.getWidth() / 2){
                    //滑动的透明度
                    float childAlpha = Math.abs(dX) - viewWidth;
                    float fatherAlpha = recyclerView.getWidth() / 2 - viewWidth;
                    float alpha = childAlpha / fatherAlpha;

                    int color = Color.argb((int)(alpha * 255),255,255,255);
                    alphaView.setTextColor(color);
                }
            } else {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
    }
}
