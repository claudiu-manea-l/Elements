package com.codez.mainlibrary.utilities.connect_manager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by eptron on 2015.03.11..
 * Singleton class in charge of keeping track of the connectivity to the internet *
 */
public class ConnectManager {

    //instance of the class
    private static ConnectManager mInst;
    private boolean isConnected = false;

    /**
     * Private constructor as depicted by the singleton pattern
     */
    private ConnectManager() {
    }

    //SingleTon pattern
    public static ConnectManager getManager() {
        if (mInst == null) {
            mInst = new ConnectManager();
        }
        return mInst;
    }

    public static boolean hasInternet(Context context) {
        try {
            final ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getActiveNetworkInfo() != null && conMgr.getActiveNetworkInfo().isAvailable() && conMgr.getActiveNetworkInfo().isConnected()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Checks if the phone/tablet are in the middle of establishing
     * internet connection
     *
     * @param context Application/Activity context needed to get
     *                a hold of the ConnectivityManager service
     * @return true if connection is possible, false if not
     */
    public boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        isConnected = true;
                        return true;
                    }

        }
        isConnected = false;
        return false;
    }

    /**
     * Getter for the connection status
     *
     * @return connection status (true/false)
     */
    public boolean getConnected() {
        return isConnected;
    }

    /**
     * Setter for the connection status(used by the ConnectReciever)
     *
     * @param connected
     */
    public void setConnected(boolean connected) {
        isConnected = connected;
    }
}
