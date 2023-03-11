package com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Declination;

public abstract class CelestialBody implements Parcelable {

    public static String BUNDLE_BODY_INDEX = "StarIndex";

    protected String name; // Name of body
    protected int indexNo; // Index of body used in CelestialBodies

    //Constructors
    public CelestialBody() {}
    public CelestialBody(String iName, int iIndexNo) {name = iName; indexNo = iIndexNo;}

    //Abstract methods must be provided by the subclass
    public abstract Double LocalHourAngle(ObserverPosition observerPosition);
    public abstract Declination Declination (ObserverPosition observerPosition);

    public String getName() {return name;}
    public int getIndexNo() {return indexNo;}

    public static final Parcelable.Creator<CelestialBody> CREATOR
            = new Parcelable.Creator<CelestialBody>() {
        public CelestialBody createFromParcel(Parcel in) {
            return CelestialBodies.CreateCelestialBody(in);
        }

        public CelestialBody[] newArray(int size) {
            return new CelestialBody[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(getBundledForm());
    }
    
    public Bundle getBundledForm() {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_BODY_INDEX, indexNo);
        return bundle;
    }

    public String toString() {return name;}

}
