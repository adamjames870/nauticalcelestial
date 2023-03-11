package com.checkmyrest.nauticalcelestial;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations.AlmanacData;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations.SextantObservation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.io.File;

public class ActivityGetAlmanacData extends ActionBarActivity {

    SextantObservation sextantObservation;
    TextView txtAlmanacTimeOfObservation;
    TextView txtAlmanacBodyObserved;
    File observationsFile;
    TextView txtApparentAltitude;
    TextView txtSextantAngle;

    protected void onCreate(Bundle savedInstanceState) {

        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_get_almanc_data_star);
            sextantObservation = getIntent().getParcelableExtra(Constants.INTENT_EXTRA_OBSVERVATION);
            observationsFile = (File) getIntent().getSerializableExtra(Constants.INTENT_RECORD_FILENAME);
            SetInitialVariables();
            GetInfoFromBundles();

        } catch (Exception e) {
            ((TextView) findViewById(R.id.txtAlmanacBodyObserved)).setText(e.toString());
        }

    }

    private void SetInitialVariables() {
        txtAlmanacTimeOfObservation = (TextView) findViewById(R.id.txtAlmanacTimeOfObservation);
        txtAlmanacBodyObserved = (TextView) findViewById(R.id.txtAlmanacBodyObserved);
        txtSextantAngle = (TextView) findViewById(R.id.txtSextantAngle);
        txtApparentAltitude = (TextView) findViewById(R.id.txtApparentAltitude);
    }

    private void GetInfoFromBundles() {
        txtAlmanacTimeOfObservation.setText(Constants.parseTime(sextantObservation.getCelestialPosition().getObserverPosition().getObservationTime()) + " UTC");
        txtAlmanacBodyObserved.setText("Body observed: " + sextantObservation.getCelestialPosition().getCelestialBody().getName());
        txtSextantAngle.setText("SA: " + Constants.toSexisegimal(sextantObservation.Get(SextantObservation.SEXTANT_ALTITUDE)) );
    }

    public void clkSubmitAlmanacData(View view) {
        UpdateSextantObservation();
        sextantObservation.Complete();
        //txtApparentAltitude.setText(sextantObservation.WriteToFile(observationsFile));
        sextantObservation.WriteToFile(observationsFile);
        finish();
    }

    public void clkAlamancCalcApparentAlt(View view) {
        UpdateSextantObservation();
        txtApparentAltitude.setText(Constants.toSexisegimal(sextantObservation.Get(SextantObservation.APPARENT_ALTITUDE)));
    }

    private void UpdateSextantObservation() {
        sextantObservation.setAlmanacData(fetchAlmanacData());
        sextantObservation.Set(SextantObservation.DIP, fetchDip());
        sextantObservation.Set(SextantObservation.INDEX_ERROR, fetchIndexError());
        sextantObservation.Set(SextantObservation.TOTAL_CORRECTION, fetchTotalCorrection());
        sextantObservation.Set(SextantObservation.ADDITIONAL_CORRECTION, fetchAdditionalCorrection());
        sextantObservation.Set(SextantObservation.MET_CORRECTION, fetchArcCorrection());
    }

    private AlmanacData fetchAlmanacData() {
        AlmanacData almanacData = new AlmanacData();
        almanacData.Set(AlmanacData.GHA, fetchGha());
        almanacData.Set(AlmanacData.INC, fetchIncrement());
        almanacData.Set(AlmanacData.SHA, fetchSha());
        almanacData.Set(AlmanacData.DEC, fetchDeclination());
        return almanacData;
    }

    private double fetchGha() {
        double ghaDeg = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacGhaDeg)).getText().toString());
        double ghaMin = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacGhaMin)).getText().toString());
        return ghaDeg + (ghaMin / 60);
    }

    private double fetchIncrement() {
        double incDeg = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacIncDeg)).getText().toString());
        double incMin = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacIncMin)).getText().toString());
        return incDeg + (incMin / 60);
    }

    private double fetchSha() {
        double cvDeg = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacShaDeg)).getText().toString());
        double cvMin = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacShaMin)).getText().toString());
        return cvDeg + (cvMin / 60);
    }

    private double fetchDeclination() {
        double decDeg = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacDecDeg)).getText().toString());
        double decMin = Constants.Nz(((TextView) findViewById(R.id.txtAlmanacDecMin)).getText().toString());
        String decNs = String.valueOf(((Spinner) findViewById(R.id.cmbAlmanacDecNs)).getSelectedItem());
        if(decNs.equals("S")) {return -(decDeg + (decMin / 60));} else {return decDeg + (decMin / 60);}
    }

    private double fetchIndexError() {
        double ieDeg = Constants.Nz(((TextView) findViewById(R.id.txtIeDeg)).getText().toString());
        double ieMin = Constants.Nz(((TextView) findViewById(R.id.txtIeMin)).getText().toString());
        String ieOnOff = String.valueOf(((Spinner) findViewById(R.id.cmbIeOnOff)).getSelectedItem());
        double returnValue = ieDeg + (ieMin / 60);
        if (ieOnOff.equals("On")) {return returnValue * -1;} else {return returnValue;}
    }

    private double fetchDip() {
        double dipDeg = Constants.Nz(((TextView) findViewById(R.id.txtDipDeg)).getText().toString());
        double dipMin = Constants.Nz(((TextView) findViewById(R.id.txtDipMin)).getText().toString());
        String dipPlusMinus = String.valueOf(((Spinner) findViewById(R.id.cmbDipPlusMinus)).getSelectedItem());
        double returnValue = dipDeg + (dipMin / 60);
        if (dipPlusMinus.equals("-")) {return returnValue * -1;} else {return returnValue;}
    }

    private double fetchTotalCorrection () {
        double adjDeg = Constants.Nz(((TextView) findViewById(R.id.txtTcDeg)).getText().toString());
        double adjMin = Constants.Nz(((TextView) findViewById(R.id.txtTcMin)).getText().toString());
        String adjPlusMinus = String.valueOf(((Spinner) findViewById(R.id.cmbTcPlusMinus)).getSelectedItem());
        double returnValue = adjDeg + (adjMin / 60);
        if (adjPlusMinus.equals("-")) {return returnValue * -1;} else {return returnValue;}
    }

    private double fetchAdditionalCorrection () {
        double adjDeg = Constants.Nz(((TextView) findViewById(R.id.txtAcDeg)).getText().toString());
        double adjMin = Constants.Nz(((TextView) findViewById(R.id.txtAcMin)).getText().toString());
        String adjPlusMinus = String.valueOf(((Spinner) findViewById(R.id.cmbAcPlusMinus)).getSelectedItem());
        double returnValue = adjDeg + (adjMin / 60);
        if (adjPlusMinus.equals("-")) {return returnValue * -1;} else {return returnValue;}
    }

    private double fetchArcCorrection () {
        double adjDeg = Constants.Nz(((TextView) findViewById(R.id.txtArcDeg)).getText().toString());
        double adjMin = Constants.Nz(((TextView) findViewById(R.id.txtArcMin)).getText().toString());
        String adjPlusMinus = String.valueOf(((Spinner) findViewById(R.id.cmbArcPlusMinus)).getSelectedItem());
        double returnValue = adjDeg + (adjMin / 60);
        if (adjPlusMinus.equals("-")) {return returnValue * -1;} else {return returnValue;}
    }

}