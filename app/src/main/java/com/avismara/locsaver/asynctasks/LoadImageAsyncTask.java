package com.avismara.locsaver.asynctasks;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;


import com.avismara.locsaver.adapters.SavedLocationsAdapter;
import com.avismara.locsaver.entities.LocationInfoEntity;
import com.avismara.locsaver.utils.Utils;

public class LoadImageAsyncTask extends AsyncTask<String, String, Bitmap> {

     LocationInfoEntity mLocationInfoEntity;
     SavedLocationsAdapter mAdapter;
    @Override
    protected void onPreExecute() {
        Log.i("ImageLoadTask", "Loading image...");
    }

     public LoadImageAsyncTask(LocationInfoEntity locationInfoEntity,SavedLocationsAdapter adapter) {
        mLocationInfoEntity = locationInfoEntity;
         mAdapter = adapter;
     }

    // PARAM[0] IS IMG URL
    protected Bitmap doInBackground(String... param) {
        Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
        try {
            Bitmap b = Utils.getBitmapFromURL(param[0]);
            return b;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected void onProgressUpdate(String... progress) {
        // NO OP
    }

    protected void onPostExecute(Bitmap ret) {
        if (ret != null) {

            mLocationInfoEntity.mapImage = ret;
            if (mAdapter != null) {
                // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                mAdapter.notifyDataSetChanged();
            }
        } else {

        }
    }
}