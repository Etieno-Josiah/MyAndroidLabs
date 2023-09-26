package com.example.etienosandroidlabs.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etienosandroidlabs.data.MainViewModel;
import com.example.etienosandroidlabs.databinding.ActivityMain2Binding;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        model = new ViewModelProvider(this).get(MainViewModel.class);

        variableBinding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());

        variableBinding.mybutton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });

        model.editString.observe(this, s -> {
            variableBinding.textview.setText("Your edit text has " + s);
        });

        variableBinding.mycheckBox.setOnCheckedChangeListener((checkbtn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });
        variableBinding.mySwitch.setOnCheckedChangeListener((switchbtn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });
        variableBinding.myRadio.setOnCheckedChangeListener((radiobtn, isChecked) -> {
            model.isSelected.postValue(isChecked);
        });

        model.isSelected.observe(this, selected -> {
            variableBinding.mycheckBox.setChecked(selected);
            variableBinding.mySwitch.setChecked(selected);
            variableBinding.myRadio.setChecked(selected);

            String message = "The value is now " + selected;
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        });

        variableBinding.myimagebutton.setOnClickListener(click -> {
            int width = variableBinding.myImage.getWidth();
            int height = variableBinding.myImage.getHeight();
            String message = "The width = " + width + " and height = " + height;
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        });
    }
}