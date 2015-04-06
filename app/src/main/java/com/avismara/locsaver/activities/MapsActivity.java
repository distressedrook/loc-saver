package com.avismara.locsaver.activities;



import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;


import com.avismara.locsaver.R;
import com.avismara.locsaver.adapters.PlacesAutoCompleteAdapter;
import com.avismara.locsaver.asynctasks.GeoCodeAsyncTask;
import com.avismara.locsaver.entities.LocationInfoEntity;
import com.avismara.locsaver.utils.GlobalVariables;
import com.avismara.locsaver.utils.SwipeKillDetectorService;
import com.avismara.locsaver.utils.Utils;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Calendar;


public class MapsActivity extends FragmentActivity implements  LocationListener,GoogleMap.OnMapClickListener,GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private AutoCompleteTextView mAutoCompleteTextView;
    private LocationListener mLocationListener;
    private LocationManager mLocationManager;
    private LocationInfoEntity currentSelectedLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setupAutoCompleteTextView();
        setUpMapIfNeeded(12.9100,77.64);
        startService(new Intent(this, SwipeKillDetectorService.class));
    }

    private void getLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("TestLogger","Latitude:" + location.getLatitude() + "\nLongitude" + location.getLongitude());
        mLocationManager.removeUpdates(this);
    }

    private void setupMap(Double latitude,Double longitude) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        markerOptions.position(new LatLng(latitude,longitude));
        mMap.addMarker(markerOptions);
    }
    private void setupAutoCompleteTextView() {
        mAutoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView);
        final Context context = this;
        final MapsActivity mapsActivity = this;
        mAutoCompleteTextView.setAdapter(new PlacesAutoCompleteAdapter(this,android.R.layout.simple_dropdown_item_1line));
        mAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAutoCompleteTextView.setText("");
                hideKeyBoard(mAutoCompleteTextView);
                LocationInfoEntity locationInfoEntity = (LocationInfoEntity) parent.getItemAtPosition(position);
                currentSelectedLocation = locationInfoEntity;
                new GeoCodeAsyncTask(mapsActivity).execute(locationInfoEntity);
            }
        });
        mAutoCompleteTextView.clearFocus();

    }

    public void handleGeoCodeAsyncTaskCallback(LocationInfoEntity locationInfoEntity) {
        Log.d("TestLogger", "Latitude:" + locationInfoEntity.getLatitude() + "\nLongitude" + locationInfoEntity.getLongitude());
        if(locationInfoEntity.getLatitude() != null && locationInfoEntity.getLongitude() != null) {
            setupMap(locationInfoEntity.getLatitude(), locationInfoEntity.getLongitude());
        } else {
            Utils.showToast("Unable to get geocodes",this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setupMap(Double, Double)} ()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded(Double latitude,Double longitude) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                currentSelectedLocation = new LocationInfoEntity();
                currentSelectedLocation.setLatitude(latitude);
                currentSelectedLocation.setLongitude(longitude);
                currentSelectedLocation.setLocationDescription("HSR Layout");
                setupMap(latitude,longitude);
                mMap.setOnMarkerDragListener(this);
                mMap.setOnMapClickListener(this);

            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */


    public void onProviderDisabled(String arg0) {}
    public void onProviderEnabled(String arg0) {}
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}

    private void hideKeyBoard(View v) {
        Log.d("TestLogger","Hide Keyboard called");
        RelativeLayout layout = (RelativeLayout)findViewById(R.id.root_layout);
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Geocoder geocoder = new Geocoder(this);
        currentSelectedLocation = new LocationInfoEntity();
        currentSelectedLocation.setLatitude(marker.getPosition().latitude);
        currentSelectedLocation.setLongitude(marker.getPosition().longitude);
        Utils.showToast((CharSequence)("GeoCode: Lat - " + marker.getPosition().latitude + "\nLong - "+marker.getPosition().longitude),this);
        try {
            Address address =  geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1).get(0);
            Log.d("address",address.toString());

                if(address.getAddressLine(0) == null) {
                    currentSelectedLocation.setLocationDescription("Unknown Location");
                } else {
                    String fullAddress = "";
                    int i = 0;
                    while (address.getAddressLine(i) != null) {
                        if(address.getAddressLine(i+1) == null) {
                            Log.d("FULLADDRESS",fullAddress);
                            fullAddress = fullAddress+ address.getAddressLine(i);
                        } else {
                            fullAddress = fullAddress+ address.getAddressLine(i) + ",";
                        }

                        i++;
                    }
                    currentSelectedLocation.setLocationDescription(fullAddress);
                }


            Log.d("Tag",currentSelectedLocation.getLocationDescription());
        } catch (IOException e) {

        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            currentSelectedLocation.setLocationDescription("Unknown Location");
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {


    }

    @Override
    public void onMapClick(LatLng point) {
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.draggable(true);
        markerOptions.position(new LatLng(point.latitude,point.longitude));
        mMap.addMarker(markerOptions);
        Geocoder geocoder = new Geocoder(this);
        currentSelectedLocation = new LocationInfoEntity();
        currentSelectedLocation.setLatitude(point.latitude);
        currentSelectedLocation.setLongitude(point.longitude);
        Utils.showToast((CharSequence)("GeoCode: Lat - " + point.latitude + "\nLong - "+point.longitude),this);
        try {
            Address address =  geocoder.getFromLocation(point.latitude,point.longitude, 1).get(0);

                if(address.getAddressLine(0) == null) {
                    currentSelectedLocation.setLocationDescription("Unknown Location");
                } else {
                    String fullAddress = "";
                    int i = 0;


                    while (address.getAddressLine(i) != null) {
                        if(address.getAddressLine(i+1) == null) {
                            fullAddress = fullAddress+ address.getAddressLine(i);
                        } else {
                            fullAddress = fullAddress+ address.getAddressLine(i) + ",";
                        }

                        i++;
                    }
                    currentSelectedLocation.setLocationDescription(fullAddress);
                }


        } catch (IOException e) {

        } catch (IndexOutOfBoundsException e) {

        } catch (Exception e) {
            currentSelectedLocation.setLocationDescription("Unknown Location");
        }

    }

    public void onSaveClicked(View v) {
        if(Utils.isPlaceAlreadySaved(currentSelectedLocation)) {
            Utils.showToast("Location Already Saved",this);
            return;
        }
        Utils.showToast("Location Saved",this);
        Utils.saveLocation(currentSelectedLocation);

    }

}

