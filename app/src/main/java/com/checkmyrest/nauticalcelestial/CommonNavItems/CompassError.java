package com.checkmyrest.nauticalcelestial.CommonNavItems;

public class CompassError extends MagAtion{

    private MagAtion ceVariation;
    private MagAtion ceDeviation;

    public CompassError(MagAtion iVariation, MagAtion iDeviation) {
        ceVariation = iVariation;
        ceDeviation = iDeviation;
    }

    public CompassError(MagAtion iVariation, Quadrantal iTrueBearing, Quadrantal iMagneticBearing) {
        ceVariation = iVariation;
        MagAtion compErr = new MagAtion(COMPASS_ERROR, iTrueBearing, iMagneticBearing);

        double devMagnitude;
        int devDirection;

        if (ceVariation.getDirMagAtion() == compErr.getDirMagAtion() && ceVariation.getAbsMagAtion() < compErr.getAbsMagAtion()) {
            devMagnitude = compErr.getAbsMagAtion() - ceVariation.getAbsMagAtion();
            devDirection = compErr.getDirMagAtion();
        } else if (ceVariation.getDirMagAtion() == compErr.getDirMagAtion() && ceVariation.getAbsMagAtion() > compErr.getAbsMagAtion()) {
            devMagnitude = ceVariation.getAbsMagAtion() - compErr.getAbsMagAtion();
            devDirection = getOppositeDirection(ceVariation.getDirMagAtion());
        } else if (ceVariation.getAbsMagAtion() == compErr.getAbsMagAtion()) {
            devMagnitude = 0;
            devDirection = EAST;
        } else {
            devMagnitude = ceVariation.getAbsMagAtion() + compErr.getAbsMagAtion();
            devDirection = getOppositeDirection(ceVariation.getDirMagAtion());
        }

        ceDeviation = new MagAtion(DEVIATION, devMagnitude, devDirection);

    }

    public MagAtion getDeviation() {return ceDeviation;}
    public MagAtion getVariation() {return ceVariation;}

    public MagAtion getCompassError() {

        double ceMagnitude;
        int ceDirection;

        if (ceVariation.getDirMagAtion() == ceDeviation.getDirMagAtion()) {
            ceMagnitude = ceVariation.getAbsMagAtion() + ceDeviation.getAbsMagAtion();
            ceDirection = ceVariation.getDirMagAtion();
        } else if (ceVariation.getAbsMagAtion() > ceDeviation.getAbsMagAtion()) {
            ceMagnitude = ceVariation.getAbsMagAtion() - ceDeviation.getAbsMagAtion();
            ceDirection = ceVariation.getDirMagAtion();
        } else {
            ceMagnitude = ceDeviation.getAbsMagAtion() - ceVariation.getAbsMagAtion();
            ceDirection = ceDeviation.getDirMagAtion();
        }

        return new MagAtion(COMPASS_ERROR, ceMagnitude, ceDirection);

    }

}
