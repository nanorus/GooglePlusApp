package com.official.nanorus.googleplusapp.entity.business.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface BusinessmanDAO {
    @Query("SELECT * FROM dbbusinessman")
    Flowable<List<DBBusinessman>> getAll();

    @Query("SELECT * FROM dbbusinessman WHERE id = :id")
    Single<DBBusinessman> getById(long id);

    @Insert
    void insert(DBBusinessman dbBusinessman);

    @Query("DELETE FROM dbbusinessman")
    void clearTable();
}
