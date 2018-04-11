package aero.pilotlog.models;

/**
 * Created by phuc.dd on 5/11/2017.
 */
public class FlightModel {
    private String aircraftName;
    private byte[] mFlightCode;
    private String mFlightDateUTC;
    private byte[] mArrCode;
    private byte[] mDepCode;
    private String mFlightAirfield;
    private String mFlightNumber;
    private String mFlightTime;
    //private String mRecStatus;
    private String mReference;
    private byte[] mP1;
    private byte[] mP2;
    private byte[] mP3;
    private byte[] mP4;
    private boolean toEdit;
    private boolean nextPage;
    private Long aircraftDeviceCode;
    //private String dateMode;
    //private String timeMode;
    private boolean mIsSelected;

    public FlightModel() {
    }

    public FlightModel( byte[] mFlightCode, String mFlightDateUTC, byte[] mArrCode, byte[] mDepCode,
                               String mFlightAirfield, String mFlightNumber, String mFlightTime,
                               String mReference, byte[] mP1, byte[] mP2, byte[] mP3, byte[] mP4, boolean toEdit, boolean nextPage,
                       long aircraftDeviceCode, String aircraftName) {
        this.mFlightCode = mFlightCode;
        this.mFlightDateUTC = mFlightDateUTC;
        this.mArrCode = mArrCode;
        this.mDepCode = mDepCode;
        this.mFlightAirfield = mFlightAirfield;
        this.mFlightNumber = mFlightNumber;
        this.mFlightTime = mFlightTime;
        //this.mRecStatus = mRecStatus;
        this.mReference = mReference;
        this.mP1 = mP1;
        this.mP2 = mP2;
        this.mP3 = mP3;
        this.mP4 = mP4;
        this.toEdit = toEdit;
        this.nextPage = nextPage;
        this.mIsSelected = false;
        this.aircraftDeviceCode = aircraftDeviceCode;
        this.aircraftName = aircraftName;
    }
    public String getAircraftName() {
        return aircraftName;
    }

    public void setAircraftName(String aircraftName) {
        this.aircraftName = aircraftName;
    }

    public byte[] getFlightCode() {
        return mFlightCode;
    }

    public void setFlightCode(byte[] mFlightCode) {
        this.mFlightCode = mFlightCode;
    }

    public String getFlightDateUTC() {
        return mFlightDateUTC;
    }

    public void setFlightDateUTC(String mFlightDateUTC) {
        this.mFlightDateUTC = mFlightDateUTC;
    }

    public byte[] getArrCode() {
        return mArrCode;
    }

    public void setArrCode(byte[] mArrCode) {
        this.mArrCode = mArrCode;
    }

    public byte[] getDepCode() {
        return mDepCode;
    }

    public void setDepCode(byte[] mDepCode) {
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

    public byte[] getmP1() {
        return mP1;
    }

    public void setmP1(byte[] mP1) {
        this.mP1 = mP1;
    }

    public byte[] getmP2() {
        return mP2;
    }

    public void setmP2(byte[] mP2) {
        this.mP2 = mP2;
    }

    public byte[] getmP3() {
        return mP3;
    }

    public void setmP3(byte[] mP3) {
        this.mP3 = mP3;
    }

    public byte[] getmP4() {
        return mP4;
    }

    public void setmP4(byte[] mP4) {
        this.mP4 = mP4;
    }


    public boolean isToEdit() {
        return toEdit;
    }

    public void setToEdit(boolean toEdit) {
        this.toEdit = toEdit;
    }

    public boolean isNextPage() {
        return nextPage;
    }

    public void setNextPage(boolean nextPage) {
        this.nextPage = nextPage;
    }

    public Long getAircraftDeviceCode() {
        return aircraftDeviceCode;
    }

    public void setAircraftDeviceCode(Long aircraftDeviceCode) {
        this.aircraftDeviceCode = aircraftDeviceCode;
    }
}
