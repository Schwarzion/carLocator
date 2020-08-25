package com.schwarzion.carLocator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AddUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        Button addButton = findViewById(R.id.registerButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EditText username = findViewById(R.id.nameText);
                EditText car = findViewById(R.id.carText);
                try {
                    JSONObject json = new JSONObject();
                    json.put("Name", username.getText());
                    json.put("Car", car.getText());
                    addJsonToFile(json, "user.json");
                    finish();
                } catch (JSONException e) {
                    Log.e("USER_INFO", "unexpected JSON exception", e);
                }
            }
        });
    }

    private void addJsonToFile(JSONObject json, String fileName) {
        String data = json.toString();
        File file = new File(getApplicationContext().getFilesDir(), fileName);
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
