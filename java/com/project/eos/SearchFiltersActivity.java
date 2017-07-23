package com.project.eos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;


public class SearchFiltersActivity  extends AppCompatActivity {

    int MY_LOCATION = 1;
    int SEARCHING_LOCATION = 2;
    public  SeekBar seek_bar;
    public TextView seek_Text;
    public TextView searchlocationText;
    double searchLat;
    double seacrhLong;
    CharSequence searchLoc;
    String distance;
    FloatingActionButton mylocation,searchinglocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchfilters);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        seek_bar = (SeekBar) findViewById(R.id.seekBar);
        seek_Text = (TextView) findViewById(R.id.DistanceText);
        searchlocationText = (TextView) findViewById(R.id.searchlocation);
        searchinglocation = (FloatingActionButton) findViewById(R.id.searchlocmapButton);
        OperateSeekbar();
        //////////////////////////////////////////////

        searchinglocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(SearchFiltersActivity.this), SEARCHING_LOCATION);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void OperateSeekbar() {
        seek_bar.setMax(500);
        seek_Text.setText(seek_bar.getProgress()+"-km");
        seek_bar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    int Progress_value;
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Progress_value =i;
                        seek_Text.setText(i+"-km");
                    }
                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        seek_Text.setText(Progress_value+"-km");
                    }
                }
        );
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCHING_LOCATION) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                searchLat = place.getLatLng().latitude;
                seacrhLong = place.getLatLng().longitude;
                searchLoc = place.getAddress();
                searchlocationText.setText(place.getAddress());
                Log.e("valuesss","Searching Location = "+ place.getLatLng().latitude +" and "+ place.getLatLng().longitude);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainn, menu);

        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("searchLat",searchLat+"");
            returnIntent.putExtra("seacrhLong",seacrhLong+"");
            returnIntent.putExtra("distance",seek_Text.getText().toString());
            returnIntent.putExtra("searchlocation",searchLoc);
            setResult(Activity.RESULT_OK,returnIntent);
            finish();
            return true;
        }
        if(id == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
