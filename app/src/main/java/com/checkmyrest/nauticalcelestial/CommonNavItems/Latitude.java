package com.checkmyrest.nauticalcelestial.CommonNavItems;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Latitude {

    private double lat;
    private String latNS;

    public Latitude(double iLat, String iLatNS) {
        try {
            setLat(iLat);
            setLatNS(iLatNS);
        } catch (Exception e) {throw new IllegalArgumentException(e.toString());}
    }

    public Latitude(Latitude iLatitude) {
        lat = iLatitude.getLat();
        latNS = iLatitude.getLatNS();
    }

    private void setLat(double iLat) {
        if (iLat < Constants.MIN_LAT || iLat > Constants.MAX_LAT) {
            throw new IllegalArgumentException("Unable to set Lat as " + Double.toString(iLat));
        } else {lat = iLat;}
    }

    private void setLatNS(String iLatNS) {
        if (!iLatNS.equals("N") && !iLatNS.equals("S")) {
            throw new IllegalArgumentException("Unable to set LatNS as " + iLatNS);
        } else {latNS = iLatNS;}
    }

    public double getLat() {return lat;}
    public String getLatNS() {return latNS;}

    public String getText() {
        NumberFormat nfLat = new DecimalFormat("00");
        NumberFormat nfMin = new DecimalFormat("00.0");
        nfLat.setRoundingMode(RoundingMode.DOWN);
        return nfLat.format(lat) + "\u00B0" + nfMin.format( (lat % 1) * 60 ) + "'" + latNS;
    }

    public String toString() {return getText();}

    public int getNsSelected() {if (latNS.equals("N")) {return 0;} else {return 1;}}
    public double getMeridinalPart() {
        return Constants.MeridinalPart(lat);
    }

    public void MoveLatitude(double distance, String direction) {
        if (direction.equals(latNS)) {
            //Moving in same dir as NS, therefore going more in same direction
            lat = lat  + distance;
        } else {
            //Moving in opp dir to NS therefore going less, subtract
            lat = lat - distance;
        }
        CheckEquatorAndPoles();
    }

    private void CheckEquatorAndPoles() {
        if (lat > 90) {
            lat = lat - 90;
        } else if (lat < 0) {
            lat = Math.abs(lat);
            latNS = Constants.getOppositeEW(latNS);
        }
    }

}
