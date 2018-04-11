package aero.pilotlog.databases.entities;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ZAIRCRAFT_MAKE.
 */
public class ZAircraftMake {

    private Integer ACCode;
    private String ACRef;
    private String Make;
    private String Model;
    private String SubModel;
    private String Rating;
    private Integer Category;
    private Integer Class;
    private Boolean Sea;
    private Integer Power;
    private Boolean Kg5700;
    private Boolean TMG;
    private Boolean Tailwheel;
    private Boolean Complex;
    private Boolean HighPerf;
    private Boolean Aerobatic;
    private Long Record_Modified;

    public ZAircraftMake() {
    }

    public ZAircraftMake(Integer ACCode) {
        this.ACCode = ACCode;
    }

    public ZAircraftMake(Integer ACCode, String ACRef, String Make, String Model, String SubModel, String Rating, Integer Category, Integer Class, Boolean Sea, Integer Power, Boolean Kg5700, Boolean TMG, Boolean Tailwheel, Boolean Complex, Boolean HighPerf, Boolean Aerobatic, Long Record_Modified) {
        this.ACCode = ACCode;
        this.ACRef = ACRef;
        this.Make = Make;
        this.Model = Model;
        this.SubModel = SubModel;
        this.Rating = Rating;
        this.Category = Category;
        this.Class = Class;
        this.Sea = Sea;
        this.Power = Power;
        this.Kg5700 = Kg5700;
        this.TMG = TMG;
        this.Tailwheel = Tailwheel;
        this.Complex = Complex;
        this.HighPerf = HighPerf;
        this.Aerobatic = Aerobatic;
        this.Record_Modified = Record_Modified;
    }

    public Integer getACCode() {
        return ACCode;
    }

    public void setACCode(Integer ACCode) {
        this.ACCode = ACCode;
    }

    public String getACRef() {
        return ACRef;
    }

    public void setACRef(String ACRef) {
        this.ACRef = ACRef;
    }

    public String getMake() {
        return Make;
    }

    public void setMake(String Make) {
        this.Make = Make;
    }

    public String getModel() {
        return Model;
    }

    public void setModel(String Model) {
        this.Model = Model;
    }

    public String getSubModel() {
        return SubModel;
    }

    public void setSubModel(String SubModel) {
        this.SubModel = SubModel;
    }

    public String getRating() {
        return Rating;
    }

    public void setRating(String Rating) {
        this.Rating = Rating;
    }

    public Integer getCategory() {
        return Category;
    }

    public void setCategory(Integer Category) {
        this.Category = Category;
    }

    public Integer getClassZ() {
        return Class;
    }

    public void setClass(Integer Class) {
        this.Class = Class;
    }

    public Boolean getSea() {
        return Sea;
    }

    public void setSea(Boolean Sea) {
        this.Sea = Sea;
    }

    public Integer getPower() {
        return Power;
    }

    public void setPower(Integer Power) {
        this.Power = Power;
    }

    public Boolean getKg5700() {
        return Kg5700;
    }

    public void setKg5700(Boolean Kg5700) {
        this.Kg5700 = Kg5700;
    }

    public Boolean getTMG() {
        return TMG;
    }

    public void setTMG(Boolean TMG) {
        this.TMG = TMG;
    }

    public Boolean getTailwheel() {
        return Tailwheel;
    }

    public void setTailwheel(Boolean Tailwheel) {
        this.Tailwheel = Tailwheel;
    }

    public Boolean getComplex() {
        return Complex;
    }

    public void setComplex(Boolean Complex) {
        this.Complex = Complex;
    }

    public Boolean getHighPerf() {
        return HighPerf;
    }

    public void setHighPerf(Boolean HighPerf) {
        this.HighPerf = HighPerf;
    }

    public Boolean getAerobatic() {
        return Aerobatic;
    }

    public void setAerobatic(Boolean Aerobatic) {
        this.Aerobatic = Aerobatic;
    }

    public Long getRecord_Modified() {
        return Record_Modified;
    }

    public void setRecord_Modified(Long Record_Modified) {
        this.Record_Modified = Record_Modified;
    }

}