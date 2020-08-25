package com.schwarzion.carLocator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    private int PERMISSION_CODE = 1;
    ListView myListView;
    String[] longs;
    String[] lats;
    String[] times;
    String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testPermissions();
        createUser();
        myListView = findViewById(R.id.locationsListView);
        //Retrieve objects from file => lines to json

        longs = new String[] {"43","44"};
        lats = new String[] {"47", "57"};
        times = new String[] {"20/11/2020", "17/04/2020"};
        titles = new String[] {"day1", "day2"};

        //ItemAdapter itemAdapter = new ItemAdapter(this, titles, times, longs, lats);
        //myListView.setAdapter(itemAdapter);
        addCoord("day 1", "20/11/2020", 43.455443, 102.345323);
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
        try {
            JSONObject json = new JSONObject();
            json.put("title", title);
            json.put("time", time);
            json.put("lat", lat);
            json.put("long", lon);
            addJsonToFile(json, "data.json");
        } catch (JSONException e) {
            Log.e("USER_INFO", "unexpected JSON exception", e);
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

    private void addJsonToFile(JSONObject json, String fileName) {
        String[] list = checkIfFileExits("data.json");
        if (list.length < 1)
        {
            
        }
        else {
            File file = new File(getApplicationContext().getFilesDir(), fileName);
            JSONArray array = new JSONArray();
            array.put(json);
            String data = array.toString();
            try {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(data);
                bufferedWriter.close();
            } catch (java.io.IOException e) {
                Log.e("File", "cannot open file", e);
            }
        }
    }
}
