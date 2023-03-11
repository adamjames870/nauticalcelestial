package com.checkmyrest.nauticalcelestial.CommonNavItems;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

final public class Constants {

    public static final double MIN_LAT = 0;
    public static final double MAX_LAT = 90;
    public static final double MIN_LON = 0;
    public static final double MAX_LON = 180;

    public static final String INTENT_EXTRA_CELESTIAL_POSITION = "com.checkmyrest.nauticalcelestial.CELESTIAL_POSITION";
    public static final String INTENT_EXTRA_OBSERVER_POSITION = "com.checkmyrest.nauticalcelestial.OBSERVER_POSITION";
    public static final String INTENT_EXTRA_CELESTIAL_BODY = "com.checkmyrest.nauticalcelestial.CELESTIAL_POSITION";
    public static final String INTENT_EXTRA_OBSVERVATION = "com.checkmyrest.nauticalcelestial.OBSERVATION";
    public static final String INTENT_RECORD_FILENAME = "come.checkmyrest.nauticalcelestial.RECORD_FILENAME";

    public static final int BEARING_TRUE = 0;
    public static final int BEARING_RELATIVE = 1;

    public static final int DISPLAY_LEADING_ZERO_NONE = 0;
    public static final int DISPLAY_LEADING_ZERO_TWO_ZEROS = 1;
    public static final int DISPLAY_LEADING_ZERO_THREE_ZEROS = 2;

    private static double SPHEROID_ECCENT_WGS84 = 0.08181919034;
    private static double SPHEROID_ECCENT_CLARKE1884 = 0.08248339919132311;

    public enum WaypointPositions {WP_FROM_A, WP_TO_B, WP_AROUND_V, WP_ON_MERIDIANS}

    private Constants() {
    }

    private static double SPHEROID_ECCENT() {return SPHEROID_ECCENT_CLARKE1884;}

    public static double MeridinalPart (double latitude) {

        //http://www.globmaritime.com/forum/Marine-Navigation/411-formula-for-meridional-parts-calculation

        //A = 2700 / arctan(1)
        double a = 2700 / Math.atan(1);

        //M = log10(e)
        double m = Math.log10(Math.exp(1));

        //MP = (A/M) . log10( Tan(45 Deg + Lat/2).( ( (1-Eccent.Sin(Lat))/(1+Eccent.Sin(Lat) )^(Eccent/2))
        return (a/m) * Math.log10(
                Math.tan(Math.toRadians(45 + latitude / 2)) *
                        Math.pow((1 - SPHEROID_ECCENT() * Math.sin(Math.toRadians(latitude))) /
                                        (1 + SPHEROID_ECCENT() * Math.sin(Math.toRadians(latitude))),
                                (SPHEROID_ECCENT() / 2)));

    }

    public static double ReverseMeridinalPart(double mp) {

        // http://mymathforum.com/new-users/7484-meridional-parts.html
        // Lat = 2 * ( ( ATAN( 10^(Meridional Part/7929.915) ) ) - 45 )

        double lat;
        lat = Math.toDegrees(Math.atan(Math.pow(10, mp / 7929.915)));
        lat = 2 * (lat - 45);

        return lat;

    }

    public static double Ceiling(double value, double ceiling) {
        return ceiling * Math.ceil(value / ceiling);
    }

    public static double Floor(double value, double floor) {
        return floor * Math.floor(value / floor);
    }

    public static String getOppositeNS(String iNS) {
        if (iNS.equals("N")) {
            return "S";
        } else {
            return "N";
        }
    }

    public static String getOppositeEW(String iEW) {
        if (iEW.equals("W")) {
            return "E";
        } else {
            return "W";
        }
    }

    public static double CorrectTo360(double i) {
        if (i < 0) {return i + 360;}
        else if (i > 360) {return i - 360;}
        else {return i;}
    }

    public static double Mod(double value, double divisor) {
        return value % divisor;
    }

    public static double SinD(double x) {return Math.toDegrees(Math.sin(Math.toRadians(x)));}
    public static double CosD(double x) {return Math.toDegrees(Math.cos(Math.toRadians(x)));}
    public static double TanD(double x) {return Math.toDegrees(Math.tan(Math.toRadians(x)));}

