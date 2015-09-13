package com.mityakoval.whereami;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mityakoval.whereami.containers.Constants;
import com.mityakoval.whereami.containers.Weather;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private GoogleApiClient googleApiClient;
    GoogleMap map;
    private Location location;
    private double latitude;
    private double longitude;
    private double altitude;
    private String city = null;
    private String country = null;
    private LocationRequest locationRequest;
    private boolean initialAnimation = false;
    private Weather weather;


    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_CODE) {
                city = resultData.getString("City");
                country = resultData.getString("Country");
            }
        }
    }

    class WeatherResultReceiver extends ResultReceiver {
        public WeatherResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            if (resultCode == Constants.SUCCESS_CODE) {
                weather = new Weather();
                weather.setTemperatureC(resultData.getInt("tempC"));
                weather.setTemperatureF(resultData.getInt("tempF"));
                weather.setWeatherSummary(resultData.getString("weatherSum"));
                weather.setWindStrength(resultData.getString("wind"));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Try to obtain the map from the SupportMapFragment.
        if (checkGooglePlayServices()) {
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setBuildingsEnabled(true);
            buildGoogleApiClient();
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
        putMarker();
        if (location != null) {
            startAddressIntentService();
            startWeatherIntentService();
        }

    }

    public void putMarker() {
        map.clear();
        map.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("You are here"));
        if(!initialAnimation){
            initialAnimation = true;
            animateCamera();
        }
    }

    public void animateCamera(){
        map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .zoom(15)
                .target(new LatLng(latitude, longitude))
                .build()));
    }

    protected void startAddressIntentService() {
        Intent i = new Intent(this, FetchAddressIntentService.class);
        AddressResultReceiver resultReceiver = new AddressResultReceiver(new Handler());
        i.putExtra(Constants.RECEIVER, resultReceiver);
        i.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(i);
    }

    protected void startWeatherIntentService() {
        Intent i = new Intent(this, FetchWeatherService.class);
        WeatherResultReceiver resultReceiver = new WeatherResultReceiver(new Handler());
        i.putExtra(Constants.RECEIVER, resultReceiver);
        i.putExtra(Constants.LOCATION_DATA_EXTRA, location);
        startService(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null)
            googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent();

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        altitude = Double.parseDouble(decimalFormat.format(altitude));
        i.putExtra("latitude", latitude);
        i.putExtra("longitude", longitude);
        i.putExtra("altitude", altitude);
        i.putExtra("city", city);
        i.putExtra("country", country);

        if(weather != null){
            i.putExtra("tempC", weather.getTemperatureC());
            i.putExtra("tempF", weather.getTemperatureF());
            i.putExtra("weatherSum", weather.getWeatherSummary());
            i.putExtra("wind", weather.getWindStrength());
        }

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
        this.location = location;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LocationManager locationManager;
        LocationProvider provider;
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        provider = locationManager.getProvider(LocationManager.GPS_PROVIDER);
        if (provider.supportsAltitude() && locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            altitude = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getAltitude();
            location.setAltitude(altitude);
        }

        else
            altitude = -1000;

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
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
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
