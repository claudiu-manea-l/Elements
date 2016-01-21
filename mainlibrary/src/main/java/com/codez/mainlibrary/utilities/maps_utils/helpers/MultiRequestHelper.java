package com.codez.mainlibrary.utilities.maps_utils.helpers;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;

import com.codez.mainlibrary.restfulkit.RestController;
import com.codez.mainlibrary.restfulkit.RestHandler;
import com.codez.mainlibrary.restfulkit.SampleRestController;
import com.codez.mainlibrary.utilities.OtherUtils;
import com.codez.mainlibrary.utilities.maps_utils.MapDirections;
import com.codez.mainlibrary.utilities.maps_utils.MapUtils;
import com.codez.mainlibrary.utilities.maps_utils.MultipleDirCallEvent;
import com.codez.mainlibrary.utilities.maps_utils.Waypoint;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.Leg;
import com.codez.mainlibrary.utilities.maps_utils.model.MapsConstants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 10/27/2015.
 */
public class MultiRequestHelper extends RequestHelper {

    private static MultiRequestHelper mHelper;
    private boolean shouldSortWaypoints = false;
    private boolean executeWithName = true;

    public static MultiRequestHelper getHelper() {
        if (mHelper == null) mHelper = new MultiRequestHelper();
        return mHelper;
    }

    protected MultiRequestHelper() {
    }

    private List<PolylineOptions> mRectLines = new ArrayList<>();

    @Override
    public void reset() {
        super.reset();
        mHelper = null;
        mRectLines = new ArrayList<>();
    }

    public void setFlagSortWaypoints(boolean shouldSort, boolean withName) {
        shouldSortWaypoints = shouldSort;
        executeWithName = withName;
    }

    public boolean executeRequest() {
        boolean hasExecuted = false;
        if (shouldSortWaypoints) {
            sortWaypoints();
        }
        executeNormal();
        return hasExecuted;
    }

    private void sortWaypoints() {
        List<Waypoint> temp = buildWaypointsLatLng();
        LatLng home = MapUtils.getCoordinatesFromAddress(mAddress, mContext);
        Waypoint[] sorted = MapUtils.sortWaypoints(temp, home);
        mWaypoints = new String[sorted.length];
        for (int i = 0; i < sorted.length; i++) {
            if (executeWithName) {
                mWaypoints[i] = sorted[i].getAddress();
            } else {
                mWaypoints[i] = sorted[i].getLatLng().latitude + "," + sorted[i].getLatLng().longitude;
            }
        }
    }

    private void executeNormal() {
        for (int i = 0; i < mWaypoints.length; i++) {
            RequestParams params;
            mBuilder = new LatLngBounds.Builder();
            String start, end;
            MultipleDirCallEvent event = new MultipleDirCallEvent(i);
            if (i == 0) {
                start = mAddress;
                end = mWaypoints[i];
            } else {
                start = mWaypoints[i - 1];
                end = mWaypoints[i];

            }
            event.setAddresses(start, end);
            params = buildDirectionsParams(start, end, MODE);
            Log.i("MultiRequestHelper", "Executing request: i=" + i + "  Start = " + start + "  End = " + end);
            postDelayed(params, event, i);
            if (i == mWaypoints.length - 1) {
                event = new MultipleDirCallEvent(i + 1);
                event.setLast();
                start = mWaypoints[i];
                end = mAddress;
                event.setAddresses(start, end);
                params = buildDirectionsParams(start, end, MODE);
                postDelayed(params, event, i);
                Log.i("MultiRequestHelper", "Executing request: i=" + i + "  Start = " + start + "  End = " + end);
            }
        }
    }

