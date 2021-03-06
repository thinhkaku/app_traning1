package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table LIMIT_RULES.
 */
public class LimitRules {

    private byte[] LimitCode;
    private Integer LType;
    private Integer LMinutes;
    private Integer LPeriodCode;
    private String LFrom;
    private String LTo;
    private Integer LZone;
    private Long Record_Modified;
    private Boolean Record_Upload;
    private int sumTotalTime;
    private String lPeriodLong;

    public LimitRules() {
    }

    public LimitRules(byte[] LimitCode) {
        this.LimitCode = LimitCode;
    }

    public LimitRules(byte[] LimitCode, Integer LType, Integer LMinutes, Integer LPeriodCode, String LFrom, String LTo, Integer LZone, Long Record_Modified, Boolean Record_Upload) {
        this.LimitCode = LimitCode;
        this.LType = LType;
        this.LMinutes = LMinutes;
        this.LPeriodCode = LPeriodCode;
        this.LFrom = LFrom;
        this.LTo = LTo;
        this.LZone = LZone;
        this.Record_Modified = Record_Modified;
        this.Record_Upload = Record_Upload;
    }

    public byte[] getLimitCode() {
        return LimitCode;
    }

    public void setLimitCode(byte[] LimitCode) {
        this.LimitCode = LimitCode;
    }

    public Integer getLType() {
        return LType;
    }

    public void setLType(Integer LType) {
        this.LType = LType;
    }

    public Integer getLMinutes() {
        return LMinutes;
    }

    public void setLMinutes(Integer LMinutes) {
        this.LMinutes = LMinutes;
    }

    public Integer getLPeriodCode() {
        return LPeriodCode;
    }

    public void setLPeriodCode(Integer LPeriodCode) {
        this.LPeriodCode = LPeriodCode;
    }

    public String getLFrom() {
        return LFrom;
    }

    public void setLFrom(String LFrom) {
        this.LFrom = LFrom;
    }

    public String getLTo() {
        return LTo;
    }

    public void setLTo(String LTo) {
        this.LTo = LTo;
    }

    public Integer getLZone() {
        return LZone;
    }

    public void setLZone(Integer LZone) {
        this.LZone = LZone;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

    public Boolean getRecord_Upload() {
        return Record_Upload;
    }

    public void setRecord_Upload(Boolean Record_Upload) {
        this.Record_Upload = Record_Upload;
    }

    public int getSumTotalTime() {
        return sumTotalTime;
    }

    public void setSumTotalTime(int sumTotalTime) {
        this.sumTotalTime = sumTotalTime;
    }

    public String getlPeriodLong() {
        return lPeriodLong;
    }

    public void setlPeriodLong(String lPeriodLong) {
        this.lPeriodLong = lPeriodLong;
    }
}
