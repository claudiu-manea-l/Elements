package com.codez.mainlibrary.utilities.maps_utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eptron on 10/28/2015.
 */
public class MapUtils {

    public static String simpleAddress(String address) {
        int pos = address.indexOf("-");
        return address.substring(0, pos - 2);
    }

    /**
     * Decodes the polyline string received from Google Directions API
     * and adds the polyline points to an array
     *
     * @param encoded the encoded string
     * @return the array containing all the polylines in
     * coordinate(LatLng) format
     */
    public static ArrayList<LatLng> decodePoly(String encoded) {
        ArrayList<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
            poly.add(position);
        }
        return poly;
    }


    public static String buildWaypointString(String[] waypoints, boolean isOptimized) {
        String waypointsString = "";
        if (isOptimized) {
            waypointsString = "optimize:true|";
        }
        String dashChar = "|";
        int length = waypoints.length;
        if (waypoints.length > 8) {
            length = 8;
        }
        for (int i = 0; i < length; i++)
            if (i == 0) {
                waypointsString += waypoints[i];
            } else {
                waypointsString += dashChar + waypoints[i];
            }
        return waypointsString;
    }

    public static LatLng getCoordinatesFromAddress(String address, Context context) {
        Geocoder coder = new Geocoder(context);
        List<Address> foundAddresses = new ArrayList<>();
        try {
            foundAddresses = coder.getFromLocationName(address, 2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (foundAddresses.isEmpty()) {
            try {
                foundAddresses = coder.getFromLocationName(simpleAddress(address), 2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LatLng temp = null;
        if (!foundAddresses.isEmpty())
            temp = new LatLng(foundAddresses.get(0).getLatitude(), foundAddresses.get(0).getLongitude());
        return temp;
    }

    public static Waypoint[] sortWaypoints(List<Waypoint> waypoints, LatLng home) {
        Waypoint[] sortedWaypoints = new Waypoint[waypoints.size()];
        int position;
        for (int i = 0; i < sortedWaypoints.length + 1; i++) {
            if (i == 0) {
                position = MapUtils.getMinDistance(waypoints, home);
            } else {
                position = MapUtils.getMinDistance(waypoints, sortedWaypoints[i - 1].getLatLng());
            }
            if (position != -1) {
                sortedWaypoints[i] = waypoints.get(position);
                waypoints.remove(position);
            }
        }
        return sortedWaypoints;
    }

    public static int getMinDistance(List<Waypoint> waypoints, LatLng initial) {
        double min = 999999999;
        int position = -1;
        for (int i = 0; i < waypoints.size(); i++) {
            Log.i("CrashCheck", "Lat=" +
                    waypoints.get(i).getLatLng().latitude + "  Long =" + waypoints.get(i).getLatLng().longitude);
            double cur = MapUtils.distanceBetween(initial, waypoints.get(i).getLatLng());
            if (min > cur) {
                min = cur;
                position = i;
            }
        }
        return position;
    }

    public static double distanceBetween(LatLng start, LatLng end) {
        return distance(start.latitude, start.longitude, end.latitude, end.longitude, "K");
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) +
                Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts decimal degrees to radians						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::	This function converts radians to decimal degrees						 :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
}
