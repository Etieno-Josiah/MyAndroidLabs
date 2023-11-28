package com.example.etienosandroidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /** This holds the text shown at the center of the screen*/
    private TextView tv;
    /** This holds the password input from user*/
    private EditText et;
    /** This holds the login button*/
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}