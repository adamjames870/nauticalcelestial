package com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies;

import android.os.Parcel;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Declination;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.util.ArrayList;
import java.util.Calendar;

final public class CelestialBodies {

    private static int COUNT_OF_BODIES = 58;

    private CelestialBodies() {}

    private static CelestialBody CreateCelestialBody(String bodyName) {
        if ( bodyName.equals("Acamar") ) {return new Star(1, bodyName, 2.97111111111111, new Declination(40.31, "S"));}
        else if ( bodyName.equals("Achernar") ) {return new Star(2, bodyName, 1.62861111111111, new Declination(57.2433333333333, "S"));}
        else if ( bodyName.equals("Acrux") ) {return new Star(3, bodyName, 12.4433333333333, new Declination(63.0916666666667, "S"));}
        else if ( bodyName.equals("Adhara") ) {return new Star(4, bodyName, 6.9775, new Declination(28.9733333333333, "S"));}
        else if ( bodyName.equals("Aldebaran") ) {return new Star(5, bodyName, 4.59888888888889, new Declination(16.5083333333333, "N"));}
        else if ( bodyName.equals("Alioth") ) {return new Star(6, bodyName, 12.9, new Declination(55.9566666666667, "N"));}
        else if ( bodyName.equals("Alkaid") ) {return new Star(7, bodyName, 13.7919444444444, new Declination(49.3116666666667, "N"));}
        else if ( bodyName.equals("Al Nair") ) {return new Star(8, bodyName, 22.1363888888889, new Declination(46.965, "S"));}
        else if ( bodyName.equals("Alnilam") ) {return new Star(9, bodyName, 5.60361111111111, new Declination(1.195, "S"));}
        else if ( bodyName.equals("Alphard") ) {return new Star(10, bodyName, 9.46, new Declination(8.65833333333333, "S"));}
        else if ( bodyName.equals("Alphecca") ) {return new Star(11, bodyName, 15.5775, new Declination(26.715, "N"));}
        else if ( bodyName.equals("Alpheratz") ) {return new Star(12, bodyName, 0.139444444444444, new Declination(29.0916666666667, "N"));}
        else if ( bodyName.equals("Altair") ) {return new Star(13, bodyName, 19.8458333333333, new Declination(8.86833333333333, "N"));}
        else if ( bodyName.equals("Ankaa") ) {return new Star(14, bodyName, 0.437777777777778, new Declination(42.3116666666667, "S"));}
        else if ( bodyName.equals("Antares") ) {return new Star(15, bodyName, 16.4894444444444, new Declination(26.4283333333333, "S"));}
        else if ( bodyName.equals("Arcturus") ) {return new Star(16, bodyName, 14.2608333333333, new Declination(19.1866666666667, "N"));}
        else if ( bodyName.equals("Atria") ) {return new Star(17, bodyName, 16.8097222222222, new Declination(69.0233333333333, "S"));}
        else if ( bodyName.equals("Avior") ) {return new Star(18, bodyName, 8.37583333333333, new Declination(59.5083333333333, "S"));}
        else if ( bodyName.equals("Bellatrix") ) {return new Star(19, bodyName, 5.41888888888889, new Declination(6.34833333333333, "N"));}
        else if ( bodyName.equals("Betelgeuse") ) {return new Star(20, bodyName, 5.91972222222222, new Declination(7.405, "N"));}
        else if ( bodyName.equals("Canopus") ) {return new Star(21, bodyName, 6.39972222222222, new Declination(52.6983333333333, "S"));}
        else if ( bodyName.equals("Capella") ) {return new Star(22, bodyName, 5.27833333333333, new Declination(45.9983333333333, "N"));}
        else if ( bodyName.equals("Deneb") ) {return new Star(23, bodyName, 20.69, new Declination(45.2816666666667, "N"));}
        else if ( bodyName.equals("Denebola") ) {return new Star(24, bodyName, 11.8175, new Declination(14.5716666666667, "N"));}
        else if ( bodyName.equals("Diphda") ) {return new Star(25, bodyName, 0.726111111111111, new Declination(17.99, "S"));}
        else if ( bodyName.equals("Dubhe") ) {return new Star(26, bodyName, 11.0619444444444, new Declination(61.7466666666667, "N"));}
        else if ( bodyName.equals("Elnath") ) {return new Star(27, bodyName, 5.43833333333333, new Declination(28.6066666666667, "N"));}
        else if ( bodyName.equals("Eltanin") ) {return new Star(28, bodyName, 17.9427777777778, new Declination(51.4883333333333, "N"));}
        else if ( bodyName.equals("Enif") ) {return new Star(29, bodyName, 21.7361111111111, new Declination(9.875, "N"));}
        else if ( bodyName.equals("Formalhaut") ) {return new Star(30, bodyName, 22.9602777777778, new Declination(29.6266666666667, "S"));}
        else if ( bodyName.equals("Gacrux") ) {return new Star(31, bodyName, 12.5194444444444, new Declination(57.1066666666667, "S"));}
        else if ( bodyName.equals("Gienah") ) {return new Star(32, bodyName, 12.2633333333333, new Declination(17.5383333333333, "S"));}
        else if ( bodyName.equals("Hadar") ) {return new Star(33, bodyName, 14.0633333333333, new Declination(60.3666666666667, "S"));}
        else if ( bodyName.equals("Hamal") ) {return new Star(34, bodyName, 2.11944444444444, new Declination(23.4633333333333, "N"));}
        else if ( bodyName.equals("Kaus Austr.") ) {return new Star(35, bodyName, 18.4019444444444, new Declination(34.3833333333333, "S"));}
        else if ( bodyName.equals("Kochab") ) {return new Star(36, bodyName, 14.8441666666667, new Declination(74.1533333333333, "N"));}
        else if ( bodyName.equals("Markab") ) {return new Star(37, bodyName, 23.0788888888889, new Declination(15.205, "N"));}
        else if ( bodyName.equals("Menkar") ) {return new Star(38, bodyName, 3.03805555555556, new Declination(4.08666666666667, "N"));}
        else if ( bodyName.equals("Menkent") ) {return new Star(39, bodyName, 14.1111111111111, new Declination(36.365, "S"));}
        else if ( bodyName.equals("Miaplacidus") ) {return new Star(40, bodyName, 9.22111111111111, new Declination(69.7133333333333, "S"));}
        else if ( bodyName.equals("Mirfak") ) {return new Star(41, bodyName, 3.40555555555556, new Declination(49.8616666666667, "N"));}
        else if ( bodyName.equals("Nunki") ) {return new Star(42, bodyName, 18.9202777777778, new Declination(26.295, "S"));}
        else if ( bodyName.equals("Peacock") ) {return new Star(43, bodyName, 20.4263888888889, new Declination(56.7366666666667, "S"));}
        else if ( bodyName.equals("Pollux") ) {return new Star(44, bodyName, 7.75555555555556, new Declination(28.025, "N"));}
        else if ( bodyName.equals("Procyon") ) {return new Star(45, bodyName, 7.65527777777778, new Declination(5.225, "N"));}
        else if ( bodyName.equals("Rasalhague") ) {return new Star(46, bodyName, 17.5816666666667, new Declination(12.5616666666667, "N"));}
        else if ( bodyName.equals("Regulus") ) {return new Star(47, bodyName, 10.1394444444444, new Declination(11.9666666666667, "N"));}
        else if ( bodyName.equals("Rigel") ) {return new Star(48, bodyName, 5.2425, new Declination(8.205, "S"));}
        else if ( bodyName.equals("Rigil Kent") ) {return new Star(49, bodyName, 14.6602777777778, new Declination(60.83, "S"));}
        else if ( bodyName.equals("Sabik") ) {return new Star(50, bodyName, 17.1722222222222, new Declination(15.7233333333333, "S"));}
        else if ( bodyName.equals("Schedar") ) {return new Star(51, bodyName, 0.675, new Declination(56.54, "N"));}
        else if ( bodyName.equals("Shaula") ) {return new Star(52, bodyName, 17.5594444444444, new Declination(37.1016666666667, "S"));}
        else if ( bodyName.equals("Sirius") ) {return new Star(53, bodyName, 6.75277777777778, new Declination(16.715, "S"));}
        else if ( bodyName.equals("Spica") ) {return new Star(54, bodyName, 13.4197222222222, new Declination(11.1583333333333, "S"));}
        else if ( bodyName.equals("Suhail") ) {return new Star(55, bodyName, 9.13361111111111, new Declination(43.43, "S"));}
        else if ( bodyName.equals("Vega") ) {return new Star(56, bodyName, 18.615, new Declination(38.7833333333333, "N"));}
        else if ( bodyName.equals("Zuben-ubi") ) {return new Star(57, bodyName, 14.8475, new Declination(16.0383333333333, "S"));}
        else {return new Star(0, "No Star", 0,  new Declination(0, "N"));}
    }

