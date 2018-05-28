package com.yousuf.advancerecyclerview.fragments;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.dgreenhalgh.android.simpleitemdecoration.grid.GridDividerItemDecoration;
import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.adapter.RecyclerViewAdapter;
import com.yousuf.advancerecyclerview.model.SongInfo;
import com.yousuf.advancerecyclerview.touchhelper.OnStartDragListener;
import com.yousuf.advancerecyclerview.touchhelper.SimpleItemTouchHelperCallback;
import com.yousuf.advancerecyclerview.utils.StartGridSnapHelper;
import com.yousuf.advancerecyclerview.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class GridItemFragment extends Fragment implements OnStartDragListener{


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private static final int NUM_OF_COLUMN = 2;
    @BindView(R.id.checkBoxSnap)
    CheckBox checkBoxSnap;


    private RecyclerViewAdapter recyclerViewAdapter;
    private boolean reverseOrder = false;

    StartGridSnapHelper startGridSnapHelper;
    ItemTouchHelper touchHelper;
    private List<SongInfo> itemList;

    public GridItemFragment() {
        // Required empty public constructor
        startGridSnapHelper = new StartGridSnapHelper(NUM_OF_COLUMN);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static GridItemFragment newInstance() {

        Bundle args = new Bundle();

        GridItemFragment fragment = new GridItemFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_grid_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_shuffle:
                shuffleItems();
                break;

            case R.id.action_sort:
                sortItems();
                break;

            default:
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void sortItems() {

        List<SongInfo> sortItems = new ArrayList<>();
        sortItems.addAll(itemList);

        Collections.sort(sortItems);
        recyclerViewAdapter.swapItems(sortItems);
    }

    private void shuffleItems() {

        List<SongInfo> shuffleItems = new ArrayList<>();
        shuffleItems.addAll(itemList);

        Collections.shuffle(shuffleItems);

        recyclerViewAdapter.swapItems(shuffleItems);

    }




    private void init() {


        itemList = Util.getSongList(getContext());

        checkBoxSnap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUpSnapHelper();
            }
        });

        recyclerViewAdapter = new RecyclerViewAdapter(itemList, R.layout.item_view_grid);
        recyclerViewAdapter.setOnStartDragListener(this);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUM_OF_COLUMN, RecyclerView.VERTICAL, reverseOrder);

        GridLayoutManager.SpanSizeLookup spanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                if (position + 1 == recyclerViewAdapter.getItemCount()) {
                    if (position % 2 == 0) return NUM_OF_COLUMN;
                }

                return 1;
            }
        };

        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);

        recyclerView.addItemDecoration(new com.yousuf.advancerecyclerview.utils.GridDividerItemDecoration(getContext(), Util.dpToPx(getContext(), 10), NUM_OF_COLUMN, false));

       // Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.item_divider_drawable);
       // recyclerView.addItemDecoration(new GridDividerItemDecoration(dividerDrawable, dividerDrawable, NUM_OF_COLUMN));

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);
        //  recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        setUpSnapHelper();


        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);


    }


    private void setUpSnapHelper() {

        if (checkBoxSnap.isChecked()) {
            startGridSnapHelper.attachToRecyclerView(recyclerView);
        } else {
            startGridSnapHelper.attachToRecyclerView(null);
        }


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        Timber.i("Touch: start dragging");
        if (touchHelper != null) {
            touchHelper.startDrag(viewHolder);
        }

    }
}
