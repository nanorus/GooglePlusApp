package com.official.nanorus.googleplusapp.model.data.api;

import com.official.nanorus.googleplusapp.model.data.api.services.BusinessService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BusinessRetroClient {
    private static BusinessRetroClient instance;
    private static Retrofit retrofit;

    public static BusinessRetroClient getInstance() {
        if (instance == null)
            instance = new BusinessRetroClient();
        return instance;
    }

    private Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public BusinessService getBusinessmenService() {
        return getRetrofit().create(BusinessService.class);
    }
}
