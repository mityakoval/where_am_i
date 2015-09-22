package com.mityakoval.whereami;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.mityakoval.whereami.containers.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by mityakoval on 09/09/15.
 */
public class FetchAddressService extends IntentService {

    private ResultReceiver resultReceiver;

    public FetchAddressService(String name) {
        super(name);
    }

    public FetchAddressService(){
        super(null);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        Location location = intent.getParcelableExtra(Constants.LOCATION_DATA_EXTRA);
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);

        List<Address> addresses = null;

        try{
            addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (addresses == null || addresses.size() == 0){
            deliverResultToReceiver(Constants.FAILURE_CODE, "No address was found", null);
        } else {
            Address address = addresses.get(0);
            deliverResultToReceiver(Constants.SUCCESS_CODE, "Address was found successfully", address);
        }

    }

    private void deliverResultToReceiver(int resultCode, String message, Address address) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_DATA_KEY, message);
        if (address != null) {
            bundle.putString("City", address.getLocality());
            bundle.putString("Country", address.getCountryName());
        }
        resultReceiver.send(resultCode, bundle);
    }


}
