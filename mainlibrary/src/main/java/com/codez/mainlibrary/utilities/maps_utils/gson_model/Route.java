package com.codez.mainlibrary.utilities.maps_utils.gson_model;

import com.codez.mainlibrary.utilities.maps_utils.gson_model.common.Poly;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by Eptron on 10/22/2015.
 */
public class Route {
    private Bounds bounds;
    private String copyrights;
    private List<Leg> legs;
    private Poly overview_polyline;
    private String summary;
    private List<Object> warnings;

    public Bounds getBounds() {
        return bounds;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public List<Leg> getLegs() {
        return legs;
    }

    public Poly getOverview_polyline() {
        return overview_polyline;
    }

    public String getSummary() {
        return summary;
    }

    public List<Object> getWarnings() {
        return warnings;
    }

    class Bounds {
        private LatLngBounds northeast;
        private LatLngBounds southeast;
    }

}
