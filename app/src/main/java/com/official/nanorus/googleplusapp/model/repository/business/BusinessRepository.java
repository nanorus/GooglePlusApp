package com.official.nanorus.googleplusapp.model.repository.business;

import android.util.Log;

import com.official.nanorus.googleplusapp.entity.business.api.Address;
import com.official.nanorus.googleplusapp.entity.business.api.Businessman;
import com.official.nanorus.googleplusapp.entity.business.api.Company;
import com.official.nanorus.googleplusapp.entity.business.database.DBAddress;
import com.official.nanorus.googleplusapp.entity.business.database.DBBusinessman;
import com.official.nanorus.googleplusapp.entity.business.database.DBCompany;
import com.official.nanorus.googleplusapp.model.data.api.BusinessRetroClient;
import com.official.nanorus.googleplusapp.model.data.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class BusinessRepository {
    private String TAG = this.getClass().getName();
    private BusinessRetroClient retroClient;

    public BusinessRepository() {
        retroClient = BusinessRetroClient.getInstance();
    }

    public Observable<List<Businessman>> getRefreshedBusinessmen() {
        return retroClient.getBusinessmenService().getBusinessmen()
                .map(businessmen -> {
                    AppDatabase appDatabase = AppDatabase.getInstance();
                    Log.d(TAG, "Clear db");
                    appDatabase.businessmanDao().clearTable();

                    Log.d(TAG, "Inserting into db");
                    for (Businessman businessman : businessmen) {
                        long adressId = appDatabase.addressDao().insert(DBAddress.map(businessman.getAddress()));
                        long companyId = appDatabase.companyDao().insert(DBCompany.map(businessman.getCompany()));
                        appDatabase.businessmanDao().insert(DBBusinessman.map(businessman, adressId, companyId));
                    }
                    return businessmen;
                });

    }

    public Observable<List<Businessman>> getBusinessmen() {
        AppDatabase appDatabase = AppDatabase.getInstance();
        Observable<List<DBBusinessman>> dbBusinessmanObservable = appDatabase.businessmanDao().getAll().toObservable();
        Observable<List<Businessman>> businessmanObservable = dbBusinessmanObservable
                .map(dbBusinessmen -> {
                    List<Businessman> businessmen = new ArrayList<>();
                    for (DBBusinessman dbBusinessman : dbBusinessmen) {
                        Address address = Address.map(appDatabase.addressDao().getById(dbBusinessman.id));
                        Company company = Company.map(appDatabase.companyDao().getById(dbBusinessman.id));
                        Businessman businessman = Businessman.map(dbBusinessman, address, company);
                        businessmen.add(businessman);
                    }
                    return businessmen;
                });


        return businessmanObservable;

    }
}
