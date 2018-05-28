package com.yousuf.advancerecyclerview.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.adapter.RecyclerViewAdapter;
import com.yousuf.advancerecyclerview.model.SongInfo;
import com.yousuf.advancerecyclerview.touchhelper.OnStartDragListener;
import com.yousuf.advancerecyclerview.touchhelper.SimpleItemTouchHelperCallback;
import com.yousuf.advancerecyclerview.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StaggeredGridItemFragment extends Fragment implements OnStartDragListener{


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;

    private static final int NUM_OF_COLUMN = 2;


    private RecyclerViewAdapter recyclerViewAdapter;
    private ItemTouchHelper touchHelper;
    private List<SongInfo> itemList;

    public StaggeredGridItemFragment() {
        // Required empty public constructor
    }

    public static StaggeredGridItemFragment newInstance() {

        Bundle args = new Bundle();

        StaggeredGridItemFragment fragment = new StaggeredGridItemFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_staggered_grid_item, container, false);
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

        recyclerViewAdapter = new RecyclerViewAdapter(itemList, R.layout.item_view_staggered_grid);


        recyclerView.setLayoutManager(new StaggeredGridLayoutManager( NUM_OF_COLUMN, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnStartDragListener(this);

        //  recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);

        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        if (touchHelper != null) {
            touchHelper.startDrag(viewHolder);
        }
    }
}
