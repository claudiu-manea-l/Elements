package com.codez.mainlibrary.utilities.maps_utils.model;

import android.text.Html;
import android.util.Log;

import com.codez.mainlibrary.utilities.maps_utils.MapUtils;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eptron on 19/03/2015.
 * Holder and parser class for the intermediary points between departure and
 * destination address. Data needed for the class should be pulled from
 * Google Directions API.
 */
public class Logistics {

    //Indexes for the LatLng holder array
    private final static int START = 0;
    private final static int END = 1;

    private List<Step> mSteps;
    private RouteInfo mDuration;
    private RouteInfo mDistance;
    private LatLng mStartLocation;
    private LatLng mEndLocation;
    private String mStartAddress;
    private String mEndAddress;

    /**
     * Parses the JSONObject of all the data and adds it to this holder class
     *
     * @param jsonObject JSONObject to be parsed containing all the information
     *                   between the start and end location(if no waypoints were
     *                   given the Route will be between start and end location)
     *                   Otherwise it will take in account the waypoints and contain
     *                   information for them as well.
     * @throws JSONException Exception if some of the data pulled does not match
     *                       the expected format
     */
    public Logistics(JSONObject jsonObject) throws JSONException {
        parseArray(jsonObject.getJSONArray(MapsConstants.STEP));
        mDuration = new RouteInfo(
                jsonObject.getJSONObject(MapsConstants.DURATION));
        mDistance = new RouteInfo(
                jsonObject.getJSONObject(MapsConstants.DISTANCE));
        mStartLocation = getLocation(
                jsonObject.getJSONObject(MapsConstants.START_LOC));
        mEndLocation = getLocation(
                jsonObject.getJSONObject(MapsConstants.END_LOC));
        mStartAddress = jsonObject.getString(MapsConstants.START_ADD);
        mEndAddress = jsonObject.getString(MapsConstants.END_ADD);
    }

    /**
     * @param jsonObject JSONObject to be parse of LATITUDE AND LONGITUDE
     * @return The LatLng initialized by the parsed values
     * @throws JSONException Exception if some of the data pulled does not match
     *                       the expected format
     */
    public static LatLng getLocation(JSONObject jsonObject) throws JSONException {
        double lat, lng;
        lat = jsonObject.getDouble(MapsConstants.LATITUDE);
        lng = jsonObject.getDouble(MapsConstants.LONGITUDE);
        return new LatLng(lat, lng);
    }

    /**
     * Parses through the JSONArray retrieved from the Google Directions
     * API
     *
     * @param jsonArray Steps array in JSON format to be parsen
     * @throws JSONException Exception if some of the data pulled does not match
     *                       the expected format
     */
    private void parseArray(JSONArray jsonArray) throws JSONException {
        mSteps = new ArrayList<Step>();
        for (int i = 0; i < jsonArray.length(); i++)
            mSteps.add(new Step(jsonArray.getJSONObject(i)));
    }

    /**
     * Getter for all the steps required in the Route between Start
     * location and End location
     *
     * @return The steps in a mAbsView format
     */
    public List<Step> getSteps() {
        return mSteps;
    }

    /**
     * Getter for the distance between start and end(overall
     * or between waypoints if they were given)
     *
     * @return the Duration in RouteInfo format(value+text)
     */
    public RouteInfo getDuration() {
        return mDuration;
    }

    /**
     * Getter for the distance between start and end(overall
     * or between waypoints if they were given)
     *
     * @return the Distance in RouteInfo format(value+text)
     */
    public RouteInfo getDistance() {
        return mDistance;
    }

    /**
     * Getter for the start route/waypoint address
     *
     * @return the start address in coordinates(LatLng) format
     */
    public LatLng getStartLocation() {
        return mStartLocation;
    }

    /**
     * Getter for the end route
     *
     * @return the end address in coordinates(LatLng) format
     */
    public LatLng getEndLocation() {
        return mEndLocation;
    }

    /**
     * Getter for the start route/waypoint address
     *
     * @return the address in string format
     */
    public String getStartAddress() {
        return mStartAddress;
    }

