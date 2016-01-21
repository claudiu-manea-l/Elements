package com.codez.mainlibrary.restfulkit.model;

import org.json.JSONException;

/**
 * Created by eptron on 13/05/2015.
 */
public class FailedRequest extends MainEvent {
    protected String mError;

    public FailedRequest() {
        mError = "";
    }

    public String getMethodCall() {
        return getRestCall().getMethodCall();
    }

    @Override
    public void parseObject() {
        try {
            mError = mJSONObject.getString("error");
        } catch (JSONException e) {
        }
    }

    public String getError() {
        return mError;
    }
}
