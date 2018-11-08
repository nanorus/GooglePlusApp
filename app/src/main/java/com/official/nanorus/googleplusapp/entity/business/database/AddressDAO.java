package com.official.nanorus.googleplusapp.entity.business.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;
@Dao
public interface AddressDAO {
    @Query("SELECT * FROM DBAddress")
    Flowable<List<DBAddress>> getAllRx();

    @Query("SELECT * FROM DBAddress WHERE id = :id")
    Single<DBAddress> getByIdRx(long id);

    @Query("SELECT * FROM DBAddress")
    List<DBAddress> getAll();

    @Query("SELECT * FROM DBAddress WHERE id = :id")
    DBAddress getById(long id);

    @Insert
    long insert(DBAddress DBAddress);
}
