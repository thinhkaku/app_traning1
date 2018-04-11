package aero.pilotlog.models;

/**
 * Created by phuc.dd on 7/14/2017.
 */

public class DutyModels {
    public byte[] getDutyCode() {
        return mDutyCode;
    }

    public void setDutyCode(byte[] mDutyCode) {
        this.mDutyCode = mDutyCode;
    }

    private byte[] mDutyCode;

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    private String mDescription;

    public String getDutyDate() {
        return mDutyDate;
    }

    public void setDutyDate(String mDutyDate) {
        this.mDutyDate = mDutyDate;
    }

    private String mDutyDate;

    public String getDutyTime() {
        return mDutyTime;
    }

    public void setDutyTime(String mDutyTime) {
        this.mDutyTime = mDutyTime;
    }

    private String mDutyTime;

    public String getDuration() {
        return mDuration;
    }

    public void setDuration(String mDuration) {
        this.mDuration = mDuration;
    }

    private String mDuration;

    public String getEventDateUTC() {
        return eventDateUTC;
    }

    public void setEventDateUTC(String eventDateUTC) {
        this.eventDateUTC = eventDateUTC;
    }

    private String eventDateUTC;

    public int getEventStartUTC() {
        return eventStartUTC;
    }

    public void setEventStartUTC(int eventStartUTC) {
        this.eventStartUTC = eventStartUTC;
    }

    private int eventStartUTC;

    public int getEventEndUTC() {
        return eventEndUTC;
    }

    public void setEventEndUTC(int eventEndUTC) {
        this.eventEndUTC = eventEndUTC;
    }

    private int eventEndUTC;
    private boolean mIsSelected;

    public DutyModels() {
    }

    public DutyModels(byte[] dutyCode, String description, String dutyDate, String dutyTime, String duration,
                      String eventDateUTC, int eventStartUTC, int eventEndUTC) {
        this.mDutyCode = dutyCode;
        this.mDescription = description;
        this.mDutyDate = dutyDate;
        this.mDutyTime = dutyTime;
        this.mDuration = duration;
        this.eventDateUTC = eventDateUTC;
        this.eventStartUTC = eventStartUTC;
        this.eventEndUTC = eventEndUTC;
        this.mIsSelected = false;
    }

    public boolean isSelected() {
        return mIsSelected;
    }

    public void setSelected(boolean mIsEdited) {
        this.mIsSelected = mIsEdited;
    }

}
