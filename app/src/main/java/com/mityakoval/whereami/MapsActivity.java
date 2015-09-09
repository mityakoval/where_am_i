package com.mityakoval.whereami;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    private Location location;
    private double latitude;
    private double longitude;
    private double altitude;
    private String city = null;
    private String country = null;
    private LocationRequest locationRequest;
    private AddressResultReceiver resultReceiver;


    class AddressResultReceiver extends ResultReceiver{
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if(resultCode == FetchAddressIntentService.Constants.SUCCESS_CODE){
                city = resultData.getString("City");
                country = resultData.getString("Country");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
            // Try to obtain the map from the SupportMapFragment.
        if(checkGooglePlayServices()) {
            GoogleMapOptions mapOptions = new GoogleMapOptions();
            mapOptions.mapType(GoogleMap.MAP_TYPE_NORMAL);
            mapOptions.zoomControlsEnabled(true);
            mapOptions.rotateGesturesEnabled(true);
            mapOptions.zoomGesturesEnabled(true);
            MapFragment mapFragment = MapFragment.newInstance(mapOptions);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.add(R.id.map, mapFragment);
            transaction.commit();
            buildGoogleApiClient();
            mapFragment.getMapAsync(this);
            createLocationRequest();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        setLocationData(location);
        if (location != null){
            startIntentService();
        }
    }

    protected void startIntentService(){
        Intent i = new Intent(this, FetchAddressIntentService.class);
        i.putExtra(FetchAddressIntentService.Constants.RECEIVER, resultReceiver);
        i.putExtra(FetchAddressIntentService.Constants.LOCATION_DATA_EXTRA, location);
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Marker"));
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed(){

        Intent i = new Intent();

        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        i.putExtra("altitude", altitude);
        i.putExtra("city", city);
        i.putExtra("country", country);

        setResult(RESULT_OK, i);

        finish();
    }

    public void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    public void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    public void setLocationData(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        altitude = location.getAltitude();
    }

    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    }

    public boolean checkGooglePlayServices() {
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS) {
            if(GooglePlayServicesUtil.isUserRecoverableError(result))
                GooglePlayServicesUtil.getErrorDialog(result, this, 1000);
            else {
                Toast.makeText(getApplicationContext(), "Device is not supported", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }
}
