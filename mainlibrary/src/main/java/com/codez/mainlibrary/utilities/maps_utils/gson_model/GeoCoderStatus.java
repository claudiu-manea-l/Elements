package com.codez.mainlibrary.utilities.maps_utils.gson_model;

/**
 * Created by Eptron on 10/22/2015.
 */
public class GeoCoderStatus {
    private String geocoder_status;
    private String place_id;
    private String[] types;

    public String getGeocoderStatus() {
        return geocoder_status;
    }

    public String getPlaceId() {
        return place_id;
    }

    public String[] getTypes() {
        return types;
    }
}
