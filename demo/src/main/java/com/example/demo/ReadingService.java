package com.example.demo;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReadingService {
    private final ReadingRepository repo;
    private static final double THRESHOLD = 23.0;
    private final AlertRepository alertRepo;
    public ReadingService(ReadingRepository repo, AlertRepository alertRepo) {
        this.repo=repo;
        this.alertRepo=alertRepo;
    }

    public Reading saveReading(Reading reading) {
        if(reading.getTemperature()>THRESHOLD){
            Alert alert = new Alert();
            alert.setSensorId(reading.getSensorId());
            alert.setTemperature(reading.getTemperature());
            alert.setTimestamp(reading.getTimestamp());
            alertRepo.save(alert);

        }
        return repo.save(reading);
    }

}
