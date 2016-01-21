package com.codez.mainlibrary.utilities.maps_utils;

import android.util.Log;

import com.codez.mainlibrary.restfulkit.model.MainEvent;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.DirectionsResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Created by Eptron on 7/30/2015.
 */
public class SuccessfulMapEvent extends MainEvent {

    private DirectionsResponse mResponse;

    @Override
    public void parseObject() {
        Gson gson = new GsonBuilder().serializeNulls().create();
        mResponse = gson.fromJson(mJSONObject.toString(), DirectionsResponse.class);
        Log.i("SuccessfulMapEvent","Success");
    }

    public DirectionsResponse getDirections() {
        return mResponse;
    }
}
