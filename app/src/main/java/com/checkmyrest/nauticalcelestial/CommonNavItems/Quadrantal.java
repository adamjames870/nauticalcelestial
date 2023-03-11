package com.checkmyrest.nauticalcelestial.CommonNavItems;

import java.text.DecimalFormat;

public class Quadrantal {

    private String NS;
    private double course;
    private String EW;

    public Quadrantal(String iNS, double iCourse, String iEW) {
        Set(iNS, iCourse, iEW);
    }

    public Quadrantal(double iTrueCourse) {
        iTrueCourse = Constants.CorrectTo360(iTrueCourse);
        if (iTrueCourse <= 90) {Set("N", iTrueCourse, "E");}
        else if (iTrueCourse <= 180) {Set("S", 180-iTrueCourse, "E");}
        else if (iTrueCourse <= 270) {Set("S", iTrueCourse - 180, "W");}
        else {Set("N", 360 - iTrueCourse, "W");}
    }

    private void Set(String iNS, double iCourse, String iEW) {
        NS = iNS;
        course = iCourse % 360;
        EW = iEW;
    }

    public double getTrueCourse() {
        if (NS.equals("N")) {
            if (EW.equals("E")) {return course;} else {return 360 - course;}
        } else {
            if (EW.equals("E")) {return 180 - course;} else {return 180 + course;}
        }
    }

    public double getRawCourse() {return course;}
    public String EwDirection() {return EW;}
    public String NsDirection() {return NS;}

    public String toString() {
        try {
            DecimalFormat f = new DecimalFormat("000.0");
            return f.format(getTrueCourse()) + "\u00B0T";
        } catch (Exception e) {
            return e.toString();
        }
    }

    public String toQuadString() {
        DecimalFormat f = new DecimalFormat("00.0");
        String s;
        s =  NS + " ";
        s += f.format(course);
        s += " " + EW;
        return s;
    }

}
