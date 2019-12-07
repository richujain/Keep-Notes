package com.example.keepnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapView extends FragmentActivity implements OnMapReadyCallback {

    private Integer noteId;
    private String lat = "", lon = "";
    GoogleMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);
        Intent mIntent = getIntent();
        noteId = mIntent.getIntExtra("noteId", 0);
        lat = mIntent.getStringExtra("lat");
        lon = mIntent.getStringExtra("lon");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;
        LatLng noteLocation = new LatLng(Double.valueOf(lat),Double.valueOf(lon));
        map.addMarker(new MarkerOptions().position(noteLocation).title("NoteID: "+noteId));
        //map.moveCamera(CameraUpdateFactory.newLatLng(noteLocation));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(noteLocation,13));

    }
}
