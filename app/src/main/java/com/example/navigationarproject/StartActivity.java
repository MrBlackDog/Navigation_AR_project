package com.example.navigationarproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import okhttp3.WebSocket;

public class StartActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 100;
    SharedPreferences sharedPreferences;

    public enum Mode
    {
        Base,Target;
    }
    public static Mode mode;
    public final static String model = android.os.Build.MODEL;
    public static WebSocket _ws;
    public static SocketManager sm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        if ( (ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED)) {
            // You can use the API that requires the permission.
           // performAction(...);
        } //else if (shouldShowRequestPermissionRationale(...)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
           // showInContextUI(...);}
        else {
            // You can directly ask for the permission.
            requestPermissions(
                    new String[] { Manifest.permission.CAMERA ,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_CODE);
        }

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case -1:
                    Toast.makeText(getApplicationContext(), "Ничего не выбрано",
                            Toast.LENGTH_SHORT).show();
                    break;
                case R.id.radioButtonBase:
                    Toast.makeText(getApplicationContext(), "Выбран режим базы",
                            Toast.LENGTH_SHORT).show();
                    mode = Mode.Base;
                    break;
                case R.id.radioButtonRover:
                    Toast.makeText(getApplicationContext(), "Выбран режим метки",
                            Toast.LENGTH_SHORT).show();
                    mode = Mode.Target;
                    break;
                default:
                    break;
            }
        });

        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(view -> {
            Intent intent;
            switch (mode)
            {
                case Base:
                     intent = new Intent(StartActivity.this, MainActivity.class);
                     intent.putExtra("Mode",mode);
                     Connect();
                     _ws.send("Phone:" + model + ":" + "StandAlone" +":" + mode);
                     startActivity(intent);
                     break;
                case Target:
                     intent = new Intent(StartActivity.this, QuestActivity.class);
                     intent.putExtra("Mode",mode);
                     Connect();
                     _ws.send("Phone:" + model + ":" + "StandAlone" +":" + mode);
                     startActivity(intent);
                     break;
            }

            Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public void Connect(){
        sm = new SocketManager();
        _ws = sm.Connect();
    }



    /**
     * lifecycle handler
     */
    //отрабатывает,когда мы возвращаемся из другого активити
    @Override
    protected void onRestart() {
        super.onRestart();
      //  loadText();
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  loadText();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  saveText();
    }
}
