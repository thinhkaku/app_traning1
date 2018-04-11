package aero.pilotlog.common;

import java.util.Calendar;

import android.content.Context;
import android.text.format.Time;

public class SunCalc {
    public Calendar PosDate;
    public long PosLatitude;
    public long PosLongitude;

    private double pLatitude;
    private double pLongitude;

    public long SR;
    public long SS;
    public String SunRise;
    public String SunSet;
    public long PolarDayNight;

    /**
     * Works the same way as VB6 Int() function
     *
     * @return Integer
     */
    private static double Int(double val) {
        if (val < 0)
            return -Math.ceil(-val);
        else
            return Math.ceil(val);
    }

    private static double Right(double val, int len) {
        //Convert the val into a char array
        String s = Double.toString(val);
        String ret = Double.toString(val).substring(s.length() - 3);
        return Double.parseDouble(ret);
    }

    public boolean CalculateSun(Context c) {
        double t, jd, jd2;
        SR = 0;
        SS = 0;
        SunRise = "";
        SunSet = "";
        PolarDayNight = 0;

        if (PosLatitude == 0 && PosLongitude == 0)
            return false;
        if (PosDate == null) {
            return false;
        }

//		pLongitude = (long)((Math.signum(PosLongitude) * Int(Math.abs(PosLongitude / 1000))) + (Math.signum(PosLongitude) * (Right(Math.abs(PosLongitude), 3)) / 600));
//	    pLatitude = (long)((Math.signum(PosLatitude) * Int(Math.abs(PosLatitude / 1000))) + (Math.signum(PosLatitude) * (Right(Math.abs(PosLatitude), 3)) / 600));
        pLongitude = GetLongitude(PosLongitude);
        pLatitude = GetLatitude(PosLatitude);
        if (pLatitude >= -90 && pLatitude < -89.8) pLatitude = -89.8;
        if (pLatitude <= 90 && pLatitude > 89.8) pLatitude =  89.8;
        jd = Time.getJulianDay(PosDate.getTimeInMillis(), 0);
        //jd2 = julianDay(PosDate.get(Calendar.YEAR),PosDate.get(Calendar.MONTH),PosDate.get(Calendar.DAY_OF_MONTH));
        t = CalcTimeJulianCent(jd);

        //Return values SR / SS
        //range -1440 (yesterday) 0 (today) 1440 (tomorrow) 2880
        SR = (long) SunriseUTC(t, pLatitude, pLongitude);
        SS = (long) SunsetUTC(t, pLatitude, pLongitude);

        // 11.11.2011
       /* if ((SR < 0) || (SS < 0)) {
            SR += 1440;
            SS += 1440;
        }*/
        PolarDayNight = 0;

        //Polar Day/Night
        if (SR == SS) {
            if (PosDate.get(Calendar.MONTH) > 3 && PosDate.get(Calendar.MONTH) < 10)    //From april till september
            {
                if (PosLatitude > 0)   //Latitude of Airfield is North
                {
                    SR = -1440; //polar day in North
                    SS = 2880;
                    PolarDayNight = 1;   //Polar Day
                } else {
                    SR = 2880; //polar night in South
                    SS = -1440;
                    PolarDayNight = 2;  //Polar Night
                }
            } else {
                if (PosLatitude > 0)   //Latitude of Airfield is North
                {
                    SR = 2880; //polar night in North
                    SS = -1440;
                    PolarDayNight = 2;   //Polar Night
                } else {
                    SR = -1440; //polar day in South
                    SS = 2880;
                    PolarDayNight = 1;   //Polar Day
                }
            }
        }

        //Return values as readable text
        //including date
        switch ((int) PolarDayNight) {
            case 0:
                if (SR < 0 || SS > 1440) {
                    SunRise = String.format("%tD", PosDate) + "   " + ShowHHMM((SR + 1440) % 1440) + " UTC";
                    Calendar n = PosDate;
                    n.add(Calendar.HOUR, 24);
                    SunSet = String.format("%tD", n) + "   " + ShowHHMM((SS + 1440) % 1440) + " UTC";
                } else {
                    SunRise = String.format("%tD", PosDate) + "   " + ShowHHMM(SR) + " UTC";
                    SunSet = String.format("%tD", PosDate) + "   " + ShowHHMM(SS) + " UTC";
                }
                break;
            case 1:
                SunRise = "Polar Day";
                SunSet = "Sun up all day or twilight";
                break;
            case 2:
                SunRise = "Polar Night";
                SunSet = "Sun down all day";
                break;
        }

        return true;

    }

