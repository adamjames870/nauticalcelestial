package com.checkmyrest.nauticalcelestial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBody;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations.SextantObservation;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class ActivityRecordAltitudes extends ActionBarActivity {

    EditText txtRaSextAltDeg;
    EditText txtRaSextAltMin;
    TextView txtRaTimeOfObservation;
    TextView txtRaBodyObserved;
    ArrayAdapter mArrayAdapter;
    TextView txtRaShipMovement;

    File observationsFile;
    CelestialPosition celestialPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_altitudes);
        Intent intent = getIntent();

        observationsFile = (File) intent.getSerializableExtra(Constants.INTENT_RECORD_FILENAME);
        ObserverPosition observerPosition = intent.getParcelableExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION);
        CelestialBody celestialBody = intent.getParcelableExtra(Constants.INTENT_EXTRA_CELESTIAL_BODY);
        celestialPosition = new CelestialPosition(celestialBody, observerPosition);

        SetInitialVariables();
        FillObservationsList();

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
        txtRaSextAltDeg = (EditText) findViewById(R.id.txtRaSextAltDeg);
        txtRaSextAltMin = (EditText) findViewById(R.id.txtRaSextAltMin);
        txtRaTimeOfObservation = (TextView) findViewById(R.id.txtRaTimeOfObservation);
        txtRaBodyObserved = (TextView) findViewById(R.id.txtRaBodyObserved);
        txtRaShipMovement = (TextView) findViewById(R.id.txtRaShipMovement);
        txtRaTimeOfObservation.setText(Constants.parseTime(celestialPosition.getObserverPosition().getObservationTime()) + " UTC");
        txtRaBodyObserved.setText("Body observed: " + celestialPosition.getCelestialBody().getName());
        txtRaShipMovement.setText("Heading " + celestialPosition.getObserverPosition().getCourse().toString() + " x " +
            celestialPosition.getObserverPosition().getSpeed() + "Kn");
    }

    private double fetchAltitudeDegs() {return Constants.Nz(txtRaSextAltDeg.getText().toString());}
    private double fetchAltitudeMins() {return Constants.Nz(txtRaSextAltMin.getText().toString());}
    private double fetchAltitude() {return fetchAltitudeDegs() + ( fetchAltitudeMins() / 60 );}

    public void clkSubmitReturn(View view) {
        try {
            SaveObservation();
            finish();
        } catch (Exception e) {
            txtRaBodyObserved.setText(e.toString());
        }
    }

    public void clkSubmitAdvance(View view) {
        SaveObservation();
        FillObservationsList();
    }

    private void SaveObservation() {
        // txtRaBodyObserved.setText(fetchSextantObservation().WriteToFile(observationsFile));
        fetchSextantObservation().WriteToFile(observationsFile);
    }

    private SextantObservation fetchSextantObservation() {
        return new SextantObservation(celestialPosition, fetchAltitude());
    }

    private void FillObservationsList() {

        try {

            ListView lstObservations = (ListView) findViewById(R.id.lstObservations);
            ArrayList<SextantObservation> ObsList = new ArrayList<SextantObservation>();
            mArrayAdapter = new ArrayAdapter(this, R.layout.stars_layout, ObsList);
            lstObservations.setAdapter(mArrayAdapter);

            ObsList.clear();

            for (SextantObservation sextantObservation: SextantObservation.ReadObservationsFromFile(observationsFile)) {
                ObsList.add(sextantObservation);
            }

            mArrayAdapter.notifyDataSetChanged();
            lstObservations.setOnItemClickListener(observationListClickListener);

        } catch (Exception e) {
            txtRaBodyObserved.setText(e.toString());
        }
    }

    private AdapterView.OnItemClickListener observationListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {

                Intent intent = new Intent(ActivityRecordAltitudes.this, ActivityGetAlmanacData.class);

                SextantObservation thisObservation = (SextantObservation) mArrayAdapter.getItem(position);

                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(observationsFile));
                bufferedWriter.write("");
                bufferedWriter.flush();
                bufferedWriter.close();

                for (int i = 0; i < mArrayAdapter.getCount(); i++) {
                    if (i != position) {( (SextantObservation) mArrayAdapter.getItem(i) ).WriteToFile(observationsFile);}
                }

                intent.putExtra(Constants.INTENT_RECORD_FILENAME, observationsFile);
                intent.putExtra(Constants.INTENT_EXTRA_OBSVERVATION, thisObservation);

                startActivityForResult(intent, 0);

            } catch (Exception e) {
                txtRaBodyObserved.setText(e.toString());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FillObservationsList();
    }

}
