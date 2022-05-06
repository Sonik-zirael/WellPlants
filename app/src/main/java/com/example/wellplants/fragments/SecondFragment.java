package com.example.wellplants.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.wellplants.R;
import com.example.wellplants.messages.MqttHelper;
import com.example.wellplants.messages.PostRequest;
import com.example.wellplants.messages.PostRequestParams;
import com.example.wellplants.pojo.Plant;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import info.mqtt.android.service.MqttAndroidClient;

public class SecondFragment extends Fragment {

    private Activity activity;
    private View root;

    private TextView plantName;
    private TextView humidity;
    private TextView temperature;
    private TextView illumination;

    private LinearLayout tempLayout;
    private LinearLayout humLayout;
    private LinearLayout illLayout;

    private ImageView feedImage;

    MqttAndroidClient client;
    Runnable mRunnableTask;
    MqttHelper mqttHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_second, container, false);
        activity = getActivity();

        plantName = root.findViewById(R.id.plant_card_name);
        humidity = root.findViewById(R.id.humidity);
        temperature = root.findViewById(R.id.temp);
        illumination = root.findViewById(R.id.illumination);
        tempLayout = root.findViewById(R.id.linear_1_1);
        humLayout = root.findViewById(R.id.linear_1_2);
        illLayout = root.findViewById(R.id.linear_1_3);
        feedImage = root.findViewById(R.id.card_pet_image);

        Plant plant = (Plant) getArguments().getSerializable("petInfo");
        plantName.setText(plant.getName());
        humidity.setText(String.valueOf(plant.getHumidity()));
        temperature.setText(String.valueOf(plant.getTemperature()));
        illumination.setText(String.valueOf(plant.getIllumination()));
        feedImage.setImageBitmap(plant.getPlantImage());

        startMqtt(plant);


        String url = "http://3.238.21.184:1880/test";
        try {
            JSONObject requestObject = new JSONObject();
            requestObject.put("h", plant.getHumidity());
            requestObject.put("t", plant.getTemperature());
            requestObject.put("i", plant.getIllumination());
            final Handler handler = new Handler();
            Timer timer = new Timer();
            TimerTask doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            try {
                                System.out.println("Send");
                                String response = new PostRequest().execute(new PostRequestParams(url, requestObject.toString())).get();
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                            }
                        }
                    });
                }
            };
            timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 50000 ms

        } catch (JSONException error) {
            Log.d("VOLLEY", "Not Success response (add to favourite): " + error.toString());
        }

        return root;

    }


    private void startMqtt(Plant plant) {
        mqttHelper = new MqttHelper(activity.getApplicationContext());
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("Debug", "Connected");
            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                String[] messages = mqttMessage.toString().split(",");
                Double t = Double.parseDouble(messages[0]);
                Log.w("DebugAAA", t.toString());
                Double h = Integer.parseInt(messages[1]) * 1.0 / 1023.0 * 100;
                System.out.println(h);
                Double i = Integer.parseInt(messages[2]) * 1.0 / 1023.0 * 100;
                System.out.println(i);
                if (t < plant.getTemperature()) {
                    tempLayout.setVisibility(View.VISIBLE);
                } else {
                    tempLayout.setVisibility(View.INVISIBLE);
                }
                if (h < plant.getHumidity()) {
                    humLayout.setVisibility(View.VISIBLE);
                } else {
                    humLayout.setVisibility(View.INVISIBLE);
                }
                if (i < plant.getIllumination()) {
                    illLayout.setVisibility(View.VISIBLE);
                } else {
                    illLayout.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

}