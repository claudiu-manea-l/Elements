package com.codez.mainlibrary.utilities.maps_utils;

/**
 * Created by Eptron on 10/27/2015.
 */
public class MultipleDirCallEvent extends SuccessfulMapEvent {


    private boolean isLast = false;
    private String mStartAddress;
    private String mEndAddress;
    private int mOrder;

    public MultipleDirCallEvent(int order) {
        mOrder = order;
    }

    public boolean isLast() {
        return isLast;
    }

    public void setLast() {
        isLast = true;
    }

    public String getStartAddress() {
        return mStartAddress;
    }

    public String getEndAddress() {
        return mEndAddress;
    }

    public void setAddresses(String startAddress, String endAddress) {
        mStartAddress = startAddress;
        mEndAddress = endAddress;
    }

    public int getOrder() {
        return mOrder;
    }
}
