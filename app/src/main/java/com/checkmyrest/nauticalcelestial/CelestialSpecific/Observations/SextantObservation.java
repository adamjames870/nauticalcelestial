package com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBodies;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBody;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Declination;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Intercept;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.SightReduction;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Latitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SextantObservation implements Parcelable {

    private static String BUNDLE_FIELDS = "SextantObservationFields";
    private static String BUNDLE_ISSET = "SextantObservationIsSet";
    private static String BUNDLE_COMPLETED = "SextantObservationCompleted";
    private static String BUNDLE_ALMANAC_DATA = "ObservationAlmanacData";
    private static String BUNDLE_CELESTIAL_POSITION = "ObservationCelestialPosition";

    private static final NumberFormat IO_DOUBLES = new DecimalFormat("00.0000");

    private CelestialPosition celestialPosition;
    private AlmanacData almanacData;
    private boolean completed = false;

    public static int SEXTANT_ALTITUDE = 0;
    public static int INDEX_ERROR = 1;
    public static int DIP = 2;
    public static int TOTAL_CORRECTION = 3;
    public static int ADDITIONAL_CORRECTION = 4;
    public static int MET_CORRECTION = 5;
    private static int FIELD_COUNT = 6;

    public static int APPARENT_ALTITUDE = 7;
    public static int TRUE_ALTITUDE = 8;
    private static int TOTAL_FIELDS = 9;

    private double fields[] = new double[TOTAL_FIELDS];
    private boolean isSet[] = new boolean[TOTAL_FIELDS];

    public SextantObservation(CelestialPosition iCelestialPosition) {
        celestialPosition = iCelestialPosition;
        setNils();
    }

    public SextantObservation(CelestialPosition iCelestialPosition, double iSextantAltitude) {
        celestialPosition = iCelestialPosition;
        setNils();
        Set(SEXTANT_ALTITUDE, iSextantAltitude);
    }

    private SextantObservation(Parcel in) {
        Bundle bundle = in.readBundle();
        almanacData = new AlmanacData(bundle.getBundle(BUNDLE_ALMANAC_DATA));
        celestialPosition = new CelestialPosition(bundle.getBundle(BUNDLE_CELESTIAL_POSITION));
        fields = bundle.getDoubleArray(BUNDLE_FIELDS);
        isSet = bundle.getBooleanArray(BUNDLE_ISSET);
        completed = bundle.getBoolean(BUNDLE_COMPLETED);
    }

    private SextantObservation(CelestialPosition iCelestialPosition, String fileString) {
        celestialPosition = iCelestialPosition;
        setNils();
        for (int i = 0; i < FIELD_COUNT; i++) {
            String s = fileString.substring( (i * 8), ((i * 8) + 9) );
            double val = Double.parseDouble(s.substring(0, 7));
            if (s.substring(7, 8).equals("-")) {val = val * -1;}
            fields[i] = val;
            isSet[i] = true;
        }
    }

    private void setNils() {
        for (int i = 0; i < FIELD_COUNT; i++) {Clear(i);}
        almanacData = new AlmanacData();
    }

    public void Set(int field, double value) {
        if (field < FIELD_COUNT) {
            fields[field] = value;
            isSet[field] = true;
        }
    }

    public double Get(int field) {
        if (field < FIELD_COUNT) {
            return fields[field];
        } else if (field == APPARENT_ALTITUDE) {
            return ApparentAltitude();
        } else if (field == TRUE_ALTITUDE) {
            return TrueAltitude();
        } else {return 0;}

    }

    public AlmanacData AlmanacData() {return almanacData;}

    public void setAlmanacData(AlmanacData iAlmanacData) {
        almanacData = iAlmanacData;
    }

    public CelestialPosition getCelestialPosition() {return celestialPosition;}

    public void setCelestialPosition(CelestialPosition iCelestialPosition) {celestialPosition = iCelestialPosition;}

    public void Clear(int field) {
        if (field < FIELD_COUNT) {
            fields[field] = 0;
            isSet[field] = false;
        }
    }

    private double ApparentAltitude() {
        return fields[SEXTANT_ALTITUDE] + fields[INDEX_ERROR] + fields[DIP];
    }

    private double TrueAltitude() {
        return ApparentAltitude() + fields[TOTAL_CORRECTION] + fields[ADDITIONAL_CORRECTION] + fields[MET_CORRECTION];
    }

    private double TotalCorrection() {
        return fields[TOTAL_CORRECTION] + fields[MET_CORRECTION] + fields[ADDITIONAL_CORRECTION];
    }

    private double Lha() {
        double lha;
        if (celestialPosition.getObserverPosition().getPosition().getLonEW().equals("E")) {
            lha = almanacData.Get(AlmanacData.FINAL_GHA) + celestialPosition.getObserverPosition().getPosition().getLon();
        } else {
            lha = almanacData.Get(AlmanacData.FINAL_GHA) - celestialPosition.getObserverPosition().getPosition().getLon();
        }
        return Constants.CorrectTo360(lha);
    }

    private double CalculatedAltitude() {
        if (almanacData != null) {
            SightReduction sightReduction = new SightReduction(Lha(), new Declination(almanacData.Get(AlmanacData.FINAL_DEC)), celestialPosition.getObserverPosition().getPosition());
            return sightReduction.CalculatedAltitude();
        } else {
            return 0;
        }
    }

    public void Complete() {completed = true;}

    public String toString() {
        try {
            String s;
            s = "Obs of " + celestialPosition.getCelestialBody().getName() + " at " + Constants.parseTime(celestialPosition.getObserverPosition().getObservationTime());
            if (completed) {
                s += "\n" + " Az: " + Azimuth().toString() + " " + Intercept().toString();
            }
            return s;
        } catch (Exception e) {
            return e.toString();
        }

    }

    public Intercept Intercept() {
        return new Intercept(TrueAltitude() - CalculatedAltitude());
    }

    private double PlotOffset() {
        double timeOffsetSecs;
        Calendar
    }

    private Quadrantal Azimuth() {
        if (almanacData != null) {
            SightReduction sightReduction = new SightReduction(Lha(), new Declination(almanacData.Get(AlmanacData.FINAL_DEC)), celestialPosition.getObserverPosition().getPosition());
            return sightReduction.Azimuth();
        } else {
            return new Quadrantal(0);
        }
    }

    public static final Parcelable.Creator<SextantObservation> CREATOR
            = new Parcelable.Creator<SextantObservation>() {
        public SextantObservation createFromParcel(Parcel in) {
            return new SextantObservation(in);
        }
        public SextantObservation[] newArray(int size) {
            return new SextantObservation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle bundle = new Bundle();
        bundle.putDoubleArray(BUNDLE_FIELDS, fields);
        bundle.putBooleanArray(BUNDLE_ISSET, isSet);
        bundle.putBundle(BUNDLE_ALMANAC_DATA, almanacData.GetMyBundle());
        bundle.putBundle(BUNDLE_CELESTIAL_POSITION, celestialPosition.GetMyBundle());
        bundle.putBoolean(BUNDLE_COMPLETED, completed);
        dest.writeBundle(bundle);
    }

    private enum FileStringPart {Completed, ObsPosn, Body, Almanac}
    private static String ParseFileString(String s, FileStringPart part) {

        int startPos;
        int stringLength;

        switch (part) {
            case Completed: startPos = 0; stringLength = 1; break;
            case ObsPosn: startPos = 1; stringLength = 48; break;
            case Body: startPos = 49; stringLength = 3; break;
            case Almanac: startPos = 52; stringLength = s.length() - 52; break;
            default: return "";
        }

        return s.substring(startPos, startPos + stringLength);

    }

    public String WriteToFile(File file) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(StringForObservationsOutput());
            bufferedWriter.newLine();
            bufferedWriter.write(StringForThisObservation());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            String stringReturn =  StringForObservationsOutput()+"||"+StringForThisObservation();
            for (int i = 0; i < FIELD_COUNT; i++) {
                String s = StringForThisObservation().substring((i * 8), ((i * 8) + 8));
                double val = Double.parseDouble(s.substring(0, 7));
                if (s.substring(7, 8).equals("-")) {val = val * -1;}
                stringReturn += "|" + String.valueOf(val);
            }
            return stringReturn;
        } catch (Exception e) {
            return e.toString();
        }
    }

    private String StringForObservationsOutput() {
        // Completed        - 0
        // ObserverPosition - Zd 00 | Ce 0000 | Lat 00.00000000 | LatNS A | Lon 000.00000000 | LonEW A | Year 0000 | Month 00 | Day 00 | Hour 00 | Min 00 | Sec 00 | Course 000.0 | Speed 00.0 | BrgType 0
        //                  - 00 0000 00.00000000 A 000.00000000 A 0000 00 00 00 00 00 000.0 00.0 0
        // CelestialBody    - 000 (Index No)
        // AlmanacData
        return  StringForCompleted()
                + StringForObserverPosition(celestialPosition.getObserverPosition())
                + StringForCelestialBody(celestialPosition.getCelestialBody())
                + StringForAlmanac(almanacData);
    }

    private String StringForCompleted() {
        if (completed) {return "1";} else {return "0";}
    }

    private String StringForAlmanac(AlmanacData almanacData) {
        return almanacData.StringToWriteToFile();
    }

    private String StringForObserverPosition(ObserverPosition observerPosition) {
        // ObserverPosition - Zd 00+ | Ce 0000+ | Lat 00.00000000 LatNS A | Lon 000.00000000 LonEW A | Year 0000 Month 00 Day 00 Hour 00 | Min 00 | Sec 00 | Course 000.0 | Speed 00.0
        //                  - 00+0000+00.00000000A000.00000000A00000000000000000.000.0
        NumberFormat nf00 = new DecimalFormat("00");
        NumberFormat nf0000 = new DecimalFormat("0000");
        String zd = nf00.format(Math.abs(observerPosition.getZd())) + observerPosition.getZdPlusMinus();
        String ce = nf0000.format(Math.abs(observerPosition.getCe())) + observerPosition.getCePlusMinus();
        String lat = (new DecimalFormat("00.0000")).format(observerPosition.getPosition().getLat()) + observerPosition.getPosition().getLatNS();
        String lon = (new DecimalFormat("000.0000")).format(observerPosition.getPosition().getLon()) + observerPosition.getPosition().getLonEW();
        Calendar c = observerPosition.getObservationTimeRaw();
        String date = nf0000.format(c.get(Calendar.YEAR)) + nf00.format(c.get(Calendar.MONTH)) + nf00.format(c.get(Calendar.DAY_OF_MONTH));
        String time = nf00.format(c.get(Calendar.HOUR_OF_DAY)) + nf00.format(c.get(Calendar.MINUTE)) + nf00.format(c.get(Calendar.SECOND));
        String ship = (new DecimalFormat("000.0")).format(observerPosition.getCourse().getTrueCourse()) + (new DecimalFormat("00.0")).format(observerPosition.getSpeed());
        return zd + ce + lat + lon + date + time + ship;
    }

    private String StringForCelestialBody(CelestialBody celestialBody) {
        // CelestialBody    - 000 (Index No)
        return (new DecimalFormat("000")).format(celestialBody.getIndexNo());
    }

    public String StringForThisObservation() {
        String s = "";
        for (int i = 0; i < FIELD_COUNT; i++) {
            s += IO_DOUBLES.format(Math.abs(fields[i]));
            if (fields[i] < 0) {s +="-";} else {s += "+";}
        }
        return s + " ";
    }

    public static ArrayList<SextantObservation> ReadObservationsFromFile(File file) {
        ArrayList<SextantObservation> obs = new ArrayList<SextantObservation>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String stringCommonData = ""; String stringObservationSpecific;
            while (stringCommonData != null) {
                stringCommonData = bufferedReader.readLine();
                stringObservationSpecific = bufferedReader.readLine();
                obs.add(ReadObservationFromString(stringCommonData, stringObservationSpecific));
            }
            bufferedReader.close();
        } catch (Exception e) {

        } finally {
            return obs;
        }
    }

    private static SextantObservation ReadObservationFromString(String stringCommonData, String stringObservationSpecific) {
        CelestialPosition celestialPosition = new CelestialPosition(ReadCelestialBodyFromFile(ParseFileString(stringCommonData, FileStringPart.Body)),
                ReadObserverPositionFromFile(ParseFileString(stringCommonData, FileStringPart.ObsPosn)));
        SextantObservation sextantObservation = new SextantObservation(celestialPosition, stringObservationSpecific);
        sextantObservation.setAlmanacData(new AlmanacData(ParseFileString(stringCommonData, FileStringPart.Almanac)));
        if (ParseFileString(stringCommonData, FileStringPart.Completed).equals("1")) {sextantObservation.Complete();}
        return sextantObservation;
    }

    private static CelestialBody ReadCelestialBodyFromFile(String s) {
        return CelestialBodies.CreateCelestialBody(Integer.parseInt(s));
    }

    private static ObserverPosition ReadObserverPositionFromFile(String s) {
//                    00-02 String zd = nf00.format(Math.abs(observerPosition.getZd())) + observerPosition.getZdPlusMinus();
//                    03-07 String ce = nf0000.format(Math.abs(observerPosition.getCe())) + observerPosition.getCePlusMinus();
//                    08-15 String lat = IO_DOUBLES.format(observerPosition.getPosition().getLat()) + observerPosition.getPosition().getLatNS();
//                    16-24 String lon = IO_DOUBLES.format(observerPosition.getPosition().getLon()) + observerPosition.getPosition().getLonEW();
//        25-28 29-30 31-32 String date = nf0000.format(c.get(Calendar.YEAR)) + nf00.format(c.get(Calendar.MONTH)) + nf00.format(c.get(Calendar.DAY_OF_MONTH));
//        33-34 35-36 37-38 String time = nf00.format(c.get(Calendar.HOUR_OF_DAY)) + nf00.format(c.get(Calendar.MINUTE)) + nf00.format(c.get(Calendar.SECOND));
//              39-43 44-48 String ship = (new DecimalFormat("000.0")).format(observerPosition.getCourse().getTrueCourse()) + (new DecimalFormat("00.0")).format(observerPosition.getSpeed());

        Latitude latitude = new Latitude(Double.parseDouble(s.substring(8, 15)), s.substring(15, 16));
        Longitude longitude = new Longitude(Double.parseDouble(s.substring(16, 24)), s.substring(24, 25));
        LatLonLocation latLonLocation = new LatLonLocation(latitude, longitude);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(s.substring(25, 29)), Integer.parseInt(s.substring(29, 31)), Integer.parseInt(s.substring(31, 33)),
                Integer.parseInt(s.substring(33, 35)), Integer.parseInt(s.substring(35, 37)), Integer.parseInt(s.substring(37, 39)));

        ObserverPosition observerPosition = new ObserverPosition(calendar, latLonLocation);
        observerPosition.setZoneDifference(Integer.parseInt(s.substring(0, 2)), s.substring(2, 3));
        observerPosition.setChronError(Integer.parseInt(s.substring(3, 7)), s.substring(7, 8));
        observerPosition.setShipMovement(new Quadrantal(Double.parseDouble(s.substring(39, 45))), Double.parseDouble(s.substring(45)));

        return observerPosition;

    }

}
