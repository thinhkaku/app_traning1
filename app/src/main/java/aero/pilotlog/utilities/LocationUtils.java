package aero.pilotlog.utilities;

import android.util.Log;

import aero.pilotlog.common.MCCPilotLogConst;

import java.text.DecimalFormat;

/**
 * Created by tuan.na on 7/30/2015.
 * Class to convert Lat/Long in db to readable format
 */
public class LocationUtils {
   /* public static Float getLongitudeFloat(int longitude) {
        String afLongitude = String.valueOf(longitude);
        Float fLongitude = 0.0f;
        try {
            if (longitude < 0) {
                //East
                String string1 = afLongitude.substring(1, afLongitude.length() - 3);
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLongitude.substring(afLongitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLongitude.substring(afLongitude.length() - 1);
                fLongitude = convertToDecimal(string1, string2, string3,true);
            } else {
                // West
                String string1 = afLongitude.substring(0, afLongitude.length() - 3);
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLongitude.substring(afLongitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLongitude.substring(afLongitude.length() - 1);
                fLongitude = -convertToDecimal(string1, string2, string3,false);
            }
        } catch (Exception ignored) {
        }
        return fLongitude;
    }

    public static Float getLatitudeFloat(int latitude) {
        String afLatitude = String.valueOf(latitude);
        Float fLatitude = 0.0f;
        try {
            if (latitude < 0) {
                //South
                String string1 = afLatitude.substring(1, afLatitude.length() - 3);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLatitude.substring(afLatitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLatitude.substring(afLatitude.length() - 1);
                fLatitude = -convertToDecimal(string1, string2, string3,false);
            } else {
                // North
                String string1 = afLatitude.substring(0, afLatitude.length() - 3);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLatitude.substring(afLatitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLatitude.substring(afLatitude.length() - 1);
                fLatitude = convertToDecimal(string1, string2, string3,true);
            }
        } catch (Exception ignored) {
        }
        return fLatitude;
    }*/

