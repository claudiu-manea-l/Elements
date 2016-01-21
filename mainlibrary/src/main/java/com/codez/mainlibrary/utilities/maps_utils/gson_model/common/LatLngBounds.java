package com.codez.mainlibrary.utilities.maps_utils.gson_model.common;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eptron on 10/22/2015.
 */
public class LatLngBounds {
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public LatLng getLatLng() {
        return new LatLng(lat, lng);
    }
}
