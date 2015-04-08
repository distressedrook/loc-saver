package com.avismara.locsaver.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avismara.locsaver.R;
import com.avismara.locsaver.activities.MapsActivity;
import com.avismara.locsaver.activities.SavedLocationsActivity;
import com.avismara.locsaver.activities.ShowLocationActivity;
import com.avismara.locsaver.asynctasks.LoadImageAsyncTask;
import com.avismara.locsaver.entities.LocationInfoEntity;
import com.avismara.locsaver.miscellaneous.GlobalConstants;
import com.avismara.locsaver.miscellaneous.GlobalVariables;
import com.avismara.locsaver.miscellaneous.MapOnClickListener;


import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Avismara on 05/04/15.
 */
public class SavedLocationsAdapter extends RecyclerView.Adapter<SavedLocationsAdapter.CurrentViewHolder> {




    private ArrayList<LocationInfoEntity> mDataSet = GlobalVariables.savedLocations;
    private Context mContext;
    private Activity mActivity;



    public SavedLocationsAdapter(Context context,Activity activity) {
       if(mDataSet == null) {
           mDataSet = new ArrayList<LocationInfoEntity>();
       }
       Iterator<LocationInfoEntity> iterator = mDataSet.iterator();
       mContext = context;
        mActivity = activity;
       while (iterator.hasNext()) {
           LocationInfoEntity currentLocation = iterator.next();
           //&markers=color:blue%7Clabel:S%7C40.702147,-74.015794
           new LoadImageAsyncTask(currentLocation,this).execute(GlobalConstants.STATIC_MAP_URL + currentLocation.getLatitude() + "," +currentLocation.getLongitude() + "&size=" +
                           300 + "x" + 360 + "&zoom=16&markers=color:blue%7Clabel:A%7C" + currentLocation.getLatitude() + "," +currentLocation.getLongitude() );
                   Log.d("Context", "GlobalConstants.STATIC_MAP_URL+currentLocation.getLatitude()+\",\"+currentLocation.getLongitude()+\"&size=400x400\"");
       }

   }

    public static class CurrentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mLocationDescriptionTextView;
        private ImageView mLocationImageView;
        private LocationInfoEntity mItem;
        private MapOnClickListener mListener;

        public CurrentViewHolder(View v,MapOnClickListener listener) {
            super(v);
            mListener = listener;
            mLocationDescriptionTextView = (TextView)v.findViewById(R.id.description_textView);
            mLocationImageView = (ImageView)v.findViewById(R.id.saved_location_image);
            v.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            mListener.itemTapped(mItem);
        }
    }




    @Override
    public void onBindViewHolder(CurrentViewHolder currentViewHolder, int i) {
        LocationInfoEntity savedLocationInfo = mDataSet.get(i);
        currentViewHolder.mLocationDescriptionTextView.setText(savedLocationInfo.getLocationDescription());
        Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), GlobalConstants.OPEN_SANS_REGULAR);
        currentViewHolder.mLocationDescriptionTextView.setTypeface(typeFace);
        currentViewHolder.mLocationDescriptionTextView.setTextColor(Color.parseColor("#858585"));
        currentViewHolder.mLocationImageView.setImageBitmap(savedLocationInfo.mapImage);
        currentViewHolder.mItem = savedLocationInfo;


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }



    @Override
    public CurrentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.saved_location_cardview_layout, viewGroup, false);

        CurrentViewHolder vh = new CurrentViewHolder(v,new MapOnClickListener() {
            @Override
            public void itemTapped(LocationInfoEntity locationInfoEntity) {
                Intent intent = new Intent(mActivity, MapsActivity.class);
                intent.putExtra("latitude",locationInfoEntity.getLatitude());
                intent.putExtra("longitude",locationInfoEntity.getLongitude());
                intent.putExtra("locationDescription",locationInfoEntity.getLocationDescription());
                mActivity.startActivity(intent);
            }

        });
        return vh;
    }
}
