package com.schwarzion.carLocator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int PERMISSION_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private double wayLatitude;
    private double wayLongitude;
    ListView myListView;
    Button add;
    EditText titleText;
    ArrayList<String> longs = new ArrayList<String>();
    ArrayList<String> lats = new ArrayList<String>();
    ArrayList<String> times = new ArrayList<String>();
    ArrayList<String> titles = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testPermissions();
        createUser();
        add = findViewById(R.id.addLocationButton);
        titleText = findViewById(R.id.titleText);
        myListView = findViewById(R.id.locationsListView);

        LocationPin[] datas = getItems();
        for (LocationPin data : datas) {
            longs.add(String.valueOf(data.getLon()));
            lats.add(String.valueOf(data.getLat()));
            times.add(String.valueOf(data.getTime()));
            titles.add(String.valueOf(data.getTitre()));

        }
        String[] longsArray = new String[longs.size()];
        longsArray = longs.toArray(longsArray);
        String[] latsArray = new String[lats.size()];
        latsArray = lats.toArray(latsArray);
        String[] titlesArray = new String[titles.size()];
        titlesArray = titles.toArray(titlesArray);
        String[] timesArray = new String[times.size()];
        timesArray = times.toArray(timesArray);
        ItemAdapter itemAdapter = new ItemAdapter(this, titlesArray, timesArray, longsArray, latsArray);
        myListView.setAdapter(itemAdapter);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleText.getText().toString();
                LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
                boolean enabled = service
                        .isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!enabled) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

                addCoord(title, android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date()).toString(), wayLatitude, wayLongitude);
                finish();
                startActivity(getIntent());
            }
        });
        fusedLocationClient.getLastLocation().addOnSuccessListener(MainActivity.this, location -> {
            if (location != null) {
                wayLatitude = location.getLatitude();
                wayLongitude = location.getLongitude();
            }
        });

    }

        private LocationPin[] getItems() {
            File file = new File(getApplicationContext().getFilesDir(), "data.json");
            FileReader fileReader = null;
            try {
                fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                Gson gson = new Gson();
                LocationPin[] dataArray = gson.fromJson(bufferedReader, LocationPin[].class);
                return dataArray;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new LocationPin[0];
        }

        private void testPermissions() {
            Resources res = getResources();
            String[] permissions = res.getStringArray(R.array.permissions);

            for (String perm : permissions) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        perm) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "Granted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Not Granted", Toast.LENGTH_SHORT).show();
                    requestPermission(perm);
                }
            }
        }

        private void requestPermission(final String permission) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                new AlertDialog.Builder(this)
                        .setTitle("Permission needed")
                        .setMessage("App needs to access a permission to work : "+permission)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(MainActivity.this, new String[] {permission}, PERMISSION_CODE);
                            }
                        })
                        .setNegativeButton("Not ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {permission}, PERMISSION_CODE);
            }
        }

        private void addCoord(String title, String time, double lat, double lon) {
            LocationPin loc = new LocationPin(time, title, lat, lon);
            String jsonData = new Gson().toJson(loc);
            LocationPin newObject = new Gson().fromJson(jsonData, LocationPin.class);

            try {
                addJsonToFile(newObject, "data.json");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String[] checkIfFileExits(final String filename) {
            String[] list = getApplicationContext().getFilesDir().list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return name.matches(filename);
                }
            });
            return list;
        }

        private void createUser() {
            String[] list = checkIfFileExits("user.json");
            if (list.length < 1)
            {
                Intent addUserActivity =
                        new Intent(getApplicationContext(), AddUser.class);
                startActivity(addUserActivity);
            }
        }

        private void addJsonToFile(LocationPin json, String fileName) throws JSONException {
            String[] list = checkIfFileExits(fileName);
            if (list.length == 1)
            {
                try {
                    File file = new File(getApplicationContext().getFilesDir(), fileName);
                    FileReader fileReader = new FileReader(file);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);
                    Gson gson = new Gson();
                    LocationPin[] dataArray = gson.fromJson(bufferedReader, LocationPin[].class);
                    FileWriter fileWriter = new FileWriter(file);
                    String jsonArrayInString = new Gson().toJson(dataArray);
                    JSONArray array = new JSONArray(jsonArrayInString);
                    String jsonInString = new Gson().toJson(json);
                    JSONObject mJSONObject = new JSONObject(jsonInString);
                    array.put(mJSONObject);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(array.toString());
                    bufferedWriter.close();
                }
                catch (IOException e)
                {
                    Log.e("File", "Read All byte exception", e);
                }
            }
            else {
                File file = new File(getApplicationContext().getFilesDir(), fileName);
                JSONArray array = new JSONArray();
                String jsonInString = new Gson().toJson(json);
                JSONObject mJSONObject = new JSONObject(jsonInString);
                array.put(mJSONObject);
                try {
                    FileWriter fileWriter = new FileWriter(file);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    bufferedWriter.write(array.toString());
                    bufferedWriter.close();
                } catch (IOException e) {
                    Log.e("File", "cannot open file", e);
                }
            }
        }
    }
