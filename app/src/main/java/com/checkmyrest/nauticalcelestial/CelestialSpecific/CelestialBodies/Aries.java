package com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;

import java.util.Calendar;

public class Aries {

    private Calendar date;
    private final double DAYS_CONSTANT = 360.98564736629;
    private final double ARIES_CONSTANT = 280.46061837;

    public Aries(Calendar iDate) {
        date = iDate;
    }

    public double GrenwichHourAngle() {
        return Constants.Mod(360 - AriesRa() - GhaCorrection(), 360);
    }

    public double LocalHourAngle(LatLonLocation position) {
        return LocalHourAngle(position.getLongitude());
    }

    public double LocalHourAngle (Longitude longitude) {
        double lha;
        if ( longitude.getLonEW().equals("E") ) {lha = GrenwichHourAngle() + longitude.getLon();} else {lha = GrenwichHourAngle() - longitude.getLon();}
        return Constants.CorrectTo360(lha);
    }

    private double AriesDayCount() {
        double h = date.get(Calendar.HOUR_OF_DAY);
        double m = ( (double) date.get(Calendar.MINUTE) ) / 60;
        double s = ( (double) date.get(Calendar.SECOND) ) / 3600;
        double dec = ( h + m + s ) / 24;
        return Constants.DaysSince2000(date) - 0.5 + dec;
    }

    private double AriesRa() {
        return 360 - Constants.Mod(ARIES_CONSTANT + ( DAYS_CONSTANT * AriesDayCount() ) , 360);
    }

    private double GhaCorrection() {
        double t = AriesDayCount() / 36525;
        double Om = Constants.Mod( 125.04452 - (1934.136261 * t) ,360);
        double L = Constants.Mod( 280.4665 + (36000.7698 * t), 360);
        double L1 = Constants.Mod( 218.3165 + (481267.8813 * t), 360);
        double e = Constants.Mod( 23.4393 - (0.0000003563 * AriesDayCount()), 360);
        double dp = - ( (17.2 * Math.sin(Math.toRadians(Om)) ) - (1.32 * Math.sin(Math.toRadians(2 * L)) ) - (0.23 * Math.sin(Math.toRadians(2 * L1)) ) + (0.21 * Math.sin(Math.toRadians(2 * Om)) ) );
        double de =     ( 9.2 * Math.cos(Math.toRadians(Om)) ) + (0.57 * Math.cos(Math.toRadians(2 * L)) ) + (0.1  * Math.cos(Math.toRadians(2 * L1)) ) - (0.09 * Math.cos(Math.toRadians(2 * Om)) )  ;
        return dp * Math.cos(Math.toRadians(e + de)) /3600;
    }

}
