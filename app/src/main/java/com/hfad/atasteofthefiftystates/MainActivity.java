package com.hfad.atasteofthefiftystates;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements Serializable {

    ArrayList<String> stateList = new ArrayList<String>();
    ArrayList<String> recipeList = new ArrayList<String>();
    ArrayList<String> realRecipeList = new ArrayList<String>();
    ArrayList<String> ingredientsList = new ArrayList<String>();
    ArrayList<String> ratingList = new ArrayList<String>();


    private String savedLocation = null;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    private ResultReceiver resultReceiver;
    public static int INDEX = 0;
    public StringBuilder selection = null;
    private int locationID = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // we just made a few states to prove that everything works,
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new JSONTask().execute("https://cdn.discordapp.com/attachments/946210413849239603/969782607137108028/test-file.txt");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                        stateList);

        Button stateSelect = (Button) findViewById(R.id.button);

        stateSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), RecipeListActivity.class);
                intent.putExtra("states", stateList);
                intent.putExtra("food", recipeList);
                intent.putExtra("ingredients", ingredientsList);
                view.getContext().startActivity(intent);

            }
        });

        resultReceiver = new AddressResultReceiver(new Handler());

        //initialized button button click
        findViewById(R.id.buttonGetCurrentLocation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Determine whether we have premission to use location from user if not request it
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    //if we have premission run get current location method
                    getCurrentLocation();
                }
            }
        });
    }

    public class JSONTask extends AsyncTask<String, String, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL("https://cdn.discordapp.com/attachments/946210413849239603/969782607137108028/test-file.txt");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();

                JSONObject parentObject = new JSONObject(finalJson);
                JSONArray finalObject = parentObject.getJSONArray("states");

                for (int i = 0; i < finalObject.length(); i++) {
                    JSONObject obj = finalObject.getJSONObject(i);
                    stateList.add(obj.getString("state"));
                    JSONObject r = obj.getJSONObject("recipes");
                    recipeList.add(r.getString("recipe"));
                    ingredientsList.add(r.getString("ingredients"));
                    ratingList.add(r.getString("rating"));

                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String result){

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {

        //
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET}
                        ,10);
            }
            return;
        }
        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MainActivity.this)
                                .removeLocationUpdates(this);
                        if (locationRequest != null && locationResult.getLocations().size() > 0) {
                            int latestLocationIndex = locationResult.getLocations().size() - 1;
                            double latitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            double longitude =
                                    locationResult.getLocations().get(latestLocationIndex).getLongitude();

                            Location location = new Location("NA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);


                        }
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location){

        Intent intent = new Intent(this, getAddressService.class);
        intent.putExtra(Constants.RECEIVER, resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA, location);
        startService(intent);
    }
    private class AddressResultReceiver extends ResultReceiver {

        AddressResultReceiver(Handler handler) {
            super(handler);
        }

        //This takes the results from getAddressService and the result code and hands them  off.
        //If the result code show that it was successful then save the data to location string.
        //Else prompts a message to user.

        private int compareResults(String result, ArrayList stateList){

            int ID = 0;

            for(int i = 0; i < stateList.size(); i++){

                boolean answer = savedLocation.equals(stateList.get(i));
                if (answer == true){
                    ID = i;
                    return ID;
                }
            }
            return ID;
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            super.onReceiveResult(resultCode, resultData);
            if (resultCode == Constants.SUCESS_RESULT) {
                savedLocation = resultData.getString(Constants.RESULT_KEY);
                //Uncomment this out if you are unable to change you emulator to Florida
                //savedLocation = "Florida";
                locationID = compareResults(savedLocation, stateList);

                Integer stateIndex = locationID;
                Intent intent = new Intent(MainActivity.this, actualRecipeList.class);
                ArrayList<String> chosenFood = new ArrayList<>();
                ArrayList<String> chosenIngredient = new ArrayList<>();
                chosenFood.add(recipeList.get(locationID));
                chosenIngredient.add(ingredientsList.get(locationID));
                intent.putExtra("food", chosenFood);
                intent.putExtra("ingredients", chosenIngredient);
                intent.putExtra("number", locationID);
                MainActivity.this.startActivity(intent);

            } else {
                Toast.makeText(MainActivity.this, resultData.getString(Constants.RESULT_KEY), Toast.LENGTH_SHORT).show();
            }
        }
        }
}