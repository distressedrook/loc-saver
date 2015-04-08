package com.avismara.locsaver.miscellaneous;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.avismara.locsaver.entities.LocationInfoEntity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Avismara on 04/04/15.
 */
public class Utils {
    public static void showToast(CharSequence message,Activity activity) {
        Context context = activity.getApplicationContext();

        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
    public static void saveLocation(LocationInfoEntity object) {
        if(GlobalVariables.savedLocations == null) {
            GlobalVariables.savedLocations = new ArrayList<LocationInfoEntity>();
        }
        GlobalVariables.savedLocations.add(0,object);
        if(GlobalVariables.savedLocations.size() > 5) {
            GlobalVariables.savedLocations.remove(4);
        }
    }

    public static boolean isPlaceAlreadySaved(LocationInfoEntity object) {
        ArrayList<LocationInfoEntity> savedLocations = GlobalVariables.savedLocations;
        if(savedLocations == null) {
            return false;
        }
        Iterator<LocationInfoEntity> itr = savedLocations.iterator();
        while(itr.hasNext()) {
            LocationInfoEntity savedLocation = itr.next();
            Log.d("Utils logger","Saved Location Latitude: "+savedLocation.getLatitude().doubleValue()+"Saved Location Longitude: "+object.getLatitude().doubleValue());
            if(savedLocation.getLatitude().doubleValue() == object.getLatitude().doubleValue() && savedLocation.getLongitude().doubleValue() == object.getLongitude().doubleValue()) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
