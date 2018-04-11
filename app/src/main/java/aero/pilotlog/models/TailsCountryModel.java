package aero.pilotlog.models;

import aero.pilotlog.databases.entities.ZCountry;

/**
 * Created by tuan.na on 7/21/2015.
 */
public class TailsCountryModel {
    String mRegAC;
    ZCountry mCountry;

    public String getRegAC() {
        return mRegAC;
    }

    public void setRegAC(String mRegAC) {
        this.mRegAC = mRegAC;
    }

    public ZCountry getCountry() {
        return mCountry;
    }

    public void setCountry(ZCountry mCountry) {
        this.mCountry = mCountry;
    }

    public TailsCountryModel(String mRegAC, ZCountry mCountry) {
        this.mRegAC = mRegAC;
        this.mCountry = mCountry;
    }
}