    public static Float getLongitudeFloat(int longitude) {
        String afLongitude = String.valueOf(longitude);
        Float fLongitude = 0.0f;
        try {
            if (longitude < 0) {
                //East
                String string1 = afLongitude.substring(1).substring(0, afLongitude.length() - 4);
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLongitude.substring(afLongitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLongitude.substring(afLongitude.length() - 1);
                fLongitude = convertToDecimal(string1, string2, string3, true);
            } else {
                // West
                String string1 = afLongitude.substring(0, afLongitude.length() - 3);
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLongitude.substring(afLongitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLongitude.substring(afLongitude.length() - 1);
                fLongitude = convertToDecimal(string1, string2, string3, false);
            }
        } catch (Exception ex) {
            Log.e("Location Utils - ", "Exception when get longitude float !", ex);
        }
        return fLongitude;
    }

    public static Float getLatitudeFloat(int latitude) {
        String afLatitude = String.valueOf(latitude);
        Float fLatitude = 0.0f;
        try {
            if (latitude < 0) {
                //South
                String string1 = afLatitude.substring(1).substring(0, afLatitude.length() - 4);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLatitude.substring(afLatitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLatitude.substring(afLatitude.length() - 1);
                fLatitude = convertToDecimal(string1, string2, string3, false);
            } else {
                // North
                String string1 = afLatitude.substring(0, afLatitude.length() - 3);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = afLatitude.substring(afLatitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = afLatitude.substring(afLatitude.length() - 1);
                fLatitude = convertToDecimal(string1, string2, string3, true);
            }
        } catch (Exception ex) {
            Log.e("Location Untils - ", "Exception when get latitude float !", ex);
        }
        return fLatitude;
    }

    private static Float convertToDecimal(String string1, String string2, String string3, boolean isDirector) {
        Float fNum = 0.0f;
        int negative = isDirector ? 1 : -1;
        fNum = negative * (Float.parseFloat(string1) + Float.parseFloat(string2 + "." + string3) / 60);
        return fNum;
    }

    /**
     * @param latitude latitude in db format
     * @return String with format W0AA°BB′CC″
     */
    public static String getLatitudeString(int latitude) {
        String sLat;
        float lat = getLatitudeFloat(latitude);
        DecimalFormat format = new DecimalFormat("##.#");
        if (lat < 0) { //S
            sLat = "S ";
            int degree = Math.abs((int) lat);
            float minute = (-lat - degree) * 60;
            sLat = sLat.concat(degree + "° " + format.format(minute) + "′");
        } else { //N
            sLat = "N ";
            int degree = (int) lat;
            float minute = (lat - degree) * 60;
            sLat = sLat.concat(degree + "° " + format.format(minute) + "′");
        }
        return sLat;
    }

    public static String getLongitudeString(int longitude) {
        String sLat;
        float lat = getLatitudeFloat(longitude);
        DecimalFormat format = new DecimalFormat("##.#");
        if (lat < 0) { //E
            sLat = "E ";
            int degree = Math.abs((int) lat);
            float minute = (-lat - degree) * 60;
            sLat = sLat.concat(degree + "° " + format.format(minute) + "′");
        } else { //W
            sLat = "W ";
            int degree = (int) lat;
            float minute = (lat - degree) * 60;
            sLat = sLat.concat(degree + "° " + format.format(minute) + MCCPilotLogConst.STRING_EMPTY);
        }
        return sLat;
    }

    //TuanPV begin add

    /**
     * @param latitude latitude in db format
     * @return String with format W0AA°BB′CC″
     */
    public static String getLatitudeString(String latitude) {
        String afLatitude = "";
        try {
            if (Integer.parseInt(latitude) < 0) {
                //South
                String string1 = latitude.substring(1, latitude.length() - 3);
//                String string1 = latitude.substring(1, latitude.length() - 4);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = latitude.substring(latitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = latitude.substring(latitude.length() - 1);
                afLatitude = String.format("S %s° %s.%s'", string1, string2, string3);
            } else {
                // North
                String string1 = latitude.substring(0, latitude.length() - 3);
                while (string1.length() < 2) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = latitude.substring(latitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = latitude.substring(latitude.length() - 1);
                afLatitude = String.format("N %s° %s.%s'", string1, string2, string3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afLatitude;
    }

    /**
     * @param longitude longitude in db format
     * @return String with format W0AA°BB′CC″
     */
    public static String getLongitudeString(String longitude) {
        String afLongitude = "";
        try {
            if (Integer.parseInt(longitude) < 0) {
                //East
                String string1 = longitude.substring(1, longitude.length() - 3);
//                String string1 = longitude.substring(1, longitude.length() - 4);
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = longitude.substring(longitude.length() - 3);
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = longitude.substring(longitude.length() - 1);
                afLongitude = String.format("E %s° %s.%s'", string1, string2, string3);
            } else if (Integer.parseInt(longitude) > 0) {
                // West
                String string1 = longitude;
                if (longitude.length() >= 3) {
                    string1 = longitude.substring(0, longitude.length() - 3);
                }
                while (string1.length() < 3) {
                    string1 = String.format("0%s", string1);
                }
                String string2 = longitude;
                if (longitude.length() >= 3) {
                    string2 = longitude.substring(longitude.length() - 3);
                }
                string2 = string2.substring(0, string2.length() - 1);
                String string3 = longitude.substring(longitude.length() - 1);
                afLongitude = String.format("W %s° %s.%s'", string1, string2, string3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afLongitude;
    }

    public static String getLatLongStringFromDouble(double value, boolean isLongitude) {
        String valueString = String.valueOf(value).replace(",", ".").replace("-", "");
        String[] valueArr = valueString.split("\\.");
        long decimalValue = Long.parseLong(valueArr[1]);
        String returnValue = valueArr[0] + String.valueOf(decimalValue / 100 * 60);
        if (String.valueOf(value).contains("-")) {
            if(isLongitude){
                if (valueArr[0].length() <= 2) {
                    returnValue = returnValue.substring(0, 5);
                } else {
                    returnValue = returnValue.substring(0, 6);
                }
            }else {
                if (valueArr[0].length() <= 2) {
                    returnValue = "-" + returnValue.substring(0, 5);
                } else {
                    returnValue = "-" + returnValue.substring(0, 6);
                }
            }
        } else {
            if(isLongitude){
                if (valueArr[0].length() <= 2) {
                    returnValue = "-" + returnValue.substring(0, 5);
                } else {
                    returnValue = "-" + returnValue.substring(0, 6);
                }
            }else {
                if (valueArr[0].length() <= 2) {
                    returnValue = returnValue.substring(0, 5);
                } else {
                    returnValue = returnValue.substring(0, 6);
                }
            }
        }
        return returnValue;
    }
    //TuanPV end add
}