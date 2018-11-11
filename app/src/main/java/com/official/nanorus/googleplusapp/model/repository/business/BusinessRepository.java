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
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;

public class BusinessRepository {
    private String TAG = this.getClass().getName();
    private BusinessRetroClient retroClient;
    private AppDatabase appDatabase;

    public BusinessRepository() {
        retroClient = BusinessRetroClient.getInstance();
        appDatabase = AppDatabase.getInstance();
    }

    public Observable<List<Businessman>> getRefreshedBusinessmen() {
        return retroClient.getBusinessmenService().getBusinessmen()
                .flatMap((Function<List<Businessman>, ObservableSource<List<Businessman>>>) businessmen -> {
                    Log.d(TAG, "Clear db");
                    appDatabase.businessmanDao().clearTable();

                    Log.d(TAG, "Inserting into db");
                    for (Businessman businessman : businessmen) {
                        long addressId = appDatabase.addressDao().insert(DBAddress.map(businessman.getAddress()));
                        long companyId = appDatabase.companyDao().insert(DBCompany.map(businessman.getCompany()));
                        appDatabase.businessmanDao().insert(DBBusinessman.map(businessman, addressId, companyId));
                    }
                    return getBusinessmen();
                });

    }

    public Observable<List<Businessman>> getBusinessmen() {
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

    public Single<Businessman> getBusinessman(long id) {
        return appDatabase.businessmanDao().getById(id).map(
                dbBusinessman -> {
                    Address address = Address.map(appDatabase.addressDao().getById(dbBusinessman.id));
                    Company company = Company.map(appDatabase.companyDao().getById(dbBusinessman.id));
                    return Businessman.map(dbBusinessman, address, company);
                }


        );
    }
}
