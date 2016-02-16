package com.codez.elements.controllers;

import android.content.ContentValues;

import com.codez.elements.retrofit.RetrofitService;
import com.codez.elements.temp.DataProvider;
import com.codez.elements.temp.StaffModel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class GetStaffJob extends Job {

    public GetStaffJob() {
        super(new Params(1).requireNetwork().persist());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RetrofitService.getCustomerStaff().subscribe(new Action1<RetrofitService.ServerResponse<List<StaffModel>>>() {
            @Override
            public void call(RetrofitService.ServerResponse<List<StaffModel>> listServerResponse) {
                if (getApplicationContext() != null) {
                    ContentValues[] cvs = new ContentValues[listServerResponse.getContent().size()];
                    for (int i = 0; i < listServerResponse.getContent().size(); i++)
                        cvs[i] = listServerResponse.getContent().get(i).toContentValues();
                    getApplicationContext().getContentResolver().bulkInsert(DataProvider.STAFF_CONTENT_URI, cvs);
                }
            }
        });
    }

    @Override
    protected void onCancel() {

    }
}
