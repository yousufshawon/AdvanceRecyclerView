package com.yousuf.advancerecyclerview.model;

import android.support.annotation.NonNull;

public class SongInfo  implements Comparable<SongInfo>{

    private int id;
    private int drawableId;
    private String title;
    private String subTitle;

    public SongInfo(int id, int drawableId, String title, String subTitle) {
        this.id = id;
        this.drawableId = drawableId;
        this.title = title;
        this.subTitle = subTitle;
    }

    public int getId() {
        return id;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    @Override
    public int compareTo(@NonNull SongInfo o) {
        return Integer.valueOf(id).compareTo(Integer.valueOf(o.id));
    }
}
