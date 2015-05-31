package co.tuzun.emrehan.trafip;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.melnykov.fab.FloatingActionButton;

public class DestinationActivity extends ActionBarActivity implements OnMapReadyCallback {


    protected GoogleMap mMap;

    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setColorNormal(getResources().getColor(R.color.background_floating_material_light));
    }

    private void zoomOnCurrentLatLng() {
        CameraUpdate center = CameraUpdateFactory.newLatLng(((App)getApplicationContext()).currentLatLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
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

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                App.getInstance().destinationLatLng = point;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(App.getInstance().destinationLatLng));
                Toast.makeText(DestinationActivity.this, App.getInstance().currentLatLng + " -> " +
                        App.getInstance().destinationLatLng, Toast.LENGTH_SHORT).show();
                // Enable floating action button
                fab.setColorNormal(getResources().getColor(R.color.trafi_orange));
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), RoutesActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        zoomOnCurrentLatLng();
    }
}
