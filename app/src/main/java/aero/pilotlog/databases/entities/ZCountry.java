package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 

import android.text.TextUtils;

import aero.pilotlog.common.MCCPilotLogConst;
import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Entity mapped to table ZCOUNTRY.
 */
public class ZCountry implements IndexableEntity {

    private Integer CountryCode;
    private String ISO_3166;
    private String ISO_Long;
    private Long ISO_Numeric;
    private String CountryName;
    private String Capital;
    private String Continent;
    private String Neighbours;
    private String tld;
    private Integer CurrCode;
    private String PrefixPhone;
    private String PrefixICAO;
    private String RegAC;
    private Long Record_Modified;

    public ZCountry() {
    }

    public ZCountry(Integer CountryCode) {
        this.CountryCode = CountryCode;
    }

    public ZCountry(Integer CountryCode, String ISO_3166, String ISO_Long, Long ISO_Numeric, String CountryName, String Capital, String Continent, String Neighbours, String tld, Integer CurrCode, String PrefixPhone, String PrefixICAO, String RegAC, Long Record_Modified) {
        this.CountryCode = CountryCode;
        this.ISO_3166 = ISO_3166;
        this.ISO_Long = ISO_Long;
        this.ISO_Numeric = ISO_Numeric;
        this.CountryName = CountryName;
        this.Capital = Capital;
        this.Continent = Continent;
        this.Neighbours = Neighbours;
        this.tld = tld;
        this.CurrCode = CurrCode;
        this.PrefixPhone = PrefixPhone;
        this.PrefixICAO = PrefixICAO;
        this.RegAC = RegAC;
        this.Record_Modified = Record_Modified;
    }

    public Integer getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(Integer CountryCode) {
        this.CountryCode = CountryCode;
    }

    public String getISO_3166() {
        return ISO_3166;
    }

    public void setISO_3166(String ISO_3166) {
        this.ISO_3166 = ISO_3166;
    }

    public String getISO_Long() {
        return ISO_Long;
    }

    public void setISO_Long(String ISO_Long) {
        this.ISO_Long = ISO_Long;
    }

    public Long getISO_Numeric() {
        return ISO_Numeric;
    }

    public void setISO_Numeric(Long ISO_Numeric) {
        this.ISO_Numeric = ISO_Numeric;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String CountryName) {
        this.CountryName = CountryName;
    }

    public String getCapital() {
        return Capital;
    }

    public void setCapital(String Capital) {
        this.Capital = Capital;
    }

    public String getContinent() {
        return Continent;
    }

    public void setContinent(String Continent) {
        this.Continent = Continent;
    }

    public String getNeighbours() {
        return Neighbours;
    }

    public void setNeighbours(String Neighbours) {
        this.Neighbours = Neighbours;
    }

    public String getTld() {
        return tld;
    }

    public void setTld(String tld) {
        this.tld = tld;
    }

    public Integer getCurrCode() {
        return CurrCode;
    }

    public void setCurrCode(Integer CurrCode) {
        this.CurrCode = CurrCode;
    }

    public String getPrefixPhone() {
        return PrefixPhone;
    }

    public void setPrefixPhone(String PrefixPhone) {
        this.PrefixPhone = PrefixPhone;
    }

    public String getPrefixICAO() {
        return PrefixICAO;
    }

    public void setPrefixICAO(String PrefixICAO) {
        this.PrefixICAO = PrefixICAO;
    }

    public String getRegAC() {
        return RegAC;
    }

    public void setRegAC(String RegAC) {
        this.RegAC = RegAC;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

    @Override
    public String getFieldIndexBy() {
       return RegAC.replace(MCCPilotLogConst.SPLIT_KEY_APPEND,"");
    }

    @Override
    public void setFieldIndexBy(String indexField) {

    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }

    private String getRegAC(String oldRegAC) {
        String newRegAC = "";
        if (!TextUtils.isEmpty(oldRegAC)) {
            String[] arrayRegAC = oldRegAC.split(MCCPilotLogConst.SPLIT_KEY);
            if (arrayRegAC != null) {
                for (int i = 0; i < arrayRegAC.length; i++) {
                    newRegAC += arrayRegAC[i]+ MCCPilotLogConst.SPACE;
                }
            }
        }
        return newRegAC;
    }
}