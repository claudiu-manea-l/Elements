package com.codez.elements.retrofit;

import com.codez.elements.temp.StaffModel;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Claudiu on 2/16/2016.
 */
public interface ISampleAPI {

    @GET("SMSStaff/GetCustomerStaff")
    Observable<RetrofitService.ServerResponse<List<StaffModel>>> getCustomerStaff(@Query("customerID") int id);
}
