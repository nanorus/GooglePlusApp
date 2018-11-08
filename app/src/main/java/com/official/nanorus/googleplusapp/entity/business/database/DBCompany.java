package com.official.nanorus.googleplusapp.entity.business.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.official.nanorus.googleplusapp.entity.business.api.Company;

@Entity
public class DBCompany {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String catchPhrase;
    public String bs;

    public DBCompany(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public static DBCompany map(Company company) {
        return new DBCompany(company.getName(), company.getCatchPhrase(), company.getBs());
    }


}
