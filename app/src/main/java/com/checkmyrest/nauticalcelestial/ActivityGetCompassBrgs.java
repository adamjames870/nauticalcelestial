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

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBody;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.Observations.CompassObservation;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;
import com.checkmyrest.nauticalcelestial.CommonNavItems.MagAtion;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Quadrantal;

public class ActivityGetCompassBrgs extends ActionBarActivity{

    EditText txtGyroBrg;
    EditText txtMagBearing;
    EditText txtVariationMagnitude;
    Spinner cmbVariationDirection;
    CelestialPosition celestialPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_get_compass_brgs);
        Intent intent = getIntent();

        ObserverPosition observerPosition = intent.getParcelableExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION);
        CelestialBody celestialBody = intent.getParcelableExtra(Constants.INTENT_EXTRA_CELESTIAL_BODY);
        celestialPosition = new CelestialPosition(celestialBody, observerPosition);

        SetInitialVariables();

        ( (TextView) findViewById(R.id.txtGcbDebugText) ).setText(celestialPosition.getObserverPosition().toString());

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

        txtGyroBrg = (EditText) findViewById(R.id.txtGyroBrg);
        txtMagBearing = (EditText) findViewById(R.id.txtMagBrg);
        txtVariationMagnitude = (EditText) findViewById(R.id.txtVariation);
        cmbVariationDirection = (Spinner) findViewById(R.id.cmbVar);

    }

    public void clkGoToSightReduction(View view) {
        try {

            CompassObservation compassObservation = new CompassObservation(celestialPosition, fetchGyroBrg(), fetchMagBrg(), fetchVariation());

            Intent intent = new Intent(ActivityGetCompassBrgs.this, ActivitySightReduction.class);
            // intent.putExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION, celestialPosition.getObserverPosition());
            // intent.putExtra(Constants.INTENT_EXTRA_CELESTIAL_BODY, celestialPosition.getCelestialBody());
            intent.putExtra(Constants.INTENT_EXTRA_OBSVERVATION, compassObservation);

            startActivity(intent);

        } catch (Exception e) {
            ( (Button) findViewById(R.id.btnGoToReduction) ).setText(e.toString());
        }
    }

    private double fetchVarMag() {return Constants.Nz(txtVariationMagnitude.getText().toString());}
    private int fetchVarDir() {if (String.valueOf(cmbVariationDirection.getSelectedItem()).equals("E")) {return MagAtion.EAST;} else {return MagAtion.WEST;}}
    private MagAtion fetchVariation() {return new MagAtion(MagAtion.VARIATION, fetchVarMag(), fetchVarDir());}
    private Quadrantal fetchMagBrg() {return new Quadrantal(Constants.Nz(txtMagBearing.getText().toString()));}
    private Quadrantal fetchGyroBrg() {return new Quadrantal(Constants.Nz(txtGyroBrg.getText().toString()));}

}

