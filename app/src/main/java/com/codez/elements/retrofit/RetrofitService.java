package com.codez.elements.retrofit;


import android.util.Log;

import com.codez.elements.temp.StaffModel;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;


/**
 * Created by Claudiu on 2/16/2016.
 */
public class RetrofitService {

    private static final String BASE_URL = "http://dev-smssolutions.azurewebsites.net/v1/api/";

    public static ISampleAPI API = instantiateService(ISampleAPI.class);

        public static <T> T instantiateService(Class<T> serviceClass) {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit builder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return builder.create(serviceClass);
    }

    public static Observable<ServerResponse<List<StaffModel>>> getCustomerStaff(){
        return API.getCustomerStaff(157);
    }

    public static class ServerResponse<T>{
        private String status;
        private String message;
        private T Content;

        public T getContent(){
            return Content;
        }
    }
}
