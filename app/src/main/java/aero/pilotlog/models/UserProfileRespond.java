package aero.pilotlog.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by cong.nc on 12/19/2017.
 */

public class UserProfileRespond implements Serializable {
    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    @SerializedName("data")
    private UserProfile userProfile;

    public String getJwt() {
        return jwt;
    }

    private String jwt;

    public UserProfile getUserProfile() {
        return userProfile;
    }
}
