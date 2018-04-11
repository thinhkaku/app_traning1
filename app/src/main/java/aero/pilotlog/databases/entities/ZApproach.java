package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ZAPPROACH.
 */
public class ZApproach {

    private Integer APCode;
    private Integer APCat;
    private String APShort;
    private String APLong;
    private Integer MostUsed;
    private Long Record_Modified;

    public ZApproach() {
    }

    public ZApproach(Integer APCode) {
        this.APCode = APCode;
    }

    public ZApproach(Integer APCode, Integer APCat, String APShort, String APLong, Integer MostUsed, Long Record_Modified) {
        this.APCode = APCode;
        this.APCat = APCat;
        this.APShort = APShort;
        this.APLong = APLong;
        this.MostUsed = MostUsed;
        this.Record_Modified = Record_Modified;
    }

    public Integer getAPCode() {
        return APCode;
    }

    public void setAPCode(Integer APCode) {
        this.APCode = APCode;
    }

    public Integer getAPCat() {
        return APCat;
    }

    public void setAPCat(Integer APCat) {
        this.APCat = APCat;
    }

    public String getAPShort() {
        return APShort;
    }

    public void setAPShort(String APShort) {
        this.APShort = APShort;
    }

    public String getAPLong() {
        return APLong;
    }

    public void setAPLong(String APLong) {
        this.APLong = APLong;
    }

    public Integer getMostUsed() {
        return MostUsed;
    }

    public void setMostUsed(Integer MostUsed) {
        this.MostUsed = MostUsed;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

}