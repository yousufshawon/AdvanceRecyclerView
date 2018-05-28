package com.yousuf.advancerecyclerview.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.model.SongInfo;

import java.util.ArrayList;
import java.util.List;

public class Util {


    public Util() {
        new AssertionError("Can't initialize this class");
    }



    public static List<SongInfo> getSongList(Context context){

        TypedArray imageTypedArray = context.getResources().obtainTypedArray(R.array.albumResourceId);
        List<SongInfo> songList = new ArrayList<>();

        String title = "Title";
        String subTitle = "SubTitle";

        for (int i = 0; i < imageTypedArray.length(); i++) {
            int index = i+1;
            SongInfo songInfo = new SongInfo(index, imageTypedArray.getResourceId(i, R.drawable.album_placeholder), title + " " + index, subTitle + " " + index);
            songList.add(songInfo);
        }

        imageTypedArray.recycle();

        return songList;

    }


    /**
     * Converting dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
