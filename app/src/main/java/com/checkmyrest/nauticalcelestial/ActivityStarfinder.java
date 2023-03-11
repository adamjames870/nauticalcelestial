package com.checkmyrest.nauticalcelestial;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBodies;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialBodies.CelestialBody;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.CelestialPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.ObserverPosition;
import com.checkmyrest.nauticalcelestial.CelestialSpecific.SimpleItems.StarAzimthComparator;
import com.checkmyrest.nauticalcelestial.CommonNavItems.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

public class ActivityStarfinder extends ActionBarActivity {

    ListView lstStars;
    TextView txtDebugText;
    ArrayList<CelestialPosition> Starfinder = new ArrayList<CelestialPosition>();
    ArrayAdapter mArrayAdapter;
    ObserverPosition observerPosition;
    File observationsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_starfinder);
        Intent intent = getIntent();

        SetInitialVariables();
        observerPosition = intent.getParcelableExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION);
        observationsFile = (File) intent.getSerializableExtra(Constants.INTENT_RECORD_FILENAME);
        PopulateStarfinder();

        txtDebugText.setText(observerPosition.toString());

    }

    private void SetInitialVariables() {
        lstStars = (ListView) findViewById(R.id.lstStars);
        mArrayAdapter = new ArrayAdapter(this, R.layout.stars_layout, Starfinder);
        lstStars.setAdapter(mArrayAdapter);
        txtDebugText = (TextView) findViewById(R.id.txtDebugText);
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

    public void PopulateStarfinder() {

        Starfinder.clear();

        for (CelestialBody celestialBody: CelestialBodies.ListOfCelestialBodies()) {
            CelestialPosition celestialPosition = new CelestialPosition(celestialBody, observerPosition);
            if (celestialPosition.Altitude() > 0) {Starfinder.add(celestialPosition);}
        }

        Comparator<CelestialPosition> StarSorter = new StarAzimthComparator();
        mArrayAdapter.sort(StarSorter);
        mArrayAdapter.notifyDataSetChanged();

        lstStars.setOnItemLongClickListener(starFinderLongClickListener);
        lstStars.setOnItemClickListener(starFinderClickListener);

    }

    private AdapterView.OnItemLongClickListener starFinderLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            try {

                Intent intent = new Intent(ActivityStarfinder.this, ActivityGetCompassBrgs.class);

                CelestialPosition thisCelestialPosition = (CelestialPosition) mArrayAdapter.getItem(position);
                ObserverPosition observerPosition = thisCelestialPosition.getObserverPosition();

                CelestialBody celestialBody = thisCelestialPosition.getCelestialBody();

                intent.putExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION, observerPosition.AdvancePosition(Calendar.getInstance()));
                intent.putExtra(Constants.INTENT_EXTRA_CELESTIAL_BODY, celestialBody);

                startActivity(intent);

            } catch (Exception e) {
                txtDebugText.setText(e.toString());
            } finally {
                return true;
            }
        }
    };

    private AdapterView.OnItemClickListener starFinderClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {

                Intent intent = new Intent(ActivityStarfinder.this, ActivityRecordAltitudes.class);

                CelestialPosition thisCelestialPosition = (CelestialPosition) mArrayAdapter.getItem(position);
                ObserverPosition observerPosition = thisCelestialPosition.getObserverPosition();

                CelestialBody celestialBody = thisCelestialPosition.getCelestialBody();

                intent.putExtra(Constants.INTENT_EXTRA_OBSERVER_POSITION, observerPosition.AdvancePosition(Calendar.getInstance()));
                intent.putExtra(Constants.INTENT_EXTRA_CELESTIAL_BODY, celestialBody);
                intent.putExtra(Constants.INTENT_RECORD_FILENAME, observationsFile);

                startActivity(intent);

            } catch (Exception e) {
                txtDebugText.setText(e.toString());
            }
        }
    };

}
