package com.checkmyrest.nauticalcelestial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations.CompassObservation;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.Declination;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.SightReduction;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Latitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.MagAtion;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.text.DecimalFormat;

public class ActivitySightReduction extends ActionBarActivity{

    TextView txtAzimuth;
    TextView txtGyroError;
    TextView txtCompassError;
    TextView txtDeviation;
    EditText txtLonDeg;
    EditText txtLonMin;
    Spinner cmbLonEw;
    EditText txtLatDeg;
    EditText txtLatMin;
    Spinner cmbLatNs;
    TextView txtTimeOfObservation;
    TextView txtBodyObserved;
    EditText txtGyroBrg;
    EditText txtMagBearing;
    EditText txtVarMagnitude;
    Spinner cmbVarDirection;
    Button btnCalculateCompassError;
    CompassObservation compassObservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_sight_reduction);
            Intent intent = getIntent();

            compassObservation = intent.getParcelableExtra(Constants.INTENT_EXTRA_OBSVERVATION);

            SetInitialVariables();
            GetInfoFromBundles();

        } catch (Exception e) {
            ( (TextView) findViewById(R.id.txtBodyObserved) ).setText(e.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void SetInitialVariables() {
        txtAzimuth = (TextView) findViewById(R.id.txtAzimuth);
        txtGyroError = (TextView) findViewById(R.id.txtGyroError);
        txtCompassError = (TextView) findViewById(R.id.txtCompassError);
        txtDeviation = (TextView) findViewById(R.id.txtDeviation);
        txtLonDeg = (EditText) findViewById(R.id.txtSrLonDeg);
        txtLonMin = (EditText) findViewById(R.id.txtSrLonMin);
        cmbLonEw = (Spinner) findViewById(R.id.cmbSrLonEw);
        txtLatDeg = (EditText) findViewById(R.id.txtSrLatDeg);
        txtLatMin = (EditText) findViewById(R.id.txtSrLatMin);
        cmbLatNs = (Spinner) findViewById(R.id.cmbSrLatNs);
        txtTimeOfObservation = (TextView) findViewById(R.id.txtTimeOfObservation);
        txtBodyObserved = (TextView) findViewById(R.id.txtBodyObserved);
        btnCalculateCompassError = (Button) findViewById(R.id.btnCalculateCompassError);
        txtGyroBrg = (EditText) findViewById(R.id.txtSrGyroBrg);
        txtMagBearing = (EditText) findViewById(R.id.txtSrMagBrg);
        txtVarMagnitude = (EditText) findViewById(R.id.txtSrVarMag);
        cmbVarDirection = (Spinner) findViewById(R.id.cmbSrVarDir);
    }

    private void GetInfoFromBundles() {
        try {

            LatLonLocation positionOfObservation = compassObservation.getCelestialPosition().getObserverPosition().getPosition();

            txtLatDeg.setText(String.valueOf(Constants.getDegs(positionOfObservation.getLatitude())));
            txtLatMin.setText(String.valueOf(Constants.getMins(positionOfObservation.getLatitude())));
            cmbLatNs.setSelection(positionOfObservation.getLatitude().getNsSelected());

            txtLonDeg.setText(String.valueOf(Constants.getDegs(positionOfObservation.getLongitude())));
            txtLonMin.setText(String.valueOf(Constants.getMins(positionOfObservation.getLongitude())));
            cmbLonEw.setSelection(positionOfObservation.getLongitude().getEwSelected());

            txtTimeOfObservation.setText(Constants.parseTime(compassObservation.getCelestialPosition().getObserverPosition().getObservationTime()) + " UTC");
            txtBodyObserved.setText("Body observed: " + compassObservation.getCelestialPosition().getCelestialBody().getName());

            txtGyroBrg.setText(Constants.nfBrg().format(compassObservation.getGyroBrg().getTrueCourse()));
            txtMagBearing.setText(Constants.nfBrg().format(compassObservation.getMagBearing().getTrueCourse()));
            txtVarMagnitude.setText(Constants.nfMin().format(compassObservation.getVariation().getAbsMagAtion()));
            if (compassObservation.getVariation().getDirMagAtion() == MagAtion.EAST) {cmbVarDirection.setSelection(0);} else {cmbVarDirection.setSelection(1);}

        } catch (Exception e) {
            txtBodyObserved.setText(e.toString());
        }
    }

    public void calculateCompassError(View view) {
        try {
            DecimalFormat f = new DecimalFormat("#.0");
            txtAzimuth.setText("True Brg: " + f.format(fetchSimpleSightReduction().Azimuth().getTrueCourse()) + "\u00B0T");
            txtGyroError.setText(fetchObservation().getGyroError().toString());
            txtCompassError.setText(fetchObservation().getCompassError().toString());
            txtDeviation.setText(fetchObservation().getDeviation().toString());
        } catch (Exception e) {
            txtAzimuth.setText(e.toString());
        }
    }

    private SightReduction fetchSimpleSightReduction() {
        return new SightReduction(LocalHourAngle(), CorrectedDeclination(), new LatLonLocation(FetchLatitude(), FetchLongitude()));
    }

    private double FetchGha() {
        double ghaDeg = Constants.Nz(((TextView) findViewById(R.id.txtGhaDeg)).getText().toString());
        double ghaMin = Constants.Nz(((TextView) findViewById(R.id.txtGhaMin)).getText().toString());
        return ghaDeg + (ghaMin / 60);
    }

    private double FetchIncrement() {
        double incDeg = Constants.Nz(((TextView) findViewById(R.id.txtIncDeg)).getText().toString());
        double incMin = Constants.Nz(((TextView) findViewById(R.id.txtIncMin)).getText().toString());
        return incDeg + (incMin / 60);
    }

    private double FetchSha() {
        double cvDeg = Constants.Nz(((TextView) findViewById(R.id.txtShaDeg)).getText().toString());
        double cvMin = Constants.Nz(((TextView) findViewById(R.id.txtShaMin)).getText().toString());
        return cvDeg + (cvMin / 60);
    }

    private Longitude FetchLongitude() {
        double lonDeg = Constants.Nz(txtLonDeg.getText().toString());
        double lonMin = Constants.Nz(txtLonMin.getText().toString());
        String lonEw = String.valueOf(cmbLonEw.getSelectedItem());
        return new Longitude(lonDeg + (lonMin / 60), lonEw);
    }

    private Latitude FetchLatitude() {
        double latDeg = Constants.Nz(txtLatDeg.getText().toString());
        double latMin = Constants.Nz(txtLatMin.getText().toString());
        String latNs = String.valueOf(cmbLatNs.getSelectedItem());
        return new Latitude(latDeg + (latMin / 60), latNs);
    }

    private Latitude FetchDeclination() {
        double decDeg = Constants.Nz(((TextView) findViewById(R.id.txtDecDeg)).getText().toString());
        double decMin = Constants.Nz(((TextView) findViewById(R.id.txtDecMin)).getText().toString());
        String decNs = String.valueOf(((Spinner) findViewById(R.id.cmbDecNs)).getSelectedItem());
        return new Latitude(decDeg + (decMin / 60), decNs);
    }

    private double LocalHourAngle() {
        double corrGha = FetchGha() + FetchIncrement() + FetchSha();
        if (FetchLongitude().getLonEW().equals("E"))
        {return Constants.CorrectTo360( corrGha + FetchLongitude().getLon() );}
        else
        {return Constants.CorrectTo360( corrGha - FetchLongitude().getLon() );}
    }

    private Declination CorrectedDeclination() {
        return new Declination(FetchDeclination().getLat(), FetchDeclination().getLatNS());
    }

    private CompassObservation fetchObservation() {
        CompassObservation newCompassObservation = new CompassObservation(compassObservation.getCelestialPosition(), fetchGyroBrg(), fetchMagBrg(), fetchVariation());
        newCompassObservation.setTrueBearing(new Quadrantal(fetchSimpleSightReduction().Azimuth().getTrueCourse()));
        return newCompassObservation;
    }

    private double fetchVarMag() {return Constants.Nz(txtVarMagnitude.getText().toString());}
    private int fetchVarDir() {if (String.valueOf(cmbVarDirection.getSelectedItem()).equals("E")) {return MagAtion.EAST;} else {return MagAtion.WEST;}}

    private MagAtion fetchVariation() {return new MagAtion(MagAtion.VARIATION, fetchVarMag(), fetchVarDir());}
    private Quadrantal fetchMagBrg() {return new Quadrantal(Constants.Nz(txtMagBearing.getText().toString()));}
    private Quadrantal fetchGyroBrg() {return new Quadrantal(Constants.Nz(txtGyroBrg.getText().toString()));}

}
