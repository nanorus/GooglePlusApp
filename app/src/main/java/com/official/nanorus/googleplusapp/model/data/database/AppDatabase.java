package com.official.nanorus.googleplusapp.model.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;

import com.official.nanorus.googleplusapp.app.App;
import com.official.nanorus.googleplusapp.entity.business.database.DBAddress;
import com.official.nanorus.googleplusapp.entity.business.database.AddressDAO;
import com.official.nanorus.googleplusapp.entity.business.database.DBBusinessman;
import com.official.nanorus.googleplusapp.entity.business.database.BusinessmanDAO;
import com.official.nanorus.googleplusapp.entity.business.database.DBCompany;
import com.official.nanorus.googleplusapp.entity.business.database.CompanyDAO;

@Database(entities = {DBBusinessman.class, DBAddress.class, DBCompany.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public static AppDatabase getInstance() {
        if (instance == null)
            instance = Room.databaseBuilder(App.getApp().getApplicationContext(), AppDatabase.class, "database")
                    .build();
        return instance;
    }

    public abstract BusinessmanDAO businessmanDao();

    public abstract AddressDAO addressDao();

    public abstract CompanyDAO companyDao();
}
