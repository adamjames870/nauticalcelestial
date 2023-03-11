package com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;
import com.checkmyrest.nauticalcelestial.CommonNavItems.CompassError;
import com.checkmyrest.nauticalcelestial.CommonNavItems.MagAtion;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class CompassObservation implements Parcelable{

    private static String BUNDLE_GYRO_BRG = "GyroBearing";
    private static String BUNDLE_MAGN_BRG = "MagneticBearing";
    private static String BUNDLE_VAR_MAG = "VariationMagnitude";
    private static String BUNDLE_VAR_DIR = "VariationDirection";
    private static String BUNDLE_CELESTIAL_POSITION = "ObservationCelestialPosition";

    private static NumberFormat NF = new DecimalFormat("000.0");

    private Quadrantal gyroBrg;
    private Quadrantal magBearing;
    private MagAtion variation;
    private Quadrantal trueBearing;
    private CelestialPosition celestialPosition;

    public CompassObservation(CelestialPosition iCelestialPosition, Quadrantal iGyroBrg, Quadrantal iMagBrg, MagAtion iVariation) {
        celestialPosition = iCelestialPosition;
        gyroBrg = iGyroBrg;
        magBearing = iMagBrg;
        variation = iVariation;
    }

    private CompassObservation(Parcel in) {
        Bundle bundle = in.readBundle();
        celestialPosition = new CelestialPosition(bundle.getBundle(BUNDLE_CELESTIAL_POSITION));
        gyroBrg = new Quadrantal(bundle.getDouble(BUNDLE_GYRO_BRG));
        magBearing = new Quadrantal(bundle.getDouble(BUNDLE_MAGN_BRG));
        variation = new MagAtion(MagAtion.VARIATION, bundle.getDouble(BUNDLE_VAR_MAG), bundle.getInt(BUNDLE_VAR_DIR));
    }

    @Override
    public String toString() {
        return null;
    }

    public Quadrantal getGyroBrg() {return gyroBrg;}
    public Quadrantal getMagBearing() {return magBearing;}
    public MagAtion getVariation() {return variation;}
    public Quadrantal getTrueBearing() {return trueBearing;}
    public void setTrueBearing(Quadrantal iTrueBearing) {trueBearing = iTrueBearing;}
    public void setTrueBearing(CelestialPosition celestialPosition) {trueBearing = celestialPosition.Azimuth();}

    public MagAtion getGyroError() {return new MagAtion(MagAtion.GYRO_ERROR, trueBearing, gyroBrg);}
    public MagAtion getCompassError() {return compErr().getCompassError();}
    public MagAtion getDeviation() {return compErr().getDeviation();}
    public CelestialPosition getCelestialPosition() {return celestialPosition;}

    private CompassError compErr() {return new CompassError(variation, trueBearing, magBearing);}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        Bundle bundle = new Bundle();
        bundle.putDouble(BUNDLE_GYRO_BRG, gyroBrg.getTrueCourse());
        bundle.putDouble(BUNDLE_MAGN_BRG, magBearing.getTrueCourse());
        bundle.putDouble(BUNDLE_VAR_MAG, variation.getMagAtion());
        bundle.putInt(BUNDLE_VAR_DIR, variation.getDirMagAtion());
        bundle.putBundle(BUNDLE_CELESTIAL_POSITION, celestialPosition.GetMyBundle());
        dest.writeBundle(bundle);

    }

    public static final Parcelable.Creator<CompassObservation> CREATOR
            = new Parcelable.Creator<CompassObservation>() {
        public CompassObservation createFromParcel(Parcel in) {
            return new CompassObservation(in);
        }
        public CompassObservation[] newArray(int size) {
            return new CompassObservation[size];
        }
    };

}
