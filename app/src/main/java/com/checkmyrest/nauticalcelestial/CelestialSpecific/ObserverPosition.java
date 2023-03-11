package com.checkmyrest.nauticalcelestial.CelestialSpecific;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;
import com.checkmyrest.nauticalcelestial.Routes.RlRoute;
import com.checkmyrest.nauticalcelestial.Routes.Route;

import java.util.Calendar;

public class ObserverPosition implements Parcelable {

    private static String BUNDLE_OBS_ZD = "ObsZd";
    private static String BUNDLE_OBS_CE = "ObsCe";
    private static String BUNDLE_LOC_LATVAL = "LatValue";
    private static String BUNDLE_LOC_LATTXT = "LatNs";
    private static String BUNDLE_LOC_LONVAL = "LonValue";
    private static String BUNDLE_LOC_LONTXT = "LonEw";
    private static String BUNDLE_CAL_YEAR = "CalYear";
    private static String BUNDLE_CAL_MONT = "CalMonth";
    private static String BUNDLE_CAL_DAYS = "CalDayOfMonth";
    private static String BUNDLE_CAL_HOUR = "CalHourOfDay";
    private static String BUNDLE_CAL_MINS = "CalMins";
    private static String BUNDLE_CAL_SECS = "CalSecs";
    private static String BUNDLE_CRS_TRUE = "CourseTrue";
    private static String BUNDLE_CRS_SPEED = "CourseSpeed";
    public static String BUNDLE_BRG_TYPE = "BearingTypeInt";

    private Calendar observationTime;
    private LatLonLocation position;
    private Quadrantal course = new Quadrantal("N", 0, "E");
    private double speed = 0;
    private int zd = 0; //in hours;
    private int ce = 0; //in seconds;
    private int bearingType = Constants.BEARING_TRUE;

    public ObserverPosition(Calendar iObservationTime, LatLonLocation iPosition) {
        Set(iObservationTime, iPosition);
    }

    private void Set(Calendar iObservationTime, LatLonLocation iPosition) {
        observationTime = iObservationTime;
        position = iPosition;
    }

    public void setShipMovement(Quadrantal iCourse, double iSpeed) {
        course = iCourse;
        speed = iSpeed;
    }

    public void setZoneDifference(int iZd) {
        zd = iZd;
    }

    public void setZoneDifference(int iZd, String iZdPlusMinus) {
        if (iZdPlusMinus.equals("+")) {
            setZoneDifference(iZd);
        } else {
            setZoneDifference(-1 * iZd);
        }
    }

    public void setChronError(int iCe) {
        ce = iCe;
    }

    public void setChronError(int iCe, String iCeFastSlow) {
        if (iCeFastSlow.equals("+")) {
            setChronError(iCe);
        } else {
            setChronError(-1 * iCe);
        }
    }

    public LatLonLocation getPosition() {return position;}

    public Calendar getObservationTimeRaw() {return observationTime;}

    public Calendar getObservationTime() {
        Calendar calendar = (Calendar) observationTime.clone();
        calendar.add(Calendar.HOUR_OF_DAY, zd);
        calendar.add(Calendar.SECOND, ce);
        return calendar;
    }

    public ObserverPosition AdvancePosition(Calendar newLocalObservationTime) {
        newLocalObservationTime.set(Calendar.MONTH, newLocalObservationTime.get(Calendar.MONTH) + 1);
        ObserverPosition pos = new ObserverPosition
                (newLocalObservationTime, position); // AdvanceLocation(TimeDifference(newLocalObservationTime)));
        pos.setChronError(ce);
        pos.setZoneDifference(zd);
        pos.setShipMovement(course, speed);
        return pos;
    }

    private int TimeDifference(Calendar newObservationTime) {
        return 0;
        // return (int) ( ( newObservationTime.getTimeInMillis() - observationTime.getTimeInMillis() ) / 1000 );
    }

