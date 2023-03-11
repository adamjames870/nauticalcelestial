package com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class AlmanacData implements Parcelable{

    protected static final NumberFormat IO_DOUBLES = new DecimalFormat("000.0000"); // 8 chars

    private static String BUNDLE_FIELDS = "AlmanacDataFields";
    private static String BUNDLE_ISSET = "AlmanacDataIsSet";

    public static final int GHA = 0;
    public static final int INC = 1;
    public static final int V_CORR = 2;
    public static final int SHA = 3;
    public static final int DEC = 4;
    public static final int D_CORR = 5;

    private static int FIELD_COUNT = 6;

    public static int FINAL_GHA = 7;
    public static int FINAL_DEC = 8;

    public static int TOTAL_FIELDS = 9;

    private double fields[] = new double[TOTAL_FIELDS];
    private boolean isSet[] = new boolean[TOTAL_FIELDS];

    public AlmanacData() {setNils();}
    public AlmanacData(String fileString) {
        for (int i = 0; i < FIELD_COUNT; i++) {
            String s = fileString.substring( (i * 9), ((i * 9) + 10) );
            double val = Double.parseDouble(s.substring(0, 8));
            if (s.substring(8, 9).equals("-")) {val = val * -1;}
            fields[i] = val;
            isSet[i] = true;
        }
    }

    private void setNils() {for (int i = 0; i < FIELD_COUNT; i++) {Clear(i);}}

    public void Set(int field, double value) {
        if (field < FIELD_COUNT) {
            fields[field] = value;
            isSet[field] = true;
        }
    }

    public double Get(int field) {
        if (field < FIELD_COUNT) {
            return fields[field];
        } else if (field == FINAL_GHA) {return CalculateGha();
        } else if (field == FINAL_DEC) {return CalculateDec();
        } else {return 0;}

    }

    private double CalculateDec() {
        return Get(DEC) + Get(D_CORR);
    }

    private double CalculateGha() {
        return Get(GHA) + Get(INC) + Get(V_CORR) + Get(SHA);
    }

    public boolean IsSet(int field) {
        return isSet[field];
    }

    public void Clear(int field) {
        if (field < FIELD_COUNT) {
            fields[field] = 0;
            isSet[field] = false;
        }
    }

    public String StringToWriteToFile() {
        String s = "";
        for (int i = 0; i < FIELD_COUNT; i++) {
            s += IO_DOUBLES.format(Math.abs(fields[i]));
            if (fields[i] < 0) {s +="-";} else {s += "+";}
        }
        return s + " ";
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < FIELD_COUNT; i++) {
            if (isSet[i]) {s += parseField(i) + ": " + toSexisegimal(fields[i]) + " ";}
        }
        return s;
    }

    private String parseField(int fieldToParse) {
        switch (fieldToParse) {
            case GHA: return "GHA";
            case INC: return "Inc";
            case V_CORR: return "cV";
            case SHA: return "SHA";
            case DEC: return "Dec";
            case D_CORR: return "cD";
            default: return "";
        }
    }

    private String toSexisegimal(double d) {
        NumberFormat nfDeg = new DecimalFormat("00");
        nfDeg.setRoundingMode(RoundingMode.DOWN);
        NumberFormat nfMin = new DecimalFormat("00.0");
        String s;
        if (d < 0) {s = "-"; d = Math.abs(d);} else {s = "+";}
        if (d < 1) {s += nfMin.format(d) + "'";} else {s += nfDeg.format(d) + "\u00B0" + nfMin.format((d % 1) * 60) + "'";}
        return s;
    }

    public static final Parcelable.Creator<AlmanacData> CREATOR
            = new Parcelable.Creator<AlmanacData>() {
        public AlmanacData createFromParcel(Parcel in) {
            return new AlmanacData(in.readBundle());
        }
        public AlmanacData[] newArray(int size) {
            return new AlmanacData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(GetMyBundle());
    }

    public Bundle GetMyBundle() {
        Bundle bundle = new Bundle();
        bundle.putDoubleArray(BUNDLE_FIELDS, fields);
        bundle.putBooleanArray(BUNDLE_ISSET, isSet);
        return bundle;
    }

    public AlmanacData(Bundle in) {
        fields = in.getDoubleArray(BUNDLE_FIELDS);
        isSet = in.getBooleanArray(BUNDLE_ISSET);
    }

}


