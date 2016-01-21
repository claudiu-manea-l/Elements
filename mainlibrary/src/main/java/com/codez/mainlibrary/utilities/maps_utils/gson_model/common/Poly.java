package com.codez.mainlibrary.utilities.maps_utils.gson_model.common;

import android.text.TextUtils;

import com.codez.mainlibrary.utilities.OtherUtils;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by Eptron on 10/22/2015.
 */
public class Poly {
    private String points;

    public String getPoints() {
        return points;
    }

    public ArrayList<LatLng> getLine() {
        if (!TextUtils.isEmpty(points))
            return OtherUtils.decodePoly(points);
        else return new ArrayList<>();
    }
}
