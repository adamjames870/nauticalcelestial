package com.checkmyrest.nauticalcelestial.CommonNavItems;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MagAtion {

    public static int GYRO_ERROR = 0;
    public static int VARIATION = 1;
    public static int DEVIATION = 2;
    public static int COMPASS_ERROR = 3;

    public static int EAST = -1;
    public static int WEST = 1;

    public static int HIGH = 2;
    public static int LOW = -2;

    private static NumberFormat NF = new DecimalFormat("000.0");

    private double magnitude;
    private int direction;
    private int type;

    protected MagAtion(){}

    public MagAtion(int iType, double iMagnitude, int iDirection) {
        type = iType;
        if (iMagnitude < 0) {
            magnitude = Math.abs(-1);
            direction = getOppositeDirection(iDirection);
        } else if (iMagnitude > 180) {
            magnitude = 180 - iMagnitude;
            direction = getOppositeDirection(iDirection);
        } else {
            magnitude = iMagnitude;
            direction = iDirection;
        }
    }

    public MagAtion(int iType, Quadrantal iTrueBearing, Quadrantal iTakenBearing) {
        type = iType;
        magnitude = Math.abs(iTakenBearing.getTrueCourse() - iTrueBearing.getTrueCourse());
        if (iTrueBearing.getTrueCourse() < iTakenBearing.getTrueCourse()) {direction = fetchMatchingErrorDirection(iType, WEST);}
        else {direction = fetchMatchingErrorDirection(iType, EAST);
        }
    }

    protected int getOppositeDirection(int dir) {
        if (dir == EAST) {return WEST;}
        else if (dir == WEST) {return EAST;}
        else if (dir == HIGH) {return LOW;}
        else if (dir == LOW) {return HIGH;}
        else {return 0;}
    }

    private String parseType() {
        if (type == GYRO_ERROR) {return "Gyro Error";}
        else if (type == VARIATION) {return "Variation";}
        else if (type == DEVIATION) {return "Deviation";}
        else if (type == COMPASS_ERROR) {return "Compass Err";}
        else {return null;}
    }

    private String parseDirection() {
        if (direction == HIGH) {return "H";}
        else if (direction == LOW) {return "L";}
        else if (direction == EAST) {return "E";}
        else if (direction == WEST) {return "W";}
        else {return null;}
    }

    public double getMagAtion() {
        if (direction == LOW || direction == EAST) {return magnitude * -1;}
        else {return magnitude;}
    }

    public double getAbsMagAtion() {return magnitude;}
    public int getDirMagAtion() {return direction;}

    private int fetchMatchingErrorDirection(int iType, int iDir) {
        if (iType == GYRO_ERROR && iDir == EAST) {return LOW;}
        else if (iType == GYRO_ERROR && iDir == WEST) {return HIGH;}
        else if (iDir == HIGH) {return WEST;}
        else if (iDir == LOW) {return EAST;}
        else {return iDir;}
    }

    public String toString() {
        return parseType() + ": " + NF.format(magnitude) + "\u00B0" + parseDirection();
    }

}
