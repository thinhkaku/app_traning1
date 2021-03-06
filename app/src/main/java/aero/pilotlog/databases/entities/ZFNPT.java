package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ZFNPT.
 */
public class ZFNPT {

    private Integer FnptCode;
    private String FnptShort;
    private String FnptLong;
    private Boolean Drone;
    private Long Record_Modified;

    public ZFNPT() {
    }

    public ZFNPT(Integer FnptCode) {
        this.FnptCode = FnptCode;
    }

    public ZFNPT(Integer FnptCode, String FnptShort, String FnptLong, Boolean Drone, Long Record_Modified) {
        this.FnptCode = FnptCode;
        this.FnptShort = FnptShort;
        this.FnptLong = FnptLong;
        this.Drone = Drone;
        this.Record_Modified = Record_Modified;
    }

    public Integer getFnptCode() {
        return FnptCode;
    }

    public void setFnptCode(Integer FnptCode) {
        this.FnptCode = FnptCode;
    }

    public String getFnptShort() {
        return FnptShort;
    }

    public void setFnptShort(String FnptShort) {
        this.FnptShort = FnptShort;
    }

    public String getFnptLong() {
        return FnptLong;
    }

    public void setFnptLong(String FnptLong) {
        this.FnptLong = FnptLong;
    }

    public Boolean getDrone() {
        return Drone;
    }

    public void setDrone(Boolean Drone) {
        this.Drone = Drone;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

}
