package com.avismara.locsaver.entities;

import android.content.Context;
import android.graphics.Bitmap;

import com.avismara.locsaver.utils.GlobalConstants;
import com.avismara.locsaver.utils.Utils;

import java.io.Serializable;

/**
 * Created by Avismara on 04/04/15.
 */
public class LocationInfoEntity implements Serializable {
    private static final long serialVersionUID = 2528733313241407973L;
    private String locationDescription;
    private Double latitude;
    private Double longitude;
    private String placeID;
    public transient Bitmap mapImage;
    private transient Context context;





    public LocationInfoEntity(String locationDescription,String placeID) {
        super();
        this.locationDescription = locationDescription;
        this.placeID = placeID;
    }

    public LocationInfoEntity() {
        super();
    }
    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}
