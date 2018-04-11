package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ZOPERATION.
 */
public class ZOperation {

    private Integer OpsCode;
    private String OpsShort;
    private String OpsLong;
    private Integer MostUsed;
    private Long Record_Modified;

    public ZOperation() {
    }

    public ZOperation(Integer OpsCode) {
        this.OpsCode = OpsCode;
    }

    public ZOperation(Integer OpsCode, String OpsShort, String OpsLong, Integer MostUsed, Long Record_Modified) {
        this.OpsCode = OpsCode;
        this.OpsShort = OpsShort;
        this.OpsLong = OpsLong;
        this.MostUsed = MostUsed;
        this.Record_Modified = Record_Modified;
    }

    public Integer getOpsCode() {
        return OpsCode;
    }

    public void setOpsCode(Integer OpsCode) {
        this.OpsCode = OpsCode;
    }

    public String getOpsShort() {
        return OpsShort;
    }

    public void setOpsShort(String OpsShort) {
        this.OpsShort = OpsShort;
    }

    public String getOpsLong() {
        return OpsLong;
    }

    public void setOpsLong(String OpsLong) {
        this.OpsLong = OpsLong;
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