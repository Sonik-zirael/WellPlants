package com.example.wellplants.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wellplants.R;
import com.example.wellplants.db.DataBaseHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class AddPlantFragment extends Fragment {
    private Activity activity;
    private View root;

    private Button savePlantBtn;
    private TextView plantName;
    private Spinner humidity;
    private Spinner temp;
    private Spinner illum;

    private ImageView feedImage;

    static final int GALLERY_REQUEST = 1;

    static final String HUM_LOW = "20";
    static final String HUM_MID = "60";
    static final String HUM_HIG = "80";

    static final String TEM_LOW = "15";
    static final String TEM_MID = "20";
    static final String TEM_HIG = "25";

    static final String ILL_LOW = "10";
    static final String ILL_MID = "60";
    static final String ILL_HIG = "80";

    private static int RESULT_LOAD_IMAGE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_add_plant, container, false);
        activity = getActivity();

        savePlantBtn = root.findViewById(R.id.save_plant);
        plantName = root.findViewById(R.id.plant_name);
        humidity = root.findViewById(R.id.humidity_spinner);
        temp = root.findViewById(R.id.temperature_spinner);
        illum = root.findViewById(R.id.illuminnation_spinner);
        feedImage = root.findViewById(R.id.add_photo);

        feedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        DataBaseHelper db = null;
        try {
            db = new DataBaseHelper(activity.getApplicationContext());
        } catch (IOException e) {
            System.out.println("BBBBBBBBBBBBBBBB");
            e.printStackTrace();
        }
        SQLiteDatabase readableDatabase = db.getReadableDatabase();

        savePlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ContentValues newValues = new ContentValues();
                newValues.put("PLANT_NAME", plantName.getText().toString());
                String humVal = "50";
                if (humidity.getSelectedItem().equals("Низкий")) {
                    humVal = HUM_LOW;
                } else if (humidity.getSelectedItem().equals("Средний")) {
                    humVal = HUM_MID;
                } else {
                    humVal = HUM_HIG;
                }
                newValues.put("HUMIDITY", humVal);
                String tempVal = "50";
                if (temp.getSelectedItem().equals("Низкий")) {
                    tempVal = TEM_LOW;
                } else if (temp.getSelectedItem().equals("Средний")) {
                    tempVal = TEM_MID;
                } else {
                    tempVal = TEM_HIG;
                }
                newValues.put("TEMPERATURE", tempVal);
                String illVal = "50";
                if (illum.getSelectedItem().equals("Тенелюбивое")) {
                    illVal = ILL_LOW;
                } else if (temp.getSelectedItem().equals("Средне")) {
                    illVal = ILL_MID;
                } else {
                    illVal = ILL_HIG;
                }
                newValues.put("ILLUMINATION", illVal);
                newValues.put("PLANT_IMG", getBitmapAsByteArray(((BitmapDrawable) feedImage.getDrawable()).getBitmap()));
                readableDatabase.insert("Plants", null, newValues);
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = activity.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            feedImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
}