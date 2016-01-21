package com.codez.mainlibrary;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Toast;

import com.codez.mainlibrary.restfulkit.model.FailedRequest;
import com.codez.mainlibrary.utilities.connect_manager.ConnectedToWifi;

import de.greenrobot.event.EventBus;

/**
 * Created by Eptron on 1/21/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public static boolean DEBUG = BuildConfig.DEBUG;

    @Override
    protected void onResume() {
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().registerSticky(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(ConnectedToWifi event) {
        Toast.makeText(this, getString(R.string.wifi_connection), Toast.LENGTH_SHORT).show();
        onConnectedToWifi();
    }

    public void onEventMainThread(FailedRequest event) {
        String error;
        if (DEBUG)
            error = buildErrorString(event);
        else {
            error = getString(R.string.error_server);
            error = error + "\n" + event.getError();
        }
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();

    }

    public void onConnectedToWifi() {
    }

    private String buildErrorString(FailedRequest event) {
        String temp = "Request call failed = " + event.getMethodCall() + "\n";
        temp = temp + "ResponseCode = " + event.getRestCall().getResponseCode();
        if (TextUtils.isEmpty(event.getResponseString()))
            temp = temp + "Reason =" + event.getError();
        else temp = temp + "Reason = " + event.getError();
        return temp;
    }

    protected void notifyConnectionStatus() {
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            notifyConnectionStatus();
        }
    };
}
