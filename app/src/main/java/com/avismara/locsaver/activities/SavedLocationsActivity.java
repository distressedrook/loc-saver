package com.avismara.locsaver.activities;

import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.avismara.locsaver.R;
import com.avismara.locsaver.adapters.SavedLocationsAdapter;
import com.avismara.locsaver.libraries.SwipeableRecyclerViewTouchListener;
import com.avismara.locsaver.miscellaneous.GlobalConstants;
import com.avismara.locsaver.miscellaneous.GlobalVariables;

public class SavedLocationsActivity extends FragmentActivity {

    private RecyclerView mRecyclerView;
    private SavedLocationsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mHeaderTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_locations);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mHeaderTextView = (TextView)findViewById(R.id.header_textview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new SavedLocationsAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        Typeface typeFace = Typeface.createFromAsset(getAssets(), GlobalConstants.OPEN_SANS_SEMI_BOLD);
        mHeaderTextView.setTypeface(typeFace);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(mRecyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    GlobalVariables.savedLocations.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {

                                    GlobalVariables.savedLocations.remove(position);
                                    mAdapter.notifyItemRemoved(position);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });

        mRecyclerView.addOnItemTouchListener(swipeTouchListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }
}