    private int sign(double numb) {
        if (numb < 0)
            return -1;
        else if (numb > 0)
            return 1;
        else
            return 0;
    }


    private double GetLatitude(long posLatitude) {
        double nLatitude;

        double lt1 = sign(this.PosLatitude) * (int) (Math.abs(this.PosLatitude / 1000.0));//grad
        double lt2 = sign(this.PosLatitude) * Math.abs(this.PosLatitude - (1000.0 * (int) (this.PosLatitude / 1000.0))) / 600.0;

        return nLatitude = lt1 + lt2;
    }

    private double GetLongitude(long posLongitude) {
        double lg1 = sign(this.PosLongitude) * (int) (Math.abs(this.PosLongitude / 1000.0));//grad
        double lg2 = sign(this.PosLongitude) * Math.abs(this.PosLongitude - (1000.0 * (int) (this.PosLongitude / 1000.0))) / 600.0;

        return lg1 + lg2;

    }

    public static String ShowHHMM(long t) {
        return String.format("%02d:%02d", (int) Int(t / 60), (int) (t - (Int(t / 60) * 60)));
    }

    private static double CalcTimeJulianCent(double julianDay) {
        return (julianDay - 2451545) / 36525;
    }

    private static double CalcJDFromJulianCent(double julianCent) {
        return julianCent * 36525 + 2451545;
    }

    private static double SunriseUTC(double julianCent, double lat, double lon) {
        double eqTime, hourAngle, delta, timeDiff, timeUTC, newt;

        eqTime = EquationOfTime(julianCent);
        hourAngle = HourAngleSunrise(julianCent, lat);

        delta = lon - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;              //in minutes of time
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        // *** Second pass includes fractional jday in gamma calc

        newt = CalcTimeJulianCent(CalcJDFromJulianCent(julianCent) + timeUTC / 1440);

        eqTime = EquationOfTime(newt);
        hourAngle = HourAngleSunrise(newt, lat);
        delta = lon - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes
        return timeUTC;

    }

    private static double SunsetUTC(double julianCent, double lat, double lon) {
        double eqTime, hourAngle, delta, timeDiff, timeUTC, newt;

        eqTime = EquationOfTime(julianCent);
        hourAngle = HourAngleSunset(julianCent, lat);

        delta = lon - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;              //in minutes of time
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        // *** Second pass includes fractional jday in gamma calc

        newt = CalcTimeJulianCent(CalcJDFromJulianCent(julianCent) + timeUTC / 1440);

        eqTime = EquationOfTime(newt);
        hourAngle = HourAngleSunset(newt, lat);
        delta = lon - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes
        return timeUTC;

    }

    private static double EquationOfTime(double julianCent) {
        double epsilon, ten, e, m, y, sin210, sinm, cos210, sin410, sin2m, Etime;

        epsilon = ObliquityCorrection(julianCent);
        ten = GeomMeanLongSun(julianCent);
        e = EccentricityEarthOrbit(julianCent);
        m = GeomMeanAnomalySun(julianCent);

        y = Math.tan(Math.toRadians(epsilon) / 2.0);
        y = y * y;

        sin210 = Math.sin(2 * Math.toRadians(ten));
        sinm = Math.sin(Math.toRadians(m));
        cos210 = Math.cos(2 * Math.toRadians(ten));
        sin410 = Math.sin(4 * Math.toRadians(ten));
        sin2m = Math.sin(2 * Math.toRadians(m));

        Etime = y * sin210 - 2 * e * sinm + 4 * e * y * sinm * cos210 - 0.5 * y * y * sin410 - 1.25 * e * e * sin2m;

        return Math.toDegrees(Etime) * 4;
    }

