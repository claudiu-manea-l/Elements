package com.codez.mainlibrary.restfulkit;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.codez.mainlibrary.restfulkit.model.MainEvent;
import com.codez.mainlibrary.restfulkit.model.RestCall;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Controller class in charge of executing the specific requests
 * sent by the subClass implementing this class. Also keeps track of the
 * calls executed
 * The subClass will simply call on doRestCall with the proper parameters and
 * rest will be handled by the controller
 * Created by Claudiu on 19/05/2015.
 */
public abstract class RestController {

    public static String BASE_URL;
    public static boolean DEBUG;
    protected RestHandler.Builder sBuilder;
    private int mErrors = 0;
    private int mSuccesses = 0;
    private ArrayList<RestCall> mCalls = new ArrayList<>();
    private List<RestHandler> mRestCallsToBeExecuted = new ArrayList<>();

    protected abstract Context getContext();

    /**
     * Execute rest call of the given type to the specified methodCall
     * with the given parameters
     *
     * @param methodCall The API method name to be called to
     * @param params     The parameters needed for the method name (@RequestParams)
     * @param type       The type of HttpCall (GET,POST,PUT)
     * @return if the call was executed or not (lack of internet connection)
     */
    public boolean doRestCall(String methodCall, RequestParams params, int type) {
        return doRestCall(methodCall, params, type, null);
    }

    /**
     * Execute rest call of the given type to the specified methodCall
     * with the given parameters
     *
     * @param methodCall      The API method name to be called to
     * @param params          The parameters needed for the method name (@RequestParams)
     * @param type            The type of HttpCall (GET,POST,PUT)
     * @param successfulEvent the event to be posted if methodCall is successful
     *                        if null is given it will create an empty (@MainEvent)
     *                        with the returned JSONObject/JSONArray by the call
     * @return if the call was executed or not (lack of internet connection)
     */
    public boolean doRestCall(String methodCall, RequestParams params, int type, MainEvent successfulEvent) {
        return doRestCall(methodCall, params, type, successfulEvent, null);
    }

    /**
     * Builds and execute rest call of the given type to the specified methodCall
     * with the given parameters
     *
     * @param methodCall      The API method name to be called to
     * @param params          The parameters needed for the method name (@RequestParams)
     * @param type            The type of HttpCall (GET,POST,PUT)
     * @param successfulEvent the event to be posted if methodCall is successful
     *                        if null is given it will create a default event
     *                        of type MainEvent (@MainEvent) containing the
     *                        returned JSONObject/JSONArray by the call
     * @param failEvent       The event to be posed if methodCall is unsuccessful
     *                        if null is given it will create a default event
     *                        of type MainEvent (@MainEvent) containing the
     *                        returned JSONObject/JSONArray by the call
     * @return if the call was executed or not (lack of internet connection)
     */
    public boolean doRestCall(String methodCall, RequestParams params, int type,
                              MainEvent successfulEvent, MainEvent failEvent) {
        return doRestCall(methodCall, params, type, successfulEvent, failEvent, false);
    }

    public boolean doRestCall(String methodCall, RequestParams params, int type,
                              MainEvent successfulEvent, MainEvent failEvent, boolean shouldExecuteLater) {
        RestHandler temp = sBuilder
                .setMethodCall(methodCall)
                .setRequestParams(params)
                .setEventSuccess(successfulEvent)
                .setEventFail(failEvent)
                .setType(type)
                .create(this);
        if (hasInternet()) {
            if (DEBUG) Log.i("RestController", "Starting execution of request = " + methodCall);
            sBuilder.build(temp);
            return true;
        } else {
            if (shouldExecuteLater)
                mRestCallsToBeExecuted.add(temp);
        }
        return false;
    }

    public boolean doCustomRestCall(String url, RequestParams params, int type,
                                    MainEvent successfulEvent, MainEvent failEvent) {
        if (hasInternet()) {
            if (DEBUG) Log.i("RestController", "Starting execution of custom request = " + url);
            sBuilder
                    .setCustomCall(true)
                    .setCustomUrl(url)
                    .setRequestParams(params)
                    .setEventSuccess(successfulEvent)
                    .setEventFail(failEvent)
                    .setType(type)
                    .build(this);
            return true;
        }
        return false;
    }

    public boolean doJSONRestPost(String methodCall, String jsonObject) {
        return doJSONRestPost(methodCall, jsonObject, null, null);
    }

    public boolean doJSONRestPost(String methodCall, String jsonObject, MainEvent successfulEvent, MainEvent failureEvent) {
        return doJSONRestPost(methodCall, jsonObject, null, successfulEvent, failureEvent);
    }

    public boolean doJSONRestPost(String methodCall, String jsonObject, RequestParams params, MainEvent successfulEvent, MainEvent failureEvent) {
        if (hasInternet()) {
            if (DEBUG) Log.i("RestController", "Starting execution of json Post =" + methodCall);
            StringEntity entity = new StringEntity(jsonObject, "UTF-8");
            sBuilder
                    .setMethodCall(methodCall)
                    .setRequestParams(params)
                    .setEntity(entity)
                    .setType(RestHandler.TYPE_JSON)
                    .setEventSuccess(successfulEvent)
                    .setEventFail(failureEvent)
                    .build(this);
            return true;
        }
        return false;
    }

    public void executeLeftOverCalls() {
        for (int i = 0; i < mRestCallsToBeExecuted.size(); i++) {
            sBuilder.build(mRestCallsToBeExecuted.get(i));
        }
        mRestCallsToBeExecuted.clear();
    }


    /**
     * Handles the response from the RestHandler and does some stuff
     * Still to be improved and added more logic
     *
     * @param call The HttpCall request and response details
     */
    public void handleResponse(RestCall call) {
        mCalls.add(call);
        if (call.isSuccessful()) mSuccesses++;
        else mErrors++;
    }

    /**
     * Getter
     *
     * @return Returns all the calls handled by this controller
     */
    public ArrayList<RestCall> getCalls() {
        return mCalls;
    }

    /**
     * Getter
     *
     * @return number of failed rest calls
     */
    public int getErrorCount() {
        return mErrors;
    }

    /**
     * Getter
     *
     * @return number of successful rest calls
     */
    public int getSuccessCount() {
        return mSuccesses;
    }

    public boolean hasInternet() {
        try {
            final ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }
}
