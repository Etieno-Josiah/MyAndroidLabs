package com.example.etienosandroidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.etienosandroidlabs.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    /** This holds the text shown at the center of the screen*/
    private TextView tv;
    /** This holds the password input from user*/
    private EditText et;
    ActivityMainBinding binding;
    String cityName;
    RequestQueue queue = null;
    Bitmap image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        queue = Volley.newRequestQueue(this);

        binding.fBtn.setOnClickListener(clk -> {
            Log.i("BUTTON", "Clicked");
            cityName = binding.city.getText().toString();
            Log.i("CITY", cityName);
            String api = "26b69dfadadc132c466d23b05ec8c42c";
            String url = " ";
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        URLEncoder.encode(cityName, "UTF-8") +
                        "&appid=" + api;

            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (response) -> {
                        Log.i("REQUEST", cityName);
                        try {
                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            Log.i("WEATHER", weatherArray.toString());

                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject mainObject = response.getJSONObject("main");
                            double current = mainObject.getDouble("temp");
                            double min = mainObject.getDouble("temp_min");
                            double max = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            runOnUiThread(() -> {
                                binding.tempText.setText("The current temperature is: " + current);
                                binding.tempText.setVisibility(View.VISIBLE);
                                binding.minText.setText("The min temperature is: " + min);
                                binding.minText.setVisibility(View.VISIBLE);
                                binding.maxText.setText("The max temperature is: " + max);
                                binding.maxText.setVisibility(View.VISIBLE);
                                binding.humidText.setText("The humidity is: " + humidity);
                                binding.humidText.setVisibility(View.VISIBLE);

                            });


                            String imageUrl = "http://openweathermap.org/img/w/" + iconName + ".png";

                            String pathName = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathName);
                            if(file.exists()){
                                image = BitmapFactory.decodeFile(pathName);
                            }else{
                                ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
                                    @Override
                                    public void onResponse(Bitmap bitmap) {
                                        try{
                                            image = bitmap;
                                            image.compress(Bitmap.CompressFormat.PNG, 100,
                                                    MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                            runOnUiThread(() -> {
                                                binding.icon.setImageBitmap(image);
                                                binding.icon.setVisibility(View.VISIBLE);
                                            });
                                        }
                                        catch (Exception e) {
                                        }
                                    }
                                }, 1024, 10242, ImageView.ScaleType.CENTER, null,
                                        (error -> {

                                        }));
                                queue.add(imageRequest);
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    },

                    (error) -> {
                        Log.e("IMAGE_REQUEST_ERROR", "Error loading image: " + error.toString());
                    }   );

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