    private static double ObliquityCorrection(double julianCent) {
        double omega = 125.04 - 1934.136 * julianCent;

        return MeanObliquityOfEcliptic(julianCent) + 0.00256 * Math.cos(Math.toRadians(omega));
    }

    private static double MeanObliquityOfEcliptic(double julianCent) {
        double sec = 21.448 - julianCent * (46.815 + julianCent * (0.00059 - julianCent * (0.001813)));
        return 23 + (26 + (sec / 60)) / 60;

    }

    private static double GeomMeanLongSun(double julianCent) {
        double lo = 280.46646 + julianCent * (36000.76983 + 0.0003032 * julianCent);
        while (lo > 360)
            lo -= 360;
        while (lo < 0)
            lo += 360;
        return lo;
    }

    private static double EccentricityEarthOrbit(double julianCent) {
        return 0.016708634 - julianCent * (0.000042037 + 0.0000001267 * julianCent);
    }

    private static double GeomMeanAnomalySun(double julianCent) {
        return 357.52911 + julianCent * (35999.05029 - 0.0001537 * julianCent);
    }

    private static double SunDeclination(double julianCent) {
        double e, lambda, sint;

        e = ObliquityCorrection(julianCent);
        lambda = SunApparentLong(julianCent);
        sint = Math.sin(Math.toRadians(e)) * Math.sin(Math.toRadians(lambda));
        return Math.toDegrees(Math.asin(sint));
    }

    private static double SunApparentLong(double julianCent) {
        double omega = 125.04 - 1934.136 * julianCent;
        return SunTrueLong(julianCent) - 0.00569 - 0.00478 * Math.sin(Math.toRadians(omega));
    }

    private static double SunTrueLong(double julianCent) {
        return GeomMeanLongSun(julianCent) + SunEqOfCenter(julianCent);
    }


    private static double SunEqOfCenter(double julianCent) {
        double mrad, sinm, sin2m, sin3m;

        mrad = Math.toRadians(GeomMeanAnomalySun(julianCent));
        sinm = Math.sin(mrad);
        sin2m = Math.sin(mrad + mrad);
        sin3m = Math.sin(mrad + mrad + mrad);
        return sinm * (1.914602 - julianCent * (0.004817 + 0.000014 * julianCent)) + sin2m * (0.019993 - 0.000101 * julianCent) + sin3m * 0.000289;
    }

    private static double HourAngleSunrise(double julianCent, double lat) {
        double latRad, sdRad, HAarg;

        latRad = Math.toRadians(lat);
        sdRad = Math.toRadians(SunDeclination(julianCent));
        HAarg = Math.cos(Math.toRadians(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad);

        if (Math.abs(HAarg) < 1)
            return Math.acos(HAarg);
        else
            return 0;
    }

    private static double HourAngleSunset(double julianCent, double lat) {
        double latRad, sdRad, HAarg;

        latRad = Math.toRadians(lat);
        sdRad = Math.toRadians(SunDeclination(julianCent));
        HAarg = Math.cos(Math.toRadians(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad);

        if (Math.abs(HAarg) < 1)
            return -Math.acos(HAarg);
        else
            return 0;
    }

/* private int julianDay(int year, int month, int day) {
     int a = (14 - month) / 12;
     int y = year + 4800 - a;
     int m = month + 12 * a - 3;
     int jdn = day + (153 * m + 2)/5 + 365*y + y/4 - y/100 + y/400 - 32045;
     return jdn;
 }*/
}
