package com.codez.elements.controllers;

import com.codez.elements.ElementsApp;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class DataController {

    private static DataController mInst;

    private DataController() {
    }

    public static DataController getController() {
        if (mInst == null) mInst = new DataController();
        return mInst;
    }

    public void getCustomerStaff() {
        ElementsApp.getInstance().getJobManager().addJob(new GetStaffJob());
    }
}
