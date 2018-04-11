package aero.pilotlog.models;

/**
 * Created by tuan.pv on 2015/09/15.
 */
public class SubFlightPilotModel {
    private String mFlightCode;
    private String mFlightDate;
    private String mArrCode;
    private String mDepCode;
    private String mFlightAirfield;
    private String mFlightNumber;
    private String mFlightTime;
    private String mRecStatus;
    private String mReference;
    private String mP1;
    private String mP2;
    private String mP3;
    private String mP4;
    private boolean mIsSelected;

    public SubFlightPilotModel() {
    }

    public SubFlightPilotModel(String mFlightCode, String mFlightDate, String mArrCode, String mDepCode,
                               String mFlightAirfield, String mFlightNumber, String mFlightTime, String mRecStatus,
                               String mReference, String mP1, String mP2, String mP3, String mP4) {
        this.mFlightCode = mFlightCode;
        this.mFlightDate = mFlightDate;
        this.mArrCode = mArrCode;
        this.mDepCode = mDepCode;
        this.mFlightAirfield = mFlightAirfield;
        this.mFlightNumber = mFlightNumber;
        this.mFlightTime = mFlightTime;
        this.mRecStatus = mRecStatus;
        this.mReference = mReference;
        this.mP1 = mP1;
        this.mP2 = mP2;
        this.mP3 = mP3;
        this.mP4 = mP4;
        this.mIsSelected = false;
    }

    public String getFlightCode() {
        return mFlightCode;
    }

    public void setFlightCode(String mFlightCode) {
        this.mFlightCode = mFlightCode;
    }

    public String getFlightDate() {
        return mFlightDate;
    }

    public void setFlightDate(String mFlightDate) {
        this.mFlightDate = mFlightDate;
    }

    public String getArrCode() {
        return mArrCode;
    }

    public void setArrCode(String mArrCode) {
        this.mArrCode = mArrCode;
    }

    public String getDepCode() {
        return mDepCode;
    }

    public void setDepCode(String mDepCode) {
        this.mDepCode = mDepCode;
    }

    public String getFlightAirfield() {
        return mFlightAirfield;
    }

    public void setFlightAirfield(String mFlightAirfield) {
        this.mFlightAirfield = mFlightAirfield;
    }

    public String getFlightNumber() {
        return mFlightNumber;
    }

    public void setFlightNumber(String mFlightNumber) {
        this.mFlightNumber = mFlightNumber;
    }

    public String getFlightTime() {
        return mFlightTime;
    }

    public void setFlightTime(String mFlightTime) {
        this.mFlightTime = mFlightTime;
    }

    public String getRecStatus() {
        return mRecStatus;
    }

    public void setRecStatus(String mRecStatus) {
        this.mRecStatus = mRecStatus;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean mIsEdited) {
        this.mIsSelected = mIsEdited;
    }

    public String getmReference() {
        return mReference;
    }

    public void setmReference(String mReference) {
        this.mReference = mReference;
    }

    public String getmP1() {
        return mP1;
    }

    public void setmP1(String mP1) {
        this.mP1 = mP1;
    }

    public String getmP2() {
        return mP2;
    }

    public void setmP2(String mP2) {
        this.mP2 = mP2;
    }

    public String getmP3() {
        return mP3;
    }

    public void setmP3(String mP3) {
        this.mP3 = mP3;
    }

    public String getmP4() {
        return mP4;
    }

    public void setmP4(String mP4) {
        this.mP4 = mP4;
    }
}

