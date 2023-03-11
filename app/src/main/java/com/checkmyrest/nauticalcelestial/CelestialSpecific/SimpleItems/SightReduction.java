package com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

public class SightReduction {

    private double lha;
    private Declination dec;
    private LatLonLocation position;

    public SightReduction(double iLha, Declination iDec, LatLonLocation iPosition) {
        lha = iLha;
        dec = iDec;
        position = iPosition;
    }

    public Quadrantal Azimuth() {
        double abcA; double abcB; double abcC;
        String abcAdir; String abcBdir; String abcCdir;
        double az;

        //A = Tan(Lat) / Tan(LHA) | 90 < LHA < 270 name as Lat, else name opp Lat
        abcA = Math.abs(Math.tan(Math.toRadians(position.getLat())) / Math.tan(Math.toRadians(lha)));
        if (lha < 90 || lha > 270) {abcAdir = Constants.getOppositeNS(position.getLatNS());} else {abcAdir = position.getLatNS();}

        //B = Tan(Dec) / Sin(LHA) | Name as Dec
        abcB = Math.abs(Math.tan(Math.toRadians(dec.getDec())) / Math.sin(Math.toRadians(lha)));
        abcBdir = dec.getDecNS();

        //C = A +/- B Same dir Add, else subtract | Name as largest
        if (abcAdir.equals(abcBdir)) {
            //A + B same, add and take sign
            abcC = abcA + abcB;
            abcCdir = abcAdir;
        } else  {
            //A / B different, take difference and name as largest
            abcC = Math.abs(abcA - abcB);
            if (abcA > abcB) {abcCdir = abcAdir;} else {abcCdir = abcBdir;}
        }

        //Tan(Az) = 1 / C.Cos(Lat) | QR: Dir C - course - LHA < 180 W, LHA > 180 E
        az = Math.toDegrees(Math.atan(1 / (abcC * Math.cos(Math.toRadians(position.getLat())))));
        String azDir;
        if (lha < 180) {azDir = "W";} else {azDir = "E";}
        return new Quadrantal(abcCdir, az, azDir);
    }

    public double CalculatedAltitude() {
        double partA = Math.cos(Math.toRadians(lha)) * Math.cos(Math.toRadians(position.getLat())) * Math.cos(Math.toRadians(Math.abs(dec.getDec())));
        double partB = Math.sin(Math.toRadians(position.getLat())) * Math.sin(Math.toRadians(Math.abs(dec.getDec())));
        if (position.getLatNS().equals(dec.getDecNS())) {
            //Same Lat ADD
            return Math.toDegrees(Math.asin(partA + partB));
        } else {
            //Diff Lat MINUS
            return Math.toDegrees(Math.asin(partA - partB));
        }
    }

}
