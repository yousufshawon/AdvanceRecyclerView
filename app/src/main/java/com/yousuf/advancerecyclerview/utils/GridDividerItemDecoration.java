package com.yousuf.advancerecyclerview.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yousuf.advancerecyclerview.R;

import timber.log.Timber;

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {


    private static final int[] ATTRS = { android.R.attr.listDivider };

    private Drawable mDivider;
    private int mInsets;
    private int margin;

    private int spanCount;
    private boolean includeEdge;

    public GridDividerItemDecoration(Context context, int margin, int spanCount, boolean includeEdge) {
        this.margin = margin;
        this.spanCount = spanCount;
        this.includeEdge = includeEdge;
        TypedArray a = context.obtainStyledAttributes(ATTRS);
       // mDivider = a.getDrawable(0);
        mDivider = ContextCompat.getDrawable(context, R.drawable.item_divider_drawable);
        a.recycle();

        mInsets = context.getResources().getDimensionPixelSize(R.dimen.card_insets);

        Timber.i("margin: " + margin);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //Draw under the view content

    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        //Draw over the view content
        drawVertical(c, parent);
        drawHorizontal(c, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

//        int absolutePosition = parent.getChildAdapterPosition(view);
//        if (absolutePosition == RecyclerView.NO_POSITION) {
//            return;
//        } else if (absolutePosition < 2) {
//            // First 2 items are top row
//            outRect.set(margin, 0, margin, margin);
//        } else {
//            outRect.set(margin, margin, margin, margin);
//        }
//



        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (includeEdge) {
            outRect.left = margin - column * margin / spanCount; // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = (column + 1) * margin / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = margin;
            }
            outRect.bottom = margin; // item bottom
        } else {
            outRect.left = column * margin / spanCount; // column * ((1f / spanCount) * spacing)
            outRect.right = margin - (column + 1) * margin / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = margin; // item top
            }
        }


      //  Timber.i("Position: " + position + " " + outRect.toString());


    }

    /** Draw dividers at each expected grid interval */
    public void drawVertical(Canvas c, RecyclerView parent) {
        if (parent.getChildCount() == 0) return;

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();



        final View child = parent.getChildAt(0);
        if (child.getHeight() == 0) return;

        final RecyclerView.LayoutParams params =
                (RecyclerView.LayoutParams) child.getLayoutParams();
        int top = child.getBottom() + params.bottomMargin + mInsets;
        int bottom = top + mDivider.getIntrinsicHeight();

        final int parentBottom = parent.getHeight() - parent.getPaddingBottom();
        if(bottom  < parentBottom ){

            do{
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);

                top += mInsets + params.topMargin + child.getHeight() + params.bottomMargin + mInsets;
                bottom = top + mDivider.getIntrinsicHeight();
            }while (bottom < parentBottom );
        }

    }

    /** Draw dividers to the right of each child view */
    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
         int bottom = parent.getHeight() - parent.getPaddingBottom();

        final int childCount = parent.getChildCount();

        if (childCount%2 != 0) {
            View child = parent.getChildAt(childCount-1);
            if (child != null) {
                bottom = child.getTop() - child.getPaddingBottom() - mInsets  - mDivider.getIntrinsicHeight();
            }


        }

        int i = 0;
       // for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params =
                    (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left = child.getRight() + params.rightMargin + mInsets;
            final int right = left + mDivider.getIntrinsicWidth();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
      // }
    }
}


