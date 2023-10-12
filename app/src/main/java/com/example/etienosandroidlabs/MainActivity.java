package com.example.etienosandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import algonquin.cst2335.inclassexamples_s21.SecondActivity;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent nextPage = new Intent(MainActivity.this, SecondActivity.class);

        Button loginButton = findViewById(R.id.loginButton);
        EditText emailEdit = findViewById(R.id.emailEditText);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("LoginName", " ");

        emailEdit.setText(emailAddress);

        loginButton.setOnClickListener(clk -> {
            nextPage.putExtra("EmailAddress", emailEdit.getText().toString());
            startActivity(nextPage);

            SharedPreferences.Editor editor = prefs.edit();
            String mainEmail = emailEdit.getText().toString();
            editor.putString("LoginName", mainEmail);

            editor.apply();
        });


//        Log.w("MainActivity", "In onCreate() - Loading Widgets");
//        Log.d(TAG, "Message");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.w("MainActivity", "In onStart() - The application is now visible on screen");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.w("MainActivity", "In onResume() - The application is now responding to user" +
                "input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - The application no longer responds to user" +
                "input");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onStop() - The application is no longer visible");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onDestroy() - Any memory used by the application is" +
                "freed");
    }
}
