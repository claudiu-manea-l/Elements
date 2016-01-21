package com.codez.mainlibrary.utilities.maps_utils.model;

import com.loopj.android.http.RequestParams;

/**
 * Created by eptron on 19/03/2015.
 * Util class holding all the names of the JSON hierarchy retrieved from
 * Google Directions API
 */
public class MapsConstants {

    public static final String DIR_URL = "http://maps.googleapis.com/maps/api/directions/json";

    //Universal names
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "lng";
    public static final String POINTS = "points";
    public static final String POLYLINE = "polyline";
    public static final String VALUE = "value";
    public static final String TEXT = "text";
    public static final String START_LOC = "start_location";
    public static final String END_LOC = "end_location";

    public static final String ROUTE = "routes";
    //Route children names
    public static final String SUMMARY = "summary";
    public static final String LOGISTICS = "legs";
    //Logistics children names
    public static final String DURATION = "duration";
    public static final String DISTANCE = "distance";
    public static final String START_ADD = "start_address";
    public static final String END_ADD = "end_address";
    public static final String STEP = "steps";
    //Step children names
    public static final String MODE = "travel_mode";
    public static final String INSTRUCTION = "html_instructions";
    public static final String MANEUVER = "maneuver";

    public static final String OVERVIEW_POLY = "overview_polyline";
    public static final String BOUNDS = "bounds";
    //Bounds children names
    public static final String SOUTHWEST = "southwest";
    public static final String NORTHEAST = "northeast";

    public static RequestParams buildDirectionApiParams(String startAddress, String endAddress,
                                                        String mode, String units) {
        RequestParams params = new RequestParams("origin", startAddress);
        params.put("destination", endAddress);
        params.put("mode", mode);
        params.put("units", "metric");
        params.put("sensor", false);
        return params;
    }


}
