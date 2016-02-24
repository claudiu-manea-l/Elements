package com.codez.customviews.dialogs.time_picker;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;


import com.codez.customviews.dialogs.SingleOrientationDialog;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

/**
 * Used for (@TimePickerDialog) but instead of registering for a callback
 * to get the time set, the data will be delivered via EventBus (Event = @TimeSetEvent)
 * Created by eptron on 16/06/2015.
 */
public class MyTimePicker extends SingleOrientationDialog
        implements TimePickerDialog.OnTimeSetListener {

    public static final String TAG = "time_picker";
    public static final String PAGE_ID = "page_id";

    private int mID;

    public static Bundle makeBundle(int page){
        Bundle bundle = new Bundle();
        bundle.putInt(PAGE_ID,page);
        return bundle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        if(getArguments()!=null)
            mID = getArguments().getInt(PAGE_ID);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        TimeSetEvent event = new TimeSetEvent(hourOfDay, minute);
        event.setID(mID);
        EventBus.getDefault().post(event);
    }

    public static class TimeSetEvent{
        private int mRefId;

        private int mHour;
        private int mMinute;

        public TimeSetEvent(int hourOfDay, int minute) {
            mHour = hourOfDay;
            mMinute = minute;
        }

        public void setID(int id){
            mRefId = id;
        }

        public int getID(){
            return mRefId;
        }

        public int getHour() {
            return mHour;
        }

        public int getMinute() {
            return mMinute;
        }

        public String getString() {
            String hours = "", minutes = "";
            if (mHour < 10) hours = "0" + mHour;
            else hours = mHour + "";
            if (mMinute < 10) minutes = "0" + mMinute;
            else minutes = mMinute + "";
            return hours + ":" + minutes;
        }
    }
}