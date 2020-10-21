package com.example.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;

import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  static final  String TAG ="Estilo del mapa";


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.map_options, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
     switch (item.getItemId()){
         case R.id.normal_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
             break;
         case R.id.hybrid_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
             break;
         case R.id.satellite_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
             break;
         case R.id.terrain_map:
             mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
             break;
         default:
             return super.onOptionsItemSelected(item);

     }
return  true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        float zoom = 16;

        try{
            boolean success = mMap.setMapStyle(MapStyleOptions
                    .loadRawResourceStyle(getApplicationContext(),R.raw.map_style));
            if(!success){
                Log.e(TAG, "Fallo al cargar estilo del mapa");
            }
        } catch (Resources.NotFoundException exception){
            Log.e(TAG, "No es posible hallar el estilo. Error: ",exception);
        }

        // Add a marker in Sydney and move the camera
        LatLng tacna = new LatLng(-18.0237082,-70.2673986);
        mMap.addMarker(new MarkerOptions().position(tacna).title("Marker in Tacna"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tacna,zoom));
        setMapLongClick(mMap);
        setPoiClick(mMap);
    }


    private  void  setMapLongClick(final GoogleMap map){
        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng){
                String snippet = String.format(Locale.getDefault()
                        ,"Lat: %1$.5f, Long: %2$.5f",
                        latLng.latitude,
                latLng.longitude);
                map.addMarker(new MarkerOptions()
                        .position(latLng)
                .title(getString(R.string.app_name))
                .snippet(snippet));
            }
        });
    }
    private void  setPoiClick(final GoogleMap map){
        map.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest pointOfInterest) {
                Marker poiMarker = map.addMarker(new MarkerOptions()
                .position(pointOfInterest.latLng)
                .title(pointOfInterest.name));
                poiMarker.showInfoWindow();
            }
        });
    }
}