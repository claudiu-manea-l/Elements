package com.codez.mainlibrary.restfulkit;

import android.content.Context;

import com.codez.mainlibrary.restfulkit.loopj.LoopjHandler;
import com.codez.mainlibrary.restfulkit.loopj.LoopjController;
import com.codez.mainlibrary.utilities.maps_utils.MultipleDirCallEvent;
import com.codez.mainlibrary.utilities.maps_utils.SuccessfulMapEvent;
import com.codez.mainlibrary.utilities.maps_utils.model.MapsConstants;
import com.loopj.android.http.RequestParams;

/**
 * Created by Eptron on 1/18/2016.
 */
public class SampleLoopjController extends LoopjController {

    private static SampleLoopjController sInstance;
    private Context mContext;

    private SampleLoopjController() {
    }

    public static SampleLoopjController getController(Context context) {
        if (sInstance == null) {
            sInstance = new SampleLoopjController();
            sInstance.mContext = context;
            sInstance.sBuilder = new LoopjHandler.Builder();
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
                LoopjHandler.TYPE_GET, event, null);
    }

    public boolean getDirections(RequestParams params) {
        return doCustomRestCall(MapsConstants.DIR_URL, params,
                LoopjHandler.TYPE_GET, new SuccessfulMapEvent(), null);
    }
}
