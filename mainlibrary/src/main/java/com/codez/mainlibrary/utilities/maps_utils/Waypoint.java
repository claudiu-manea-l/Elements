package com.codez.mainlibrary.utilities.maps_utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Eptron on 10/28/2015.
 */
public class Waypoint {
    LatLng latLng;
    String address;

    public Waypoint(LatLng latLng, String address) {
        this.latLng = latLng;
        this.address = address;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public String getAddress() {
        return address;
    }
}
