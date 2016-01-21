package com.codez.mainlibrary.utilities.maps_utils.worker;

import com.codez.mainlibrary.utilities.maps_utils.model.MapsConstants;
import com.codez.mainlibrary.utilities.maps_utils.model.RouteDirections;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;
import cz.msebera.android.httpclient.protocol.BasicHttpContext;
import cz.msebera.android.httpclient.protocol.HttpContext;

/**
 * Created by eptron on 19/03/2015.
 * In charge of requesting the route information from Google Directions API service
 */
public class GMapV2Directions {

    public final static String MODE_DRIVING = "driving";
    public final static String MODE_WALKING = "walking";

    public GMapV2Directions() {
    }

    /**
     * Fetches the RouteDirections between the 2 specify addresses taking into
     * account the given waypoints. The route will be optimized for the specified
     * waypoints
     *
     * @param address1  the start address from which to calculate the route
     * @param address2  the end address at which the route ends
     * @param mode      Driving/Walking/Bicycling/etc
     * @param waypoints The route waypoints
     * @return all the route info stored
     */
    public RouteDirections getRouteWithWaypoints(String address1, String address2,
                                                 String mode, String[] waypoints) {
        RouteDirections directions = null;
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + address1 + "&destination=" + address2
                + "&sensor=false&units=metric&mode=" + mode + "&waypoints=optimize:true";
        String dashChar = "|";
        try {
            dashChar = URLEncoder.encode("|", "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < waypoints.length; i++)
            url = url + dashChar + waypoints[i];

        JSONObject mainJson = fetchMainJSON(url);

        try {
            JSONArray routesArray = mainJson.getJSONArray(MapsConstants.ROUTE);
            for (int i = 0; i < routesArray.length(); i++) {
                JSONObject json = routesArray.getJSONObject(i);
                directions = new RouteDirections(json);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return directions;
    }


    /**
     * Fetches the RouteDirections between the 2 specify addresses
     *
     * @param address1 the start address from which to calculate the route
     * @param address2 the end address at which the route ends
     * @param mode     Driving/Walking/Bicycling/etc
     * @return all the route info stored
     */
    public RouteDirections getRouteDirections(String address1, String address2, String mode) {
        RouteDirections directions = null;
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin=" + address1 + "&destination=" + address2
                + "&sensor=false&units=metric&mode=" + mode;
        JSONObject mainJson = fetchMainJSON(url);

        try {
            if(mainJson!=null) {
                JSONArray routesArray = mainJson.getJSONArray(MapsConstants.ROUTE);
                for (int i = 0; i < routesArray.length(); i++) {
                    JSONObject json = routesArray.getJSONObject(i);
                    directions = new RouteDirections(json);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return directions;
    }

    /**
     * Queries the Google Directions for a JSON type of data
     *
     * @param url Points towards Google Directions Api
     * @return The route directions in JSON format
     */
    public JSONObject fetchMainJSON(String url) {
        JSONObject jsonObject = null;
        InputStream inputStream = null;
        String result = "";
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpContext localContext = new BasicHttpContext();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse response = httpClient.execute(httpPost, localContext);
            inputStream = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inputStream != null)
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        inputStream, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                result = sb.toString();
                return new JSONObject(result);
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }

}
