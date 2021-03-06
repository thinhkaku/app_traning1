package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table WEATHER.
 */
public class Weather {

    private byte[] WxCode;
    private String WxRoute;
    private Long Record_Modified;
    private Boolean Record_Upload;

    public Weather() {
    }

    public Weather(byte[] WxCode) {
        this.WxCode = WxCode;
    }

    public Weather(byte[] WxCode, String WxRoute, Long Record_Modified, Boolean Record_Upload) {
        this.WxCode = WxCode;
        this.WxRoute = WxRoute;
        this.Record_Modified = Record_Modified;
        this.Record_Upload = Record_Upload;
    }

    public byte[] getWxCode() {
        return WxCode;
    }

    public void setWxCode(byte[] WxCode) {
        this.WxCode = WxCode;
    }

    public String getWxRoute() {
        return WxRoute;
    }

    public void setWxRoute(String WxRoute) {
        this.WxRoute = WxRoute;
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

}
