package com.codez.mainlibrary.restfulkit;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;

import com.codez.mainlibrary.restfulkit.model.FailedRequest;
import com.codez.mainlibrary.restfulkit.model.MainEvent;
import com.codez.mainlibrary.restfulkit.model.RestCall;
import com.codez.mainlibrary.restfulkit.model.SuccessfulRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;
import cz.msebera.android.httpclient.protocol.HTTP;
import de.greenrobot.event.EventBus;

/**
 * Handler class for JSON type HttpRequest and Response
 * Created by Claudiu on 19/05/2015.
 */
public class RestHandler extends JsonHttpResponseHandler {
    private static final int SECONDS = 1000;

    public static final int TYPE_GET = 0;
    public static final int TYPE_POST = 1;
    public static final int TYPE_PUT = 2;
    public static final int TYPE_JSON = 3;
    private static final int SOCKET_TIMEOUT = 40 * SECONDS;
    //API URL -- Needs to be changed to the appropriate URL
    public static String BASE_URL = RestController.BASE_URL;
    public static boolean DEBUG = RestController.DEBUG;
    //Things needed for the Builder to execute calls
    private static AsyncHttpClient client = new AsyncHttpClient();
    private RestController mController;
    private RequestParams mParams;
    private MainEvent mSuccessEvent;
    private MainEvent mFailureEvent;
    private String mMethodCall;
    private String mCustomUrl;
    private boolean mHasCustomUrl = false;
    private long mStartTime;
    private StringEntity mStringEntity;
    private boolean isSuccessful;

    /**
     * Default constructor
     *
     * @param controller Controller that started this handler needed in order to post back
     *                   execution data to the controller
     */
    public RestHandler(RestController controller) {
        mController = controller;
        client.setTimeout(SOCKET_TIMEOUT);
    }

    public static String getAbsoluteUrl(String url) {
        return BASE_URL + url;
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
        isSuccessful = true;
        handleResponse(statusCode, null, response);
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
        isSuccessful = true;
        handleResponse(statusCode, response, null);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        isSuccessful = false;
        handleResponse(statusCode, errorResponse, null);
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        isSuccessful = false;
        long tempTime = SystemClock.currentThreadTimeMillis();
        RestCall temp = new RestCall(statusCode, mMethodCall, (int) tempTime, isSuccessful, mParams);
        mFailureEvent.setRestCall(temp);
        mFailureEvent.setResponseString(responseString);
        EventBus.getDefault().post(mFailureEvent);
    }

    public Context getContext() {
        return mController.getContext();
    }

    /**
     * Handles the response from the HttpCall
     *
     * @param statusCode Status code of the HttpCall response
     * @param object     the JSONObject returned by the Call (null if an array was returned)
     * @param array      the JSONArray returned by the Call (null if an object was returned)
     */
    private void handleResponse(int statusCode, JSONObject object, JSONArray array) {
        long tempTime = SystemClock.currentThreadTimeMillis();
        RestCall temp = new RestCall(statusCode, mMethodCall, (int) tempTime, isSuccessful, mParams);
        mController.handleResponse(temp);
        if (isSuccessful) {
            mSuccessEvent.setJSON(object);
            mSuccessEvent.setJSON(array);
            mSuccessEvent.parseJSON();
            mSuccessEvent.setRestCall(temp);
            EventBus.getDefault().post(mSuccessEvent);
            if (DEBUG) Log.i("RestHandler", "Successful request  " + temp.getMethodCall());
        } else {
            mFailureEvent.setJSON(object);
            mFailureEvent.parseJSON();
            mFailureEvent.setRestCall(temp);
            EventBus.getDefault().post(mFailureEvent);
            if (DEBUG) Log.i("RestHandler", "Failed request  " + temp.getMethodCall());
        }
    }

    /**
     * Builder class in charge of building RestHandlers and executing them over
     * and AsyncHttpClient
     */
    public static class Builder {
        // RestHandler mHandler;
        MainEvent sSuccessEvent;
        MainEvent sFailureEvent;
        String sMethodCall;
        RequestParams sParams;
        StringEntity sEntity;
        long sStartTime;
        int sType;
        boolean sCustom = false;
        String sCustomUrl;

        /**
         * Sets the RequestParams (@RequestParams) for the request.
         *
         * @param params The request parameters to be used for the RestHandler
         * @return this instance of the class for easier execution
         */
        public Builder setRequestParams(RequestParams params) {
            sParams = params;
            return this;
        }

