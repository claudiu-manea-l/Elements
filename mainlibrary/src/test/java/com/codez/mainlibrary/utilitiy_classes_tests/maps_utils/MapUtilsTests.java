package com.codez.mainlibrary.utilitiy_classes_tests.maps_utils;

import android.test.suitebuilder.annotation.SmallTest;

import com.codez.mainlibrary.utilities.maps_utils.MapUtils;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by Shafqat on 2/11/2016.
 */
@SmallTest
public class MapUtilsTests {

    @Test
    public void simpleAddressTest() {
        //Why did you create MapUtils.simpleAddress() ?
        String address = MapUtils.simpleAddress("Riga Central Market, Latgale Suburb, Riga LV-1050, Latvia");
        assertThat(address, is("Riga Central Market, Latgale Suburb, Riga "));
    }

    @Test
    public void latLongDistanceTest() {
        LatLng start = new LatLng(50.06639, 5.714722);
        LatLng end = new LatLng(58.64389, 3.07);
        double distance = MapUtils.distanceBetween(start, end);
        assertThat(new Double(String.format("%.1f", distance)), is(968.8));
    }

    @Test
    public void latLongDistanceInKmTest() {
        LatLng start = new LatLng(50.06639, 5.714722);
        LatLng end = new LatLng(58.64389, 3.07);
        double distance = MapUtils.distance(start.latitude, start.longitude, end.latitude, end.longitude, "K");
        assertThat(new Double(String.format("%.2f", distance)), is(968.81));
    }

    @Test
    public void latLongDistanceInNmTest() {
        LatLng start = new LatLng(50.06639, 5.714722);
        LatLng end = new LatLng(58.64389, 3.07);
        double distance = MapUtils.distance(start.latitude, start.longitude, end.latitude, end.longitude, "N");
        assertThat(new Double(String.format("%.2f", distance)), is(522.77));
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

    @Test
    public void degreesToRadiansConversionTest() {
        double radians = deg2rad(200);
        assertThat(new Double(String.format("%.2f", radians)), is(3.49));
    }

    @Test
    public void radiansTodegreesConversionTest() {
        double degrees = rad2deg(3.49);
        assertThat(new Double(String.format("%.0f", degrees)), is(200.0));
    }
}
