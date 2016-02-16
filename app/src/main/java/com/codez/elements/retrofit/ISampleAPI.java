package com.codez.elements.retrofit;

import com.codez.elements.temp.StaffModel;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by Claudiu on 2/16/2016.
 */
public interface ISampleAPI {

    @GET("/SMSStaff/GetCustomerStaff")
    Observable<RetrofitService.ServerResponse<List<StaffModel>>> getCustomerStaff(@Query("customerID") int id);
}
