package com.example.gmapsproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener,  GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;

    private Polygon icesiArea;

    private Marker miUbicacion;

    private TextView sitioTv;

    private ArrayList<Marker> markers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 11);
        sitioTv = findViewById(R.id.sitioTv);

        markers = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * overlays
     * polygon
     * polyline
     * markers
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(this);

        // Add a marker in Sydney and move the camera

        //3.343017,-76.530754 SI
        //3.343194,-76.529108 SD
        //3.339843,-76.531101 II
        //3.339875,-76.529417 ID
        icesiArea = mMap.addPolygon(new PolygonOptions().add(
                new LatLng(3.343017,-76.530754), new LatLng(3.343194,-76.529108), new LatLng(3.339875,-76.529417),new LatLng(3.339843,-76.531101)
        ));
        LatLng icesi = new LatLng(3.342165,-76.530170);
       miUbicacion =  mMap.addMarker(new MarkerOptions().position(icesi).title("icesi"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(icesi, 15));

        //solicitud de ubicación

        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);


    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng pos = new LatLng(location.getLatitude(), location.getLongitude());

        miUbicacion.setPosition(pos);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

        boolean isInIcesi = PolyUtil.containsLocation(pos, icesiArea
        .getPoints(), true);

        if(isInIcesi){
            sitioTv.setVisibility(View.VISIBLE);
        }else{
            sitioTv.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        Marker m1 = mMap.addMarker(new MarkerOptions().position(latLng));
        markers.add(m1);
        sitioTv.setVisibility(View.GONE);
        if(markers.size()>=2){

            Marker a = markers.get(markers.size()-1);
            Marker b = markers.get(markers.size()-2);

            double distance = Math.sqrt(Math.pow(a.getPosition().latitude -b.getPosition().latitude, 2) +Math.pow(a.getPosition().longitude -b.getPosition().longitude, 2) );
            distance = distance*111.12 *1000;
            sitioTv.setText("distance :" + distance);
            sitioTv.setVisibility(View.VISIBLE);
        }
    }
}
