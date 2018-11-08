package com.official.nanorus.googleplusapp.model.data.api.services;

import com.official.nanorus.googleplusapp.entity.business.api.Businessman;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface BusinessService {
    @GET("users")
    Observable<List<Businessman>> getBusinessmen();
}
