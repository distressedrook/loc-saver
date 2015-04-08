package com.avismara.locsaver.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Context;

import com.avismara.locsaver.R;
import com.avismara.locsaver.miscellaneous.GlobalConstants;
import com.avismara.locsaver.miscellaneous.SwipeKillDetectorService;


public class HomeActivity extends Activity {

    private TextView mLogoTextView;
    private Button mPickLocationButton;
    private Button mSavedLocationsButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        styleViews();
        addListenerToPickLocationButton();
        addListenerToSavedLocationsButton();
        startService(new Intent(this, SwipeKillDetectorService.class));
    }

    private void styleViews() {
        setTypeFaceForLogoTextView();
        setTypeFaceForPickLocationButton();
        setTypeFaceForSavedLocationsButton();
    }

    private void setTypeFaceForLogoTextView() {
        mLogoTextView = (TextView)findViewById(R.id.home_screen_app_name);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),GlobalConstants.OPEN_SANS_BOLD);
        mLogoTextView.setTypeface(typeFace);
    }
    private void setTypeFaceForPickLocationButton() {
        mPickLocationButton = (Button)findViewById(R.id.show_map_button);
        Typeface typeFace = Typeface.createFromAsset(getAssets(), GlobalConstants.OPEN_SANS_SEMI_BOLD);
        mPickLocationButton.setTypeface(typeFace);
    }

    private void setTypeFaceForSavedLocationsButton() {
        mSavedLocationsButton = (Button)findViewById(R.id.saved_locations_button);
        Typeface typeFace = Typeface.createFromAsset(getAssets(),GlobalConstants.OPEN_SANS_SEMI_BOLD);
        mSavedLocationsButton.setTypeface(typeFace);
    }

    private void addListenerToPickLocationButton() {
        final Context context = this;
        mPickLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showMapsActivityIntent = new Intent(context,MapsActivity.class);
                showMapsActivityIntent.putExtra("locationDescription","HSR Layout");
                startActivity(showMapsActivityIntent);
            }
        });
    }

    private  void addListenerToSavedLocationsButton() {
        final Context context = this;
        mSavedLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showSavedLocationsActivityIntent = new Intent(context,SavedLocationsActivity.class);
                startActivity(showSavedLocationsActivityIntent);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("TestLogger","Home Activity's On Stop called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TestLogger","Home Activity's On Destroy called");
    }
}
