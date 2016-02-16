package com.codez.elements.controllers;

import android.content.ContentValues;
import android.content.Context;

import com.codez.elements.ElementsApp;
import com.codez.elements.retrofit.RetrofitService;
import com.codez.elements.temp.DataProvider;
import com.codez.elements.temp.StaffModel;
import com.path.android.jobqueue.JobManager;

import java.util.List;

import rx.functions.Action1;

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