    public static CelestialBody CreateCelestialBody(int starIndex) {
        if (starIndex == 1) {return CreateCelestialBody("Acamar");}
        else if (starIndex == 2) {return CreateCelestialBody("Achernar");}
        else if (starIndex == 3) {return CreateCelestialBody("Acrux");}
        else if (starIndex == 4) {return CreateCelestialBody("Adhara");}
        else if (starIndex == 5) {return CreateCelestialBody("Aldebaran");}
        else if (starIndex == 6) {return CreateCelestialBody("Alioth");}
        else if (starIndex == 7) {return CreateCelestialBody("Alkaid");}
        else if (starIndex == 8) {return CreateCelestialBody("Al Nair");}
        else if (starIndex == 9) {return CreateCelestialBody("Alnilam");}
        else if (starIndex == 10) {return CreateCelestialBody("Alphard");}
        else if (starIndex == 11) {return CreateCelestialBody("Alphecca");}
        else if (starIndex == 12) {return CreateCelestialBody("Alpheratz");}
        else if (starIndex == 13) {return CreateCelestialBody("Altair");}
        else if (starIndex == 14) {return CreateCelestialBody("Ankaa");}
        else if (starIndex == 15) {return CreateCelestialBody("Antares");}
        else if (starIndex == 16) {return CreateCelestialBody("Arcturus");}
        else if (starIndex == 17) {return CreateCelestialBody("Atria");}
        else if (starIndex == 18) {return CreateCelestialBody("Avior");}
        else if (starIndex == 19) {return CreateCelestialBody("Bellatrix");}
        else if (starIndex == 20) {return CreateCelestialBody("Betelgeuse");}
        else if (starIndex == 21) {return CreateCelestialBody("Canopus");}
        else if (starIndex == 22) {return CreateCelestialBody("Capella");}
        else if (starIndex == 23) {return CreateCelestialBody("Deneb");}
        else if (starIndex == 24) {return CreateCelestialBody("Denebola");}
        else if (starIndex == 25) {return CreateCelestialBody("Diphda");}
        else if (starIndex == 26) {return CreateCelestialBody("Dubhe");}
        else if (starIndex == 27) {return CreateCelestialBody("Elnath");}
        else if (starIndex == 28) {return CreateCelestialBody("Eltanin");}
        else if (starIndex == 29) {return CreateCelestialBody("Enif");}
        else if (starIndex == 30) {return CreateCelestialBody("Formalhaut");}
        else if (starIndex == 31) {return CreateCelestialBody("Gacrux");}
        else if (starIndex == 32) {return CreateCelestialBody("Gienah");}
        else if (starIndex == 33) {return CreateCelestialBody("Hadar");}
        else if (starIndex == 34) {return CreateCelestialBody("Hamal");}
        else if (starIndex == 35) {return CreateCelestialBody("Kaus Austr.");}
        else if (starIndex == 36) {return CreateCelestialBody("Kochab");}
        else if (starIndex == 37) {return CreateCelestialBody("Markab");}
        else if (starIndex == 38) {return CreateCelestialBody("Menkar");}
        else if (starIndex == 39) {return CreateCelestialBody("Menkent");}
        else if (starIndex == 40) {return CreateCelestialBody("Miaplacidus");}
        else if (starIndex == 41) {return CreateCelestialBody("Mirfak");}
        else if (starIndex == 42) {return CreateCelestialBody("Nunki");}
        else if (starIndex == 43) {return CreateCelestialBody("Peacock");}
        else if (starIndex == 44) {return CreateCelestialBody("Pollux");}
        else if (starIndex == 45) {return CreateCelestialBody("Procyon");}
        else if (starIndex == 46) {return CreateCelestialBody("Rasalhague");}
        else if (starIndex == 47) {return CreateCelestialBody("Regulus");}
        else if (starIndex == 48) {return CreateCelestialBody("Rigel");}
        else if (starIndex == 49) {return CreateCelestialBody("Rigil Kent");}
        else if (starIndex == 50) {return CreateCelestialBody("Sabik");}
        else if (starIndex == 51) {return CreateCelestialBody("Schedar");}
        else if (starIndex == 52) {return CreateCelestialBody("Shaula");}
        else if (starIndex == 53) {return CreateCelestialBody("Sirius");}
        else if (starIndex == 54) {return CreateCelestialBody("Spica");}
        else if (starIndex == 55) {return CreateCelestialBody("Suhail");}
        else if (starIndex == 56) {return CreateCelestialBody("Vega");}
        else if (starIndex == 57) {return CreateCelestialBody("Zuben-ubi");}
        else {return new Star(0, "No Star", 0,  new Declination(0, "N"));}
    }

