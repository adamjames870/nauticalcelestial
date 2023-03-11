package com.checkmyrest.nauticalcelestial.CommonNavItems;

public class LatLonLocation {

    private Latitude lat;
    private Longitude lon;

    public LatLonLocation(double iLat, String iLatNS, double iLon, String iLonEW) {
        try {
            lat = new Latitude(iLat, iLatNS);
            lon = new Longitude(iLon, iLonEW);
        } catch (Exception e) {throw new IllegalArgumentException(e.toString() + "|in LatLonLoc");}

    }

    public LatLonLocation(Latitude iLat, Longitude iLon) {
        lat = iLat;
        lon = iLon;
    }

    public LatLonLocation(LatLonLocation lll) {
        lat = lll.lat;
        lon = lll.lon;
    }

    public double getLat() {return lat.getLat();}
    public double getLon() {return lon.getLon();}
    public String getLatNS() {return lat.getLatNS();}
    public String getLonEW() {return lon.getLonEW();}
    public Latitude getLatitude() {return lat;}
    public Longitude getLongitude() {return lon;}
    public double getMeridinalPart() {
        return Constants.MeridinalPart(lat.getLat());
    }

    public String getLatText() {return lat.getText();}
    public String getLonText() {return lon.getText();}

    public String toString() {return "Lat " + lat.toString() + " Lon " + lon.toString();}

}