        /**
         * Sets the Event to be posted in case of Success
         *
         * @param event the event of type (@MainEvent)
         * @return *this instance of the class for easier execution
         */
        public Builder setEventSuccess(MainEvent event) {
            sSuccessEvent = event;
            return this;
        }

        /**
         * Sets the Event to be posted in case of Failure
         *
         * @param event the event of type (@MainEvent)
         * @return *this instance of the class for easier execution
         */
        public Builder setEventFail(MainEvent event) {
            sFailureEvent = event;
            return this;
        }

        /**
         * Sets the method name in the api that shall be executed
         *
         * @param methodCall the method name
         * @return this instance of the class for easier execution
         */
        public Builder setMethodCall(String methodCall) {
            sMethodCall = methodCall;
            return this;
        }

        /**
         * Not yet implemented
         *
         * @param startTime the start time when the tread would start
         * @return
         */
        public Builder setStartTime(long startTime) {
            sStartTime = startTime;
            return this;
        }

        /**
         * Sets the type of HttpCall to be executed (ex: GET,POST,PUT)
         * types can be found under RestHandler.* (ex: RestHandler.TYPE_GET)
         *
         * @param type the type for the HttpCall
         * @return this instance of the class for easier execution
         */
        public Builder setType(int type) {
            sType = type;
            return this;
        }

        public Builder setEntity(StringEntity entity) {
            sEntity = entity;
            return this;
        }

        public Builder setCustomCall(boolean isCustom) {
            sCustom = true;
            return this;
        }

        public Builder setCustomUrl(String url) {
            sCustomUrl = url;
            return this;
        }

        public RestHandler create(RestController controller) {
            RestHandler handler = new RestHandler(controller);
            if (sSuccessEvent == null) {
                sSuccessEvent = new SuccessfulRequest();
            }
            if (sFailureEvent == null) {
                sFailureEvent = new FailedRequest();
            }
            handler.mSuccessEvent = sSuccessEvent;
            handler.mFailureEvent = sFailureEvent;
            handler.mMethodCall = sMethodCall;
            handler.mStartTime = sStartTime;
            handler.mParams = sParams;
            handler.mCustomUrl = sCustomUrl;
            handler.mHasCustomUrl = sCustom;
            handler.mStringEntity = sEntity;
            return handler;
        }

        /**
         * Builds the RestHandler and executes it with the build parameters set to the builder over
         * Http.
         *
         * @param controller the controller executing this build
         *                   Needed in order to post back from the RestHandler to the controller
         *                   successes and failures for better tracking of the api calls
         */
        public void build(RestController controller) {
            RestHandler mHandler = create(controller);
            String url;
            if (!mHandler.mHasCustomUrl)
                url = getAbsoluteUrl(mHandler.mMethodCall);
            else url = mHandler.mCustomUrl;
            Log.i("RestHandler", "Executing url=" + url);
            switch (sType) {
                case TYPE_GET:
                    client.get(url, mHandler.mParams, mHandler);
                    break;
                case TYPE_POST:
                    client.post(url, mHandler.mParams, mHandler);
                    break;
                case TYPE_PUT:
                    client.put(url, mHandler.mParams, mHandler);
                    break;
                case TYPE_JSON:
                    Context context = controller.getContext();
                    sEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.post(context, url, mHandler.mStringEntity, "application/json", mHandler);
            }
            reset();
        }

        public void build(RestHandler handler) {
            String url;
            if (!handler.mHasCustomUrl)
                url = getAbsoluteUrl(handler.mMethodCall);
            else url = handler.mCustomUrl;
            Log.i("RestHandler", "Executing url=" + url);
            switch (sType) {
                case TYPE_GET:
                    client.get(url, handler.mParams, handler);
                    break;
                case TYPE_POST:
                    client.post(url, handler.mParams, handler);
                    break;
                case TYPE_PUT:
                    client.put(url, handler.mParams, handler);
                    break;
                case TYPE_JSON:
                    Context context = handler.getContext();
                    sEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                    client.post(context, url, sEntity, "application/json", handler);
            }
        }

        /**
         * Resets the builders member variables to null to avoid errors
         */
        private void reset() {
            sSuccessEvent = null;
            sFailureEvent = null;
            sMethodCall = null;
            sCustom = false;
            sCustomUrl = BASE_URL;
            sParams = null;
            sStartTime = 0;
            sType = 0;
        }
    }
}