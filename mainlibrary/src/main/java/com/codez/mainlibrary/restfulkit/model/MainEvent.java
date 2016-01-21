package com.codez.mainlibrary.restfulkit.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Super class needed for all events to extend if they are to use the
 * RestController. Holds the JSONObject or JSONArray response from the
 * HttpCall
 * Created by Claudiu on 19/05/2015.
 */
public class MainEvent {
    protected JSONObject mJSONObject;
    protected JSONArray mJSONArray;
    private RestCall mRestCall;
    private String mResponseString;

    /**
     * Default Constructor
     */
    public MainEvent() {
        mResponseString = "";
    }

    /**
     * Getter
     *
     * @return
     */
    public RestCall getRestCall() {
        return mRestCall;
    }

    /**
     * Setter
     *
     * @param restCall the data of the rest call that executed this event
     */
    public void setRestCall(RestCall restCall) {
        mRestCall = restCall;
    }

    /**
     * Getter
     *
     * @return The response of the Rest Call (can be for both Fail and Success)
     */
    public String getResponseString() {
        return mResponseString;
    }

    /**
     * Setter
     *
     * @param response the Response from the rest call
     */
    public void setResponseString(String response) {
        mResponseString = response;
    }

    /**
     * Setter
     *
     * @param obj the JSONObject response from the rest call
     */
    public void setJSON(JSONObject obj) {
        mJSONObject = obj;
    }

    /**
     * Setter
     *
     * @param array the JSONArray response from the rest call
     */
    public void setJSON(JSONArray array) {
        mJSONArray = array;
    }

    /**
     * Parses the response depending on which type of object was
     * returned by the rest call
     */
    public void parseJSON() {
        if (mJSONObject != null)
            parseObject();
        else if ((mJSONArray != null)) {
            parseArray();
        }
    }

    public JSONObject getJSONObject() {
        return mJSONObject;
    }

    /**
     * Method needed to be override by the sub classes to parse the
     * JSONObject to their data structure
     */
    public void parseObject() {
        Log.w("MainEvent", "JSONObject was received but parseObject() was not override" + getClass().toString());
    }

    /**
     * Method needed to be override by the sub classes to parse the
     * JSONArray to their data structure
     */
    public void parseArray() {
        Log.w("MainEvent", "JSONArray was received but parseArray() was not override" + getClass().toString());
    }

    public static <T> List<T> readJsonStream(JSONArray array, Class<T> clazz) throws IOException {
        InputStream is = new ByteArrayInputStream(array.toString().getBytes());
        JsonReader reader = new JsonReader(new InputStreamReader(is, "UTF-8"));
        Gson gson = new GsonBuilder().serializeNulls().create();
        List<T> items = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            T item = gson.fromJson(reader, clazz);
            items.add(item);
        }
        reader.endArray();
        reader.close();
        return items;
    }
}
