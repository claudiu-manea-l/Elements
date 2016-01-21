package com.codez.mainlibrary.utilities.maps_utils.ui;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codez.mainlibrary.R;
import com.codez.mainlibrary.utilities.OtherUtils;


/**
 * Created by eptron on 10/04/2015.
 */
public class MapsHolderFragment extends Fragment {

    private int mErrorStatus = 0;
    private GMapsFragment mMapsFragment;

    public static MapsHolderFragment newInstance() {
        MapsHolderFragment frag = new MapsHolderFragment();
        frag.setRetainInstance(true);
        return frag;
    }

    public void setMapsFragment(GMapsFragment mapsFragment) {
        mMapsFragment = mapsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isGpsEnabled = checkIfGpsEnabled();
        boolean isInternetAvailable = OtherUtils.hasInternet(getActivity());
        mErrorStatus = !isGpsEnabled ? 1 : 0;
        mErrorStatus = !isInternetAvailable ? 2 : 0;
        mErrorStatus = ((!isGpsEnabled) && (!isInternetAvailable)) ? 3 : 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_holder, null);
        Fragment frag;
        String tag;
        if (mErrorStatus == 0) {
            frag = mMapsFragment;
            tag = GMapsFragment.TAG;
        } else {
            frag = EmptyMapFragment.newInstance(mErrorStatus);
            tag = EmptyMapFragment.TAG;
        }
        getChildFragmentManager().beginTransaction()
                .add(R.id.holder_content, frag, tag)
                .commit();

        return view;
    }

    private boolean checkIfGpsEnabled() {
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabledGPS = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabledGPS) {
            Toast.makeText(getActivity(), "GPS signal not found", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            return false;
        }
        return true;
    }
}
