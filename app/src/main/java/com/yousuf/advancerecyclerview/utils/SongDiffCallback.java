package com.yousuf.advancerecyclerview.utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.yousuf.advancerecyclerview.model.SongInfo;

import java.util.List;

public class SongDiffCallback extends DiffUtil.Callback {


    private List<SongInfo> oldList;
    private List<SongInfo> newList;

    public SongDiffCallback(List<SongInfo> oldList, List<SongInfo> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getId() == newList.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return newList.get(newItemPosition).getTitle().equals(oldList.get(oldItemPosition).getTitle());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
