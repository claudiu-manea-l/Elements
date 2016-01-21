package com.codez.mainlibrary.utilities.maps_utils.ui;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codez.mainlibrary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by eptron on 08/04/2015.
 */
public abstract class GMapsFragment extends Fragment implements OnMapReadyCallback,
        LocationSource.OnLocationChangedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public static String TAG = "gmaps_fragment";
    protected GoogleMap mGoogleMap;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private boolean mShowBeforeMapReady = false;
    private View mView;

    public static LatLng getLocationFromAddress(Context context, String address) {
        Geocoder coder = new Geocoder(context);
        List<Address> listAddress = null;
        try {
            listAddress = coder.getFromLocationName(address, 1);
        } catch (IOException ex) {
            ex.printStackTrace();
            Toast.makeText(context, "IOException ....", Toast.LENGTH_SHORT).show();
        }
        if (listAddress != null && listAddress.get(0) != null) {
            LatLng latLng = new LatLng(
                    listAddress.get(0).getLatitude(),
                    listAddress.get(0).getLongitude());
            return latLng;
        }
        return null;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    protected abstract View inflateFragmentView(LayoutInflater inflater, ViewGroup container);

    protected abstract void initFragment(View view);

    protected void setShowBeforeMapReady(boolean showBeforeMapReady) {
        mShowBeforeMapReady = showBeforeMapReady;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflateFragmentView(inflater, container);
        if (mView == null) {
            mView = inflater.inflate(R.layout.f_main_map, container, false);
        }
        ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        if (mShowBeforeMapReady) initFragment(mView);
        return mView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if (!mShowBeforeMapReady) initFragment(mView);
    }

    protected void moveCameraToLocation(Location location, int zoom) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        LatLng coordinate = new LatLng(lat, lng);
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate, zoom));
    }

    protected Marker addMarkerAtLocation(Location location, String title) {
        LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
        if (title == null) title = "";
        Marker locationMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(coordinate)
                .title(title));
        return locationMarker;
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
