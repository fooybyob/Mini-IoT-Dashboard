package com.example.demo;

import jakarta.persistence.*;

@Entity
public class Reading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY  )
    private long id;

    private String sensorId;
    private double temperature;
    private String timestamp;
    private long timestampLong;



    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestampLong() {
        return timestampLong;
    }
    public void setTimestampLong(long timestampLong) {
        this.timestampLong = timestampLong;
    }
}
