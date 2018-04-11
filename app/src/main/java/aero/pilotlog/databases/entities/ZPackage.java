package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ZPACKAGE.
 */
public class ZPackage {

    private Integer PackageCode;
    private String PackID;
    private String Table;
    private String Details;
    private Long Record_Modified;

    public ZPackage() {
    }

    public ZPackage(Integer PackageCode) {
        this.PackageCode = PackageCode;
    }

    public ZPackage(Integer PackageCode, String PackID, String Table, String Details, Long Record_Modified) {
        this.PackageCode = PackageCode;
        this.PackID = PackID;
        this.Table = Table;
        this.Details = Details;
        this.Record_Modified = Record_Modified;
    }

    public Integer getPackageCode() {
        return PackageCode;
    }

    public void setPackageCode(Integer PackageCode) {
        this.PackageCode = PackageCode;
    }

    public String getPackID() {
        return PackID;
    }

    public void setPackID(String PackID) {
        this.PackID = PackID;
    }

    public String getTable() {
        return Table;
    }

    public void setTable(String Table) {
        this.Table = Table;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String Details) {
        this.Details = Details;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

}
