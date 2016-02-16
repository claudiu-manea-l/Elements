package com.codez.mainlibrary.maps_utils;

import android.content.Context;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.MediumTest;

import com.codez.mainlibrary.utilities.maps_utils.MapUtils;
import com.codez.mainlibrary.utilities.maps_utils.Waypoint;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by Shafqat on 2/11/2016.
 */
@RunWith(AndroidJUnit4.class)
@MediumTest
public class MapUtilsTests {

    private Context mContext;

    @Before
    public void initTargetContext() {
        mContext = getTargetContext();
        assertThat(mContext, notNullValue());
    }

    @Test
    public void getCoordinatesFromAddressTest() {
        // ref: http://www.gps-coordinates.net/
        LatLng coordinates = MapUtils.getCoordinatesFromAddress
                ("Riga Central Market, Latgale Suburb, Riga, LV-1050, Latvia", mContext);
        assertThat(coordinates, is(new LatLng(56.9436762, 24.1146194)));
    }

    @Test
    public void wayPointsSortingTest() {
        // ref: http://www.gps-coordinates.net/
        List<Waypoint> waypoints = new ArrayList<Waypoint>();
        String address = "Maskavas iela 2, Latgales priekšpilsēta, Rīga, LV-1050, Latvia";
        waypoints.add(new Waypoint(MapUtils.getCoordinatesFromAddress(address, mContext), address));
        address = "Centrāltirgus iela 5, Latgales priekšpilsēta, Rīga, LV-1050, Latvia";
        waypoints.add(new Waypoint(MapUtils.getCoordinatesFromAddress(address, mContext), address));
        address = "Prāgas iela 1, Latgales priekšpilsēta, Rīga, LV-1050, Latvia";
        waypoints.add(new Waypoint(MapUtils.getCoordinatesFromAddress(address, mContext), address));
        address = "Centrāltirgus iela 1A, Latgales priekšpilsēta, Rīga, LV-1050, Latvia";
        waypoints.add(new Waypoint(MapUtils.getCoordinatesFromAddress(address, mContext), address));

        //Waypoint have been sorted out manually for comparison..
        Waypoint[] benchmark = new Waypoint[waypoints.size()];
        benchmark[0] = waypoints.get(0);
        benchmark[1] = waypoints.get(3);
        benchmark[2] = waypoints.get(1);
        benchmark[3] = waypoints.get(2);

        Waypoint[] sortedWaypoints = MapUtils.sortWaypoints(waypoints, waypoints.get(0).getLatLng());
        assertThat(sortedWaypoints, is(benchmark));
    }

    //@Test
    public void buildWaypointStringTest() {
        //What is MapUtils.buildWaypointString() is doing exactly ?
    }
}
