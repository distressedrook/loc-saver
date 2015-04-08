package com.avismara.locsaver.miscellaneous;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SwipeKillDetectorService extends Service {
    public SwipeKillDetectorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.d("TestLogger","Service Started");
        GlobalVariables.savedLocations = ObjectSaver.readSavedLocationsFromFile(getApplicationContext());


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.d("TestLogger","Service Ended");
        ObjectSaver.writeSavedLocationsToFile(getApplicationContext(),GlobalVariables.savedLocations);
        stopSelf();
    }
}
