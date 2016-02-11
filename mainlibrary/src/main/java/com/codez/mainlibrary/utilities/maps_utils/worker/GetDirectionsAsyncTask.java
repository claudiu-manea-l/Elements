package com.codez.mainlibrary.utilities.maps_utils.worker;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.utilities.maps_utils.model.RouteDirections;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Worker class that pulls the Route information from Google Direction API service
 * And returns an RouteDirections object that contains all the required information
 * for the route between the 2 given addresses
 */
public class GetDirectionsAsyncTask extends AsyncTask<String, Object, ArrayList<LatLng>> {
    //private NavigationActivity activity;
    private FragCallback mCallback;
    private Context mContext;
    private Exception exception;
    private ProgressDialog progressDialog;
    private String[] mWaypoints;
    private String Mode;

    public GetDirectionsAsyncTask(Context context) {
        super();
        mContext = context;
    }

    public GetDirectionsAsyncTask(Context context, String[] waypoints) {
        this(context);
        mWaypoints = waypoints;
    }

    public void setCallback(FragCallback callback) {
        mCallback = callback;
    }

    public void setMode(String mode) {
        Mode = mode;
    }

    public void onPreExecute() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("Calculating directions");
        progressDialog.show();
    }

    @Override
    public void onPostExecute(ArrayList<LatLng> result) {
        try {
            progressDialog.dismiss();
        }catch (IllegalArgumentException e){}
        if (exception == null && mCallback != null) {
            //mCallback.handleGetDirectionsResult(result);
            EventBus.getDefault().post(new DirectionsEvent(result));
        } else {
            processException();
        }
    }

    @Override
    protected ArrayList<LatLng> doInBackground(String... params) {
        String address1 = params[0];
        String address2 = params[1];
        if (Mode == null) Mode = "driving";
        try {
            GMapV2Directions md = new GMapV2Directions();
            RouteDirections route;
            if (mWaypoints != null && mWaypoints.length > 0) {
                // JobsCache.getCache().setRouteWaypoints(mWaypoints);
                route = md.getRouteWithWaypoints(address1, address2, Mode, mWaypoints);
            } else route = md.getRouteDirections(address1, address2, Mode);
            //if(JobsCache.getCache().getRouteDirections()==null)
            //JobsCache.getCache().setRouteDirections(route);
            ArrayList<LatLng> directionPoints = route.getOverviewPolyline();
            return directionPoints;
        } catch (Exception e) {
            e.printStackTrace();
            exception = e;
            return null;
        }
    }

    private void processException() {
        Toast.makeText(mContext, mContext.getString(R.string.common_error_data), Toast.LENGTH_SHORT).show();
    }

    //Callback to the fragment who started this worker with the results
    public interface FragCallback {
        void handleGetDirectionsResult(ArrayList<LatLng> result);
    }

    public static class DirectionsEvent {
        private ArrayList<LatLng> mDirections;

        public DirectionsEvent(ArrayList<LatLng> route){
            mDirections = route;
        }

        public ArrayList<LatLng> getDirections(){
            return mDirections;
        }
    }
}