package aero.pilotlog.models;

/**
 * Created by tuan.na on 7/10/2015.
 */
public class AccountCalendarModel {
    private String subject;
    private String body;
    private String startdate;
    private String starttime;
    private String duration;
    private String category;
    private String location;
    private String reminder;
    private String remindersound;

    public AccountCalendarModel(){}

    public AccountCalendarModel(String subject, String body, String startdate, String starttime, String duration, String category, String location, String reminder, String remindersound) {
        this.subject = subject;
        this.body = body;
        this.startdate = startdate;
        this.starttime = starttime;
        this.duration = duration;
        this.category = category;
        this.location = location;
        this.reminder = reminder;
        this.remindersound = remindersound;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getRemindersound() {
        return remindersound;
    }

    public void setRemindersound(String remindersound) {
        this.remindersound = remindersound;
    }
}