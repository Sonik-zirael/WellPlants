package com.example.wellplants.pojo;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Plant implements Serializable {
    private String name;
    private Integer humidity;
    private Integer temperature;
    private Integer illumination;
    private Bitmap plantImage;

    public Plant(String name, Integer humidity, Integer temperature, Integer illumination, Bitmap plantImage) {
        this.name = name;
        this.humidity = humidity;
        this.temperature = temperature;
        this.illumination = illumination;
        this.plantImage = plantImage;
    }

    public Plant(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getIllumination() {
        return illumination;
    }

    public void setIllumination(Integer illumination) {
        this.illumination = illumination;
    }

    public Bitmap getPlantImage() {
        return plantImage;
    }

    public void setPlantImage(Bitmap plantImage) {
        this.plantImage = plantImage;
    }

    @Override
    public String toString() {
        return "Plant{" +
                "name='" + name + '\'' +
                ", humidity=" + humidity +
                ", temperature=" + temperature +
                ", illumination=" + illumination +
                '}';
    }
}
