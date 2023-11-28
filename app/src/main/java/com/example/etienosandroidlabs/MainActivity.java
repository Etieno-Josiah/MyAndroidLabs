package com.example.etienosandroidlabs;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.etienosandroidlabs.databinding.ActivityMainBinding;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    /** This holds the text shown at the center of the screen*/
    private TextView tv;
    /** This holds the password input from user*/
    private EditText et;
    /** This holds the login button*/
    private Button forecastBtn;
    ActivityMainBinding binding;
    String cityName;
    RequestQueue queue = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        queue = Volley.newRequestQueue(this);

        binding.forecastButton.setOnClickListener(clk -> {
            cityName = binding.cityText.getText().toString();
            String api = "26b69dfadadc132c466d23b05ec8c42c";
            String url = null;
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        URLEncoder.encode(cityName, "UTF-8") +
                        "}&appid=" + api;
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {

                    },
                    (error) -> {

                    });
            queue.add(request);
        });
    }

    /**This function checks a password input is complex enough.
     *
     * @param password The string that we are checking
     * @return Returns true if string is complex enough and false if it is not complex enough.
     */
    boolean checkPasswordComplexity(String password){
        boolean foundUpperCase, foundLowerCase, foundNumber, foundSpecial;
        foundUpperCase = foundLowerCase = foundNumber = foundSpecial = false;

        for (int i = 0; i <password.length(); i++){
            char currentChar = password.charAt(i);
            if (Character.isUpperCase(currentChar)){
                foundUpperCase = true;
            }
            if (Character.isLowerCase(currentChar)){
                foundLowerCase = true;
            }
            if (Character.isDigit(currentChar)){
                foundNumber = true;
            }
            if (isSpecialCharacter(currentChar)){
                foundSpecial = true;
            }
        }

        if (!foundUpperCase){
            Toast.makeText(getApplicationContext(), "Add at least one upper case letter",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (!foundLowerCase){
            Toast.makeText(getApplicationContext(), "Add at least one lower case letter",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (!foundNumber) {
            Toast.makeText(getApplicationContext(), "Add at least one number",
                    Toast.LENGTH_SHORT).show();
            return false;
        }else if (!foundSpecial){
            Toast.makeText(getApplicationContext(), "Add at least one of the special" +
                    "characters", Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
        }
    }

    /** This checks if a password contains any special characters
     *
     * @param c each character in the password string
     * @return Returns true if there is at least special character and false if there is no special character
     */
    public boolean isSpecialCharacter(char c){
        switch (c){
            case '!':
            case '@':
            case '#':
            case '$':
            case '%':
            case '^':
            case '*':
            case '?':
                return true;
            default:
                return false;
        }
    }
}