    private LatLonLocation AdvanceLocation(int timeDifference) {
        // timeDifference in seconds
        Route route = new RlRoute(position, course, speed);
        return route.Waypoint(timeDifference);
    }

    public String toString() {
        return Constants.parseTime(getObservationTime()) + " | " + Constants.ParseLocation(position);
    }

    public static final Parcelable.Creator<ObserverPosition> CREATOR
            = new Parcelable.Creator<ObserverPosition>() {
        public ObserverPosition createFromParcel(Parcel in) {
            return new ObserverPosition(in);
        }

        public ObserverPosition[] newArray(int size) {
            return new ObserverPosition[size];
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

        bundle.putInt(BUNDLE_OBS_ZD, zd);
        bundle.putInt(BUNDLE_OBS_CE, ce);

        bundle.putDouble(BUNDLE_LOC_LATVAL, Math.abs(position.getLatitude().getLat()));
        bundle.putString(BUNDLE_LOC_LATTXT, position.getLatitude().getLatNS());
        bundle.putDouble(BUNDLE_LOC_LONVAL, Math.abs(position.getLongitude().getLon()));
        bundle.putString(BUNDLE_LOC_LONTXT, position.getLongitude().getLonEW());

        bundle.putInt(BUNDLE_CAL_YEAR, observationTime.get(Calendar.YEAR));
        bundle.putInt(BUNDLE_CAL_MONT, observationTime.get(Calendar.MONTH));
        bundle.putInt(BUNDLE_CAL_DAYS, observationTime.get(Calendar.DAY_OF_MONTH));
        bundle.putInt(BUNDLE_CAL_HOUR, observationTime.get(Calendar.HOUR_OF_DAY));
        bundle.putInt(BUNDLE_CAL_MINS, observationTime.get(Calendar.MINUTE));
        bundle.putInt(BUNDLE_CAL_SECS, observationTime.get(Calendar.SECOND));

        bundle.putDouble(BUNDLE_CRS_TRUE, course.getTrueCourse());
        bundle.putDouble(BUNDLE_CRS_SPEED, speed);

        bundle.putInt(BUNDLE_BRG_TYPE, bearingType);

        return bundle;
    }

    private ObserverPosition(Parcel in) {
        SetFromBundle(in.readBundle());
    }

    public ObserverPosition(Bundle bundle) {
        SetFromBundle(bundle);
    }

    private void SetFromBundle(Bundle bundle) {

        position = new LatLonLocation(bundle.getDouble(BUNDLE_LOC_LATVAL), bundle.getString(BUNDLE_LOC_LATTXT),
                bundle.getDouble(BUNDLE_LOC_LONVAL), bundle.getString(BUNDLE_LOC_LONTXT));

        observationTime = Calendar.getInstance();
        observationTime.set(bundle.getInt(BUNDLE_CAL_YEAR), bundle.getInt(BUNDLE_CAL_MONT), bundle.getInt(BUNDLE_CAL_DAYS),
                bundle.getInt(BUNDLE_CAL_HOUR), bundle.getInt(BUNDLE_CAL_MINS), bundle.getInt(BUNDLE_CAL_SECS));

        zd = bundle.getInt(BUNDLE_OBS_ZD);
        ce = bundle.getInt(BUNDLE_OBS_CE);

        course = new Quadrantal(bundle.getDouble(BUNDLE_CRS_TRUE));
        speed = bundle.getDouble(BUNDLE_CRS_SPEED);

        bearingType = bundle.getInt(BUNDLE_BRG_TYPE);

    }

    public Quadrantal getCourse() {return course;}
    public int getBearingType() {return bearingType;}
    public void setBearingType(int iBearingType) {bearingType = iBearingType;}

    public int getZd() {return zd;}
    public String getZdPlusMinus() {if (zd>0) {return "+";} else {return "-";}}
    public int getCe() {return ce;}
    public String getCePlusMinus() {if (ce>0) {return "+";} else {return "-";}}
    public double getSpeed() {return speed;}
}
