
package com.official.nanorus.googleplusapp.entity.business.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.official.nanorus.googleplusapp.entity.business.database.DBCompany;

public class Company {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("catchPhrase")
    @Expose
    private String catchPhrase;
    @SerializedName("bs")
    @Expose
    private String bs;

    public Company(String name, String catchPhrase, String bs) {
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatchPhrase() {
        return catchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    public String getBs() {
        return bs;
    }

    public void setBs(String bs) {
        this.bs = bs;
    }

    public static Company map(DBCompany dbCompany) {
        return new Company(dbCompany.name, dbCompany.catchPhrase, dbCompany.bs);
    }
}
