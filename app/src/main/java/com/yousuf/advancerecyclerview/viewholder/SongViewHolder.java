package com.yousuf.advancerecyclerview.viewholder;

import android.graphics.Color;
import android.media.Image;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.model.SongInfo;
import com.yousuf.advancerecyclerview.touchhelper.ItemTouchHelperViewHolder;
import com.yousuf.advancerecyclerview.touchhelper.OnStartDragListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class SongViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder{


    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewSubTitle)
    TextView textViewSubTitle;

    ImageView imageViewHandler;
    private OnStartDragListener onStartDragListener;

    Animation animZoomInOut;


    public SongViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        animZoomInOut = AnimationUtils.loadAnimation(itemView.getContext().getApplicationContext(), R.anim.zoom_in_out);

        imageViewHandler = itemView.findViewById(R.id.imageViewHandle);

        if (imageViewHandler != null) {
            imageViewHandler.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (onStartDragListener != null) {
                            onStartDragListener.onStartDrag(SongViewHolder.this);
                        }
                    }
                    return false;
                }
            });
        }

    }



    public void bind(SongInfo songInfo){

        Glide.with(imageView.getContext())
                .load(songInfo.getDrawableId())
               // .override(400, 400)
               // .centerCrop()
                .into(imageView);



        textViewTitle.setText(songInfo.getTitle());
        textViewSubTitle.setText(songInfo.getSubTitle());



    }


    public void setOnStartDragListener(OnStartDragListener onStartDragListener) {

        this.onStartDragListener = onStartDragListener;
    }

    @Override
    public void onItemSelected() {
       // itemView.setBackgroundColor(Color.LTGRAY);
        Timber.i("onItemSelected()");

        itemView.startAnimation(animZoomInOut);
    }

    @Override
    public void onItemClear() {
      //  itemView.setBackgroundColor(0);
        Timber.i("onItemClear()");
    }
}
