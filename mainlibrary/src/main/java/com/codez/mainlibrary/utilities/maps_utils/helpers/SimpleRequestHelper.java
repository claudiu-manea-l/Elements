package com.codez.mainlibrary.utilities.maps_utils.helpers;

import android.graphics.Color;
import android.widget.Toast;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.restfulkit.SampleRestController;
import com.codez.mainlibrary.utilities.maps_utils.MapDirections;
import com.codez.mainlibrary.utilities.maps_utils.MultipleDirCallEvent;
import com.codez.mainlibrary.utilities.maps_utils.SuccessfulMapEvent;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.Leg;
import com.codez.mainlibrary.utilities.maps_utils.gson_model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.loopj.android.http.RequestParams;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 10/27/2015.
 */
public class SimpleRequestHelper extends RequestHelper {

    private static SimpleRequestHelper mHelper;

    public static SimpleRequestHelper getHelper() {
        if (mHelper == null) mHelper = new SimpleRequestHelper();
        return mHelper;
    }

    private SimpleRequestHelper() {
    }

    private PolylineOptions mRectLine;

    @Override
    public void reset() {
        super.reset();
        mHelper = null;
        mRectLine = null;
    }

    public boolean executeRequest() {
        RequestParams params = MultiRequestHelper.buildDirectionsParams(
                mAddress, mAddress, mWaypoints, MODE, true);
        return SampleRestController.getController(mContext).getDirections(params);
    }

    public void onEventMainThread(SuccessfulMapEvent event) {
        EventBus.getDefault().post(new MapDirections());
        if (event instanceof MultipleDirCallEvent)
            return;
        if (event.getDirections().getStatus().equals("OK")) {
            Route route = event.getDirections().getRoutes().get(0);
            buildLines(route.getOverview_polyline().getLine());
            addRouteMarkers(route.getLegs());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(mBuilder.build(), 100));
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.common_error_waypoint), Toast.LENGTH_SHORT).show();
        }
    }

    protected void buildLines(List<LatLng> directionPoints) {
        mRectLine = new PolylineOptions().width(7).color(Color.CYAN);
        for (int i = 0; i < directionPoints.size(); i++) {
            mRectLine.add(directionPoints.get(i));
        }
        mGoogleMap.addPolyline(mRectLine);
    }

    @Override
    public void buildLines(GoogleMap googleMap) {
        super.buildLines(googleMap);
        mGoogleMap.addPolyline(mRectLine);
    }

    protected void addRouteMarkers(List<Leg> legs) {
        for (int i = 0; i < legs.size(); i++) {
            addMarker(legs.get(i).getStartAddress(), legs.get(i).getStartLocation().getLatLng(), i);
            mBuilder.include(legs.get(i).getStartLocation().getLatLng());
        }
        addMarkers();
    }

}
