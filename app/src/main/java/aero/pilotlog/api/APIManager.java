package aero.pilotlog.api;
import com.squareup.okhttp.OkHttpClient;

import aero.pilotlog.common.CloudApiConstants;
import aero.pilotlog.common.MCCPilotLogConst;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import aero.pilotlog.utilities.DateTimeUtils;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Robert on 9/4/14.
 */
public class APIManager {    
   // private static MccNetWorkingService networkingServiceInstance;

    public static MccNetWorkingService getNetworkingServiceInstance() {
        MccNetWorkingService networkingServiceInstance = null;
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        if (networkingServiceInstance == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(okHttpClient))
                    .setEndpoint(CloudApiConstants.API_BASE).build();
            if (MCCPilotLogConst.DEBUG) {
                restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            }

            networkingServiceInstance = restAdapter.create(MccNetWorkingService.class);
        }
        return networkingServiceInstance;
    }
    public static MccNetWorkingService getNetworkingServiceInstance1() {
        MccNetWorkingService networkingServiceInstance = null;
        final OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        if (networkingServiceInstance == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setClient(new OkClient(okHttpClient))
                    .setEndpoint(CloudApiConstants.API_BASE1).build();
            if (MCCPilotLogConst.DEBUG) {
                restAdapter.setLogLevel(RestAdapter.LogLevel.FULL);
            }

            networkingServiceInstance = restAdapter.create(MccNetWorkingService.class);
        }
        return networkingServiceInstance;
    }

    public static String getSignature(String userId){
        Random rd = new Random();
        String randomLetter = "A";
        int multiplier = 19;
        int randomNumber = rd.nextInt(4);
        switch (randomNumber){
            case 0:
                randomLetter = "A";
                multiplier = 19;
                break;
            case 1:
                randomLetter = "B";
                multiplier = 11;
                break;
            case 2:
                randomLetter = "C";
                multiplier = 27;
                break;
            case 3:
                randomLetter = "D";
                multiplier = 7;
                break;
        }
        int intUserId = Integer.parseInt(userId);
        long timeStamp = DateTimeUtils.getCurrentUTCUnixTimeStamp() + 19821967;
        long sum = ((timeStamp * multiplier) + intUserId) * (multiplier - 5);
        String signature = md5(String.valueOf(sum)) + ":" + timeStamp + "|" + randomLetter;
        return signature;
    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
