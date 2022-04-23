package com.hfad.atasteofthefiftystates;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.ResultReceiver;
import androidx.annotation.Nullable;
import java.util.List;
import java.util.Locale;


public class getAddressService extends IntentService {


    private ResultReceiver resultReceiver;
    public getAddressService(){
        super("getAddressService");
    }

    @Override

    /*This method takes the location details from geocoder and determines if it produces
    a successful result or a failure result. It takes that data and produces a state and passes
    it and the result back to the main activity class.
     */

    protected void onHandleIntent(@Nullable Intent intent) {

        if(intent != null){
            String errorMessage = "";
            resultReceiver = intent.getParcelableExtra(Constants.RECEIVER);
            Location location = intent.getParcelableExtra(Constants.LOCATION_DATA);
            if(location == null){
                return;
            }
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());

            List<Address> addresses = null;
            try{
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            }catch(Exception e){
                errorMessage = e.getMessage();
            }
            if(addresses == null || addresses.isEmpty()){
                deliverResultToReceiver(Constants.FAILURE_RESULT, errorMessage);
            } else {
                Address address = addresses.get(0);
                deliverResultToReceiver(
                        Constants.SUCESS_RESULT, address.getAdminArea()
                );
            }
        }
    }

    //Sends the data and result back to the main activity.
    private void deliverResultToReceiver(int resultCode, String addressMessage){

        Bundle bundle = new Bundle();
        bundle.putString(Constants.RESULT_KEY, addressMessage);
        resultReceiver.send(resultCode, bundle);
    }
}
