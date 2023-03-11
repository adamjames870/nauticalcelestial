package com.checkmyrest.nauticalcelestial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.LatLonLocation;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Latitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Longitude;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    private TextView txtDateDay;
    private Spinner cmbDateMonth;
    private Spinner cmbDateYear;
    private TextView txtTimeHour;
    private TextView txtTimeMins;
    private TextView txtTimeSecs;
    private Spinner cmbZdPlusMinus;
    private TextView txtZdDifference;
    private TextView txtCeHour;
    private TextView txtCeMins;
    private TextView txtCeSecs;
    private Spinner cmbCeFastSlow;
    private Button btnMakeStarfinder;
    private EditText txtDrHdg;
    private EditText txtDrSpeed;
    private EditText txtDrLatDeg;
    private EditText txtDrLatMin;
    private EditText txtDrLonDeg;
    private EditText txtDrLonMin;
    private Spinner cmbBearingTypeToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetInitialVariables();
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
        btnMakeStarfinder = (Button) findViewById(R.id.btnMakeStarfinder);
        txtDateDay = (TextView) findViewById(R.id.txtDateDay);
        cmbDateMonth = (Spinner) findViewById(R.id.cmbDateMonth);
        cmbDateYear = (Spinner) findViewById(R.id.cmbDateYear);
        txtTimeHour = (TextView) findViewById(R.id.txtTimeHour);
        txtTimeMins = (TextView) findViewById(R.id.txtTimeMins);
        txtTimeSecs = (TextView) findViewById(R.id.txtTimeSecs);
        cmbZdPlusMinus = (Spinner) findViewById(R.id.cmbZdPlusminus);
        txtZdDifference = (TextView) findViewById(R.id.txtTimeZd);
        txtCeHour = (TextView) findViewById(R.id.txtCeHour);
        txtCeMins = (TextView) findViewById(R.id.txtCeMins);
        txtCeSecs = (TextView) findViewById(R.id.txtCeSecs);
        cmbCeFastSlow = (Spinner) findViewById(R.id.cmbCeFastSlow);
        txtDrLatDeg = (EditText) findViewById(R.id.txtDrLatDeg);
        txtDrLatMin = (EditText) findViewById(R.id.txtDrLatMin);
        txtDrLonDeg = (EditText) findViewById(R.id.txtDrLonDeg);
        txtDrLonMin = (EditText) findViewById(R.id.txtDrLonMin);
        txtDrHdg = (EditText) findViewById(R.id.txtDrHdg);
        txtDrSpeed = (EditText) findViewById(R.id.txtDrSpeed);
        cmbBearingTypeToShow = (Spinner) findViewById(R.id.cmbBearingTypeToShow);
        SetTimeNow(btnMakeStarfinder);
        SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int ceHour = sharedPreferences.getInt(getString(R.string.pref_chron_err_hour), 0);
        int ceMins = sharedPreferences.getInt(getString(R.string.pref_chron_err_mins), 0);
        int ceSecs = sharedPreferences.getInt(getString(R.string.pref_chron_err_secs), 0);
        int ceDirn = sharedPreferences.getInt(getString(R.string.pref_chron_err_dirn), 0);
        setChronError(ceHour, ceMins, ceSecs, ceDirn);
        int zd = sharedPreferences.getInt(getString(R.string.pref_zone_difference), 0);
        setZoneDifference(zd);
    }

    private void setChronError(int hour, int mins, int secs, int direction) {
        txtCeHour.setText(String.valueOf(hour));
        txtCeMins.setText(String.valueOf(mins));
        txtCeSecs.setText(String.valueOf(secs));
        cmbCeFastSlow.setSelection(direction);
    }

    private void setZoneDifference(int zd) {
        txtZdDifference.setText(String.valueOf(Math.abs(zd)));
        if (zd < 0) {cmbZdPlusMinus.setSelection(1);} else {cmbZdPlusMinus.setSelection(0);}
    }

    private Quadrantal fetchHeading() {
        return new Quadrantal(Constants.Nz(txtDrHdg.getText().toString()));
    }

    private int fetchBearingTypeToShow() {
        if ( cmbBearingTypeToShow.getSelectedItem().toString().equals("Relative") ) {return Constants.BEARING_RELATIVE;}
        else {return Constants.BEARING_TRUE;}
    }

    private double fetchSpeed() {
        return Constants.Nz(txtDrSpeed.getText().toString());
    }

    private Longitude fetchLongitude() {
        double lonDeg = Constants.Nz(txtDrLonDeg.getText().toString());
        double lonMin = Constants.Nz(txtDrLonMin.getText().toString());
        String lonEw = String.valueOf(((Spinner) findViewById(R.id.cmbDrLonEw)).getSelectedItem());
        return new Longitude(lonDeg + (lonMin / 60), lonEw);
    }

    private Latitude fetchLatitude() {
        double latDeg = Constants.Nz(txtDrLatDeg.getText().toString());
        double latMin = Constants.Nz(txtDrLatMin.getText().toString());
        String latNs = String.valueOf(((Spinner) findViewById(R.id.cmbDrLatNs)).getSelectedItem());
        return new Latitude(latDeg + (latMin / 60), latNs);
    }

    private LatLonLocation fetchPosition() {
        return new LatLonLocation(fetchLatitude(), fetchLongitude());
    }

    private Calendar fetchObservationTime() {
        Calendar observationTime = Calendar.getInstance();
        observationTime.set(fetchYear(), fetchMonth(), fetchDay(), fetchHour(), fetchMinutes(), fetchSeconds());
        return observationTime;
    }

    private int fetchYear() {return Integer.parseInt(cmbDateYear.getSelectedItem().toString());}
    private int fetchMonth() {return cmbDateMonth.getSelectedItemPosition() + 1;}
    private int fetchDay() {return Integer.parseInt(txtDateDay.getText().toString());}
    private int fetchHour() {return Integer.parseInt(txtTimeHour.getText().toString());}
    private int fetchMinutes() {return Integer.parseInt(txtTimeMins.getText().toString());}
    private int fetchSeconds() {return Integer.parseInt(txtTimeSecs.getText().toString());}
    private int fetchZoneDifferenceMag() {return Integer.parseInt(txtZdDifference.getText().toString());}
    private String fetchZoneDifferenceDir() {return cmbZdPlusMinus.getSelectedItem().toString();}
    private int fetchChronErrorMag() {return ( fetchChronErrorHour() * 3600 ) + ( fetchChronErrorMins() * 60 ) + ( fetchChronErrorSecs() );}
    private String fetchChronErrorDir() {return cmbCeFastSlow.getSelectedItem().toString();}
    private int fetchChronErrorHour() {return Integer.parseInt(txtCeHour.getText().toString());}
    private int fetchChronErrorMins() {return Integer.parseInt(txtCeMins.getText().toString());}
    private int fetchChronErrorSecs() {return Integer.parseInt(txtCeSecs.getText().toString());}

    public void SetTimeNow(View view) {
        Calendar Now = Calendar.getInstance();
        txtDateDay.setText(String.valueOf(Now.get(Calendar.DAY_OF_MONTH)));
        cmbDateMonth.setSelection(Now.get(Calendar.MONTH));
        cmbDateYear.setSelection(Now.get(Calendar.YEAR) - 2000);
        txtTimeHour.setText(String.valueOf(Now.get(Calendar.HOUR_OF_DAY)));
        txtTimeMins.setText(String.valueOf(Now.get(Calendar.MINUTE)));
        txtTimeSecs.setText(String.valueOf(Now.get(Calendar.SECOND)));
    }

    public void clkMakeStarfinder(View view) {

        try {

            File file = File.createTempFile("ObservationData", null, this.getCacheDir());
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("");
            bufferedWriter.flush();
            bufferedWriter.close();

            ObserverPosition observerPosition = new ObserverPosition(fetchObservationTime(), fetchPosition());
            observerPosition.setZoneDifference(fetchZoneDifferenceMag(), fetchZoneDifferenceDir());
            observerPosition.setChronError(fetchChronErrorMag(), fetchChronErrorDir());
            observerPosition.setShipMovement(fetchHeading(), fetchSpeed());
            observerPosition.setBearingType(fetchBearingTypeToShow());

            Intent intent = new Intent(MainActivity.this, ActivityStarfinder.class);
            intent.putExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION, observerPosition);
            intent.putExtra(Constants.INTENT_RECORD_FILENAME, file);
            startActivity(intent);

        } catch (Exception e) {
            btnMakeStarfinder.setText(e.toString());
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Context context = MainActivity.this;
        SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt (getString(R.string.pref_chron_err_hour), fetchChronErrorHour());
        editor.putInt (getString(R.string.pref_chron_err_mins), fetchChronErrorMins());
        editor.putInt (getString(R.string.pref_chron_err_secs), fetchChronErrorSecs());
        if (fetchChronErrorDir().equals("Fast")) {editor.putInt (getString(R.string.pref_chron_err_dirn), 0);} else {editor.putInt (getString(R.string.pref_chron_err_dirn), 1);}
        int zd; if (fetchZoneDifferenceDir().equals("+")) {zd = fetchZoneDifferenceMag();} else {zd = -1 * fetchZoneDifferenceMag();}
        editor.putInt (getString(R.string.pref_zone_difference), zd);
        editor.commit();
    }

}
