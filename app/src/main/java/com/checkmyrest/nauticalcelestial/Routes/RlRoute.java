package com.checkmyrest.nauticalcelestial.Routes;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Latitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

public class RlRoute extends Route {

    public RlRoute(LatLonLocation iFrom, LatLonLocation iTo) {super(iFrom, iTo);}
    public RlRoute(LatLonLocation iFrom, Quadrantal iCourse, Double iSpeed) {super(iFrom, iCourse, iSpeed);}

    protected LatLonLocation ToFromCourse(Quadrantal iCourse, double iSpeed) {

        double dLat = ( iSpeed * Math.cos(Math.toRadians(iCourse.getRawCourse())) ) / 60;

        Latitude latB = new Latitude(from.getLatitude());
        latB.MoveLatitude(dLat, iCourse.NsDirection());

        double dmp = dmpInputs(from.getLatitude(), latB);
        double dLon = dmp * Math.tan(Math.toRadians(iCourse.getRawCourse()));

        Longitude lonB = new Longitude(from.getLongitude());
        lonB.MoveLongitude(dLon, iCourse.EwDirection());

        return new LatLonLocation(latB, lonB);

    }

    @Override
    public double Distance() {
        //Distance = D'Lat (in minutes) / Cos(Course)
        return (DLat() * 60) / Math.cos(Math.toRadians(Course().getRawCourse()));
    }

    @Override
    public Quadrantal Course() {
        //Tan(Course) = D'Long (in minutes) / DMP
        return new Quadrantal(DLatDirNS(), Math.toDegrees(Math.atan( (60 * DLong()) / DMP() )), DLongDirEW());
    }

    @Override
    public LatLonLocation Vertex() {
        if (to.getLat() > from.getLat()) {return to;} else {return from;}
    }

    @Override
    public LatLonLocation Waypoint(Longitude wLon) {

        double dLongAtoW;

        //Calculate D'Long A->W
        if (from.getLonEW().equals(wLon.getLonEW())) {
            //Same EE or WW so DLong is difference
            dLongAtoW = Math.max(wLon.getLon(), from.getLon()) - Math.min(wLon.getLon(), from.getLon());
        } else {
            dLongAtoW = wLon.getLon() + from.getLon();
        }

        if (dLongAtoW > 180) dLongAtoW = 360 - dLongAtoW;

        //Tan(Course) = D'Long (in minutes) / DMP
        //TF DMP = D'Long (in minutes) / Tan (Course)

        double dmpAtoW;
        dmpAtoW = ( dLongAtoW * 60 ) / Math.tan(Math.toRadians(Course().getRawCourse()));

        double mpLatW;
        if (from.getLatNS().equals(Course().NsDirection())) {
            // N heading N or S heading S so increase Lat
            mpLatW = from.getMeridinalPart() + dmpAtoW;
        } else {
            mpLatW = from.getMeridinalPart() - dmpAtoW;
        }

        double latW;
        String wpNS;

        latW = Constants.ReverseMeridinalPart(Math.abs(mpLatW));
        if (mpLatW < 0) {wpNS = Constants.getOppositeNS(from.getLatNS());} else {wpNS = from.getLatNS();}

        return new LatLonLocation(latW, wpNS, wLon.getLon(), wLon.getLonEW());

    }

    @Override
    public LatLonLocation Waypoint(double secondsFromPositionA) {
        double distance = speed * (secondsFromPositionA / 3600);
        double dLat = ( distance * Math.cos(Math.toRadians(Course().getRawCourse())) ) / 60;
        Latitude latB = new Latitude(from.getLatitude());
        latB.MoveLatitude(dLat, Course().NsDirection());
        double dmp = dmpInputs(from.getLatitude(), latB);
        double dLon = dmp * Math.tan(Math.toRadians(Course().getRawCourse()));
        Longitude lonB = new Longitude(from.getLongitude());
        lonB.MoveLongitude(dLon, Course().EwDirection());
        return new LatLonLocation(latB, lonB);
    }


}
