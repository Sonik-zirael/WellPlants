package com.example.wellplants.fragments;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellplants.R;
import com.example.wellplants.adapters.PlantCardAdapter;
import com.example.wellplants.db.DataBaseHelper;
import com.example.wellplants.pojo.Plant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirstFragment extends Fragment {

    private Activity activity;
    private View root;

    private RecyclerView plantCardRecycleView;
    private PlantCardAdapter plantCardAdapter;

    private Button addPlantBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_first, container, false);
        activity = getActivity();

        try {
            initRecycleView();
        } catch (IOException e) {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAA");
            e.printStackTrace();
        }

        addPlantBtn = root.findViewById(R.id.add_plant);
        addPlantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.AddPlantFragment);
            }
        });
        return root;
    }

    private void initRecycleView() throws IOException {
        plantCardRecycleView = root.findViewById(R.id.plants_cards_recycle_view);
        plantCardRecycleView.setLayoutManager(new LinearLayoutManager(activity));

        plantCardAdapter = new PlantCardAdapter();
        plantCardRecycleView.setAdapter(plantCardAdapter);

        DataBaseHelper db = new DataBaseHelper(activity.getApplicationContext());
        SQLiteDatabase readableDatabase = db.getReadableDatabase();
        Cursor plants = readableDatabase.query("Plants",
                new String[]{"PLANT_NAME", "HUMIDITY", "TEMPERATURE", "ILLUMINATION", "PLANT_IMG"},
                null, null, null, null, null);
        List<Plant> plantsFromDb = new ArrayList<>();
        while (plants.moveToNext()) {
            byte[] imgByte = imagemTratada(plants.getBlob(4));
//            System.out.println(Arrays.toString(imgByte));
            Bitmap bitmap = BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
            Plant tmpPlant = new Plant(plants.getString(0),
                    plants.getInt(1), plants.getInt(2),
                    plants.getInt(3), bitmap);
            plantsFromDb.add(tmpPlant);
        }
        plantCardAdapter.setItems(plantsFromDb);
    }

    private byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }

}