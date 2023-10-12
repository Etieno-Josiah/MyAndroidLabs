package algonquin.cst2335.inclassexamples_s21;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.etienosandroidlabs.MainActivity;
import com.example.etienosandroidlabs.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private TextView textView;
    private EditText callEdit;

    private String emailAddress;
    private String TAG = "SecondActivity";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent fromPrevious = getIntent();
        emailAddress = fromPrevious.getStringExtra("EmailAddress");
        callEdit = findViewById(R.id.callEdit);

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String savedPhoneNumber = prefs.getString("PhoneNumber", " ");

        callEdit.setText(savedPhoneNumber);

        Button callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(clk -> {
            String phoneNumber = callEdit.getText().toString();
            Intent call = new Intent(Intent.ACTION_DIAL);
            call.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(call);
        });

        Button picButton = findViewById(R.id.picButton);
        profileImage = findViewById(R.id.imageView);
        profileImage.setImageResource(R.drawable.ic_menu_camera);

        ActivityResultLauncher<Intent> cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            assert result.getData() != null;
                            Bundle extras = result.getData().getExtras();
                            if (extras != null){
                                Bitmap thumbnail = (Bitmap) extras.get("data");
                                if(thumbnail != null){
                                    Log.i(TAG, thumbnail.toString());
                                    profileImage.setImageBitmap(thumbnail);

                                    FileOutputStream fOut = null;
                                    try {
                                        fOut = openFileOutput("Picture.png", Context.MODE_PRIVATE);
                                        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                                        fOut.flush();
                                        fOut.close();
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });

        picButton.setOnClickListener(click -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraResult.launch(cameraIntent);
        });

        File file = new File(getFilesDir(), "Picture.png");
        if (file.exists()){
            Bitmap theImage = BitmapFactory.decodeFile(file.getAbsolutePath());
            profileImage.setImageBitmap(theImage);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        textView = findViewById(R.id.welcomeTV);
        String header = getString(R.string.welcome) + " " + emailAddress;
        textView.setText(header);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String phone = callEdit.getText().toString();
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phone);
        editor.apply();

    }
}