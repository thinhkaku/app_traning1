package aero.pilotlog.common;

/**
 * Created by tuan.na on 7/29/2015.
 * Class for airfield navigation
 */
public class Navigation {
    public int latitudeA;
    public int longitudeA;

    public double loxoDist;
    public double loxoTrack;
    public int latitudeB;
    public int longitudeB;

    public int orthoDist;

    private double latA, longA, latB, longB;

    private static final double DBL_EPSILON = 1.19209290e-07;

    private static boolean DOUBLE_EQ(double x, double v) {
        return (((v - DBL_EPSILON) < x) && (x < (v + DBL_EPSILON)));
    }

    //public methods
    public boolean calculate() {
        this.loxoDist = 0;
        this.loxoTrack = 0;
        this.orthoDist = 0;

        if (((this.latitudeA == 0) && (this.longitudeA == 0)) || ((this.latitudeB == 0) && (this.longitudeB == 0))) {
            //Coordinates are missing
            return false;
        } else if ((this.latitudeA == this.latitudeB) && (this.longitudeA == this.longitudeB)) {
            //From/to same airfield
            return false;
        }

        //convert coordinates to radians
        latA = convertDEGToDEC(this.latitudeA) * Math.PI / 180.0;
        longA = convertDEGToDEC(this.longitudeA) * Math.PI / 180.0;
        latB = convertDEGToDEC(this.latitudeB) * Math.PI / 180.0;
        longB = convertDEGToDEC(this.longitudeB) * Math.PI / 180.0;

        calcloxoDist();
        calcorthoDist();

        return true;
    }

    public void goToPos() {
        double calc;

        latA = convertDEGToDEC(this.latitudeA) * Math.PI / 180.0;
        longA = convertDEGToDEC(this.longitudeA) * Math.PI / 180.0;
        latB = 0;
        longB = 0;

        if (DOUBLE_EQ(this.loxoDist, 0.0)) {
            this.latitudeB = this.latitudeA;
            this.longitudeB = this.longitudeA;
        }

        calc = Math.acos(Math.cos(this.loxoTrack * Math.PI / 180.0));
        if (calc > (Math.PI / 2.0))
            calc = Math.PI - calc;

        latB = latA + ((this.loxoDist / 60.0 * Math.cos(calc) * sgn(Math.cos(this.loxoTrack * Math.PI / 180.0))) * Math.PI / 180.0);
        if (DOUBLE_EQ((Math.cos(calc)), 0)) { //Cos(Calc) = 0 Then
            longB = longA - (loxoDist * Math.sin(loxoTrack * Math.PI / 180) / Math.cos(latA) / 60);
        } else {
//            longB = longA - fabs(p1 - p2) * p3 * p4;
            longB = longA - ((float) Math.abs(Math.log(Math.tan(latB / 2.0 + (45.0 * Math.PI / 180.0))) - Math.log(Math.tan(latA / 2.0 + (45.0 * Math.PI / 180.0)))))
                    * Math.tan(calc) * sgn(Math.sin(this.loxoTrack * Math.PI / 180.0));
            if (Math.abs(longB) > Math.PI)
                longB = -sgn(longB) * ((2.0 * Math.PI) - (float) Math.abs(longB));
        }

        this.latitudeB = convertDECToDEG(latB * 180.0 / Math.PI);
        this.longitudeB = convertDECToDEG(longB * 180.0 / Math.PI);
    }


    //private methods
    private void calcloxoDist() {
        //Calculates Rhumb Line Distance between A and B in NM
        //Latitude and Longitude should be given in format 52436 for 52° 43' 24"

        double Calc;

        Calc = longA - longB;
        if (Math.abs(Calc) > Math.PI)
            Calc = Calc - sgn(Calc) * 2.0 * Math.PI;

        if (DOUBLE_EQ(latA, latB)) { //(latA == latB)
            this.loxoTrack = 180.0 - 90.0 * sgn(Calc);
            this.loxoDist = ((float) Math.abs(Calc)) * 60.0 * Math.cos(latA);
        } else {
            //SouthPole
            if (DOUBLE_EQ(Math.abs(latB), (Math.PI / 2.0))) { //  (abs(latB) == (M_PI / 2.0)) 
                Calc = -Math.atan(Calc / Math.log(Math.tan(latA / 2.0 + (45.0 * Math.PI / 180.0)) / 0.0000000001));
            } else if (DOUBLE_EQ(Math.abs(latA), (Math.PI / 2.0))) { //(abs(latA) == (M_PI / 2.0))
                Calc = -Math.atan(Calc / Math.log(0.0000000001 / Math.tan(latB / 2.0 + (45.0 * Math.PI / 180.0))));
            } else {
                Calc = -Math.atan(Calc / Math.log(Math.tan(latA / 2.0 + (45.0 * Math.PI / 180.0)) / Math.tan(latB / 2.0 + (45.0 * Math.PI / 180.0))));
            }

            if (latA > latB)
                Calc = Math.PI + Calc;

            if (Calc < 0)
                Calc = Calc + (2.0 * Math.PI);

            this.loxoTrack = Calc * 180.0 / Math.PI;
            this.loxoDist = 60.0 * (latB - latA) / Math.cos(Calc) * 180.0 / Math.PI;
        }
    }

    private void calcorthoDist() {
        double calc;

        if ((DOUBLE_EQ(latA, latB)) && (DOUBLE_EQ(longA, longB))) {
            this.orthoDist = 0;
        } else {
            calc = (Math.sin(latA) * Math.sin(latB)) + (Math.cos(latA) * Math.cos(latB) * Math.cos(longA - longB));
            this.orthoDist = (int) Math.round(60.0 * Math.acos(calc) * 180.0 / Math.PI);
        }

    }

    private int convertDECToDEG(double coord) {
        //Convert Decimal Degrees to Degrees-Min-Seconds
        //e.g.  52.723611 --> 52° 43' 24" --> 52434

        int p1 = sgn(coord);
        int p3 = (int) Math.abs(coord);
        int p2 = p3 * 1000;

        return p1 * p2 + p1 * (int) Math.round((((float) Math.abs(coord)) - p3) * 600.0);
    }

    private double convertDEGToDEC(int coord) {
        //Convert Degrees-Min-Seconds to Decimal Degrees
        //e.g.  52° 43' 24" --> 52.723333
        //Coord should be in format 52434 !

        int p1 = sgn(coord);
        int p2 = Math.abs(coord % 1000);
        int p3 = Math.abs(coord / 1000);
        return (p1 * p3) + (p1 * p2) / 600.0;
    }

    private int sgn(double number) {
        if (number < 0)
            return -1;
        else if (number > 0)
            return 1;
        else
            return 0;
    }
}
