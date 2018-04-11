package aero.pilotlog.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cong.nc on 12/19/2017.
 */

public class UserProfile implements Serializable {
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeFunction() {
        return employeeFunction;
    }

    public void setEmployeeFunction(String employeeFunction) {
        this.employeeFunction = employeeFunction;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public PrivacyUserProfile getPrivacyUserProfile() {
        return privacyUserProfile;
    }

    public void setPrivacyUserProfile(PrivacyUserProfile privacyUserProfile) {
        this.privacyUserProfile = privacyUserProfile;
    }

    public ProfileUserProfile getProfileUserProfile() {
        return profileUserProfile;
    }

    public void setProfileUserProfile(ProfileUserProfile profileUserProfile) {
        this.profileUserProfile = profileUserProfile;
    }

    public int getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(int interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getLicenseDate() {
        return licenseDate;
    }

    public void setLicenseDate(String licenseDate) {
        this.licenseDate = licenseDate;
    }

    public int getLicenseDays() {
        return licenseDays;
    }

    public void setLicenseDays(int licenseDays) {
        this.licenseDays = licenseDays;
    }

    public String getMinimumDatabase() {
        return minimumDatabase;
    }

    public void setMinimumDatabase(String minimumDatabase) {
        this.minimumDatabase = minimumDatabase;
    }

    private String userId;
    private String firstName;
    private String lastName;
    private String companyId;
    private String employeeId;
    private String employeeFunction;
    private String picture;
    private String thumbnail;
    @SerializedName("privacy")
    private PrivacyUserProfile privacyUserProfile;
    @SerializedName("profile")
    private ProfileUserProfile profileUserProfile;
    private int interfaceId;
    private String licenseDate;
    private int licenseDays;
    private String minimumDatabase;

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    private String accountEmail;

    public long getLicenseExpiration() {
        return licenseExpiration;
    }

    public void setLicenseExpiration(long licenseExpiration) {
        this.licenseExpiration = licenseExpiration;
    }

    private long licenseExpiration;

}
