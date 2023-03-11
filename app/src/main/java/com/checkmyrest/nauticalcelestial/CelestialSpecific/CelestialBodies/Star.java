package com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Declination;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.util.Calendar;

public class Star extends CelestialBody {

    public double ra2000;
    public Declination dec2000;

    public Star(int iIndexNo, String iName, double iSiderealHourAngle, Declination iDeclination) {
        super(iName, iIndexNo);
        ra2000 = iSiderealHourAngle;
        dec2000 = iDeclination;
    }

    private double RawSiderealHourAngle(Calendar calendar) {
        double sha;
        sha = ( 1.336 * Math.sin(Math.toRadians(ra2000)) * Math.tan(Math.toRadians(dec2000.getDec())) ) + 3.075;
        sha = sha * Constants.DaysSince2000(calendar) / 365;
        sha = sha / 3600;
        sha = 360 - ( ( sha + ra2000 ) * 15 );
        return Constants.Mod(sha, 360);
    }

    private Declination RawDeclination(Calendar calendar) {
        double decAdjustment;
        decAdjustment = Math.cos(Math.toRadians(15 * ra2000));
        decAdjustment = decAdjustment * Constants.DaysSince2000(calendar);
        decAdjustment = 20.04 * decAdjustment / 365;
        decAdjustment = decAdjustment / 3600;
        if (dec2000.getDec() + decAdjustment < 0) {
            return new Declination(Math.abs(dec2000.getDec() + decAdjustment), "S");
        } else{
            return new Declination(dec2000.getDec() + decAdjustment, "N");
        }
     }

    public double StarSiderealHourAngle(Calendar calendar) {
        return RawSiderealHourAngle(calendar) + SiderealHourAngleAdjustment(calendar);
    }

    private double SiderealHourAngleAdjustment(Calendar calendar) {
        double adjDE, adjDL;
        adjDE = Math.cos(Math.toRadians(ra2000 * 15));
        adjDE = adjDE * Math.tan(Math.toRadians(dec2000.getDec()));
        adjDE = adjDE * CelestialBodies.NutDe(calendar);
        adjDL = Math.tan(Math.toRadians(dec2000.getDec())) * Math.toRadians(ra2000 * 15);
        adjDL = 0.3978 * Math.sin(adjDL) * CelestialBodies.NutDl(calendar);
        return ( ( adjDL - adjDE ) + 0.9175 ) / 3600;
    }

    private double DeclinationAdjustment(Calendar calendar) {
        double adjDE, adjDL;
        adjDE = Math.sin(Math.toRadians(ra2000)) * CelestialBodies.NutDe(calendar);
        adjDL = Math.cos( Math.toRadians(ra2000 * 15) );
        adjDL = 0.3978 * adjDL * CelestialBodies.NutDl(calendar);
        return ( adjDE + adjDL ) / 3600;
    }


    @Override
    public Double LocalHourAngle(ObserverPosition observerPosition) {
        Aries aries = new Aries(observerPosition.getObservationTime());
        return Constants.CorrectTo360(aries.LocalHourAngle(observerPosition.getPosition()) + StarSiderealHourAngle(observerPosition.getObservationTime()));
    }

    @Override
    public Declination Declination(ObserverPosition observerPosition) {
        if (RawDeclination(observerPosition.getObservationTime()).getDec() + DeclinationAdjustment(observerPosition.getObservationTime()) < 0) {
            return new Declination(Math.abs(RawDeclination(observerPosition.getObservationTime()).getDec() + DeclinationAdjustment(observerPosition.getObservationTime())), "S");
        } else {
            return new Declination(RawDeclination(observerPosition.getObservationTime()).getDec() + DeclinationAdjustment(observerPosition.getObservationTime()), "N");
        }
    }
}
