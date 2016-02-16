package com.codez.elements.controllers;

import android.content.ContentValues;
import android.content.Context;

import com.codez.elements.retrofit.RetrofitService;
import com.codez.elements.temp.DataProvider;
import com.codez.elements.temp.StaffModel;

import java.util.List;

import rx.functions.Action1;

/**
 * Created by Claudiu on 2/16/2016.
 */
public class DataController {

    private static DataController mInst;

    private DataController(){}

    public static DataController getController(){
        if(mInst == null) mInst = new DataController();
        return mInst;
    }

    public void getCustomerStaff(final Context context){
        RetrofitService.getCustomerStaff().subscribe(new Action1<RetrofitService.ServerResponse<List<StaffModel>>>() {
            @Override
            public void call(RetrofitService.ServerResponse<List<StaffModel>> listServerResponse) {
                if(context!=null) {
                    ContentValues[] cvs = new ContentValues[listServerResponse.getContent().size()];
                    for(int i =0;i<listServerResponse.getContent().size();i++)
                        cvs[i] = listServerResponse.getContent().get(i).toContentValues();
                    context.getContentResolver().bulkInsert(DataProvider.STAFF_CONTENT_URI,cvs);

                }
            }
        });
    }
}
