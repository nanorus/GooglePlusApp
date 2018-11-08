package com.official.nanorus.googleplusapp.entity.business.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
@Dao
public interface CompanyDAO {
    @Query("SELECT * FROM DBCompany")
    Flowable<List<DBCompany>> getAllRx();

    @Query("SELECT * FROM DBCompany WHERE id = :id")
    Single<DBCompany> getByIdRx(long id);


    @Query("SELECT * FROM DBCompany")
    List<DBCompany> getAll();

    @Query("SELECT * FROM DBCompany WHERE id = :id")
    DBCompany getById(long id);

    @Insert
    long insert(DBCompany DBCompany);
}
