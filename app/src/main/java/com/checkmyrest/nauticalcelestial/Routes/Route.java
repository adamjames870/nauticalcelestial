package com.checkmyrest.nauticalcelestial.Routes;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Latitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.util.ArrayList;

public abstract class Route {
    protected LatLonLocation from;
    protected LatLonLocation to;
    protected double speed;

    //Constructors
    public Route(LatLonLocation iFrom, LatLonLocation iTo) {
        from = iFrom;
        to = iTo;
    }

    public Route(LatLonLocation iFrom, Quadrantal iCourse, Double iSpeed) {
        from = iFrom;
        to = ToFromCourse(iCourse, iSpeed);
        speed = iSpeed;
    }

    //Methods that must be provided by the SubClass
    protected abstract LatLonLocation ToFromCourse(Quadrantal iCourse, double iSpeed);
    public abstract double Distance();
    public abstract Quadrantal Course();
    public abstract LatLonLocation Vertex();
    public abstract LatLonLocation Waypoint (Longitude wLon);
    public abstract LatLonLocation Waypoint (double secondsFromPositionA);

    //Used to iterate through waypoints. Uses the abstract Waypoint method to get WPs for different routes
    public ArrayList<LatLonLocation> Waypoints(Constants.WaypointPositions wpType, int wpFreq) {
        ArrayList<LatLonLocation> wps = new ArrayList<LatLonLocation>();
        Longitude wpLon; // Iterate through the Longitudes to create the WP

        switch( wpType ) {
            case WP_FROM_A:
                wpLon = new Longitude(from.getLon(), from.getLonEW());
                break;
            case WP_ON_MERIDIANS:
                wpLon = new Longitude(from.getLon(), from.getLonEW());
                if ( DLongDirEW().equals(from.getLonEW()) ) {
                    //Round UP to nearest Meridian
                    wpLon.MoveLongitude(wpFreq - (from.getLon() % wpFreq), DLongDirEW());
                } else {
                    //Round DOWN to nearest Meridian
                    wpLon.MoveLongitude(from.getLon() % wpFreq, DLongDirEW());
                }
                break;
            case WP_TO_B:
                wpLon = new Longitude(to.getLon(), to.getLonEW());
                wpLon.MoveLongitude(Constants.Floor(DLong(), wpFreq),
                        Constants.getOppositeEW(DLongDirEW()));
                break;
            case WP_AROUND_V:
                wpLon = new Longitude(Vertex().getLon() - Constants.Floor(
                        new RlRoute(from, Vertex()).DLong() , wpFreq), Vertex().getLonEW());
                break;
            default:
                wpLon = new Longitude(from.getLon(), from.getLonEW());
        }

        wps.add( Waypoint(wpLon) ); //Add a first one before starting iteration

        for (int i=0; i < Constants.Floor(DLong() / wpFreq, 1); i += 1) {
            wpLon.MoveLongitude(wpFreq, DLongDirEW());
            wps.add( Waypoint(wpLon) );
        }

        return wps;
    }

    //Generally useful methods that can be used by subclasses
    protected double DMP() {return dmpInputs(from.getLatitude(), to.getLatitude());}

    protected double dmpInputs(Latitude latA, Latitude latB) {
        if (latB.getLatNS().equals(latA.getLatNS())) {
            //Same NN or SS so DMP is difference
            return Math.abs(latB.getMeridinalPart() - latA.getMeridinalPart());
        } else {
            //Different NN or SS so DMP is summation
            return latB.getMeridinalPart() + latA.getMeridinalPart();
        }
    }

    protected double DLong() {
        if (rawDLong() > 180) return 360 - rawDLong(); else return rawDLong();
    }

    private double rawDLong() {
        if (to.getLonEW().equals(from.getLonEW())) {
            //Same EE or WW so DLong is difference
            return Math.max(to.getLon(), from.getLon()) - Math.min(to.getLon(), from.getLon());
        } else {
            return to.getLon() + from.getLon();
        }
    }

    protected double DLat() {
        if (to.getLatNS().equals(from.getLatNS())) {
            //Same NN or SS so D'Lat is difference
            return Math.abs(to.getLat() - from.getLat());
        } else {
            //Differnt NN or SS so D'Lat is summation
            return to.getLat() + from.getLat();
        }
    }

    protected String DLatDirNS() {
        if (to.getLatNS().equals(from.getLatNS())) {
            //Same NW
            if (to.getLat() > from.getLat()) return from.getLatNS(); return Constants.getOppositeNS((from.getLatNS()));
        } else {
            //Different NS
            return Constants.getOppositeNS(from.getLatNS());
        }
    }

    protected String DLongDirEW() {
        if (to.getLonEW().equals(from.getLonEW())) {
            //Same EW
            if (to.getLon() > from.getLon()) return from.getLonEW(); return Constants.getOppositeEW((from.getLonEW()));
        } else {
            //Different EW
            if (rawDLong() > 180) return from.getLonEW(); else return Constants.getOppositeEW(from.getLonEW());
        }
    }

}
