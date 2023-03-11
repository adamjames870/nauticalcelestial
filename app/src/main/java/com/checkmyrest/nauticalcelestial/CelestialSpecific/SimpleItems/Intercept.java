package com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems;

import java.text.DecimalFormat;

public class Intercept {

    public static int INTERCEPT_TOWARDS = 0;
    public static int INTERCEPT_AWAY = 1;

    private double intercept;
    private int interceptDirection;
    private boolean verbose = false;

    public Intercept (double iIntercept) {
        intercept = Math.abs(iIntercept);
        if (iIntercept > 0) {interceptDirection = INTERCEPT_TOWARDS;}
        else {interceptDirection = INTERCEPT_AWAY;}
    }

    public Intercept (double iIntercept, int iInterceptDirection) {
        intercept = iIntercept;
        interceptDirection = iInterceptDirection;
    }

    public void setVerboseMode(boolean v) {verbose = v;}

    public String toString() {
        try {
            DecimalFormat f = new DecimalFormat("0.0");
            return "Int " + f.format(intercept * 60) + "' " + parseInterceptDirection();
        } catch (Exception e) {
            return e.toString();
        }
    }

    private String parseInterceptDirection() {
        if (!verbose) {return parseInterceptDirectionShort();} else {return parseInterceptDirectionVerbose();}
    }

    private String parseInterceptDirectionShort() {
        if (interceptDirection == INTERCEPT_AWAY) {return "A";}
        else if (interceptDirection == INTERCEPT_TOWARDS) {return "T";}
        else {return "";}
    }

    private String parseInterceptDirectionVerbose() {
        if (interceptDirection == INTERCEPT_AWAY) {return "Away";}
        else if (interceptDirection == INTERCEPT_TOWARDS) {return "Towards";}
        else {return "";}
    }

}
