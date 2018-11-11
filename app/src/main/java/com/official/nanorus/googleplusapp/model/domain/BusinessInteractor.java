package com.official.nanorus.googleplusapp.model.domain;

import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.model.repository.business.BusinessRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public class BusinessInteractor {

    private BusinessRepository businessRepository;

    public BusinessInteractor() {
        businessRepository = new BusinessRepository();
    }

    public Observable<List<Businessman>> getRefreshedBusinessmen() {
        return businessRepository.getRefreshedBusinessmen();
    }

    public Observable<List<Businessman>> getBusinessmen() {
        return businessRepository.getBusinessmen();
    }

    public Single<Businessman> getBusinessman(int businessmanId) {
        return businessRepository.getBusinessman(businessmanId);
    }
}
