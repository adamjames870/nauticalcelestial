package com.checkmyrest.nauticalcelestial.CelestialSpecific;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBodies;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBody;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.SightReduction;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.util.Calendar;

public class CelestialPosition implements Parcelable{

    public static String BUNDLE_BODY_INDEX = "StarIndex";

    private CelestialBody celestialBody;
    private ObserverPosition observerPosition;
    private boolean observed = false;

    public CelestialPosition(CelestialBody iCelestialBody, ObserverPosition iObserverPosition) {
        celestialBody = iCelestialBody;
        observerPosition = iObserverPosition;
    }

    public ObserverPosition getObserverPosition () {return observerPosition;}
    public CelestialBody getCelestialBody () {return celestialBody;}

    public CelestialPosition AdvanceObserverPosition(Calendar newLocalObservationTime){
        return new CelestialPosition(celestialBody, observerPosition.AdvancePosition(newLocalObservationTime));
    }

    public Quadrantal Azimuth() {
        if (observerPosition.getBearingType() == Constants.BEARING_RELATIVE) {
            return new Quadrantal(Constants.CorrectTo360(rawAzimuth().getTrueCourse() - observerPosition.getCourse().getTrueCourse()));
        } else {
            return rawAzimuth();
        }
    }

    private Quadrantal rawAzimuth() {
        return ( new SightReduction(celestialBody.LocalHourAngle(observerPosition), celestialBody.Declination(observerPosition), observerPosition.getPosition()) ).Azimuth();
    }

    public Double Altitude() {
        return ( new SightReduction(celestialBody.LocalHourAngle(observerPosition), celestialBody.Declination(observerPosition), observerPosition.getPosition()) ).CalculatedAltitude();
    }

    public void setObserved() {observed = true;}

    public String toString() {
        String s = "";
        if (observed) s = "** ";
        s += celestialBody.getName() + "   Az " + Azimuth().toString() + "   Alt " + Constants.toSexisegimal(Altitude());
        return s;
    }

    public static final Parcelable.Creator<CelestialPosition> CREATOR
            = new Parcelable.Creator<CelestialPosition>() {
        public CelestialPosition createFromParcel(Parcel in) {
            return new CelestialPosition(in.readBundle());
        }

        public CelestialPosition[] newArray(int size) {
            return new CelestialPosition[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(GetMyBundle());
    }

    public CelestialPosition(Bundle in) {
        observerPosition = new ObserverPosition(in.getBundle(Constants.INTENT_EXTRA_OBSERVER_POSITION));
        celestialBody = CelestialBodies.CreateCelestialBody(in.getInt(BUNDLE_BODY_INDEX));
    }

    public Bundle GetMyBundle() {
        Bundle bundle = new Bundle();
        bundle.putBundle(Constants.INTENT_EXTRA_OBSERVER_POSITION, observerPosition.getBundledForm());
        bundle.putInt(BUNDLE_BODY_INDEX, celestialBody.getIndexNo());
        return bundle;
    }

}
