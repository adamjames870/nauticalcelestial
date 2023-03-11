package com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems;

final public class DipTable {

    private DipTable() {}

    public static final double CalculateDip(double heightInMetres) {
        return - (11.7 / 60);
    }

}