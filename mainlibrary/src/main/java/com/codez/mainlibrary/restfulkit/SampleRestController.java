package com.codez.mainlibrary.restfulkit;

import android.content.Context;

import com.codez.mainlibrary.utilities.maps_utils.MultipleDirCallEvent;
import com.codez.mainlibrary.utilities.maps_utils.SuccessfulMapEvent;
import com.codez.mainlibrary.utilities.maps_utils.model.MapsConstants;
import com.loopj.android.http.RequestParams;

/**
 * Created by Eptron on 1/18/2016.
 */
public class SampleRestController extends RestController{

    private static SampleRestController sInstance;
    private Context mContext;

    private SampleRestController() {
    }

    public static SampleRestController getController(Context context) {
        if (sInstance == null) {
            sInstance = new SampleRestController();
            sInstance.mContext = context;
            sInstance.sBuilder = new RestHandler.Builder();
            DEBUG = true;
        }
        return sInstance;
    }

    @Override
    protected Context getContext() {
        return mContext;
    }

    public boolean getDirections(RequestParams params, MultipleDirCallEvent event) {
        return doCustomRestCall(MapsConstants.DIR_URL, params,
                RestHandler.TYPE_GET, event, null);
    }

    public boolean getDirections(RequestParams params) {
        return doCustomRestCall(MapsConstants.DIR_URL, params,
                RestHandler.TYPE_GET, new SuccessfulMapEvent(), null);
    }
}
