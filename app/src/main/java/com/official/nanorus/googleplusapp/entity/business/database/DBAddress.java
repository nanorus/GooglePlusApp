package com.official.nanorus.googleplusapp.entity.business.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.official.nanorus.googleplusapp.entity.business.api.Address;

import io.reactivex.Observable;

@Entity
public class DBAddress {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String street;
    public String suite;
    public String city;
    public String zipcode;
    public String lat;
    public String lng;


    public DBAddress(String street, String suite, String city, String zipcode, String lat, String lng) {
        this.street = street;
        this.suite = suite;
        this.city = city;
        this.zipcode = zipcode;
        this.lat = lat;
        this.lng = lng;
    }

    public static DBAddress map(Address address) {
        return new DBAddress(address.getStreet(), address.getSuite(), address.getCity(),
                address.getZipcode(), address.getGeo().getLat(), address.getGeo().getLng());
    }

}
