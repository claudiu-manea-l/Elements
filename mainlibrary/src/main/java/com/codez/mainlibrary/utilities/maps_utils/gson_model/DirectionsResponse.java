package com.codez.mainlibrary.utilities.maps_utils.gson_model;

import java.util.List;

/**
 * Created by Eptron on 10/22/2015.
 */
public class DirectionsResponse {
    private List<GeoCoderStatus> geocoded_waypoints;
    private List<Route> routes;
    private String status;

    public List<GeoCoderStatus> getGeocodedWaypoints() {
        return geocoded_waypoints;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public String getStatus() {
        return status;
    }
}
