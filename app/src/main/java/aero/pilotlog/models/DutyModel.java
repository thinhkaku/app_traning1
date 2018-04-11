package aero.pilotlog.models;

/**
 * Created by tuan.pv on 10/28/2015.
 */
public class DutyModel {
    private String mIcaoIata;
    private String mFromToHours;
    private String mTotalHours;
    private String mFlightDate;

    public DutyModel(String mIcaoIata, String mFromToHours, String mTotalHours, String mFlightDate) {
        this.mIcaoIata = mIcaoIata;
        this.mFromToHours = mFromToHours;
        this.mTotalHours = mTotalHours;
        this.mFlightDate = mFlightDate;
    }

    public String getIcaoIata() {
        return mIcaoIata;
    }

    public void setIcaoIata(String mIcaoIata) {
        this.mIcaoIata = mIcaoIata;
    }

    public String getFromToHours() {
        return mFromToHours;
    }

    public void setFromToHours(String mFromToHours) {
        this.mFromToHours = mFromToHours;
    }

    public String getTotalHours() {
        return mTotalHours;
    }

    public void setTotalHours(String mTotalHours) {
        this.mTotalHours = mTotalHours;
    }

    public String getmFlightDate() {
        return mFlightDate;
    }
}
