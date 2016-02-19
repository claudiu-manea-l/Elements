package com.codez.elements.controllers;

import android.database.ContentObservable;
import android.widget.Toast;
import com.codez.elements.retrofit.RetrofitService;
import com.codez.elements.temp.DataProvider;
import com.codez.elements.temp.StaffModel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;
import com.path.android.jobqueue.RetryConstraint;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        RetrofitService.getCustomerStaff()
                .subscribeOn(Schedulers.immediate())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<RetrofitService.ServerResponse<List<StaffModel>>, List<StaffModel>>() {
                    @Override
                    public List<StaffModel> call(RetrofitService.ServerResponse<List<StaffModel>> listServerResponse) {
                        return listServerResponse.getContent();
                    }
                })
                .flatMap(new Func1<List<StaffModel>, Observable<StaffModel>>() {
                    @Override
                    public Observable<StaffModel> call(List<StaffModel> staffModels) {
                        return Observable.from(staffModels);
                    }
                })
                .filter(new Func1<StaffModel, Boolean>() {
                    @Override
                    public Boolean call(StaffModel staffModel) {
                        return !staffModel.getAvailability().isEmpty();
                    }
                })
                .subscribe(new Observer<StaffModel>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),"Done",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(StaffModel staffModel) {
                        if (getApplicationContext() != null) {
                            getApplicationContext().getContentResolver().insert(DataProvider.STAFF_CONTENT_URI,
                                    staffModel.toContentValues());
                        }
                    }
                });


    }

    @Override
    protected void onCancel() {

    }

     @Override
    protected RetryConstraint shouldReRunOnThrowable(Throwable throwable, int runCount, int maxRunCount) {
        throwable.printStackTrace();
        return super.shouldReRunOnThrowable(throwable, runCount, maxRunCount);
    }
}
