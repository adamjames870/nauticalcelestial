package com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems;

import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Declination {

    private double dec;
    private String decNS;

    public Declination(double iDec, String iDecNS) {
        try {
            setDec(iDec);
            setDecNS(iDecNS);
        } catch (Exception e) {throw new IllegalArgumentException(e.toString());}
    }

    public Declination(double iDec) {
        try {
            setDec(Math.abs(iDec));
            if (iDec < 0) {setDecNS("S");} else {setDecNS("N");}
        } catch (Exception e) {throw new IllegalArgumentException(e.toString());}
    }

    private void setDec(double iDec) {
        if (iDec < Constants.MIN_LAT || iDec > Constants.MAX_LAT) {
            throw new IllegalArgumentException("Unable to set Dec as " + Double.toString(iDec));
        } else {dec = iDec;}
    }

    private void setDecNS(String iDecNS) {
        if (!iDecNS.equals("N") && !iDecNS.equals("S")) {
            throw new IllegalArgumentException("Unable to set DecNS as " + iDecNS);
        } else {decNS = iDecNS;}
    }

    public double getDec() {
        if (decNS.equals("N")) {return dec;} else {return dec * -1;}
    }

    public String getDecNS() {return decNS;}

    public String toString() {
        NumberFormat nfLat = new DecimalFormat("00");
        NumberFormat nfMin = new DecimalFormat("00.0");
        nfLat.setRoundingMode(RoundingMode.DOWN);
        return nfLat.format(dec) + "\u00B0" + nfMin.format( (dec % 1) * 60 ) + "'" + decNS;
    }

}
