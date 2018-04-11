package aero.pilotlog.common;

import android.content.Context;
import android.text.TextUtils;

import aero.pilotlog.databases.entities.Airfield;
import aero.pilotlog.databases.entities.SettingConfig;
import aero.pilotlog.databases.manager.DatabaseManager;
import aero.pilotlog.utilities.TimeUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by phuc.dd on 5/19/2017.
 */
public class RouteInfoCalculator {
    public class AirfieldType {
        public int Latitude;
        public int Longitude;

        //	    public Sunrise Sun;
        public SunCalc sunCalc;

        public AirfieldType() {
//	        Sun = new Sunrise();
            sunCalc = new SunCalc();
        }
    }

    public class RouteInfo {
        public java.util.Date Date;
        float LoxoDist;
        float LoxoTrack;
        float OrthoDist;
        public int TODay;
        public int LdgDay;
        public int TONight;
        public int LdgNight;
        public int FlightTime;
        public int fNightTime;
        public int gNightTime;
    }

    DatabaseManager databaseManager;
    public RouteInfo route;

    //  struct RouteType Route;

    public AirfieldType depAirfield;

    public AirfieldType arrAirfield;
    Navigation Nav;


    double Factor = 0;
    public int nightMin;
    public int nightFrom;
    public int nightUntil;
    public int depHour;
    public int arrHour;
    public int minTotal;
    long PosTimePrev;
    long PosTime;
    private String nightMode;


    private SunCalc sunCalc;

    public RouteInfoCalculator(Context context, String nightMode) {
        nightMin = 0;
        route = new RouteInfo();
        depAirfield = new AirfieldType();
        arrAirfield = new AirfieldType();
        Nav = new Navigation();
        sunCalc = new SunCalc();
        databaseManager = DatabaseManager.getInstance(context);
        this.nightMode = nightMode;
    }

