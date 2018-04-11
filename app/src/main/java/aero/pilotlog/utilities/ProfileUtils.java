package aero.pilotlog.utilities;

import aero.pilotlog.models.UserProfileRespond;

/**
 * Created by phuc.dd on 2/5/2018.
 */

public class ProfileUtils {
    private static ProfileUtils mInstance;
    public static ProfileUtils newInstance(){
        if (mInstance == null) {
            mInstance = new ProfileUtils();
        }
        return mInstance;
    }


    public UserProfileRespond getUserProfileRespond() {
        return userProfileRespond;
    }

    public void setUserProfileRespond(UserProfileRespond userProfileRespond) {
        this.userProfileRespond = userProfileRespond;
    }

    private UserProfileRespond userProfileRespond;
}