    private void postDelayed(final RequestParams params, final MultipleDirCallEvent event, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SampleRestController.getController(mContext)
                        .getDirections(params, event);

            }
        }, position * 110);
    }

    public void onEventMainThread(MultipleDirCallEvent event) {
        switch (event.getDirections().getStatus()) {
            case "OK":
                handleRestCall(event);
                break;
            case "OVER_QUERY_LIMIT":
                retryCall(event);
                break;
            default:
                Log.i("RouteMapActivity", buildErrorMessage(event));
                break;
        }
    }

    private void handleRestCall(MultipleDirCallEvent event) {
        String start = event.getStartAddress();
        String end = event.getEndAddress();
        Leg curLeg = event.getDirections().getRoutes().get(0).getLegs().get(0);
        Log.i("RouteMapActivity", "Status OK: start=" + start + " end=" + end + " isLast=" + event.isLast());
        boolean isFirst = false;
        //mDirectionPoints.addAll(event.getDirections().getRoutes().get(0).getOverview_polyline().getLine());
        buildLines(event.getDirections().getRoutes().get(0).getOverview_polyline().getLine());
        addRouteMarkers(curLeg, event.getOrder());

        if (event.isLast()) {
            EventBus.getDefault().post(new MapDirections());
            Log.i("RouteMapActivity", "Map Camera animated");
            addMarkers();
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 100));
            //isFresh = false;
        }
    }

    private void retryCall(MultipleDirCallEvent event) {
        String start = event.getStartAddress();
        String end = event.getEndAddress();
        Log.i("RouteMapActivity", "Status OVER_QUERY_LIMIT: start=" + start + " end=" + end);
        Log.i("RouteMapActivity", "Retrying...");
        RequestParams params = buildDirectionsParams(start, end, MODE);
        postDelayed(params, event, 4);
    }

    protected void buildLines(List<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(7).color(Color.CYAN);
        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }
        mRectLines.add(rectLine);
        mGoogleMap.addPolyline(rectLine);
    }

    @Override
    public void buildLines(GoogleMap map) {
        super.buildLines(mGoogleMap);
        for (int i = 0; i < mRectLines.size(); i++)
            mGoogleMap.addPolyline(mRectLines.get(i));
    }

    private void addRouteMarkers(Leg leg, int order) {
        addMarker(leg.getStartAddress(), leg.getStartLocation().getLatLng(), order);
        mBuilder.include(leg.getStartLocation().getLatLng());
    }

    public List<Waypoint> buildWaypointsLatLng() {
        List<Waypoint> temp = new ArrayList<>();
        for (int i = 0; i < mWaypoints.length; i++) {
            LatLng cur = MapUtils.getCoordinatesFromAddress(mWaypoints[i], mContext);
            if (cur != null) {
                temp.add(new Waypoint(cur, mWaypoints[i]));
            } else {
                Log.i("CrashTest", "Found null.... Address:" + mWaypoints[i]);
            }
        }
        return temp;
    }

    public void testWaypoints() {
        List<Waypoint> temp = buildWaypointsLatLng();
        LatLng home = MapUtils.getCoordinatesFromAddress(mAddress, mContext);
        Waypoint[] sorted = MapUtils.sortWaypoints(temp, home);
        double distance;
        for (int i = 0; i < sorted.length; i++) {
            if (i == 0) {
                distance = MapUtils.distanceBetween(home, sorted[i].getLatLng());
            } else {
                distance = MapUtils.distanceBetween(sorted[i - 1].getLatLng(), sorted[i].getLatLng());
                Log.i("Testing...", "Distance between" + sorted[i - 1].getAddress() + " and " +
                        sorted[i].getAddress() + " =" + distance);
            }

            if (i == sorted.length - 1) {
                distance = MapUtils.distanceBetween(sorted[i].getLatLng(), home);
                Log.i("Testing...", "Distance between" + sorted[i].getAddress() + " and Home =" + distance);
            }


        }
    }

    private String buildErrorMessage(MultipleDirCallEvent event) {
        String response = event.getResponseString();
        String status = event.getDirections().getStatus();
        String startAddress = "---", endAddress = "---";
        int legsCount = -1;
        int routeCount = event.getDirections().getRoutes().size();
        if (routeCount > 0)
            legsCount = event.getDirections().getRoutes().get(0).getLegs().size();
        if (legsCount > 0) {
            startAddress = event.getDirections().getRoutes().get(0).getLegs().get(0).getStartAddress();
            endAddress = event.getDirections().getRoutes().get(0).getLegs().get(0).getEndAddress();
        }
        String errorString = "Response: " + response + " \n Status: " + status +
                " \n isLast: " + event.isLast() + " \n Routes Count: " + routeCount +
                " \n Legs Count: " + legsCount + " \n Start Address = " + startAddress +
                " \n End Address = " + endAddress;
        return errorString;
    }

    public static RequestParams buildDirectionsParams(String startAddress, String endAddress, String[] waypoints, String mode, boolean isOptimized) {
        RequestParams params = buildDirectionsParams(startAddress, endAddress, mode);
        if (waypoints.length > 0) {
            params.put("waypoints", OtherUtils.buildWaypointString(waypoints, isOptimized));
        }
        return params;
    }

    public static RequestParams buildDirectionsParams(String startAddress, String endAddress, String mode) {
        RequestParams params = new RequestParams("mode", mode);
        params.add("sensor", "false");
        params.add("origin", startAddress);
        params.add("destination", endAddress);
        return params;
    }
}