    public boolean initRoute(Airfield depAirfield, Airfield arrAirfield, int depHourUTC, int arrHourUTC, int minTotal, Date date) {
        if (depAirfield == null || arrAirfield == null) return false;
        if ((depAirfield.getLongitude() == 0 && depAirfield.getLatitude() == 0)
                || (arrAirfield.getLongitude() == 0 && arrAirfield.getLatitude() == 0))
            return false;
        route.Date = date;
        this.depAirfield.Latitude = depAirfield.getLatitude();
        this.depAirfield.Longitude = depAirfield.getLongitude();
        this.arrAirfield.Latitude = arrAirfield.getLatitude();
        this.arrAirfield.Longitude = arrAirfield.getLongitude();
        this.depHour = depHourUTC;
        this.arrHour = arrHourUTC;
        this.minTotal = minTotal;
        SettingConfig settingNightTimeSR = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_TIME_SRSS);
        SettingConfig settingNightTimeFrom = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_TIME_FROM);
        SettingConfig settingNightTimeUntil = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_TIME_UNTIL);
        if (settingNightTimeSR != null && !TextUtils.isEmpty(settingNightTimeSR.getData())) {
            nightMin = Integer.parseInt(settingNightTimeSR.getData());
        }
        if (settingNightTimeFrom != null && !TextUtils.isEmpty(settingNightTimeFrom.getData())) {
            nightFrom = TimeUtils.convertHourToMin(settingNightTimeFrom.getData());
        }
        if (settingNightTimeUntil != null && !TextUtils.isEmpty(settingNightTimeUntil.getData())) {
            nightUntil = TimeUtils.convertHourToMin(settingNightTimeUntil.getData());
        }
        return true;
    }

    public void calculateRoute() {
        //NightMode:
        //0 - do not calculate
        //1 - calculate
        //2 - calculate, do not suggest TO/LDG

        //NightCalc:
        //0 - Sunset / Sunrise: current method, no changes
        //1 - Fixed Hours: night time and TO/LDG per fixed hours
        //2 - Both: calculate two night times: geographical (current) and Fixed Hours; TO/LDG - per geographical hours (current method)
        SettingConfig settingNightCalc = databaseManager.getSetting(MCCPilotLogConst.SETTING_CODE_NIGHT_CALC);
        if (settingNightCalc == null || TextUtils.isEmpty(settingNightCalc.getData())) return;
        switch (settingNightCalc.getData()) {
            case MCCPilotLogConst.NIGHT_CALC_SS_SR:
                calculateRouteInfo();
                //calculate geographical data
                break;
            case MCCPilotLogConst.NIGHT_CALC_FIX_HOURS:
                calculateFixedNightTime();
                break;
            case MCCPilotLogConst.NIGHT_CALC_BOTH:
                calculateFixedNightTime();
                calculateRouteInfo();
                break;
            default:
                break;
        }
        if (nightMode == MCCPilotLogConst.NIGHT_MODE_AUTO_LOAD_WITHOUT_TO_LDG) {
            route.TODay = 0;
            route.LdgDay = 0;
            route.TONight = 0;
            route.LdgNight = 0;
        }

    }

    //Calculated Night time and TO/LDG according to Fixed Hours from user settings
    private void calculateFixedNightTime() {

        route.TODay = 0;
        route.LdgDay = 0;
        route.TONight = 0;
        route.LdgNight = 0;
        route.fNightTime = 0;


        //work in time window 0 - 2880
        if (nightUntil < nightFrom) {

            nightUntil = nightUntil + 1440;
        }

        if (arrHour < depHour) {

            arrHour = arrHour + 1440;
        }

        if (depHour < nightFrom && arrHour < nightFrom && nightUntil > 1440) {

            depHour = depHour + 1440;
            arrHour = arrHour + 1440;
        }


        if (depHour < nightFrom) {
            //departure before Night starts

            if (arrHour < nightFrom) {

                //arrival before Night starts
                //flight is outside (before) Night hours
                route.fNightTime = 0;
                route.TODay = 1;
                route.LdgDay = 1;
            } else if (arrHour > nightUntil) {

                //arrival after Night ends
                //flight spans entire Zone
                route.fNightTime = nightUntil - nightFrom + 1;
                route.TODay = 1;
                route.LdgDay = 1;
            } else {

                //arrival inside Night hours
                route.fNightTime = arrHour - nightFrom + 1;
                route.TODay = 1;
                route.LdgDay = 1;
            }
        } else if (depHour < nightUntil) {

            //departure inside Night Hours

            if (arrHour <= nightUntil) {

                //arrival inside Night Hours
                route.fNightTime = arrHour - depHour + 1;
                route.TONight = 1;
                route.LdgNight = 1;
            } else {

                //arrival after Night Ends
                route.fNightTime = nightUntil - depHour + 1;
                route.TONight = 1;
                route.LdgDay = 1;
            }
        } else {

            //departure and arrival after night Ends
            //flight is outside (after) Night Hours
            route.fNightTime = 0;
            route.TODay = 1;
            route.LdgDay = 1;
        }

        if (route.fNightTime > minTotal) {

            route.fNightTime = minTotal;
        }

    }

    public boolean calculateRouteInfo() {
        Factor = 0; //on a scale from 0 to 1
        double PrevFactor = 0;//'previous factor
        long PosTimePart1 = 0;//'Middle part of flight is day or night
        PosTime = 0;  //Time from Dep to Calculated Position
        PosTimePrev = 0; //Through the Night / Day
        long Teller = 0;
        long PrevPosTime = 0;     //'previous calculation PosTime
        int Mode = 0;//Mode = 1 Takeoff Day
//	    'Mode = 2 Takeoff Night
//	    'Mode = 3 Takeoff Day, Landing Day, Part of the flight is passing through Night
//	    'Mode = 4 Takeoff Night, Landing Night, Part of the flight is passing through Day
//	    'Extreme long flights > 720 minutes
//	    'Mode = 5 Takeoff Day, flight spans a full night and a full day, Landing Night
//	    'Mode = 6 Takeoff Night, flight spans a full day and a full night, Landing Day
        this.depAirfield.sunCalc.PosLatitude = this.depAirfield.Latitude;
        this.depAirfield.sunCalc.PosLatitude = this.depAirfield.Longitude;
        this.arrAirfield.sunCalc.PosLatitude = this.arrAirfield.Latitude;
        this.arrAirfield.sunCalc.PosLatitude = this.arrAirfield.Longitude;
        boolean result = false;
        //init structure
        route.TODay = 0;
        route.LdgDay = 0;
        route.TONight = 0;
        route.LdgNight = 0;
        route.gNightTime = 0;

        Calendar cl = new GregorianCalendar();
        cl.setTime(this.route.Date);
        this.depAirfield.sunCalc.PosDate = cl;
        if (!this.depAirfield.sunCalc.CalculateSun(null)) {
            return result;
        }
        this.depAirfield.sunCalc.SR = this.depAirfield.sunCalc.SR - nightMin;
        this.depAirfield.sunCalc.SS = this.depAirfield.sunCalc.SS + nightMin;

        this.arrAirfield.sunCalc.PosDate = cl;
        if (!this.arrAirfield.sunCalc.CalculateSun(null)) {
            return result;
        }

        arrAirfield.sunCalc.SR = arrAirfield.sunCalc.SR - nightMin;
        arrAirfield.sunCalc.SS = arrAirfield.sunCalc.SS + nightMin;

        result = true;
        //
        //Work in a time window  0 to 1440 (Today)  to 2880 (Tomorrow)
        if (arrHour < depHour) {
            arrHour = arrHour + 1440;
            route.Date = new Date(route.Date.getTime() + 1 * 24 * 60 * 60 * 1000);
            cl.setTime(this.route.Date);
            arrAirfield.sunCalc.PosDate = cl;
            arrAirfield.sunCalc.CalculateSun(null);
            arrAirfield.sunCalc.SR = arrAirfield.sunCalc.SR - nightMin;
            arrAirfield.sunCalc.SS = arrAirfield.sunCalc.SS + nightMin;
        }
        route.FlightTime = (arrHour - depHour) % 1440;

        //'Match SR/SS with flight window
        if (depAirfield.sunCalc.PolarDayNight == 0) {
            if ((Math.abs(depAirfield.sunCalc.SR - depHour) >= 1440) || (Math.abs(depAirfield.sunCalc.SS - depHour) >= 1440)) {
                depAirfield.sunCalc.SR = depAirfield.sunCalc.SR + ((depHour > depAirfield.sunCalc.SR) ? 1440 : -1440);
                depAirfield.sunCalc.SS = depAirfield.sunCalc.SS + ((depHour > depAirfield.sunCalc.SS) ? 1440 : -1440);
            }
        }
        if (arrAirfield.sunCalc.PolarDayNight == 0) {
            if ((Math.abs(arrAirfield.sunCalc.SR - arrHour) >= 1440) || (Math.abs(arrAirfield.sunCalc.SS - arrHour) >= 1440)) {
                arrAirfield.sunCalc.SR = arrAirfield.sunCalc.SR + ((arrHour > arrAirfield.sunCalc.SR) ? 1440 : -1440);
                arrAirfield.sunCalc.SS = arrAirfield.sunCalc.SS + ((arrHour > arrAirfield.sunCalc.SS) ? 1440 : -1440);
            }
        }

        //Calculate Rhumb Line Distance and Track
        Nav.latitudeA = depAirfield.Latitude;
        Nav.longitudeA = depAirfield.Longitude;
        Nav.latitudeB = arrAirfield.Latitude;
        Nav.longitudeB = arrAirfield.Longitude;
        Nav.calculate();

        // Route.OrthoDist = Nav.OrthoDist;
        route.LoxoDist = (float) Nav.loxoDist;
        route.LoxoTrack = (float) Nav.loxoTrack;

        //Get Mode
        if ((depHour >= depAirfield.sunCalc.SR && depHour < depAirfield.sunCalc.SS) || depAirfield.sunCalc.PolarDayNight == 1) {
            Mode = 1; //Takeoff Day
        } else {
            Mode = 2; //Takeoff Night
        }
        //Daylight - Nightlight
        Factor = 1;

        //     CalculatePart2: //Return back to this position (CalculatePart2) for a flight where Takeoff and Landing are both in Day or both in Night,
        //however a part of the flight is passing through Day or Night
        //If a changeover between daylight and nightlight is expected,
        //set factor to 0.5 (mid of route)


        boolean toContinue;
        // To avoid using goto:, replace it with do-while loop
        do {
            toContinue = false;
            switch (Mode) {
                case 1: //Takeoff Day
                    if (depAirfield.sunCalc.PolarDayNight > 0 || arrAirfield.sunCalc.PolarDayNight > 0 || (arrAirfield.Longitude < depAirfield.Longitude && route.FlightTime > 240) || arrHour > arrAirfield.sunCalc.SS) {
                        Factor = 0.5;
                    }
                    break;
                case 2: //Takeoff Night
                    if (depAirfield.sunCalc.PolarDayNight > 0 || arrAirfield.sunCalc.PolarDayNight > 0 || (arrAirfield.Longitude < depAirfield.Longitude && route.FlightTime > 240) || (arrHour > arrAirfield.sunCalc.SR && arrHour < arrAirfield.sunCalc.SS)) {
                        Factor = 0.5;
                    }
                    break;
                case 3:
                case 4: //Part of the flight is passing through Day or through Night
                case 5:
                case 6:
                    Factor = Factor + (1 - Factor) / 2;
                    break;
            }

            //Flights near the dateline
            if (Mode < 3)
                if (((depAirfield.sunCalc.SR + 1440) % 1440) > ((depAirfield.sunCalc.SS + 1440) % 1440) ||
                        ((arrAirfield.sunCalc.SR + 1440) % 1440) > ((arrAirfield.sunCalc.SS + 1440) % 1440))
                    Factor = 0.5;

            //avoid crash
            if (Factor < 0 || Factor > 1) {
                Factor = 0.5;
            }

            //Flight is full Day or full Night when factor is 1
            if (Factor == 1) {
                if (Mode == 1) {
                    route.gNightTime = 0; //full Day
                } else {
                    route.gNightTime = route.FlightTime; //full Night
                }

                GetTOLDG(Mode); //skip iteration
            }
            //Adjust Step Factor
            if (Factor > 0.5) {
                PrevFactor = (1 - Factor) / 2;
            } else {
                PrevFactor = Factor / 2;
            }

//	        'Iteration technique ;  jump forward or backward to half of the last half...
//	        'Iteration stops when one of the following occurs ;
//	          '- difference between aircraft position time and SS/SR or SR/SS changeover is within 3 minutes
//	          '- difference is within 5 minutes from the first trial
//	          '- 20 trials have been done
//	          '- position time is no longer moving
            Teller = 0;
            PrevPosTime = -9999; //previous calculation PosTime
            boolean exitDo = false;

            do {
                getData();
                switch (Mode) {
                    case 1:
                    case 4:
                    case 5:
                        //Takeoff Day
                        if (Math.abs(PosTime - sunCalc.SS) < 3 || Teller == 20 || PosTime == PrevPosTime) {
                            if (Math.abs(PosTime - sunCalc.SS) < 3) {
                                //Position is meeting SS (changeover from day to night)
                                PosTime = Math.round((PosTime + sunCalc.SS) / 2.0);
                            } else {
                                //Flight (or remainder of flight - CalculatePart2) is Night
                            }
                            exitDo = true;
                        }
                       // if (!exitDo) {
                            //move forward / backward along the route
                            if (PosTime >= sunCalc.SR && PosTime < sunCalc.SS) {
                                //still in daylight, move further forward
                                Factor = Factor + PrevFactor;
                            } else {
                                //now in nightlight, move backward
                                Factor = Factor - PrevFactor;
                            }
                        //}
                        break;
                    case 2:
                    case 3:
                    case 6:
                        //Takeoff Night
                        if (Math.abs(PosTime - sunCalc.SR) < 3 || Teller == 20 || PosTime == PrevPosTime) {
                            if (Math.abs(PosTime - sunCalc.SR) < 3) {
                                //Position is meeting SR (changeover from night to day)
                                PosTime = Math.round((PosTime + sunCalc.SR) / 2.0);
                            } else {
                                //Flight (or remainder of flight - CalculatePart2) is Day
                            }
                            exitDo = true;
                        }
                        //if (!exitDo) {
                            //move forward / backward along the route
                            if (!(PosTime >= sunCalc.SR && PosTime < sunCalc.SS)) {
                                //still in nightlight, move further forward
                                Factor = Factor + PrevFactor;
                            } else {
                                //now in daylight, move backward
                                Factor = Factor - PrevFactor;
                            }
                       // }
                        break;
                }
                if (!exitDo) {
                    PrevFactor = PrevFactor / 2.0;
                    PrevPosTime = (int) PosTime;
                    Teller++;
                }
            } while (!exitDo);

            //correct outerbounds
            if (Factor < 0.005) {
                PosTime = depHour;
                Factor = 0;
            } else if (Factor > 0.995) {
                PosTime = arrHour;
                Factor = 1;
            }
            //Night Time
            switch (Mode) {
                case 1: //Takeoff Day, last part of flight is Night
                    route.gNightTime = (int) (arrHour - PosTime);
                    break;
                case 2: //Takeoff Night, last part of flight is Day
                    route.gNightTime = (int) (PosTime - depHour);
                    break;
                case 3: //Takeoff Day, Landing Day, Part of flight passing through Night
                    route.gNightTime = (int) (PosTime - PosTimePart1);
                    break;
                case 4: //Takeoff Night, Landing Night, Part of fligt passing through Day
                    route.gNightTime = (int) ((PosTimePart1 - depHour) + (arrHour - PosTime));
                    break;
                case 5: //'Takeoff Day, flight spans a full night and a full day, Landing Night
                    route.gNightTime = (int) (route.gNightTime + (arrHour - PosTime));
                    break;
                case 6: //'Takeoff Night, flight spans a full day and a full night, Landing Day
                    route.gNightTime = (int) (route.gNightTime - (arrHour - PosTime));
                    break;
            }
            //Scale Nighttime to range 0 - 1440
            route.gNightTime = (route.gNightTime + 2880) % 1440;
            //Small corrections
            if (route.gNightTime >= route.FlightTime) {
                route.gNightTime = route.FlightTime; //Limit night time to flight time
            } else if (route.gNightTime < 3) {
                route.gNightTime = 0; //Delete 1 or 2 minutes night time due to roundings
            }
            //Get Mode for CalculatePart2, if applicable
            switch (Mode) {
                case 1: //Takeoff Day
                    if ((arrHour >= arrAirfield.sunCalc.SR && arrHour < arrAirfield.sunCalc.SS) || arrAirfield.sunCalc.PolarDayNight == 1
                            || (route.FlightTime > 720 && route.LoxoTrack < 180)) {
                        //Landing is also Day
                        if (route.gNightTime > 0) {
                            //part of the flight is passing through night
                            Mode = 3;
                            PosTimePart1 = PosTime;
                            toContinue = true; //GoTo Deel2
                            //GoTo CalculatePart2; //go back for a second iteration
                        }
                    }
                    break;
                case 2: //Takeoff Night
                    if (!((arrHour >= arrAirfield.sunCalc.SR && arrHour < arrAirfield.sunCalc.SS) || arrAirfield.sunCalc.PolarDayNight == 1)
                            || (route.FlightTime > 720 && route.LoxoTrack < 180)) {
                        //Landing is also Night
                        if (route.gNightTime < route.FlightTime) {
                            //part of the flight is passing through day
                            Mode = 4;
                            PosTimePart1 = PosTime;
                            toContinue = true; //GoTo Deel2
                            //GoTo CalculatePart2; //go back for a second iteration
                        }
                    }
                    break;
                case 3:
                case 4:
                    //'Extreme long flights eastbound
                    if (route.FlightTime > 720 && route.LoxoTrack < 180) {
                        if (PosTime < arrHour) {
                            Mode = Mode + 2;
                            PosTimePart1 = PosTime;
                            toContinue = true;
                            // 'go back for a third iteration
                        } else {
//					'Extreme long flight did not pass through 3 zones
//					'Revert back to previous mode for correct TO/LDG
                            Mode = Mode - 2;
                        }
                    }
                    break;
                case 5:
                case 6:
//					'If Extreme long flight did not pass through 4 zones
//					'then revert back to previous mode for correct TO/LDG
                    if (PosTime == arrHour)
                        Mode = Mode - 2;
                    break;
            }
        } while (toContinue);
        GetTOLDG(Mode);

        return result;
    }

    private void GetTOLDG(int Mode) {
        route.TODay = 0;
        route.LdgDay = 0;
        route.TONight = 0;
        route.LdgNight = 0;
        //GetTOLDG
        if (route.gNightTime == 0 || Mode == 3) {
            route.TODay = 1;
            route.LdgDay = 1;
        } else if (route.gNightTime == route.FlightTime || Mode == 4) {
            route.TONight = 1;
            route.LdgNight = 1;
        } else if (route.gNightTime > 0 && (Mode == 1 || Mode == 5)) {
            route.LdgNight = 1;
            route.TODay = 1;
        } else if (route.gNightTime > 0 && (Mode == 2 || Mode == 6)) {
            route.TONight = 1;
            route.LdgDay = 1;
        }
        //'Reset values back to window 0 - 1440
        depHour = (depHour + 1440) % 1440;
        arrHour = (arrHour + 1440) % 1440;
    }

    private void getData() {
        Nav.latitudeA = depAirfield.Latitude;
        Nav.longitudeA = depAirfield.Longitude;
        Nav.loxoDist = route.LoxoDist * Factor;
        Nav.loxoTrack = route.LoxoTrack;
        Nav.goToPos();


        Calendar cl = new GregorianCalendar();

        cl.setTime(this.route.Date);
        //this.depAirfield.sunCalc.PosDate = cl;

        this.sunCalc.PosDate = cl;
        this.sunCalc.PosLatitude = Nav.latitudeB;
        this.sunCalc.PosLongitude = Nav.longitudeB;
        this.sunCalc.CalculateSun(null);
        this.sunCalc.SR = this.sunCalc.SR - nightMin;
        this.sunCalc.SS = this.sunCalc.SS + nightMin;
        /*
		    if (SSafterMN) {
		        this.Sun.SS = this.Sun.SS - 1440;
		    }

		    PosTime = this.Route.DepHour + (int) Math.round((this.Route.FlightTime * Factor));
		*/

        PosTime = (depHour + Math.round(((double) route.FlightTime * Factor)));
        //Match SR/SS with PosTime window
        if (sunCalc.PolarDayNight == 0) {

            if (Math.abs(sunCalc.SS - PosTime) >= 1440) {
                if (PosTime > sunCalc.SS) {
                    sunCalc.SR = sunCalc.SR + 1440;
                    sunCalc.SS = sunCalc.SS + 1440;
                } else {
                    sunCalc.SR = sunCalc.SR - 1440;
                    sunCalc.SS = sunCalc.SS - 1440;
                }
            } else if (Math.abs(sunCalc.SR - PosTime) >= 1440) {
                if (PosTime > sunCalc.SR) {
                    sunCalc.SR = sunCalc.SR + 1440;
                    sunCalc.SS = sunCalc.SS + 1440;
                } else {
                    sunCalc.SR = sunCalc.SR - 1440;
                    sunCalc.SS = sunCalc.SS - 1440;
                }
            }


        }
        //show me values / development and debugging
        //Debug.Print Teller & ": PosTime " & PosTime & " - SR " & SunRise.SR & " - SS " & SunRise.SS & " - Factor " & Factor
    }
}
