package com.checkmyrest.nauticalcelestial.CommonNavItems;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Longitude {

    private double lon;
    private String lonEW;

    public Longitude(Longitude iLong) {
        lon = iLong.getLon();
        lonEW = iLong.getLonEW();
    }

    public Longitude(double iLon, String iLonEW) {
        try {
            setLonEW(iLonEW);
            setLon(iLon);
        } catch (Exception e) {throw new IllegalArgumentException(e.toString());}
    }

    private void setLon(double iLon) {
        lon = iLon;
        CheckHemisphere();
        if (iLon < Constants.MIN_LON || iLon > Constants.MAX_LON) {
            throw new IllegalArgumentException("Illegal Lon " + Double.toString(iLon));
        }
    }

    private void setLonEW(String iLonEW) {
        if (!iLonEW.equals("E") && !iLonEW.equals("W")) {
            throw new IllegalArgumentException("Illegal LonEW " + iLonEW);
        } else {lonEW = iLonEW;}
    }

    public double getLon() {return lon;}
    public String getLonEW() {return lonEW;}

    public void MoveLongitude(double distance, String direction) {
        if (direction.equals(lonEW)) {
            //Moving in same dir as EW, therefore going more in same direction
            lon = lon  + distance;
        } else {
            //Moving in opp dir to EW therefore going less, subtract
            lon = lon - distance;
        }
        CheckHemisphere();
    }

    private void CheckHemisphere() {
        if (lon > 180) {
            lon = 360 - lon;
            lonEW = Constants.getOppositeEW(lonEW);
        } else if (lon < 0) {
            lon = Math.abs(lon);
            lonEW = Constants.getOppositeEW(lonEW);
        }
    }

    public String getText() {
        NumberFormat nfLon = new DecimalFormat("000");
        NumberFormat nfMin = new DecimalFormat("00.0");
        nfLon.setRoundingMode(RoundingMode.DOWN);
        return nfLon.format(lon) + "\u00B0" + nfMin.format( (lon % 1) * 60 ) + "'" + lonEW;
    }

    public String toString() {return getText();}

    public double LongitudeInTime() {
        if ( lonEW.equals("E") ) {return (lon/15) * -1;} else {return (lon/15);}
    }

    public int getEwSelected() {if (lonEW.equals("E")) {return 0;} else {return 1;}}

}
