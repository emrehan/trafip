package co.tuzun.emrehan.trafip;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
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


public class DestinationActivity extends FragmentActivity implements OnMapReadyCallback,
        ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination);

        createGoogleApiClient();
        createLocationRequest();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        startLocationUpdates();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void createGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, (LocationListener) this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        updateUI();
    }

    private void updateUI() {
        LatLng mLastLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        CameraUpdate center=
                CameraUpdateFactory.newLatLng(mLastLatLng);
        CameraUpdate zoom= CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Location newLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if (newLocation != null)
            mLastLocation = newLocation;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this, "Connection to Google API Client has failed", Toast.LENGTH_SHORT).show();
    }
}
