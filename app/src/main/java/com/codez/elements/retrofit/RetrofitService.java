package com.codez.elements.retrofit;


import android.util.Log;

import com.codez.elements.temp.StaffModel;
import com.squareup.okhttp.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import rx.Observable;


/**
 * Created by Claudiu on 2/16/2016.
 */
public class RetrofitService {

    private static final String BASE_URL = "http://dev-smssolutions.azurewebsites.net/v1/api";

    public static ISampleAPI API = instantiateService(ISampleAPI.class);

    public static <T> T instantiateService(Class<T> serviceClass) {
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Log.i("Retrofit", message);
                    }
                })
                .setClient(new OkClient(okHttpClient));
        RestAdapter adapter = builder.build();
        return adapter.create(serviceClass);
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
