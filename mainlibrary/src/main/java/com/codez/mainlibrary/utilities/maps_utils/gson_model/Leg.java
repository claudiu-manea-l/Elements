package com.codez.mainlibrary.utilities.maps_utils.gson_model;

import com.codez.mainlibrary.utilities.maps_utils.gson_model.common.LatLngBounds;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.common.Step;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.common.TextValue;

import java.util.List;

/**
 * Created by Eptron on 10/22/2015.
 */
public class Leg {
    private TextValue distance;
    private TextValue duration;
    private String end_address;
    private LatLngBounds end_location;
    private String start_address;
    private LatLngBounds start_location;
    private List<Step> steps;
    private List<Object> via_waypoint;

    public TextValue getDistance() {
        return distance;
    }

    public TextValue getDuration() {
        return duration;
    }

    public String getEndAddress() {
        return end_address;
    }

    public LatLngBounds getEndLocation() {
        return end_location;
    }

    public String getStartAddress() {
        return start_address;
    }

    public LatLngBounds getStartLocation() {
        return start_location;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public List<Object> getViaWaypoints() {
        return via_waypoint;
    }
}
