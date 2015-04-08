package com.avismara.locsaver.asynctasks;

/**
 * Created by Avismara on 04/04/15.
 */
import android.os.AsyncTask;
import android.util.Log;


import com.avismara.locsaver.activities.MapsActivity;
import com.avismara.locsaver.entities.LocationInfoEntity;
import com.avismara.locsaver.miscellaneous.GlobalConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GeoCodeAsyncTask extends AsyncTask<LocationInfoEntity,Void,LocationInfoEntity> {

    private static final String LOG_TAG = "GetLatLongAsyncTask";



    private MapsActivity mapsActivity;

    public GeoCodeAsyncTask(MapsActivity mapsActivity){
        this.mapsActivity = mapsActivity;


    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected LocationInfoEntity doInBackground(LocationInfoEntity... parameters) {
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        LocationInfoEntity locationInfoEntity = parameters[0];
        try {
            StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json");
            sb.append("?placeid=" + locationInfoEntity.getPlaceID());
            sb.append("&key=" + GlobalConstants.GOOGLE_MAPS_KEY);
            Log.e(LOG_TAG, "url: " + sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (IOException e) {

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        try{
            JSONObject response = new JSONObject(jsonResults.toString());

            if (response.has("result")){
                JSONObject result = response.getJSONObject("result");
                if (result.has("geometry")){
                    JSONObject geometry = result.getJSONObject("geometry");
                    if (geometry.has("location")){
                        JSONObject location = geometry.getJSONObject("location");
                        locationInfoEntity.setLatitude(location.getDouble("lat"));
                        locationInfoEntity.setLongitude(location.getDouble("lng"));

                    }
                }
            }
        }catch (JSONException e){

        }
        return locationInfoEntity;
    }

    @Override
    protected void onPostExecute(LocationInfoEntity locationInfoEntity) {
        super.onPostExecute(locationInfoEntity);
        mapsActivity.handleGeoCodeAsyncTaskCallback(locationInfoEntity);
    }
}
