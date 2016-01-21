package com.codez.mainlibrary.utilities.maps_utils.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codez.mainlibrary.R;

/**
 * Created by eptron on 10/04/2015.
 */
public class EmptyMapFragment extends Fragment {
    public final static String TAG = "empty_maps_fragment";

    public final static int NO_INTERNET = 1;
    public final static int NO_GPS = 2;
    public final static int OTHER = 3;

    private final static String ERROR_ARGS = "error_message";

    public static EmptyMapFragment newInstance(int errorMessage) {
        EmptyMapFragment frag = new EmptyMapFragment();
        Bundle args = new Bundle();
        args.putInt(ERROR_ARGS, errorMessage);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        view = inflater.inflate(R.layout.f_maps_empty, null);
        TextView errorTV = (TextView) view.findViewById(R.id.error_message);

        int error = getArguments().getInt(ERROR_ARGS);
        String gps_error = getResources().getString(R.string.no_gps_error);
        String internet_error = getResources().getString(R.string.no_internet_error);
        String unknown_error = getResources().getString(R.string.unknown_error);
        String other_error = "";
        switch (error) {
            case 0:
                errorTV.setText("Nothing went wrong,this message should not even be displayer");
            case NO_INTERNET:
                errorTV.setText(internet_error);
            case NO_GPS:
                errorTV.setText(gps_error);
            case OTHER:
                errorTV.setText(other_error);
            default:
                errorTV.setText(unknown_error);
        }
        return view;
    }
}
