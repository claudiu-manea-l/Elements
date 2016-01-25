package com.codez.mainlibrary.utilities.connect_manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import de.greenrobot.event.EventBus;

/**
 * Created by eptron on 2015.03.11..
 * BroadcastReceiver listening for global connectivity changes
 */
public class ConnectReceiver extends BroadcastReceiver {
    public static final String ACTION_INFORM = "com.codez.ACTION_INFORM";

    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_INFORM)) {
            ConnectivityManager connMg = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connMg.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //NetworkInfo mobile = connMg.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isConnected()) {
                ConnectManager.getManager().setConnected(true);
                EventBus.getDefault().post(new ConnectedToWifi());
            } else {
                ConnectManager.getManager().setConnected(false);
            }
            Intent mIntent = new Intent();
            mIntent.setAction(ACTION_INFORM);
            context.sendBroadcast(mIntent);
        }
    }
}
