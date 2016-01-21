package com.codez.mainlibrary.utilities.maps_utils.model;

import com.codez.mainlibrary.utilities.OtherUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by eptron on 19/03/2015.
 * Holder and parser class for all the route information between 2 waypoints
 */
public class RouteDirections {
    //Summary holds the MODE = Driving/Walking/etc
    private String mSummary;
    private ArrayList<Logistics> mLogistics;
    //The overall route info in LatLng used to draw polylines
    private ArrayList<LatLng> mOverviewPolyline;
    private LatLng mBoundsSW;
    private LatLng mBoundsNE;

    /**
     * Constructor, parsing the given JSONObject
     *
     * @param jsonObject All the route info pulled from Google Direction API
     *                   needed in order to parse the data
     * @throws JSONException Exception if some of the data pulled does not match
     *                       the expected format
     */
    public RouteDirections(JSONObject jsonObject) throws JSONException {
        mSummary = jsonObject.getString(MapsConstants.SUMMARY);
        parseLogisticsArray(jsonObject.getJSONArray(MapsConstants.LOGISTICS));
        mOverviewPolyline = OtherUtils.decodePoly(
                jsonObject.getJSONObject(MapsConstants.OVERVIEW_POLY)
                        .getString(MapsConstants.POINTS));
        mBoundsNE = Logistics.getLocation(
                jsonObject.getJSONObject(MapsConstants.BOUNDS)
                        .getJSONObject(MapsConstants.NORTHEAST));
        mBoundsSW = Logistics.getLocation(
                jsonObject.getJSONObject(MapsConstants.BOUNDS)
                        .getJSONObject(MapsConstants.SOUTHWEST));
    }

    /**
     * Parses the "leg"-s children and assigning them to the class mLogistics member
     *
     * @param jsonArray Logistics JSONArray to be parsed
     * @throws JSONException Exception if some of the data pulled does not match
     *                       the expected format
     */
    private void parseLogisticsArray(JSONArray jsonArray) throws JSONException {
        mLogistics = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++)
            mLogistics.add(new Logistics(jsonArray.getJSONObject(i)));
    }

    /**
     * @return the route summary (Usually: the MODE requested through the url)
     * Can be: Driving/Walking/etc..
     */
    public String getSummary() {
        return mSummary;
    }

    /**
     * @return the logistics Array containing all the intermediary points
     * between the departure and destination addresses aswell as information
     * about each point such as Distance/Duration
     */
    public ArrayList<Logistics> getLogistics() {
        return mLogistics;
    }

    /**
     * The overall sum of the intermediary points used to draw the route
     * on a GoogleMap object
     *
     * @return
     */
    public ArrayList<LatLng> getOverviewPolyline() {
        return mOverviewPolyline;
    }

    //Not really sure
    public LatLng getBoundsSW() {
        return mBoundsSW;
    }

    //Not really sure
    public LatLng getBoundsNE() {
        return mBoundsNE;
    }
}