    public static CelestialBody CreateCelestialBody(Parcel in) {
        return CreateCelestialBody(in.readBundle().getInt(CelestialBody.BUNDLE_BODY_INDEX));
    }

    private static double NutationDaysConstant(Calendar calendar) {
        return Constants.DaysDifference(Constants.ExcelZeroCalendar(), calendar) - 2451545;
    }

    public static double NutDl(Calendar calendar) {
        double partA = -17.3 * Math.sin(Math.toRadians(125 - (0.05295 * NutationDaysConstant(calendar))));
        double partB = - 1.4 * Math.sin(Math.toRadians(200 + (1.97129 * NutationDaysConstant(calendar))));
        return partA + partB;
    }

    public static double NutDe(Calendar calendar) {
        double partA = 9.4 * Math.cos(Math.toRadians(125 - (0.05295 * NutationDaysConstant(calendar))));
        double partB = 0.7 * Math.cos(Math.toRadians(200 + (1.97129 * NutationDaysConstant(calendar))));
        return partA + partB;
    }

    public static ArrayList<CelestialBody> ListOfCelestialBodies() {
        ArrayList<CelestialBody> bodyList = new ArrayList<CelestialBody>();
        for (int i = 1; i < COUNT_OF_BODIES; i++) {
            bodyList.add(CreateCelestialBody(i));
        }
        return bodyList;
    }

}