    public static Calendar ExcelZeroCalendar() {
        Calendar ZeroCalendar = Calendar.getInstance();
        ZeroCalendar.set(1900, 1, 0);
        return ZeroCalendar;
    }

    public static int DaysDifference(Calendar from, Calendar to) {
        int diffYear = Math.abs(to.get(Calendar.YEAR) - from.get(Calendar.YEAR));
        int extraDays = to.get(Calendar.DAY_OF_YEAR) - from.get(Calendar.DAY_OF_YEAR);
        return (int) Floor( (365.25 * diffYear) + extraDays, 1 ) + 1;
    }

    public static int DaysSince2000(Calendar calendar) {
        Calendar yr2000Calendar = Calendar.getInstance();
        yr2000Calendar.set(2000, 1, 1);
        return Constants.DaysDifference(yr2000Calendar, calendar);
    }

    public static String toSexisegimal(double x) {
        NumberFormat nfDeg = nfDeg();
        nfDeg.setRoundingMode(RoundingMode.DOWN);
        return nfDeg.format(x) + "\u00B0" + nfMin().format((x % 1) * 60) + "'";
    }

    public static String toSexisegimal(Latitude latitude) {
        return toSexisegimal(latitude.getLat());
    }

    public static String toSexisegimal(Longitude longitude) {
        NumberFormat nfDeg = nfLonDeg();
        nfDeg.setRoundingMode(RoundingMode.DOWN);
        return nfDeg.format(longitude.getLon()) + "\u00B0" + nfMin().format( (longitude.getLon() % 1) * 60 ) + "'";
    }

    public static String ParseLocation(LatLonLocation latLonLocation) {
        return "Lat " + toSexisegimal(latLonLocation.getLatitude()) + latLonLocation.getLatitude().getLatNS()
                + " Lon " + toSexisegimal(latLonLocation.getLongitude()) + latLonLocation.getLongitude().getLonEW();
    }

    public static double getDegs(double x) {
        NumberFormat nfDeg = nfDeg();
        nfDeg.setRoundingMode(RoundingMode.DOWN);
        return Double.parseDouble(nfDeg.format(x));
    }

    public static double getDegs(Latitude latitude) {return getDegs(latitude.getLat());}
    public static double getDegs(Longitude longitude) {return getDegs(longitude.getLon());}

    public static double getMins(double x) {
        return Double.parseDouble(nfMin().format((x % 1) * 60));
    }

    public static double getMins(Latitude latitude) {return getMins(latitude.getLat());}
    public static double getMins(Longitude longitude) {return getMins(longitude.getLon());}

    public static NumberFormat nfDeg() {return new DecimalFormat("00");}
    public static NumberFormat nfLonDeg() {return new DecimalFormat("000");}
    public static NumberFormat nfMin() {return new DecimalFormat("00.0");}
    public static NumberFormat nfBrg() {return new DecimalFormat("000.0");}

    public static double Nz(String x) {
        if (x.equals("")) {return 0;} else {return Double.parseDouble(x);}
    }

    private static String ParseCalendarMonth(int month) {
        switch (month - 1) {
            case Calendar.JANUARY:
                return "Jan";
            case Calendar.FEBRUARY:
                return "Feb";
            case Calendar.MARCH:
                return "Mar";
            case Calendar.APRIL:
                return "Apr";
            case Calendar.MAY:
                return "May";
            case Calendar.JUNE:
                return "Jun";
            case Calendar.JULY:
                return "Jul";
            case Calendar.AUGUST:
                return "Aug";
            case Calendar.SEPTEMBER:
                return "Sep";
            case Calendar.OCTOBER:
                return "Oct";
            case Calendar.NOVEMBER:
                return "Nov";
            case Calendar.DECEMBER:
                return "Dec";
        }
        return null;
    }

    public static String parseTime(Calendar t) {
        String s;
        s = Constants.ParseCalendarMonth(t.get(Calendar.MONTH));
        s += " " + nfDeg().format(t.get(Calendar.DAY_OF_MONTH));
        s += " / " + nfDeg().format(t.get(Calendar.HOUR_OF_DAY));
        s += ":" + nfDeg().format(t.get(Calendar.MINUTE));
        s += ":" + nfDeg().format(t.get(Calendar.SECOND));
        return s;
    }





}
