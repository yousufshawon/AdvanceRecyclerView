package com.yousuf.advancerecyclerview.touchhelper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;

import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.viewholder.SongViewHolder;

import timber.log.Timber;

public class SimpleItemTouchHelperCallback extends  ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    private String deleteString = "Swipe to Delete";

    ColorDrawable background = new ColorDrawable();
    private Paint paint = new Paint();

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter mAdapter) {
        this.mAdapter = mAdapter;
        paint.setColor(Color.WHITE);
        paint.setTextSize(25.0f);
        paint.setAntiAlias(true);
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        int dragFlags = 0;
        int swipeFlags = 0;

        if (recyclerView.getLayoutManager().getClass() == LinearLayoutManager.class ) {

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int orientation = linearLayoutManager.getOrientation();

            if (orientation == LinearLayoutManager.VERTICAL) {

                swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;


            }else {
                // horizontal
                dragFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                // no swipe for horizontal layout
                swipeFlags = 0;
            }

        }else if( recyclerView.getLayoutManager().getClass() == GridLayoutManager.class){

            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;

        }else if (recyclerView.getLayoutManager().getClass() == StaggeredGridLayoutManager.class){
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            swipeFlags = 0;
        }



        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

       // Timber.i("Touch: " + " moved to position "+ viewHolder.getAdapterPosition() +" to " + target.getAdapterPosition());
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
      //  Timber.i("Touch: swipe dismiss" );
        // delete form here if you want to directly delete on swipe
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());

    }


    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       // super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float width = (float) viewHolder.itemView.getWidth();
            float alpha = 1.0f - Math.abs(dX) / width;

            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);


            View itemView = viewHolder.itemView;
            int itemHeight = itemView.getBottom() - itemView.getTop();
            background.setColor(Color.RED);


            background.setBounds(itemView.getRight() + (int)dX,itemView.getTop(), itemView.getRight(), itemView.getBottom() );

            background.draw(c);

            c.drawText(deleteString, itemView.getRight() + (dX + 50),itemView.getTop() + itemHeight/2,  paint);

        } else {

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY,
                    actionState, isCurrentlyActive);
        }

    }



    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {

        // We only want the active item
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {

            if (viewHolder instanceof SongViewHolder) {
                SongViewHolder songViewHolder = (SongViewHolder) viewHolder;
                songViewHolder.onItemSelected();
            }

        }

        super.onSelectedChanged(viewHolder, actionState);

    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if (viewHolder instanceof SongViewHolder) {
            SongViewHolder songViewHolder = (SongViewHolder) viewHolder;

            songViewHolder.onItemClear();
        }

        super.clearView(recyclerView, viewHolder);
    }



}
