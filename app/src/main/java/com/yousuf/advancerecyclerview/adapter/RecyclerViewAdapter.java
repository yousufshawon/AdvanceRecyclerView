package com.yousuf.advancerecyclerview.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.fragments.GridItemFragment;
import com.yousuf.advancerecyclerview.model.SongInfo;
import com.yousuf.advancerecyclerview.touchhelper.ItemTouchHelperAdapter;
import com.yousuf.advancerecyclerview.touchhelper.OnStartDragListener;
import com.yousuf.advancerecyclerview.utils.SongDiffCallback;
import com.yousuf.advancerecyclerview.utils.Util;
import com.yousuf.advancerecyclerview.viewholder.SongViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import timber.log.Timber;


public class RecyclerViewAdapter extends RecyclerView.Adapter<SongViewHolder> implements ItemTouchHelperAdapter {

    List<SongInfo> songInfoList;
    private int itemResourceId;
    private int margin = 10;


    private RecyclerView recyclerView;
    private OnStartDragListener onStartDragListener;


    public RecyclerViewAdapter(List<SongInfo> songInfoList, int itemResourceId) {
        this.songInfoList = songInfoList;
        this.itemResourceId = itemResourceId;

        if (this.songInfoList == null) {
            this.songInfoList = new ArrayList<>();
        }



    }

    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {

        this.onStartDragListener = onStartDragListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(itemResourceId, parent, false);

            LayoutManager layoutManager  = recyclerView.getLayoutManager();

            if (layoutManager instanceof GridLayoutManager) {

                GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                int spanCount = gridLayoutManager.getSpanCount();

                View parentView = rootView.findViewById(R.id.viewCard);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) parentView.getLayoutParams();

                int margin = Util.dpToPx(parent.getContext(), getMargin());

                layoutParams.width = (recyclerView.getWidth() - (spanCount +1) * margin ) / spanCount;

                Timber.i("Recycler cell width: " + layoutParams.width);

                parentView.setLayoutParams(layoutParams);


            }

            SongViewHolder songViewHolder = new SongViewHolder(rootView);
            songViewHolder.setOnStartDragListener(onStartDragListener);

        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {

        holder.bind(songInfoList.get(position));

    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public int getItemCount() {
        return songInfoList.size();
    }

    public int getMargin() {
        return margin;
    }


    public void setMargin(int margin) {
        this.margin = margin;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(songInfoList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(songInfoList, i, i - 1);
            }
        }


 //       Collections.swap(songInfoList, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        songInfoList.remove(position);
        notifyItemRemoved(position);
    }

    public void swapItems(List<SongInfo> newList) {

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new SongDiffCallback(this.songInfoList, newList));

        this.songInfoList.clear();
        this.songInfoList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);

//        songInfoList.clear();
//        songInfoList.addAll(newList);
//        notifyDataSetChanged();
    }
}
