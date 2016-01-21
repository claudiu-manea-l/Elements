package com.codez.mainlibrary.utilities.maps_utils.helpers;

import android.content.Context;
import android.graphics.Bitmap;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.utilities.OtherUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 10/27/2015.
 */
public abstract class RequestHelper {

    public static final String MODE = "driving";
    protected GoogleMap mGoogleMap;
    protected Context mContext;
    protected boolean isInit;
    private List<MarkerOptions> mMarkers;

    public RequestHelper init(GoogleMap map, Context context) {
        mGoogleMap = map;
        mContext = context;
        mMarkers = new ArrayList<>();
        mBuilder = new LatLngBounds.Builder();
        EventBus.getDefault().register(this);
        isInit = true;
        return this;
    }

    public abstract boolean executeRequest();

    public boolean isInit() {
        return isInit;
    }

    public void reset() {
        isInit = false;
        mGoogleMap = null;
        mMarkers = new ArrayList<>();
        EventBus.getDefault().unregister(this);
    }

    private float RED = BitmapDescriptorFactory.HUE_RED;
    private float ORANGE = BitmapDescriptorFactory.HUE_ORANGE;
    private float BLUE = BitmapDescriptorFactory.HUE_BLUE;

    protected LatLngBounds.Builder mBuilder;
    protected HashMap<Integer, List<String>> mPosMap = new HashMap<>();
    protected String mAddress = "";
    protected String[] mWaypoints = new String[0];

    public void buildLines(GoogleMap map) {
        mGoogleMap = map;
    }


    public RequestHelper setHomeAddress(String address) {
        mAddress = address;
        return this;
    }

    public RequestHelper setWaypoints(String[] waypoints) {
        mWaypoints = waypoints;
        return this;
    }

    public RequestHelper setPosMap(HashMap<Integer, List<String>> map) {
        mPosMap = map;
        return this;
    }

    protected void addMarker(String title, LatLng latLng, int order) {
        int pos = title.indexOf(",");
        String snippet = "";
        int zipCode = -1;
        boolean multiple = false;
        try {
            String subString = title.substring(pos + 2, pos + 6);
            zipCode = Integer.parseInt(subString);
        } catch (NumberFormatException e) {
        }
        if (zipCode > 999 && mPosMap.containsKey(zipCode)) {
            for (int i = 0; i < mPosMap.get(zipCode).size(); i++) {
                if (i == 0) {
                    snippet = "POS : " + mPosMap.get(zipCode).get(i);
                } else {
                    snippet = snippet + "\n"
                            + "POS_" + i + ": " + mPosMap.get(zipCode).get(i);
                }
            }
        }
        if (!checkExisting(order, latLng)) {
            mMarkers.add(buildMarker(latLng, title, snippet, order));
        }
    }

    protected void addMarkers() {
        for (int i = 0; i < mMarkers.size(); i++)
            mGoogleMap.addMarker(mMarkers.get(i));
    }

    private boolean checkEquals(LatLng pos1, LatLng pos2) {
        if (pos1.latitude != pos2.latitude) return false;
        if (pos1.longitude != pos2.longitude) return false;
        return true;
    }

    private boolean checkExisting(int order, LatLng latLng) {
        MarkerOptions marker;
        for (int i = 0; i < mMarkers.size(); i++)
            if (checkEquals(mMarkers.get(i).getPosition(), latLng)) {
                marker = mMarkers.get(i);
                Bitmap bmp;
                String temp = order - 1 + "," + order;
                bmp = OtherUtils.drawTextToBitmap(mContext,
                        R.drawable.ic_marker, temp);
                marker.icon(BitmapDescriptorFactory.fromBitmap(bmp));
                mMarkers.remove(i);
                mMarkers.add(i, marker);
                return true;
            }
        return false;
    }

    private MarkerOptions buildMarker(LatLng latLng, String title, String snippet, int order) {
        MarkerOptions marker = new MarkerOptions();
        marker.position(latLng);
        marker.title(title);
        marker.snippet(snippet);
        if (order == 0) {
            marker.icon(BitmapDescriptorFactory.defaultMarker(BLUE));
        } else {
            Bitmap bmp = OtherUtils.drawTextToBitmap(mContext,
                    R.drawable.ic_marker, order + "");
            marker.icon(BitmapDescriptorFactory.fromBitmap(bmp));
        }
        return marker;
    }

}
