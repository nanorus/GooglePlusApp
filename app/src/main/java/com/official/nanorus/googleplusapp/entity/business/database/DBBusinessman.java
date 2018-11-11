package com.official.nanorus.googleplusapp.entity.business.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.official.nanorus.googleplusapp.entity.business.api.Businessman;

@Entity(foreignKeys = {@ForeignKey(entity = DBAddress.class, parentColumns = "id", childColumns = "address"),
        @ForeignKey(entity = DBCompany.class, parentColumns = "id", childColumns = "company")})
public class DBBusinessman {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String name;
    public String username;
    public String email;
    public long address;
    public String phone;
    public String website;
    public long company;

    public DBBusinessman(String name, String username, String email, long address, String phone, String website, long company) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.website = website;
        this.company = company;
    }

    public static DBBusinessman map(Businessman businessman, long addressId, long companyId) {
        return new DBBusinessman(businessman.getName(), businessman.getUsername(), businessman.getEmail(), addressId, businessman.getPhone(),
                businessman.getWebsite(), companyId);
    }
}