    /**
     * Getter for the end route/waypoint address
     *
     * @return the address in string format
     */
    public String getEndAddress() {
        return mEndAddress;
    }

    /**
     * Holder and parser class for a step in a Route
     * One step is represented by a fixed/straight road until an intersection
     * or an alteration of the route(turn or stop)
     */
    public class Step {
        private String mMode;
        private LatLng mStartLocation;
        private LatLng mEndLocation;
        private ArrayList<LatLng> mPolyline;
        private RouteInfo mDuration;
        private RouteInfo mDistance;
        private String mInfo;

        /**
         * @param jsonObject Object to be parsed
         * @throws JSONException Exception if some of the data pulled does not match
         *                       the expected format
         */
        private Step(JSONObject jsonObject) throws JSONException {
            mMode = jsonObject.getString(MapsConstants.MODE);
            mStartLocation = getLocation(
                    jsonObject.getJSONObject(MapsConstants.START_LOC));
            mEndLocation = getLocation(
                    jsonObject.getJSONObject(MapsConstants.END_LOC));
            mPolyline = MapUtils.decodePoly(
                    jsonObject.getJSONObject(MapsConstants.POLYLINE)
                            .getString(MapsConstants.POINTS));
            mDistance = new RouteInfo(
                    jsonObject.getJSONObject(MapsConstants.DISTANCE));
            mDuration = new RouteInfo(
                    jsonObject.getJSONObject(MapsConstants.DURATION));
            String encoded = jsonObject.getString(MapsConstants.INSTRUCTION);
            Log.i("JSON", "before fromHtml" + encoded);
            encoded = Html.fromHtml(encoded).toString();
            Log.i("JSON", " after Html.fromHtml = " + encoded);
            try {
                mInfo = URLDecoder.decode(encoded, "UTF-32");
            } catch (Exception e) {
                e.printStackTrace();
                mInfo = encoded;
            }
            Log.i("JSON", "after URLDecoder.decode() = " + mInfo);
            //mInfo = jsonObject.getString(MapsConstants.INSTRUCTION);
        }

        /**
         * Getter for the mode of traveling (Driving/Walking/etc..)
         *
         * @return the mode of traveling
         */
        public String getMode() {
            return mMode;
        }

        /**
         * Getter for this step location
         *
         * @return This step locatation in coordinate(LatLng) format
         */
        public LatLng getStartLocation() {
            return mStartLocation;
        }

        /**
         * Getter for next step location
         *
         * @return The next step location in coordinate(LatLng) format
         */
        public LatLng getEndLocation() {
            return mEndLocation;
        }

        /**
         * Getter for Polylines between this step and next step
         *
         * @return The points between the previous and this step in
         * LatLng format used later for drawing the line between the
         * 2 steps.
         */
        public ArrayList<LatLng> getPolyline() {
            return mPolyline;
        }

        /**
         * Getter for Duration between this step and next step
         *
         * @return the Duration in RouteInfo format(value+text)
         */
        public RouteInfo getDuration() {
            return mDuration;
        }

        /**
         * Getter for Distance between this step and next step
         *
         * @return the Distance in RouteInfo format(value+text)
         */
        public RouteInfo getDistance() {
            return mDistance;
        }
    }

    /**
     * Holder and Parser class for the Info between 2 steps/points.
     * Can be Duration or Distance
     */
    public class RouteInfo {
        private int mValue;
        private String mText;

        /**
         * @param jsonObject Object to parse of data
         * @throws JSONException Exception if some of the data pulled does not match
         *                       the expected format
         */
        public RouteInfo(JSONObject jsonObject) throws JSONException {
            mValue = jsonObject.getInt(MapsConstants.VALUE);
            mText = jsonObject.getString(MapsConstants.TEXT);
        }

        /**
         * @return the value in integers of the Duration/Distance
         */
        public int getValue() {
            return mValue;
        }

        /**
         * @return the value in text of the Duration/Distance
         */
        public String getText() {
            return mText;
        }
    }
}
