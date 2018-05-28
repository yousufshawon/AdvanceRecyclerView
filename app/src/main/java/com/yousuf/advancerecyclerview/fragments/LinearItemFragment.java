package com.yousuf.advancerecyclerview.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yousuf.advancerecyclerview.R;
import com.yousuf.advancerecyclerview.adapter.RecyclerViewAdapter;
import com.yousuf.advancerecyclerview.model.SongInfo;
import com.yousuf.advancerecyclerview.touchhelper.OnStartDragListener;
import com.yousuf.advancerecyclerview.touchhelper.SimpleItemTouchHelperCallback;
import com.yousuf.advancerecyclerview.utils.StartSnapHelper;
import com.yousuf.advancerecyclerview.utils.Util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
public class LinearItemFragment extends Fragment implements OnStartDragListener{


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.radioGroupOrientation)
    RadioGroup radioGroupOrientation;
    @BindView(R.id.radioButtonVertical)
    RadioButton radioButtonVertical;
    @BindView(R.id.radioButtonHorizontal)
    RadioButton radioButtonHorizontal;
    @BindView(R.id.checkBoxSnap)
    CheckBox checkBoxSnap;



    private RecyclerViewAdapter recyclerViewAdapter;
    private boolean reverseLayout = false;

    StartSnapHelper startSnapHelper;

    ItemTouchHelper touchHelper;
    private List<SongInfo> itemList;


    public LinearItemFragment() {
        // Required empty public constructor
        startSnapHelper = new StartSnapHelper();
    }


    public static LinearItemFragment newInstance() {

        Bundle args = new Bundle();

        LinearItemFragment fragment = new LinearItemFragment();
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
        View view = inflater.inflate(R.layout.fragment_linear_item, container, false);

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
        recyclerViewAdapter = new RecyclerViewAdapter(itemList, R.layout.item_view_linear);

        radioGroupOrientation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                setUpRecyclerView();
            }
        });

        checkBoxSnap.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setUpSnapHelper();
            }
        });




        setUpRecyclerView();


    }

    private void setUpRecyclerView() {


        setUpSnapHelper();


        int checkedRadioButtonId = radioGroupOrientation.getCheckedRadioButtonId();

        switch (checkedRadioButtonId) {
            case R.id.radioButtonVertical:
                setUpRecyclerViewForVertical(recyclerView);
                break;

            case R.id.radioButtonHorizontal:
                setUpRecyclerViewForHorizontal(recyclerView);
                break;

            default:
                setUpRecyclerViewForVertical(recyclerView);


        }



        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerViewAdapter);

        if (touchHelper != null) {
            touchHelper.attachToRecyclerView(null);
        }
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void setUpSnapHelper() {
        if (checkBoxSnap.isChecked()) {
            startSnapHelper.attachToRecyclerView(recyclerView);
        } else {
            startSnapHelper.attachToRecyclerView(null);
        }
    }

    private void setUpRecyclerViewForHorizontal(RecyclerView recyclerView) {

        recyclerViewAdapter = new RecyclerViewAdapter(Util.getSongList(getContext()), R.layout.item_view_linear_horizontal);
        recyclerViewAdapter.setOnStartDragListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, reverseLayout));


        while (recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }


        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
    }

    private void setUpRecyclerViewForVertical(RecyclerView recyclerView) {

        recyclerViewAdapter = new RecyclerViewAdapter(Util.getSongList(getContext()), R.layout.item_view_linear);
        recyclerViewAdapter.setOnStartDragListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, reverseLayout));


        while (recyclerView.getItemDecorationCount() > 0){
            recyclerView.removeItemDecorationAt(0);
        }

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
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
