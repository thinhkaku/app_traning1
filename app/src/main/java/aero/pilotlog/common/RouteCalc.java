package aero.pilotlog.common;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class RouteCalc {
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

    public class RouteType {
        public Date Date;
        public int DepHour;
        public int ArrHour;
        float LoxoDist;
        float LoxoTrack;
        float OrthoDist;
        public int TODay;
        public int LdgDay;
        public int TONight;
        public int LdgNight;
        public int FlightTime;
        public int NightTime;
    }

    public RouteType Route;

    //  struct RouteType Route;

    public AirfieldType Airfield1;

    public AirfieldType Airfield2;
    Navigation Nav;


    double Factor = 0;
    public int NightMin;
    //	boolean SSafterMN;
    long PosTimePrev;
    long PosTime;

//	private Sunrise Sun;

    private SunCalc sunCalc;

    public RouteCalc() {
        NightMin = 0;
        Route = new RouteType();
        Airfield1 = new AirfieldType();
        Airfield2 = new AirfieldType();
        Nav = new Navigation();
        //   Sun = new Sunrise();
        sunCalc = new SunCalc();
    }

    public boolean CalculateNight() {
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

        boolean result = false;
        //init structure
        Route.TODay = 0;
        Route.LdgDay = 0;
        Route.TONight = 0;
        Route.LdgNight = 0;
        Route.NightTime = 0;

        Calendar cl = new GregorianCalendar();
        cl.setTime(this.Route.Date);
        this.Airfield1.sunCalc.PosDate = cl;
        if (!this.Airfield1.sunCalc.CalculateSun(null)) {
            return result;
        }
        this.Airfield1.sunCalc.SR = this.Airfield1.sunCalc.SR - NightMin;
        this.Airfield1.sunCalc.SS = this.Airfield1.sunCalc.SS + NightMin;

        this.Airfield2.sunCalc.PosDate = cl;
        if (!this.Airfield2.sunCalc.CalculateSun(null)) {
            return result;
        }

        Airfield2.sunCalc.SR = Airfield2.sunCalc.SR - NightMin;
        Airfield2.sunCalc.SS = Airfield2.sunCalc.SS + NightMin;

        result = true;
        //
        //Work in a time window  0 to 1440 (Today)  to 2880 (Tomorrow)
        if (Route.ArrHour < Route.DepHour) {
            Route.ArrHour = Route.ArrHour + 1440;
            Route.Date = new Date(Route.Date.getTime() + 1 * 24 * 60 * 60 * 1000);
            cl.setTime(this.Route.Date);
            Airfield2.sunCalc.PosDate = cl;
            Airfield2.sunCalc.CalculateSun(null);
            Airfield2.sunCalc.SR = Airfield2.sunCalc.SR - NightMin;
            Airfield2.sunCalc.SS = Airfield2.sunCalc.SS + NightMin;
        }
        Route.FlightTime = (Route.ArrHour - Route.DepHour) % 1440;

        //'Match SR/SS with flight window
        if (Airfield1.sunCalc.PolarDayNight == 0) {
            if ((Math.abs(Airfield1.sunCalc.SR - Route.DepHour) >= 1440) || (Math.abs(Airfield1.sunCalc.SS - Route.DepHour) >= 1440)) {
                Airfield1.sunCalc.SR = Airfield1.sunCalc.SR + ((Route.DepHour > Airfield1.sunCalc.SR) ? 1440 : -1440);
                Airfield1.sunCalc.SS = Airfield1.sunCalc.SS + ((Route.DepHour > Airfield1.sunCalc.SS) ? 1440 : -1440);
            }
        }
        if (Airfield2.sunCalc.PolarDayNight == 0) {
            if ((Math.abs(Airfield2.sunCalc.SR - Route.ArrHour) >= 1440) || (Math.abs(Airfield2.sunCalc.SS - Route.ArrHour) >= 1440)) {
                Airfield2.sunCalc.SR = Airfield2.sunCalc.SR + ((Route.ArrHour > Airfield2.sunCalc.SR) ? 1440 : -1440);
                Airfield2.sunCalc.SS = Airfield2.sunCalc.SS + ((Route.ArrHour > Airfield2.sunCalc.SS) ? 1440 : -1440);
            }
        }

        //Calculate Rhumb Line Distance and Track
        Nav.latitudeA = Airfield1.Latitude;
        Nav.longitudeA = Airfield1.Longitude;
        Nav.latitudeB = Airfield2.Latitude;
        Nav.longitudeB = Airfield2.Longitude;
        Nav.calculate();

        // Route.OrthoDist = Nav.OrthoDist;
        Route.LoxoDist = (float) Nav.loxoDist;
        Route.LoxoTrack = (float) Nav.loxoTrack;

        //Get Mode
        if ((Route.DepHour >= Airfield1.sunCalc.SR && Route.DepHour < Airfield1.sunCalc.SS) || Airfield1.sunCalc.PolarDayNight == 1) {
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
                    if (Airfield1.sunCalc.PolarDayNight > 0 || Airfield2.sunCalc.PolarDayNight > 0 || (Airfield2.Longitude < Airfield1.Longitude && Route.FlightTime > 240) || Route.ArrHour > Airfield2.sunCalc.SS) {
                        Factor = 0.5;
                    }
                    break;
                case 2: //Takeoff Night
                    if (Airfield1.sunCalc.PolarDayNight > 0 || Airfield2.sunCalc.PolarDayNight > 0 || (Airfield2.Longitude < Airfield1.Longitude && Route.FlightTime > 240) || (Route.ArrHour > Airfield2.sunCalc.SR && Route.ArrHour < Airfield2.sunCalc.SS)) {
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
                if (((Airfield1.sunCalc.SR + 1440) % 1440) > ((Airfield1.sunCalc.SS + 1440) % 1440) ||
                        ((Airfield2.sunCalc.SR + 1440) % 1440) > ((Airfield2.sunCalc.SS + 1440) % 1440))
                    Factor = 0.5;

            //avoid crash
            if (Factor < 0 || Factor > 1) {
                Factor = 0.5;
            }

            //Flight is full Day or full Night when factor is 1
            if (Factor == 1) {
                if (Mode == 1) {
                    Route.NightTime = 0; //full Day
                } else {
                    Route.NightTime = Route.FlightTime; //full Night
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
                getData(Mode);

                switch (Mode) {
                    case 1:
                    case 4:
                    case 5:
                        //Takeoff Day
                        if (Math.abs(PosTime - sunCalc.SS) < 3 || Teller == 20 || PosTime == PrevPosTime) {
                            if (Math.abs(PosTime - sunCalc.SS) < 3) {
                                //Position is meeting SS (changeover from day to night)
                                PosTime = (PosTime + sunCalc.SS) / 2;
                            } else {
                                //Flight (or remainder of flight - CalculatePart2) is Night
                            }
                            exitDo = true;
                        }
                        if (!exitDo) {
                            //move forward / backward along the route
                            if (PosTime >= sunCalc.SR && PosTime < sunCalc.SS) {
                                //still in daylight, move further forward
                                Factor = Factor + PrevFactor;
                            } else {
                                //now in nightlight, move backward
                                Factor = Factor - PrevFactor;
                            }
                        }
                        break;
                    case 2:
                    case 3:
                    case 6:
                        //Takeoff Night
                        if (Math.abs(PosTime - sunCalc.SR) < 3 || Teller == 20 || PosTime == PrevPosTime) {
                            if (Math.abs(PosTime - sunCalc.SR) < 3) {
                                //Position is meeting SR (changeover from night to day)
                                PosTime = (PosTime + sunCalc.SR) / 2;
                            } else {
                                //Flight (or remainder of flight - CalculatePart2) is Day
                            }
                            exitDo = true;
                        }
                        if (!exitDo) {
                            //move forward / backward along the route
                            if (!(PosTime >= sunCalc.SR && PosTime < sunCalc.SS)) {
                                //still in nightlight, move further forward
                                Factor = Factor + PrevFactor;
                            } else {
                                //now in daylight, move backward
                                Factor = Factor - PrevFactor;
                            }
                        }
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
                PosTime = Route.DepHour;
                Factor = 0;
            } else if (Factor > 0.995) {
                PosTime = Route.ArrHour;
                Factor = 1;
            }
            //Night Time
            switch (Mode) {
                case 1: //Takeoff Day, last part of flight is Night
                    Route.NightTime = (int) (Route.ArrHour - PosTime);
                    break;
                case 2: //Takeoff Night, last part of flight is Day
                    Route.NightTime = (int) (PosTime - Route.DepHour);
                    break;
                case 3: //Takeoff Day, Landing Day, Part of flight passing through Night
                    Route.NightTime = (int) (PosTime - PosTimePart1);
                    break;
                case 4: //Takeoff Night, Landing Night, Part of fligt passing through Day
                    Route.NightTime = (int) ((PosTimePart1 - Route.DepHour) + (Route.ArrHour - PosTime));
                    break;
                case 5: //'Takeoff Day, flight spans a full night and a full day, Landing Night
                    Route.NightTime = (int) (Route.NightTime + (Route.ArrHour - PosTime));
                    break;
                case 6: //'Takeoff Night, flight spans a full day and a full night, Landing Day
                    Route.NightTime = (int) (Route.NightTime - (Route.ArrHour - PosTime));
                    break;
            }
            //Scale Nighttime to range 0 - 1440
            Route.NightTime = (Route.NightTime + 2880) % 1440;
            //Small corrections
            if (Route.NightTime >= Route.FlightTime) {
                Route.NightTime = Route.FlightTime; //Limit night time to flight time
            } else if (Route.NightTime < 3) {
                Route.NightTime = 0; //Delete 1 or 2 minutes night time due to roundings
            }
            //Get Mode for CalculatePart2, if applicable
            switch (Mode) {
                case 1: //Takeoff Day
                    if ((Route.ArrHour >= Airfield2.sunCalc.SR && Route.ArrHour < Airfield2.sunCalc.SS) || Airfield2.sunCalc.PolarDayNight == 1
                            || (Route.FlightTime > 720 && Route.LoxoTrack < 180)) {
                        //Landing is also Day
                        if (Route.NightTime > 0) {
                            //part of the flight is passing through night
                            Mode = 3;
                            PosTimePart1 = PosTime;
                            toContinue = true; //GoTo Deel2
                            //GoTo CalculatePart2; //go back for a second iteration
                        }
                    }
                    break;
                case 2: //Takeoff Night
                    if (!((Route.ArrHour >= Airfield2.sunCalc.SR && Route.ArrHour < Airfield2.sunCalc.SS) || Airfield2.sunCalc.PolarDayNight == 1)
                            || (Route.FlightTime > 720 && Route.LoxoTrack < 180)) {
                        //Landing is also Night
                        if (Route.NightTime < Route.FlightTime) {
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
                    if (Route.FlightTime > 720 && Route.LoxoTrack < 180) {
                        if (PosTime < Route.ArrHour) {
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
                    if (PosTime == Route.ArrHour)
                        Mode = Mode - 2;
                    break;
            }
        } while (toContinue);
        GetTOLDG(Mode);

        return result;
    }

    private void GetTOLDG(int Mode) {
        Route.TODay = 0;
        Route.LdgDay = 0;
        Route.TONight = 0;
        Route.LdgNight = 0;
        //GetTOLDG
        if (Route.NightTime == 0 || Mode == 3) {
            Route.TODay = 1;
            Route.LdgDay = 1;
        } else if (Route.NightTime == Route.FlightTime || Mode == 4) {
            Route.TONight = 1;
            Route.LdgNight = 1;
        } else if (Route.NightTime > 0 && (Mode == 1 || Mode == 5)) {
            Route.LdgNight = 1;
            Route.TODay = 1;
        } else if (Route.NightTime > 0 && (Mode == 2 || Mode == 6)) {
            Route.TONight = 1;
            Route.LdgDay = 1;
        }
        //'Reset values back to window 0 - 1440
        Route.DepHour = (Route.DepHour + 1440) % 1440;
        Route.ArrHour = (Route.ArrHour + 1440) % 1440;
    }

    private void getData(int Mode) {
        Nav.latitudeA = Airfield1.Latitude;
        Nav.longitudeA = Airfield1.Longitude;
        Nav.loxoDist = Route.LoxoDist * Factor;
        Nav.loxoTrack = Route.LoxoTrack;
        Nav.goToPos();


        Calendar cl = new GregorianCalendar();

        cl.setTime(this.Route.Date);
        this.Airfield1.sunCalc.PosDate = cl;

        this.sunCalc.PosDate = cl;
        this.sunCalc.PosLatitude = Nav.latitudeB;
        this.sunCalc.PosLongitude = Nav.longitudeB;
        this.sunCalc.CalculateSun(null);
        this.sunCalc.SR = this.sunCalc.SR - NightMin;
        this.sunCalc.SS = this.sunCalc.SS + NightMin;
        /*
		    if (SSafterMN) {
		        this.Sun.SS = this.Sun.SS - 1440;
		    }
		    
		    PosTime = this.Route.DepHour + (int) Math.round((this.Route.FlightTime * Factor));
		*/

        PosTime = (long) (Route.DepHour + (Route.FlightTime * Factor));
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


    public int sgn(double number) {
        if (number < 0) {
            return -1;
        } else if (number > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
