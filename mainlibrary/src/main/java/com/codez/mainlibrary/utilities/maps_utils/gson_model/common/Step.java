package com.codez.mainlibrary.utilities.maps_utils.gson_model.common;

/**
 * Created by Eptron on 10/22/2015.
 */
public class Step {
    private TextValue distance;
    private TextValue duration;
    private LatLngBounds end_location;
    private LatLngBounds start_location;
    private String html_instructions;
    private Poly polyline;
    private String maneuver;

    public String getManeuver() {
        return maneuver;
    }

    private String travel_mode;

    public TextValue getDistance() {
        return distance;
    }

    public TextValue getDuration() {
        return duration;
    }

    public LatLngBounds getEndLocation() {
        return end_location;
    }

    public LatLngBounds getStartLocation() {
        return start_location;
    }

    public String getHtmlInstructions() {
        return html_instructions;
    }

    public Poly getPolyline() {
        return polyline;
    }

    public String getTravelMode() {
        return travel_mode;
    }
}
